package io.sunshower.arcus.condensation.json;

import io.sunshower.arcus.condensation.Parser;
import io.sunshower.arcus.condensation.ParserFactory;
import java.util.Locale;
import java.util.Objects;

public class JsonParserFactory implements ParserFactory {

  @Override
  public boolean supports(String format) {
    return Objects.equals(format.toLowerCase(Locale.ROOT), "json");
  }

  @Override
  public Parser newParser() {
    return sequence -> new JsonDocument(new JsonParser().parse(sequence));
  }
}
