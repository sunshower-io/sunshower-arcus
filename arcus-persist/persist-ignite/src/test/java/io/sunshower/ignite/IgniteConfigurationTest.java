package io.sunshower.ignite;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

import io.sunshower.test.common.TestConfigurationConfiguration;
import javax.inject.Inject;
import org.apache.ignite.Ignite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
  classes = {IgniteNodeConfiguration.class, TestConfigurationConfiguration.class}
)
public class IgniteConfigurationTest {

  @Inject private Ignite ignite;

  @Inject private IgniteConfigurationSource igniteConfiguration;

  @Test
  public void ensureDiscoveryModeIsCorrect() {
    String mode = igniteConfiguration.getDiscovery().mode();
    assertThat(mode, is("vm-local"));
  }

  @Test
  public void ensureFabricNameIsCorrect() {
    assertThat(igniteConfiguration.getFabricName(), is("sunshower-data-fabric"));
  }

  @Test
  void ensurePeerClassloadingEnabledIsSet() {
    assertThat(igniteConfiguration.isPeerClassloadingEnabled(), is(true));
  }

  @Test
  public void ensureNameIsInjected() {
    assertThat(igniteConfiguration.getFabricName(), is(not(nullValue())));
  }

  @Test
  public void ensureNameIsExpected() {
    assertThat(igniteConfiguration.getFabricName(), is("sunshower-data-fabric"));
  }

  @Test
  public void ensureIgniteCanStartFromProperlyConfiguredNode() {}
}
