package io.sunshower.arcus.incant;

import java.lang.reflect.Field;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class Fields {
    
    public static Predicate<Field> named(final String name) {
        return new NamePredicate(name);
    }

    public static Stream<Field> fields(Class<?> aClass) {
        return Stream.of(aClass.getDeclaredFields());
    }

    private static class NamePredicate implements Predicate<Field> {
        final String name;
        public NamePredicate(String name) {
            this.name = name;
        }

        @Override
        public boolean test(Field field) {
            return name.equals(field.getName());
        }
    }
}
