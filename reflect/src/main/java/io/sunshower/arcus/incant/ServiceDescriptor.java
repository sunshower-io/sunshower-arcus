package io.sunshower.arcus.incant;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by haswell on 4/10/16.
 */
public final class ServiceDescriptor<T> extends LazyPropertyAware {


    private final Class<T> owner;
    private final String identifier;
    private final MethodMap methods;


    public ServiceDescriptor(
            Class<T> owner,
            String identifier,
            Method[] methods
    ) {
        this.owner = owner;
        this.identifier = identifier;
        this.methods = new MethodMap(owner, methods);
    }

    public ServiceDescriptor(
            Class<T> owner,
            String identifier,
            Collection<Method> methods
    ) {
        this.owner = owner;
        this.identifier = identifier;
        this.methods = new MethodMap(owner, methods);
    }

    public ServiceDescriptor(
            String identifier,
            Class<T> owner,
            Collection<MethodDescriptor<?, ?>> methods
    ) {
        this.owner = owner;
        this.identifier = identifier;
        this.methods = new MethodMap(methods, owner);
    }

    public ServiceDescriptor(
            String identifier,
            Class<T> owner, MethodDescriptor[] methods) {
        this.owner = owner;
        this.identifier = identifier;
        this.methods = new MethodMap(methods, owner);
    }




    public Class<T> getTargetClass() {
        return owner;
    }


    @SuppressWarnings("unchecked")
    public <U> MethodDescriptor<T, U> resolve(String name) {
        return (MethodDescriptor<T, U>) methods.get(name);
    }


    @SuppressWarnings("unchecked")
    public <U> MethodDescriptor<T, U> resolve(String name, Class<?>...parameterTypes) {
        return (MethodDescriptor<T, U>) methods.get(parameterTypes, name);
    }

    public String getIdentifier() {
        return identifier;
    }

    @SuppressWarnings("unchecked")
    private static final class MethodMap {
        MethodDescriptor<?, ?>[][] methods;

        MethodMap(MethodDescriptor<?, ?>[] methods, Class<?> owner) {
            this.methods = new MethodDescriptor[2][2];
            int i = 0;
            for(MethodDescriptor current : methods) {
                final int argumentCount =
                        current.getMethod().getParameterCount();
                add(current, i++, argumentCount);
            }
        }

        MethodMap(Collection<MethodDescriptor<?, ?>> methods, Class<?> owner) {
            this.methods = new MethodDescriptor[2][2];
            int i = 0;
            for(MethodDescriptor current : methods) {
                final int argumentCount =
                        current.getMethod().getParameterCount();
                add(current, i++, argumentCount);
            }
        }

        MethodMap(final Class<?> owner, Collection<Method> methods) {
            this.methods = new MethodDescriptor[2][2];
            int i = 0;
            for(Method method : methods) {
                final MethodDescriptor<?, ?> current =
                        createMethodDescriptor(owner, method);
                final int argumentCount =
                        current.getMethod().getParameterCount();
                add(current, i++, argumentCount);
            }
        }

        MethodMap(final Class<?> owner, Method [] methods) {
            this.methods = new MethodDescriptor[2][2];
            for(int i = 0; i < methods.length; ++i) {
                final MethodDescriptor<?, ?> current =
                        createMethodDescriptor(owner, methods[i]);
                final int argumentCount =
                        current.getMethod().getParameterCount();
                add(current, i, argumentCount);
            }
        }

        MethodDescriptor<?, ?> get(Class<?>[] parameterTypes, String name) {
            final MethodDescriptor<?, ?>[] candidates
                    = methods[parameterTypes.length];
            if(candidates != null) {
                for(MethodDescriptor descriptor : candidates) {
                    if(descriptor != null && descriptor.matches(parameterTypes, name)) {
                        return descriptor;
                    }
                }
            }
            return null;
        }

        <U> MethodDescriptor<?, ?> get(String name) {
            final MethodDescriptor<?, ?>[] candidates
                    = methods[0];
            if(candidates != null) {
                for(MethodDescriptor descriptor : candidates) {
                    if(descriptor != null && descriptor.matches(name)) {
                        return descriptor;
                    }
                }
            }
            return null;
        }


        private void add(MethodDescriptor<?, ?> m, int i, int argumentCount) {
            if(argumentCount >= methods.length) {
                resize(argumentCount);
            }
            final MethodDescriptor<?, ?>[] current =
                    getCurrent(argumentCount);
            methods[argumentCount] = addMethod(current, m);
        }

        private MethodDescriptor<?, ?>[] addMethod(MethodDescriptor<?, ?>[] current, MethodDescriptor m) {
            MethodDescriptor<?, ?>[] methods = current;
            final int lastIndex = current.length - 1;
            final MethodDescriptor<?, ?> last = current[lastIndex];
            if(last == null) {
                methods = resizeArray(methods);
            }
            methods[lastIndex + 1] = m;
            return methods;
        }

        private MethodDescriptor<?, ?>[] resizeArray(
                MethodDescriptor<?, ?>[] methods) {
            return Arrays.copyOf(methods, methods.length + 2);
        }

        private MethodDescriptor<?, ?>[] getCurrent(int argumentCount) {
            MethodDescriptor<?, ?>[] methods = this.methods[argumentCount];
            if(methods == null) {
                methods = this.methods[argumentCount] =
                        new MethodDescriptor<?, ?>[2];
            }
            return methods;
        }

        private void resize(int argumentCount) {
            this.methods = Arrays.copyOf(
                    this.methods,
                    argumentCount + 2
            );
        }

        private MethodDescriptor<?,?> createMethodDescriptor(
                Class<?> owner,
                Method method
        ) {
            return new MethodDescriptor<>(owner, method);
        }

    }


}
