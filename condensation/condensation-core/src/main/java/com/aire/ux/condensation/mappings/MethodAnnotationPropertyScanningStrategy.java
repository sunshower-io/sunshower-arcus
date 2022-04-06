package com.aire.ux.condensation.mappings;

import static com.aire.ux.condensation.mappings.FieldAnnotationPropertyScanningStrategy.NONE;

import com.aire.ux.condensation.Attribute;
import com.aire.ux.condensation.Element;
import com.aire.ux.condensation.Property;
import com.aire.ux.condensation.TypeInstantiator;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import lombok.val;

@SuppressWarnings("PMD")
public class MethodAnnotationPropertyScanningStrategy implements PropertyScanningStrategy {

  private final TypeInstantiator instantiator;

  public MethodAnnotationPropertyScanningStrategy(TypeInstantiator typeInstantiator) {
    this.instantiator = typeInstantiator;
  }

  @Override
  public <T> Set<Property<?>> scan(Class<T> type) {
    val results = new HashSet<Property<?>>();
    try {
      val info = Introspector.getBeanInfo(type, Object.class);
      for (val descriptor : info.getPropertyDescriptors()) {
        val property = propertyFrom(type, descriptor);
        if (property != null) {
          results.add(property);
        }
      }
    } catch (IntrospectionException ex) {

    }
    return results;
  }

  private Property<?> propertyFrom(Class<?> type, PropertyDescriptor descriptor) {
    val readMethod = descriptor.getReadMethod();
    val writeMethod = descriptor.getWriteMethod();
    val result = doScan(type, readMethod, writeMethod, descriptor);
    checkForBoth(readMethod, writeMethod, Element.class);
    checkForBoth(readMethod, writeMethod, Attribute.class);
    return result;
  }

  private Property<?> doScan(
      Class<?> type, Method readMethod, Method writeMethod, PropertyDescriptor descriptor) {

    val element = locateOn(Element.class, readMethod, writeMethod);
    if (element != null) {
      return scanElement(type, element, readMethod, writeMethod, descriptor);
    }

    val attribute = locateOn(Attribute.class, readMethod, writeMethod);
    if (attribute != null) {
      return scanAttribute(type, attribute, readMethod, writeMethod, descriptor);
    }
    return null;
  }

  private Property<?> scanAttribute(
      Class<?> type,
      Attribute attribute,
      Method readMethod,
      Method writeMethod,
      PropertyDescriptor descriptor) {
    val alias = attribute.alias();
    val readAlias = NONE.equals(alias.read()) ? descriptor.getDisplayName() : alias.read();
    val writeAlias = NONE.equals(alias.write()) ? descriptor.getDisplayName() : alias.write();
    return new MutatorProperty(instantiator, readMethod, writeMethod, type, readAlias, writeAlias);
  }

  private Property<?> scanElement(
      Class<?> type,
      Element element,
      Method readMethod,
      Method writeMethod,
      PropertyDescriptor descriptor) {
    val alias = element.alias();
    val readAlias = NONE.equals(alias.read()) ? descriptor.getDisplayName() : alias.read();
    val writeAlias = NONE.equals(alias.write()) ? descriptor.getDisplayName() : alias.write();
    return new MutatorProperty(instantiator, readMethod, writeMethod, type, readAlias, writeAlias);
  }

  private <T extends Annotation> T locateOn(Class<T> elementClass, Method... methods) {
    for (val method : methods) {
      if (method != null && method.isAnnotationPresent(elementClass)) {
        return method.getAnnotation(elementClass);
      }
    }
    return null;
  }

  private void checkForBoth(
      Method readMethod, Method writeMethod, Class<? extends Annotation> annotation) {
    if (readMethod.isAnnotationPresent(annotation) && writeMethod.isAnnotationPresent(annotation)) {
      throw new IllegalStateException(
          String.format(
              "Error: annotation type '%s' is present on both read method (%s) and write method of (%s) of %s",
              readMethod, writeMethod, readMethod.getDeclaringClass()));
    }
  }
}
