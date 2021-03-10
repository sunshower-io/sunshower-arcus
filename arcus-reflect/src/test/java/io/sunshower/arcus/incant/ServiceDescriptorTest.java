package io.sunshower.arcus.incant;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

/** Created by haswell on 4/10/16. */
public class ServiceDescriptorTest {

    @Test
    public void ensureRetrievingAUnitaryMethodWithAnEmptyParameterArrayProducesCorrectResult()
            throws Exception {
        class A {
            public String m() {
                return "M(" + "x" + ")";
            }
        }

        final ServiceDescriptor<A> serviceDescriptor =
                new ServiceDescriptor<>(
                        A.class, "A", new Method[] {A.class.getDeclaredMethod("m")});

        assertNotNull(serviceDescriptor.resolve("m", new Class[0]));
    }
}
