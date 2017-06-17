package io.sunshower.lang;

import io.sunshower.lang.tuple.Pair;

import java.util.Set;

/**
 * Created by haswell on 4/20/16.
 */
public interface PropertyAware {


    boolean hasProperty(String key);

    String getProperty(String key);

    String removeProperty(String key);

    Set<Pair<String, String>> getProperties();

    boolean addProperty(String key, String value);


}
