package com.aire.ux.condensation;

import com.aire.ux.condensation.json.Value;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;

public interface TypeBinder {

  <T> T instantiate(Class<T> type);

  PropertyScanner getPropertyScanner();

  <T> TypeDescriptor<T> descriptorFor(Class<T> type);

  <T> T bind(Class<T> type, SyntaxNode<Value<?>, Token> root);
}
