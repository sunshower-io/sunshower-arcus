package com.aire.ux.plan.evaluators;

import static java.lang.String.format;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.select.css.CssSelectorParser.ElementSymbol;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.lambda.Option;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.val;

public class AttributeSelectorEvaluatorFactory extends AbstractMemoizingEvaluatorFactory {

  static final String WS = "\\s+";

  public AttributeSelectorEvaluatorFactory() {
    super(ElementSymbol.AttributeSelector);
  }

  @Override
  protected Evaluator createEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new AttributeSelectorEvaluator(node, context);
  }

  private static class AttributeSelectorEvaluator implements Evaluator {

    @Nullable private final String value;
    @Nonnull private final String attributeName;

    @Nullable private final ElementSymbol combinator;

    public AttributeSelectorEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      val children = node.getChildren();
      if (children.isEmpty()) {
        throw new IllegalArgumentException(
            format(
                "Somehow the parser did not catch an empty attribute selector list (node: %s)",
                node));
      }
      /** prevent children from narrowing further epochs */
      val results = node.clearChildren();
      attributeName = results.get(0).getSource().getLexeme();
      if (results.size() == 3) {
        value = normalize(results.get(2).getSource().getLexeme());
        combinator = (ElementSymbol) results.get(1).getSymbol();
      } else {
        value = null;
        combinator = null;
      }
    }

    static String normalize(String input) {
      val ch = input.charAt(0);
      if (ch == '\'' || ch == '"') {
        return input.substring(1, input.length() - 1);
      }
      return input;
    }

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
      val result = WorkingSet.<T>withExclusions(workingSet);
      return hom.reduce(
          workingSet,
          result,
          (t, u) -> {
            val attrs =
                combinator == null
                    ? selectAttributeExistance(result, t, hom)
                    : selectAttributeMatching(result, t, hom);
            u.addAll(attrs);
            return u;
          });
    }

    private <T> Option<T> selectAttributeExistance(
        WorkingSet<T> workingSet, T element, NodeAdapter<T> hom) {
      if (hom.hasAttribute(element, attributeName) && !workingSet.isExcluded(element)) {
        return Option.of(element);
      }
      workingSet.exclude(element);
      return Option.none();
    }

    private <T> Option<T> selectAttributeMatching(
        WorkingSet<T> workingSet, T element, NodeAdapter<T> hom) {
      val attribute = hom.getAttribute(element, attributeName);
      if (attribute == null) {
        return Option.none();
      }
      switch (combinator) {
        case StrictEquality:
          {
            if (Objects.equals(attribute, value) && !workingSet.isExcluded(element)) {
              return Option.of(element);
            }
            workingSet.exclude(element);
            break;
          }

        case Includes:
          {
            val values = attribute.split(WS);
            for (val value : values) {
              if (Objects.equals(this.value, value) && !workingSet.isExcluded(element)) {
                return Option.of(element);
              }
            }
            workingSet.exclude(element);
            break;
          }

        case DashMatch:
          {
            if ((Objects.equals(value, attribute) || attribute.startsWith(value + "-"))
                && !workingSet.isExcluded(element)) {
              return Option.of(element);
            }
            workingSet.exclude(element);
            break;
          }

        case PrefixMatch:
          {
            val values = attribute.split(WS);
            for (val value : values) {
              if (value.startsWith(this.value) && !workingSet.isExcluded(element)) {
                return Option.of(element);
              }
            }
            workingSet.exclude(element);
            break;
          }

        case SuffixMatch:
          {
            val values = attribute.split(WS);
            for (val value : values) {
              if (value.endsWith(this.value) && !workingSet.isExcluded(element)) {
                return Option.of(element);
              }
            }
            workingSet.exclude(element);
            break;
          }

        case SubstringMatch:
          {
            val values = attribute.split(WS);
            for (val value : values) {
              if (value.contains(this.value) && !workingSet.isExcluded(element)) {
                return Option.of(element);
              }
            }
            workingSet.exclude(element);
            break;
          }
      }
      return Option.none();
    }
  }
}
