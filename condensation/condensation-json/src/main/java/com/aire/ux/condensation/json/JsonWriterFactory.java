package com.aire.ux.condensation.json;

import io.sunshower.arcus.condensation.DocumentWriter;
import io.sunshower.arcus.condensation.TypeBinder;
import io.sunshower.arcus.condensation.TypeInstantiator;
import io.sunshower.arcus.condensation.WriterFactory;
import java.util.Locale;
import lombok.NonNull;

public class JsonWriterFactory implements WriterFactory {

  @Override
  public boolean supports(@NonNull String format) {
    return "json".equals(format.toLowerCase(Locale.ROOT).trim());
  }

  @Override
  public DocumentWriter newWriter(TypeBinder typeBinder, TypeInstantiator instantiator) {
    return new JsonWriter(typeBinder, instantiator);
  }
}
