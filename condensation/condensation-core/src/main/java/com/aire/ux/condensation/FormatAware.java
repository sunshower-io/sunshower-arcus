package com.aire.ux.condensation;

import lombok.NonNull;

public interface FormatAware {

  boolean supports(@NonNull String format);
}
