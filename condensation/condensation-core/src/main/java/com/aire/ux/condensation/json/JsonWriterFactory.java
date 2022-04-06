package com.aire.ux.condensation.json;

import com.aire.ux.condensation.DocumentWriter;
import com.aire.ux.condensation.TypeBinder;
import com.aire.ux.condensation.TypeInstantiator;
import com.aire.ux.condensation.WriterFactory;
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
