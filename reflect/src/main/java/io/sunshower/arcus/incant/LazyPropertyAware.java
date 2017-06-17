package io.sunshower.arcus.incant;

import io.sunshower.lang.PropertyAware;
import io.sunshower.lang.tuple.Pair;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by haswell on 4/20/16.
 */
@ThreadSafe
public class LazyPropertyAware implements PropertyAware {

    private Map<String, String> properties;

    @Override
    public boolean hasProperty(String key) {
        if(properties == null || properties.isEmpty()) {
            return false;
        }
        return properties.containsKey(key);
    }

    @Override
    public String getProperty(String key) {
        if(properties == null) {
            return null;
        }
        return properties.get(key);
    }

    @Override
    public Set<Pair<String, String>> getProperties() {
        if(properties == null || properties.isEmpty()) {
            return Collections.emptySet();
        }
        return properties.entrySet().stream().map(
                f -> Pair.of(f.getKey(), f.getValue())
        ).collect(Collectors.toSet());
    }

    @Override
    public boolean addProperty(String key, String value) {
        check();
        return properties.put(key, value) == null;
    }


    @Override
    public String removeProperty(String key) {
        if(properties == null || properties.isEmpty()) {
            return null;
        }
        return properties.remove(key);
    }

    private void check() {
        if(properties == null) {
            synchronized(this) {
                if(properties == null) {
                    properties = new HashMap<>();
                }
            }
        }
    }
}
