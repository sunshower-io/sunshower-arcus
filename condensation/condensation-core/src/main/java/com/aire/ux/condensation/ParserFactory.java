package com.aire.ux.condensation;

public interface ParserFactory extends FormatAware {

  boolean supports(String format);

  Parser newParser();
}
