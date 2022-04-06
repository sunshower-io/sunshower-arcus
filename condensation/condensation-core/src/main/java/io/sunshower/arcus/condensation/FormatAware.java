package io.sunshower.arcus.condensation;

import lombok.NonNull;

public interface FormatAware {

  boolean supports(@NonNull String format);
}
