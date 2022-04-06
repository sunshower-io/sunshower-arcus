package com.aire.ux.condensation.mappings;

import com.aire.ux.condensation.Attribute;
import com.aire.ux.condensation.Element;
import com.aire.ux.condensation.Property;
import com.aire.ux.condensation.TypeInstantiator;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.val;

public class FieldAnnotationPropertyScanningStrategy implements PropertyScanningStrategy {

  public static final String NONE = "..none..";
  private final TypeInstantiator instantiator;

  public FieldAnnotationPropertyScanningStrategy(final TypeInstantiator instantiator) {
    this.instantiator = instantiator;
  }

  @Override
  public <T> Set<Property<?>> scan(Class<T> type) {
    val fields = type.getDeclaredFields();
    val result = new LinkedHashSet<Property<?>>();
    for (val field : fields) {
      boolean isElement = false;
      if (field.isAnnotationPresent(Element.class)) {
        result.add(constructElementProperty(type, field, field.getAnnotation(Element.class)));
        isElement = true;
      }
      if (field.isAnnotationPresent(Attribute.class)) {
        if (isElement) {
          throw new IllegalArgumentException(
              String.format(
                  "Error: field '%s' on class '%s' has both @Element and @Attribute annotations",
                  type, field));
        }
        result.add(constructAttributeProperty(type, field, field.getAnnotation(Attribute.class)));
      }
    }
    return result;
  }

  private <T> Property<?> constructAttributeProperty(
      Class<T> type, Field field, Attribute annotation) {
    val alias = annotation.alias();
    val readAlias = NONE.equals(alias.read()) ? field.getName() : alias.read();
    val writeAlias = NONE.equals(alias.write()) ? field.getName() : alias.write();
    return new FieldProperty(instantiator, field, type, readAlias, writeAlias);
  }

  private <T> Property<?> constructElementProperty(Class<T> type, Field field, Element annotation) {
    val alias = annotation.alias();
    val readAlias = NONE.equals(alias.read()) ? field.getName() : alias.read();
    val writeAlias = NONE.equals(alias.write()) ? field.getName() : alias.write();
    return new FieldProperty(instantiator, field, type, readAlias, writeAlias);
  }
}
