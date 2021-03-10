package io.sunshower.persist.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.test.common.TestConfigurationConfiguration;
import java.sql.Connection;
import java.sql.SQLException;
import javax.inject.Inject;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
  classes = {DataSourceConfiguration.class, TestConfigurationConfiguration.class}
)
public class DataSourceConfigurationTest {
  @Inject private DataSource dataSource;

  @Inject private DatabaseConfigurationSource source;

  @Test
  public void ensureConfigurationIsInjected() {
    assertThat(source, is(not(nullValue())));
  }

  @Test
  public void ensureDataSourceIsInjected() {
    assertThat(dataSource, is(not(nullValue())));
  }

  @Test
  public void ensureDataSourceIsLive() throws SQLException {
    try (Connection cnx = dataSource.getConnection()) {
      cnx.prepareStatement("SELECT * FROM INFORMATION_SCHEMA.TABLES").execute();
    }
  }
}
