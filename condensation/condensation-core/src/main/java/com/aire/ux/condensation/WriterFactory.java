package com.aire.ux.condensation;

public interface WriterFactory extends FormatAware {

  DocumentWriter newWriter(TypeBinder binder, TypeInstantiator instantiator);
}
