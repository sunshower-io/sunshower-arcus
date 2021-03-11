package io.sunshower.arcus.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import java.io.Reader;
import java.util.Set;
import lombok.val;

public class YAMLConfigurationReader implements ConfigurationReader {

    static final Set<String> FORMATS = Set.of("application/x-yaml", "text/yaml");

    @Override
    public boolean handles(String format) {
        return FORMATS.contains(format);
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
