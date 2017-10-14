package io.sunshower.arcus.incant;

import io.sunshower.lang.PropertyAware;
import io.sunshower.lang.tuple.Pair;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by haswell on 4/20/16.
 */
public class LazyPropertyAwareTest {


    @Test
    public void ensurePropertyAwareHasPropertyReturnsFalseWhenNoPropertyIsAdded() {
        assertThat(new LazyPropertyAware().hasProperty("hello"), is(false));
    }

    @Test
    public void ensureHasPropertyReturnsFalseWhenPropertiesIsEmpty () {

        PropertyAware aware = new LazyPropertyAware();
        aware.addProperty("whatever", "whatever");
        aware.removeProperty("whatever");
        assertThat(aware.hasProperty("whatever"), is(false));
    }

    @Test
    public void ensureHasPropertyReturnsTrueWhenPropertyExists() {

        PropertyAware aware = new LazyPropertyAware();
        aware.addProperty("whatever", "whatever");
        assertThat(aware.hasProperty("whatever"), is(true));
    }

    @Test
    public void ensureGetPropertyReturnsPropertyWhenPropertyExists() {
        PropertyAware aware = new LazyPropertyAware();
        aware.addProperty("whatever", "whatever");
        assertThat(aware.getProperty("whatever"), is("whatever"));
    }


    @Test
    public void ensureGetPropertiesReturnsEmptySetWhenNoPropertiesHaveBeenAdded() {

        PropertyAware aware = new LazyPropertyAware();
        assertThat(aware.getProperties().isEmpty(), is(true));
    }

    @Test
    public void ensureGetPropertiesReturnsPropertiesWhenPropertiesExist() {
        PropertyAware aware = new LazyPropertyAware();
        aware.addProperty("whatever", "whatever");
        Set<Pair<String, String>> results = Collections.singleton(Pair.of("whatever", "whatever"));
        assertThat(aware.getProperties(), is(results));
    }

    @Test
    public void ensureGettingNonExistantPropertyReturnsNothing() {
        assertThat(new LazyPropertyAware().getProperty("whatever"), is(nullValue()));
    }

    @Test
    public void ensureRemovingNonExistantPropertyReturnsNull() {
        assertThat(new LazyPropertyAware().removeProperty("whatever"), is(nullValue()));
    }
}