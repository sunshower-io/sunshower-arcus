package io.sunshower.arcus.condensation;

public interface ParserFactory extends FormatAware {

  boolean supports(String format);

  Parser newParser();
}
