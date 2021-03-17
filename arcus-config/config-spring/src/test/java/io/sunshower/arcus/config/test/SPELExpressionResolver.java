package io.sunshower.arcus.config.test;

public class SPELExpressionResolver implements ExpressionResolver {

  static class EvaluationContext {
    //    static final Tests TESTS = Tests.getInstance();
  }

  @Override
  public String resolve(String expression) {
    return expression;
    //    val expressionParser = new SpelExpressionParser();
    //    val context = new HashMap<String, Object>();
    //
    //    val evaluationContext = new StandardEvaluationContext(context);
    //    expressionParser.parseExpression(expression)
  }
}
