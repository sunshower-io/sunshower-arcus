package io.sunshower.persist.condensation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.condensation.Alias;
import io.sunshower.arcus.condensation.Attribute;
import io.sunshower.arcus.condensation.Element;
import io.sunshower.arcus.condensation.RootElement;
import io.sunshower.persistence.config.DataSourceConfiguration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@RootElement
@SuppressFBWarnings
public class CondensationMappedDataSourceConfiguration implements DataSourceConfiguration {

  @Element(alias = @Alias(read = "scanned-packages", write = "scanned-packages"))
  @Setter
  private String[] scannedPackages;

  @Setter
  @Attribute(alias = @Alias(read = "driver-class", write = "driver-class"))
  private String driverClassName;

  @Setter @Attribute private String username;

  @Setter @Attribute private String password;

  @Setter @Attribute private Mode mode;

  @Element(alias = @Alias(read = "additional-properties", write = "additional-properties"))
  private Map<String, String> additionalProperties;

  @Setter
  @Attribute(alias = @Alias(read = "connection-string", write = "connection-string"))
  private String connectionString;

  @Getter
  @Attribute(alias = @Alias(read = "migration-locations", write = "migration-locations"))
  private List<String> migrationLocations;

  @Override
  public Mode getMode() {
    return mode;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getConnectionString() {
    return connectionString;
  }

  @Override
  public String[] getScannedPackages() {
    return scannedPackages;
  }

  @Override
  public String getDriverClassName() {
    return driverClassName;
  }

  @Override
  public List<String> getMigrationLocations() {
    return migrationLocations != null ? migrationLocations : Collections.emptyList();
  }

  @Override
  public Map<String, String> getAdditionalProperties() {
    if (additionalProperties == null) {
      additionalProperties = new HashMap<>();
    }
    return additionalProperties;
  }
}
