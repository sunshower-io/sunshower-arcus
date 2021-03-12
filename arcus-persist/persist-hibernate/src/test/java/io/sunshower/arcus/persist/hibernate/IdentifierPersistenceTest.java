package io.sunshower.arcus.persist.hibernate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestPersistenceConfiguration.class)
public class IdentifierPersistenceTest {

  @Test
  void test() {}
}
