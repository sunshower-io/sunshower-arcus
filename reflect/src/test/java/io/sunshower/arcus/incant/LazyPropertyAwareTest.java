package io.sunshower.arcus.incant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.sunshower.lang.PropertyAware;
import io.sunshower.lang.tuple.Pair;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class LazyPropertyAwareTest {

    @Test
    public void ensurePropertyAwareHasPropertyReturnsFalseWhenNoPropertyIsAdded() {
        assertEquals(new LazyPropertyAware().hasProperty("hello"), (false));
    }

    @Test
    public void ensureHasPropertyReturnsFalseWhenPropertiesIsEmpty() {

        PropertyAware aware = new LazyPropertyAware();
        aware.addProperty("whatever", "whatever");
        aware.removeProperty("whatever");
        assertEquals(aware.hasProperty("whatever"), (false));
    }

    @Test
    public void ensureHasPropertyReturnsTrueWhenPropertyExists() {

        PropertyAware aware = new LazyPropertyAware();
        aware.addProperty("whatever", "whatever");
        assertEquals(aware.hasProperty("whatever"), (true));
    }

    @Test
    public void ensureGetPropertyReturnsPropertyWhenPropertyExists() {
        PropertyAware aware = new LazyPropertyAware();
        aware.addProperty("whatever", "whatever");
        assertEquals(aware.getProperty("whatever"), ("whatever"));
    }

    @Test
    public void ensureGetPropertiesReturnsEmptySetWhenNoPropertiesHaveBeenAdded() {

        PropertyAware aware = new LazyPropertyAware();
        assertEquals(aware.getProperties().isEmpty(), (true));
    }

    @Test
    public void ensureGetPropertiesReturnsPropertiesWhenPropertiesExist() {
        PropertyAware aware = new LazyPropertyAware();
        aware.addProperty("whatever", "whatever");
        Set<Pair<String, String>> results = Collections.singleton(Pair.of("whatever", "whatever"));
        assertEquals(aware.getProperties(), (results));
    }

    @Test
    public void ensureGettingNonExistantPropertyReturnsNothing() {
        assertNull(new LazyPropertyAware().getProperty("whatever"));
    }

    @Test
    public void ensureRemovingNonExistantPropertyReturnsNull() {
        assertNull(new LazyPropertyAware().removeProperty("whatever"));
    }
}
