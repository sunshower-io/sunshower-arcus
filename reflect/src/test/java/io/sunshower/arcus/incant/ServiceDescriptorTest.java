package io.sunshower.arcus.incant;

import org.junit.Test;

import java.lang.reflect.Method;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by haswell on 4/10/16.
 */
public class ServiceDescriptorTest {

    @Test
    public void ensureRetrievingAUnitaryMethodWithAnEmptyParameterArrayProducesCorrectResult() throws Exception {
        class A {
            public String m() {
                return "M(" + "x" + ")";
            }
        }

        final ServiceDescriptor<A> serviceDescriptor =
                new ServiceDescriptor<>(A.class, "A", new Method[]{
                        A.class.getDeclaredMethod("m")

                });

        assertThat(serviceDescriptor.resolve("m", new Class[0]), is(not(nullValue())));

    }

    @Test
    public void ensureService() {
        
    }

}