package io.sunshower.persistence;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PersistenceUnitTest {

  @Test
  public void ensureParseLocationDoesntChangeHardcodedValue() {
    String location = PersistenceUnit.parseLocation("classpath:h2", Dialect.Postgres);
    assertThat(location, is("classpath:h2"));
  }

  @Test
  public void ensurePersistenceUnitCreatesCorrectPathForPostgres() {
    String location = PersistenceUnit.parseLocation("classpath:{dialect}", Dialect.Postgres);
    assertThat(location, is("classpath:postgres"));
  }
}
