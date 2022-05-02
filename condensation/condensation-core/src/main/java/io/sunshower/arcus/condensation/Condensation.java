package io.sunshower.arcus.condensation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import java.util.function.Supplier;
import lombok.NonNull;
import lombok.val;

@SuppressFBWarnings
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class Condensation {

  private final String format;
  private final Parser parser;
  private final DocumentWriter writer;
  private final PropertyScanner scanner;
  private final TypeBinder<?> typeBinder;
  private final TypeInstantiator instantiator;

  private Condensation(final String format) {
    this.format = format;
    val condensationConfiguration =
        ServiceLoader.load(
                CondensationConfiguration.class, Thread.currentThread().getContextClassLoader())
            .stream()
            .map(Provider::get)
            .filter(configuration -> configuration.supports(format))
            .findAny()
            .get();
    this.scanner = condensationConfiguration.getScanner();
    this.instantiator = condensationConfiguration.getInstantiator();
    this.typeBinder = condensationConfiguration.createBinder();
    this.parser = getParserFactory(format).newParser();
    this.writer = getWriterFactory(format).newWriter(typeBinder, instantiator);
  }

  public static <T> void write(
      @NonNull String format,
      @NonNull Class<T> type,
      @NonNull T value,
      @NonNull OutputStream outputStream)
      throws IOException {
    create(format).getWriter().write(type, value, outputStream);
  }

  public static <T> String write(@NonNull String format, @NonNull Class<T> type, @NonNull T value)
      throws IOException {
    val outputStream = new ByteArrayOutputStream();
    create(format).getWriter().write(type, value, outputStream);
    return outputStream.toString(StandardCharsets.UTF_8);
  }

  @SuppressWarnings("unchecked")
  public static <E extends Enum<E>> Document<E> parse(String format, CharSequence data) {
    val pf = getParserFactory(format);
    return pf.newParser().parse(data);
  }

  public static <E extends Enum<E>> Document<E> parse(String format, InputStream data) {
    val pf = getParserFactory(format);
    return pf.newParser().parse(data);
  }

  @SuppressWarnings("unchecked")
  public static <T, E extends Enum<E>> T read(
      Class<T> type, String format, CharSequence data, TypeBinder<E> strategy) {
    Document<E> doc = parse(format, data);
    return doc.read(type, strategy);
  }

  public static <T, E extends Enum<E>> T read(
      Class<T> type, String format, InputStream data, TypeBinder<E> strategy) {
    Document<E> doc = parse(format, data);
    return doc.read(type, strategy);
  }

  public static Condensation create(String format) {
    return new Condensation(format);
  }

  public TypeInstantiator getInstantiator() {
    return instantiator;
  }

  @SuppressWarnings("unchecked")
  public <T> T read(Class<T> type, CharSequence sequence) {
    return (T) read(type, format, sequence, typeBinder);
  }

  public <T> T read(Class<T> type, InputStream sequence) {
    return (T) read(type, format, sequence, typeBinder);
  }

  public <T> String write(Class<T> type, T value) throws IOException {
    return getWriter().write(type, value);
  }

  public DocumentWriter getWriter() {
    return writer;
  }

  @SuppressWarnings("unchecked")
  public <T, U extends Collection<? super T>> U readAll(
      Class<T> type, Supplier<U> instantiator, CharSequence s) {
    val document = parser.parse(s);
    return (U) document.readAll(type, instantiator, typeBinder);
  }

  @SuppressWarnings("unchecked")
  public <T, U extends Collection<? super T>> U readAll(
      Class<T> type, Supplier<U> instantiator, InputStream s) {
    val document = parser.parse(s);
    return (U) document.readAll(type, instantiator, typeBinder);
  }

  public <T> T copy(Class<T> type, T value) throws IOException {
    return read(type, write(type, value));
  }

  private static WriterFactory getWriterFactory(String format) {
    return ServiceLoader.load(WriterFactory.class, Thread.currentThread().getContextClassLoader())
        .stream()
        .map(Provider::get)
        .filter(parserFactory -> parserFactory.supports(format))
        .findAny()
        .orElseThrow(
            () -> new NoSuchElementException(String.format("Unsupported format: '%s'", format)));
  }

  private static ParserFactory getParserFactory(String format) {
    return ServiceLoader.load(ParserFactory.class, Thread.currentThread().getContextClassLoader())
        .stream()
        .map(Provider::get)
        .filter(parserFactory -> parserFactory.supports(format))
        .findAny()
        .orElseThrow(
            () -> new NoSuchElementException(String.format("Unsupported format: '%s'", format)));
  }
}
