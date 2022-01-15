package io.sunshower.arcus.ast;

@FunctionalInterface
public interface RewriteRule<T, U> {

  SyntaxNode<T, U> apply(SyntaxNode<T, U> node);
}
