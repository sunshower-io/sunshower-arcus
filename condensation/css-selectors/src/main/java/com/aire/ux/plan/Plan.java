package com.aire.ux.plan;

import com.aire.ux.test.NodeAdapter;
import java.util.List;

public interface Plan extends AutoCloseable {

  <T> WorkingSet<T> evaluate(T tree, NodeAdapter<T> hom);

  <T> WorkingSet<T> evaluate(WorkingSet<T> tree, NodeAdapter<T> hom);

  <T extends Evaluator> List<T> getEvaluators(Class<T> evaluatorType);
}
