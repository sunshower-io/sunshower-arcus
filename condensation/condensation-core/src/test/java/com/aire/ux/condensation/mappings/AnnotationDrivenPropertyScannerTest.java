package com.aire.ux.condensation.mappings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.condensation.Attribute;
import com.aire.ux.condensation.PropertyScanner;
import com.aire.ux.condensation.RootElement;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnotationDrivenPropertyScannerTest {

  private PropertyScanner scanner;
  private ReflectiveTypeInstantiator instantiator;

  @BeforeEach
  void setUp() {
    instantiator = new ReflectiveTypeInstantiator();
    scanner =
        new AnnotationDrivenPropertyScanner(new CachingDelegatingTypeInstantiator(instantiator));
  }

  @Test
  void ensurePropertyScannerCanScanRootElements() {
    @RootElement
    class A {}
    val properties = scanner.scan(A.class);
    assertEquals(0, properties.getProperties().size());
  }

  @Test
  void ensurePropertyScannerCanScanSimpleFieldPropertyOnTypeWithPrivateVisibility() {
    @RootElement
    class A {
      @Attribute private int numberProperty;
    }
    instantiator.register(A.class, A::new);
    val properties = scanner.scan(A.class);
    assertEquals(1, properties.getProperties().size());
    val property = properties.getProperty(0);
    assertEquals("numberProperty", property.getMemberReadName());
    assertEquals("numberProperty", property.getMemberWriteName());

    val host = new A();
    property.set(host, 10);
    assertEquals(10, (int) property.get(host));
  }
}
