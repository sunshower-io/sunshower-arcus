package io.sunshower.arcus.incant;

import org.junit.Test;

import java.beans.ParameterDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by haswell on 4/10/16.
 */
public class MethodDescriptorTest {

    @Test
    @SuppressWarnings("unchecked")
    public void ensureAssignmentMatches() throws NoSuchMethodException {

        class A {
            public void m(List<String> s) {

            }
        }


        final MethodDescriptor descriptor = new MethodDescriptor(A.class, A.class.getMethod("m", List.class));
        assertThat(descriptor.matches(new Class[]{ArrayList.class}, "m"), is(true));


    }

    @Test
    public void ensureNullaryMethodWithNoArgumentsCanBeInvokedAndAssigned() throws Exception {
        class A {
            public void m() {

            }
        }

        final MethodDescriptor<A, Void> methodDescriptor = new
                MethodDescriptor<>(A.class, A.class.getDeclaredMethod("m"));
        Void a = methodDescriptor.invoke(new A());
        assertThat(a, is(nullValue()));
    }


    @Test
    public void ensureNullaryMethodReturningAValueCanBeInvokedAndAssigned() throws Exception {
        class A {
            public String m() {
                return "Frapper";
            }
        }

        final MethodDescriptor<A, String> methodDescriptor = new
                MethodDescriptor<>(A.class, A.class.getDeclaredMethod("m"));
        String a = methodDescriptor.invoke(new A());
        assertThat(a, is("Frapper"));
    }


    @Test
    public void ensureInvokingMethodReturningNothingOnParametersProducesExpectedResults() throws NoSuchMethodException {
        class A {
            public String s;
            public void m(String s) {
                this.s = s;
            }
        }

        final MethodDescriptor<A, String> methodDescriptor = new
                MethodDescriptor<>(A.class, A.class.getDeclaredMethod("m", String.class));
        A a = new A();
        methodDescriptor.invoke(a, new Object[]{"Bean"});
        assertThat(a.s, is("Bean"));
    }


    @Test
    public void ensureMethodDescriptorReturnsCorrectValueForNonVoidMethodWithParameters() throws NoSuchMethodException {

        class A {
            public String m(String s) {
                return "M(" + s + ")";
            }
        }

        final MethodDescriptor<A, String> methodDescriptor = new
                MethodDescriptor<>(
                A.class,
                A.class.getDeclaredMethod("m", String.class)
        );
        A a = new A();
        String s = methodDescriptor.invoke(a, new Object[]{"Bean"});
        assertThat(s, is("M(Bean)"));
    }

    @Test
    public void ensureMethodMatchesSameMethod() throws NoSuchMethodException {
        class A {
            public String m(String s) {
                return "M(" + s + ")";
            }
        }

        Method m = A.class.getDeclaredMethod("m", String.class);
        final MethodDescriptor<A, String> methodDescriptor = new
                MethodDescriptor<>(
                A.class,
                m
        );
        assertThat(methodDescriptor.matches(m), is(true));
    }


    @Test
    public void ensureMethodDescriptorsAreEquivalentWhenTheirBackingMethodsAreEquivalent() throws NoSuchMethodException {
        class A {
            public String m(String s) {
                return "M(" + s + ")";
            }
        }

        Method m = A.class.getDeclaredMethod("m", String.class);
        final MethodDescriptor<A, String> methodDescriptor = new
                MethodDescriptor<>(
                A.class,
                m
        );

        final MethodDescriptor<A, String> b = new MethodDescriptor<>(A.class, m);
        assertThat(b.equals(methodDescriptor), is(true));
    }

    @Test
    public void ensureMethodDescriptorCanBeUsedAsAKeyInAMap() throws NoSuchMethodException {

        class A {
            public String m(String s) {
                return "M(" + s + ")";
            }
        }

        Method m = A.class.getDeclaredMethod("m", String.class);
        final MethodDescriptor<A, String> methodDescriptor = new
                MethodDescriptor<>(
                A.class,
                m
        );

        final MethodDescriptor<A, String> b = new MethodDescriptor<>(A.class, m);
        final Map<MethodDescriptor<?, ?>, Boolean> descriptors = new HashMap<>();
        descriptors.put(methodDescriptor, true);
        assertThat(descriptors.containsKey(b), is(true));
    }




}