package io.sunshower.arcus.condensation;

import java.io.InputStream;

public interface Parser {

  Document parse(CharSequence sequence);
  Document parse(InputStream sequence);
}
