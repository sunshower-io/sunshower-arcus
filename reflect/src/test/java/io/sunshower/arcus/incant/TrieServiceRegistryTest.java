package io.sunshower.arcus.incant;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


/**
 * Created by haswell on 4/10/16.
 */
public class TrieServiceRegistryTest {

    private OperationScanner objectScanner;

    @Before
    public void setUp() {
        objectScanner = new OpScanner();
    }

    @Test
    public void ensureScanningSimpleClassWithSingleMethodProducesExpectedResults() {
        class A {
            String a() {
                return "a";
            }
        }
        final TrieServiceRegistry registry =
                new TrieServiceRegistry(
                        objectScanner,
                        init(A.class)
                );
        registry.refresh();
        final A instance = new A();
        ServiceDescriptor<A> service = registry.resolve("A");
        assertThat(service.getIdentifier(), is("A"));
        assertThat(service.resolve("a").invoke(instance), is("a"));
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

        final TrieServiceRegistry registry =
                new TrieServiceRegistry(
                        objectScanner,
                        init(A.class)
                );
        registry.refresh();
        final A instance = new A();
        ServiceDescriptor<A> service = registry.resolve("A");
        assertThat(service.getIdentifier(), is("A"));
        assertThat(service.resolve("a").invoke(instance), is("a"));
        assertThat(service.resolve("b",
                String.class,
                String.class,
                String.class
        ).invoke(instance, "a", "b", "c"), is("abc"));
    }








    static ServiceResolver init(Class<?> types) {
        return new TestServiceResolver(types);
    }

    private static class OpScanner implements OperationScanner {

        @Override
        public Set<ServiceDescriptor<?>> scan(Class<?> type) {
            return Collections.singleton(new ServiceDescriptor<>(
                    type, type.getSimpleName(),
                    type.getDeclaredMethods()));
        }
    }

    static class TestServiceResolver implements ServiceResolver {
        final Set<Class<?>> types;
        TestServiceResolver(Class<?>...types) {
            this.types = new HashSet<>(Arrays.asList(types));
        }

        @Override
        public Set<Class<?>> resolveServiceTypes() {
            return types;
        }
    }


}