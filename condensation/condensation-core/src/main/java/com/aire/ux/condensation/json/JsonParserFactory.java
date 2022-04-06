package com.aire.ux.condensation.json;

import com.aire.ux.condensation.AstDocument;
import com.aire.ux.condensation.Document;
import com.aire.ux.condensation.Parser;
import com.aire.ux.condensation.ParserFactory;
import java.util.Locale;
import java.util.Objects;

public class JsonParserFactory implements ParserFactory {

  @Override
  public boolean supports(String format) {
    return Objects.equals(format.toLowerCase(Locale.ROOT), "json");
  }

  @Override
  public Parser newParser() {
    return new Parser() {
      @Override
      public Document parse(CharSequence sequence) {
        return new AstDocument(new JsonParser().parse(sequence));
      }
    };
  }
}
