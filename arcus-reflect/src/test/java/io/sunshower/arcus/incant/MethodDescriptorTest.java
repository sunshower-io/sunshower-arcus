package io.sunshower.arcus.incant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MethodDescriptorTest {

  @Test
  @SuppressWarnings("unchecked")
  void ensureAssignmentMatches() throws NoSuchMethodException {

    class A {
      public void m(List<String> m) {}
    }

    final MethodDescriptor descriptor =
        new MethodDescriptor(A.class, A.class.getMethod("m", List.class));
    assertEquals(descriptor.matches(new Class[] {ArrayList.class}, "m"), (true));
  }

  @Test
  void ensureNullaryMethodWithNoArgumentsCanBeInvokedAndAssigned() throws Exception {
    class A {
      void m() {}
    }

    final MethodDescriptor<A, Void> methodDescriptor =
        new MethodDescriptor<>(A.class, A.class.getDeclaredMethod("m"));
    Void a = methodDescriptor.invoke(new A());
    assertNull(a);
  }

  @Test
  void ensureNullaryMethodReturningAValueCanBeInvokedAndAssigned() throws Exception {
    class A {
      String m() {
        return "Frapper";
      }
    }

    final MethodDescriptor<A, String> methodDescriptor =
        new MethodDescriptor<>(A.class, A.class.getDeclaredMethod("m"));
    String a = methodDescriptor.invoke(new A());
    assertEquals(a, ("Frapper"));
  }

  @Test
  void ensureInvokingMethodReturningNothingOnParametersProducesExpectedResults()
      throws NoSuchMethodException {
    class A {
      String s;

      void m(String s) {
        this.s = s;
      }
    }

    final MethodDescriptor<A, String> methodDescriptor =
        new MethodDescriptor<>(A.class, A.class.getDeclaredMethod("m", String.class));
    A a = new A();
    methodDescriptor.invoke(a, new Object[] {"Bean"});
    assertEquals(a.s, ("Bean"));
  }

  @Test
  void ensureMethodDescriptorReturnsCorrectValueForNonVoidMethodWithParameters()
      throws NoSuchMethodException {

    class A {
      String m(String s) {
        return "M(" + s + ")";
      }
    }

    final MethodDescriptor<A, String> methodDescriptor =
        new MethodDescriptor<>(A.class, A.class.getDeclaredMethod("m", String.class));
    A a = new A();
    String s = methodDescriptor.invoke(a, new Object[] {"Bean"});
    assertEquals(s, ("M(Bean)"));
  }

  @Test
  void ensureMethodMatchesSameMethod() throws NoSuchMethodException {
    class A {
      String m(String s) {
        return "M(" + s + ")";
      }
    }

    Method m = A.class.getDeclaredMethod("m", String.class);
    final MethodDescriptor<A, String> methodDescriptor = new MethodDescriptor<>(A.class, m);
    assertEquals(methodDescriptor.matches(m), (true));
  }

  @Test
  void ensureMethodDescriptorsAreEquivalentWhenTheirBackingMethodsAreEquivalent()
      throws NoSuchMethodException {
    class A {
      String m(String s) {
        return "M(" + s + ")";
      }
    }

    Method m = A.class.getDeclaredMethod("m", String.class);
    final MethodDescriptor<A, String> methodDescriptor = new MethodDescriptor<>(A.class, m);

    final MethodDescriptor<A, String> b = new MethodDescriptor<>(A.class, m);
    assertEquals(b.equals(methodDescriptor), (true));
  }

  @Test
  void ensureMethodDescriptorCanBeUsedAsAKeyInAMap() throws NoSuchMethodException {

    class A {
      String m(String s) {
        return "M(" + s + ")";
      }
    }

    Method m = A.class.getDeclaredMethod("m", String.class);
    final MethodDescriptor<A, String> methodDescriptor = new MethodDescriptor<>(A.class, m);

    final MethodDescriptor<A, String> b = new MethodDescriptor<>(A.class, m);
    final Map<MethodDescriptor<?, ?>, Boolean> descriptors = new HashMap<>();
    descriptors.put(methodDescriptor, true);
    assertEquals(descriptors.containsKey(b), (true));
  }
}
