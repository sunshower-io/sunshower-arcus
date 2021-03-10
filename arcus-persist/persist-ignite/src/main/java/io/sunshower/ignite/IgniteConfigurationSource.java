package io.sunshower.ignite;

import static org.apache.ignite.internal.util.IgniteUtils.resolveClassLoader;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;

@Slf4j
@Getter
public class IgniteConfigurationSource {

  private final String fabricName;

  private final IgniteMemorySettings memory;

  private final IgniteDiscoverySettings discovery;
  private final boolean peerClassloadingEnabled;

  public IgniteConfigurationSource(
      String name,
      IgniteMemorySettings memory,
      IgniteDiscoverySettings discovery,
      boolean peerClassloadingEnabled) {
    this.memory = memory;
    this.fabricName = name;
    this.discovery = discovery;
    this.peerClassloadingEnabled = peerClassloadingEnabled;
  }

  public String getFabricName() {
    return fabricName;
  }

  public IgniteMemorySettings getMemory() {
    return memory;
  }

  public IgniteConfiguration toNative() {
    final IgniteConfiguration cfg = new IgniteConfiguration();
    cfg.setIgniteInstanceName(this.fabricName);
    cfg.setCacheConfiguration(cacheConfiguration());
    if (peerClassloadingEnabled) {
      log.info("Peer classloading is enabled");
    }
    cfg.setPeerClassLoadingEnabled(peerClassloadingEnabled);
    configureDiscovery(cfg);

    configureSerialization(cfg);
    return cfg;
  }

  private void configureSerialization(IgniteConfiguration cfg) {
    cfg.setClassLoader(new ThreadLocalProxyingClassLoader(resolveClassLoader(cfg)));
  }

  private void configureDiscovery(IgniteConfiguration cfg) {
    if (!(discovery == null || discovery.mode() == null)) {

      if (discovery.mode().trim().equals("vm-local")) {
        log.info("Configuring TCP Discovery");
        TcpDiscoverySpi disco = new TcpDiscoverySpi().setIpFinder(new TcpDiscoveryVmIpFinder(true));
        cfg.setDiscoverySpi(disco);
      }

      if (discovery.mode().trim().equals("multicast")) {
        log.info("Configuring multicast discovery");
      }
    }
  }

  public IgniteDiscoverySettings getDiscovery() {
    return discovery;
  }

  protected CacheConfiguration<?, ?> cacheConfiguration() {
    final CacheConfiguration<?, ?> cfg = new CacheConfiguration<>();
    cfg.setName(this.fabricName == null ? "default-ignite-instance" : this.fabricName);
    return cfg;
  }

  protected long externalMax() {
    return memory != null ? memory.max() : 0;
  }

  public interface IgniteMemorySettings {

    long max();

    String mode();
  }

  public interface IgniteDiscoverySettings {

    default String mode() {
      return "vm-local";
    }
  }
}
