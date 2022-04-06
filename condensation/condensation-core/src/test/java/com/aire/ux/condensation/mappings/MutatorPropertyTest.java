package com.aire.ux.condensation.mappings;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import com.aire.ux.condensation.TypeInstantiator;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

class MutatorPropertyTest {

  @Test
  @SneakyThrows
  void ensureMutatorNormalizedNameIsCorrect() {
    class A {

      int i;

      int getInt() {
        return i;
      }

      void setInt(int i) {
        this.i = i;
      }
    }
    val getter = A.class.getDeclaredMethod("getInt");
    val setter = A.class.getDeclaredMethod("setInt", int.class);
    val property =
        new MutatorProperty(mock(TypeInstantiator.class), getter, setter, A.class, "test", "test");
    assertEquals(property.getMemberNormalizedName(), "int");
    val instance = new A();
    property.set(instance, 1);
    assertEquals(1, (Integer) property.get(instance));
  }
}
