package io.sunshower.arcus.condensation;

import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;

public interface TypeBinder<E extends Enum<E>> {

  <T> T instantiate(Class<T> type);

  PropertyScanner getPropertyScanner();

  <T> TypeDescriptor<T> descriptorFor(Class<T> type);

  <T> T bind(Class<T> type, SyntaxNode<Value<?, E>, Token> root);
}
