package io.sunshower.arcus.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import io.sunshower.lang.tuple.Pair;
import java.io.Reader;
import java.util.Set;
import lombok.val;

public class YAMLConfigurationReader implements ConfigurationReader {

  static final Set<Pair<String, String>> FORMATS =
      Set.of(
          Pair.of("yml", "text/yaml"),
          Pair.of("yaml", "text/yaml"),
          Pair.of("yml", "application/x-yaml"),
          Pair.of("yaml", "application/x-yaml"));

  @Override
  public Set<Pair<String, String>> knownFileTypes() {
    return FORMATS;
  }

  @Override
  public boolean handles(String format) {
    return FORMATS.stream().anyMatch(t -> t.snd.equals(format));
  }

  @Override
  public <T> T read(Class<T> type, Reader reader) throws Exception {
    return objectMapper().readerFor(type).readValue(reader);
  }

  private ObjectMapper objectMapper() {
    val mapper = new ObjectMapper(new YAMLFactory());
    mapper.registerModule(new JaxbAnnotationModule());
    return mapper;
  }
}
