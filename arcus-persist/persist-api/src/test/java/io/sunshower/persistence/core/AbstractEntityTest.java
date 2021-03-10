package io.sunshower.persistence.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.common.Identifier;
import io.sunshower.persist.Sequence;
import org.junit.jupiter.api.Test;

public class AbstractEntityTest {

  @Test
  public void ensureAbstractEntityIsEqualToItself() {
    final MockEntity e = new MockEntity(0L);
    assertThat(e, is(e));
  }

  @Test
  public void ensureDifferentEntitiesAreNotEqual() {
    final MockEntity e = new MockEntity(0L);
    final MockEntity f = new MockEntity(1L);
    assertThat(e, is(not(f)));
  }

  @Test
  public void ensureStructurallyEquivalentEntitiesAreEqual() {
    final MockEntity e = new MockEntity(0L);
    final MockEntity f = new MockEntity(0L);
    assertThat(e, is(f));
  }

  static class MockEntity extends AbstractEntity<Long> {

    long id;

    MockEntity(long id) {
      super(id);
    }

    @Override
    public Long getId() {
      return id;
    }

    @Override
    public void setId(Long id) {

      this.id = id;
    }

    @Override
    public Identifier getIdentifier() {
      throw new UnsupportedOperationException("");
    }

    @Override
    public Sequence<Long> getSequence() {
      return new Sequence<Long>() {
        long l = 0;

        @Override
        public Long next() {
          return ++l;
        }
      };
    }
  }
}
