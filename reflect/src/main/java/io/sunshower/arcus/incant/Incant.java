package io.sunshower.arcus.incant;

import io.sunshower.arcus.reflect.Reflect;

import java.lang.reflect.Field;
import java.util.Objects;

public class Incant {

    @SuppressWarnings("unchecked")
    public static <T, U> U fieldValue(T instance, String fieldName) {
        Objects.requireNonNull(instance);
        return (U) fieldValue((Class<T>) instance.getClass(), instance, fieldName);
    }

    @SuppressWarnings("unchecked")
    public static <T, U> U fieldValue(Class<T> type, T instance, String fieldName) {
        final Field field = Reflect.collectOverHierarchy(
                type, 
                Fields::fields
        ).filter(Fields.named(fieldName)).findFirst().get();
        field.setAccessible(true);
        try {
            return (U) field.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
