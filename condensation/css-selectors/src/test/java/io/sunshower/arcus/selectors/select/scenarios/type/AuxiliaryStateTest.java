package io.sunshower.arcus.selectors.select.scenarios.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.selectors.ScenarioTestCase;
import io.sunshower.arcus.selectors.plan.PlanContext;
import io.sunshower.arcus.selectors.plan.evaluators.StateSelectorEvaluatorFactory;
import io.sunshower.arcus.selectors.test.Node;
import io.sunshower.arcus.selectors.test.Node.DomStates;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class AuxiliaryStateTest extends ScenarioTestCase {

  static {
    PlanContext.register(new StateSelectorEvaluatorFactory(), Node.DomStates.values());
  }

  public static List<String> names() {
    return Arrays.stream(DomStates.values())
        .map(t -> t.toSymbol().name())
        .collect(Collectors.toList());
  }

  @ParameterizedTest
  @MethodSource("names")
  void ensureStatesCanBeSelected(String value) {

    val root =
        parseString(
            "<html>\n"
                + "  <body>\n"
                + "    <ul>\n"
                + "      <li>one</li>\n"
                + "      <li>two</li>\n"
                + "      <li>three</li>\n"
                + "    </ul>\n"
                + "  </body>\n"
                + "</html>\n");

    val li = at(eval("ul > li:nth-child(3)", root), 0);
    val state = (((DomStates) Node.getAdapter().stateFor(value)).getValue());
    li.setState(Node.getAdapter().stateFor(value));
    val result = at(eval(state, root), 0);
    assertEquals(result, li);
  }
}
