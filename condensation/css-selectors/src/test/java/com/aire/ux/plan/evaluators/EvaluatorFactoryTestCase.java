package com.aire.ux.plan.evaluators;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.fail;

import com.aire.ux.plan.DefaultPlanContext;
import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
import com.aire.ux.test.Node;
import com.aire.ux.test.NodeAdapter;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;

public abstract class EvaluatorFactoryTestCase extends TestCase {

  protected Node tree;
  protected PlanContext context;
  protected EvaluatorFactory factory;

  @BeforeEach
  protected void setUp() {
    super.setUp();
    factory = createFactory();
    context = DefaultPlanContext.getInstance();
  }

  protected WorkingSet<Node> eval(String selector, Node root) {
    return eval(selector, root, Node.getAdapter());
  }

  protected void assertContainsTypes(WorkingSet<Node> nodes, String... types) {
    val ts = nodes.stream().map(t -> t.getType()).collect(Collectors.toSet());
    for (val t : types) {
      if (!ts.contains(t)) {
        fail(format("Expected type: '%s' out of %s", t, ts));
      }
    }
  }

  protected <T> WorkingSet<T> eval(String selector, T root, NodeAdapter<T> adapter) {
    val s = parser.parse(selector);
    val plan = s.plan(context);
    return plan.evaluate(root, adapter);
  }

  protected abstract EvaluatorFactory createFactory();

  protected <T> T at(WorkingSet<T> result, int i) {
    val iter = result.iterator();

    var j = 0;
    while (true) {
      val current = iter.next();
      if (j++ == i) {
        return current;
      }
    }
  }
}
