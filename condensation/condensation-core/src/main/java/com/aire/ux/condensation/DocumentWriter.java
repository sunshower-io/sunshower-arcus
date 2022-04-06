package com.aire.ux.condensation;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import lombok.NonNull;
import lombok.val;

public interface DocumentWriter {

  /**
   * @param type the type to write
   * @param value the value of type {@code type} to write
   * @param outputStream the OutputStream to write to
   * @param <T> the type of the value to write
   */
  <T> void write(@NonNull Class<T> type, @NonNull T value, @NonNull OutputStream outputStream)
      throws IOException;

  <T> void writeAll(
      @NonNull Class<T> type,
      @NonNull Collection<? extends T> value,
      @NonNull OutputStream outputStream)
      throws IOException;

  default <T> String writeAll(@NonNull Class<T> type, @NonNull Collection<? extends T> value)
      throws IOException {
    val outputStream = new ByteArrayOutputStream();
    writeAll(type, value, outputStream);
    return outputStream.toString(StandardCharsets.UTF_8);
  }
  /**
   * @param type the type to write
   * @param value the value to write
   * @param <T> the type-parameter
   * @return the value written to a document
   * @throws IOException if anything bad happens
   */
  default <T> String write(@NonNull Class<T> type, @NonNull T value) throws IOException {
    val outputStream = new ByteArrayOutputStream();
    write(type, value, outputStream);
    return outputStream.toString(StandardCharsets.UTF_8);
  }
}
