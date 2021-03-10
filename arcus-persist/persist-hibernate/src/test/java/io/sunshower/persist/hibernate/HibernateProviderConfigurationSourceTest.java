package io.sunshower.persist.hibernate;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sunshower.persist.HibernateTestCase;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class HibernateProviderConfigurationSourceTest extends HibernateTestCase {

  @Inject private HibernateProviderConfigurationSource props;

  @Test
  public void ensurePropertiesAreInjected() {
    assertThat(props, is(not(nullValue())));
  }

  @Test
  public void ensureProviderIsNotNull() {
    assertThat(props.getProvider(), is(not(nullValue())));
  }

  @Test
  public void ensureDdlShowSqlExists() {
    assertThat(props.ddl().showSql(), is(false));
  }

  @Test
  public void ensureDdlGenerateExists() {
    assertThat(props.ddl().generate(), is(false));
  }

  @Test
  public void ensureSearchTypeIsCorrect() {
    assertThat(props.getSearch().type(), is("hibernate.search.default.directory_provider"));
  }

  @Test
  public void ensureSearchValueIsCorrect() {
    assertThat(props.getSearch().value(), is("local-heap"));
  }

  @Test
  public void ensureRegionFactoryIsCorrect() {
    String regionFactory = props.cache().regionFactory();
    assertThat(regionFactory, is("infinispan"));
  }

  @Test
  public void ensureL2CacheIsEnabled() {
    boolean queryCacheEnabled = props.cache().enabled();
    assertTrue(queryCacheEnabled);
  }
}
