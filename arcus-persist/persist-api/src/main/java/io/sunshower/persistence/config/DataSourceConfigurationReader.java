package io.sunshower.persistence.config;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public interface DataSourceConfigurationReader {

  boolean handles(URL url);

  List<DataSourceConfiguration> read(InputStream inputStream);
}
