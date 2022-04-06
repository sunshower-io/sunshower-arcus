package io.sunshower.arcus.condensation;

public interface WriterFactory extends FormatAware {

  DocumentWriter newWriter(TypeBinder binder, TypeInstantiator instantiator);
}
