package com.aire.ux.condensation;

public interface Converter<T, U> {

  T read(U u);

  U write(T t);
}
