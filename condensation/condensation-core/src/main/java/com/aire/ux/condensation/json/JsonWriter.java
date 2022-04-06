package com.aire.ux.condensation.json;

import com.aire.ux.condensation.DocumentWriter;
import com.aire.ux.condensation.Property;
import com.aire.ux.condensation.TypeBinder;
import com.aire.ux.condensation.TypeDescriptor;
import com.aire.ux.condensation.TypeInstantiator;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Map;
import lombok.NonNull;
import lombok.val;

public class JsonWriter implements DocumentWriter {

  public static final String NULL = "null";
  private final TypeBinder binder;
  private final TypeInstantiator instantiator;

  public JsonWriter(TypeBinder typeBinder, TypeInstantiator instantiator) {
    this.binder = typeBinder;
    this.instantiator = instantiator;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> void write(
      @NonNull Class<T> type, @NonNull T value, @NonNull OutputStream outputStream)
      throws IOException {

    if (Property.isConvertible(type, String.class)) {
      outputStream.write('"');
      outputStream.write(Property.convert(value, String.class).getBytes(StandardCharsets.UTF_8));
      outputStream.write('"');
    } else if (type.isEnum()) {
      outputStream.write('"');
      outputStream.write(((Enum) value).name().getBytes(StandardCharsets.UTF_8));
      outputStream.write('"');

    } else if (String.class.equals(type)) {
      outputStream.write('"');
      outputStream.write(((String) value).getBytes(StandardCharsets.UTF_8));
      outputStream.write('"');
    } else if (type.isPrimitive() || Property.isPrimitive(type)) {
      outputStream.write(String.valueOf(value).getBytes(StandardCharsets.UTF_8));
    } else if (type.isArray()) {
      writePrologue(type, outputStream);
      writeArray(type, value, outputStream);
      writeEpilogue(type, outputStream);
    } else if (Iterable.class.isAssignableFrom(type)) {
      writeIterable(type, (Iterable<?>) value, outputStream);
    } else if (Map.class.isAssignableFrom(type)) {
      val map = (Map<Object, Object>) value;
      outputStream.write('{');
      val iter = map.entrySet().iterator();
      while (iter.hasNext()) {
        val next = iter.next();
        val key = next.getKey();
        val v = next.getValue();
        outputStream.write('"');
        outputStream.write(String.valueOf(key).getBytes(StandardCharsets.UTF_8));
        outputStream.write('"');
        outputStream.write(':');
        write((Class) v.getClass(), v, outputStream);
        if (iter.hasNext()) {
          outputStream.write(',');
        }
      }
      outputStream.write('}');
    } else {
      val descriptor = binder.descriptorFor(type);
      writePrologue(type, outputStream);
      writeProperties(descriptor, type, outputStream, value);
      writeEpilogue(type, outputStream);
    }
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T> void writeAll(
      @NonNull Class<T> type,
      @NonNull Collection<? extends T> value,
      @NonNull OutputStream outputStream)
      throws IOException {
    val iterable = value;
    val iterator = iterable.iterator();
    writePrologue(Collection.class, outputStream);
    while (iterator.hasNext()) {
      val next = iterator.next();
      write((Class) next.getClass(), next, outputStream);
      if (iterator.hasNext()) {
        outputStream.write(',');
      }
    }
    writeEpilogue(Collection.class, outputStream);
  }

  private <T> void writeIterable(Class<T> type, Iterable<?> value, OutputStream outputStream)
      throws IOException {
    val iterable = value;
    val iterator = iterable.iterator();
    writePrologue(type, outputStream);
    while (iterator.hasNext()) {
      val next = iterator.next();
      write((Class) next.getClass(), next, outputStream);
      if (iterator.hasNext()) {
        outputStream.write(',');
      }
    }
    writeEpilogue(type, outputStream);
  }

  @SuppressWarnings({"unchecked", "rawtypes", "PMD"})
  private <T> void writeArray(Class<T> type, T value, OutputStream outputStream)
      throws IOException {
    if (Property.isPrimitive(type.getComponentType())) {

    } else {
      val os = (Object[]) value;
      for (int i = 0; i < os.length; i++) {
        val o = os[i];
        if (o != null) {
          write((Class) o.getClass(), o, outputStream);
          if (i < os.length - 1) {
            outputStream.write(',');
          }
        }
      }
    }
  }

  private <T> void writeProperties(
      TypeDescriptor<T> descriptor, Class<T> type, OutputStream outputStream, T value)
      throws IOException {
    val iterator = descriptor.getProperties().iterator();
    while (iterator.hasNext()) {
      val property = iterator.next();
      if (property.isConvertable()) {
        writeConvertableProperty(property, value, outputStream);
      } else if (property.isPrimitive()) {
        writePrimitive(property, value, outputStream);
      } else if (String.class.equals(property.getType())) {
        writeString(property, value, outputStream);
      } else {
        val v = property.get(value);
        writePropertyPrelude(property, outputStream);
        if (v != null) {
          write(property.getType(), property.get(value), outputStream);
        } else {
          write(outputStream, NULL);
        }
      }
      if (iterator.hasNext()) {
        outputStream.write(',');
      }
    }
  }

  private <T> void writeString(Property<?> property, T value, OutputStream outputStream)
      throws IOException {

    writePropertyPrelude(property, outputStream);
    val v = property.get(value);
    if (v != null) {
      outputStream.write('"');
      outputStream.write(((String) v).getBytes(StandardCharsets.UTF_8));
      outputStream.write('"');
    } else {
      write(outputStream, NULL);
    }
  }

  private <T> void writeConvertableProperty(
      Property<?> property, T value, OutputStream outputStream) throws IOException {

    writePropertyPrelude(property, outputStream);
    val v = property.getConverter().write(property.get(value));
    if (v != null) {
      outputStream.write('"');
      outputStream.write(((String) v).getBytes(StandardCharsets.UTF_8));
      outputStream.write('"');
    } else {
      write(outputStream, NULL);
    }
  }

  private <T> void writePrimitive(Property<?> property, T value, OutputStream outputStream)
      throws IOException {
    writePropertyPrelude(property, outputStream);
    write(outputStream, String.valueOf((Object) property.get(value)));
  }

  void writePropertyPrelude(Property<?> property, OutputStream outputStream) throws IOException {
    outputStream.write('"');
    write(outputStream, property.getWriteAlias());
    outputStream.write('"');
    outputStream.write(':');
  }

  private void write(OutputStream outputStream, String writeAlias) throws IOException {
    outputStream.write(writeAlias.getBytes(StandardCharsets.UTF_8));
  }

  private <T> void writePrologue(Class<T> type, OutputStream outputStream) throws IOException {
    if (isCollection(type)) {
      outputStream.write('[');
    } else {
      outputStream.write('{');
    }
  }

  private <T> void writeEpilogue(Class<T> type, OutputStream outputStream) throws IOException {

    if (isCollection(type)) {
      outputStream.write(']');
    } else {
      outputStream.write('}');
    }
  }

  private <T> boolean isCollection(Class<T> type) {
    return type.isArray() || Collection.class.isAssignableFrom(type);
  }
}
