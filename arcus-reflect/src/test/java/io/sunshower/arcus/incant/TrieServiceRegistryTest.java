package io.sunshower.arcus.incant;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TrieServiceRegistryTest {

  private OperationScanner objectScanner;

  @BeforeEach
  public void setUp() {
    objectScanner = new OpScanner();
  }

  @Test
  void ensureScanningSimpleClassWithSingleMethodProducesExpectedResults() {
    class A {
      String a() {
        return "a";
      }
    }
    final TrieServiceRegistry registry = new TrieServiceRegistry(objectScanner, init(A.class));
    registry.refresh();
    final A instance = new A();
    ServiceDescriptor<A> service = registry.resolve("A");
    assertEquals(service.getIdentifier(), "A");
    assertEquals(service.resolve("a").invoke(instance), "a");
  }

  @Test
  public void ensureScanningSimpleClassWithMultipleMethodsWithVaryingTypesProducesResults() {
    class A {
      String a() {
        return "a";
      }

      String b(String a, String b, String c) {
        return a + b + c;
      }
    }

    final TrieServiceRegistry registry = new TrieServiceRegistry(objectScanner, init(A.class));
    registry.refresh();
    final A instance = new A();
    ServiceDescriptor<A> service = registry.resolve("A");
    assertEquals(service.getIdentifier(), ("A"));
    assertEquals(service.resolve("a").invoke(instance), ("a"));
    assertEquals(
        service
            .resolve("b", String.class, String.class, String.class)
            .invoke(instance, "a", "b", "c"),
        ("abc"));
  }

  static ServiceResolver init(Class<?> types) {
    return new TestServiceResolver(types);
  }

  private static class OpScanner implements OperationScanner {

    @Override
    public Set<ServiceDescriptor<?>> scan(Class<?> type) {
      return Collections.singleton(
          new ServiceDescriptor<>(type, type.getSimpleName(), type.getDeclaredMethods()));
    }
  }

  static class TestServiceResolver implements ServiceResolver {
    final Set<Class<?>> types;

    TestServiceResolver(Class<?>... types) {
      this.types = new HashSet<>(Arrays.asList(types));
    }

    @Override
    public Set<Class<?>> resolveServiceTypes() {
      return types;
    }
  }
}
