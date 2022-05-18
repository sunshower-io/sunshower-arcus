package io.sunshower.persist.condensation;

import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.persistence.config.DataSourceConfiguration;
import io.sunshower.persistence.config.DataSourceConfigurationReader;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import lombok.val;

public class CondensationDataSourceConfigurationReader implements DataSourceConfigurationReader {

  @Override
  public boolean handles(URL url) {
    return url.getFile().contains(".json");
  }

  @Override
  public List<DataSourceConfiguration> read(InputStream inputStream) {
    val condensation = Condensation.create("json");
    return condensation.readAll(
        CondensationMappedDataSourceConfiguration.class, ArrayList::new, inputStream);
  }
}
