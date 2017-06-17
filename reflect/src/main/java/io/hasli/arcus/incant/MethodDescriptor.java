package io.sunshower.arcus.incant;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Map;

/**
 * Created by haswell on 4/10/16.
 */
public final class MethodDescriptor<U, T>
        extends LazyPropertyAware
        implements PrivilegedAction<Void> {


    private final Method method;
    private final Class<U> owner;
    private Map<String, String> properties;
    private volatile Class<?>[] parameterTypes;




    public MethodDescriptor(final Class<U> owner, final Method method) {
        this.owner = owner;
        this.method = method;
        AccessController.doPrivileged(this);
    }

    public Method getMethod() {
        return method;
    }


    @SuppressWarnings("unchecked")
    public T invoke(Object o, Object...parameters) {
        try {
            Object result = method.invoke(o, parameters);
            return (T) result;
        } catch(Exception ex) {
            throw new InvocationFailureException(ex);
        }
    }


    @SuppressWarnings("unchecked")
    public T invoke(Object o) {
        try {
            Object result = method.invoke(o);
            return (T) result;
        } catch(Exception ex) {
            throw new InvocationFailureException(ex);
        }
    }


    @SuppressWarnings("unchecked")
    public Class<T> getReturnType() {
        return (Class<T>) method.getReturnType();
    }


    public boolean matches(Method m) {
        if(m == method) return true;
        loadParameterTypes();
        final Class<?>[] theirParameterTypes = m.getParameterTypes();
        final Class<?> theirHolderType = m.getDeclaringClass();
        if(parameterTypes.length == theirParameterTypes.length) {
            return owner == theirHolderType &&
                equals(
                        parameterTypes,
                        theirParameterTypes
                );
        }
        return false;
    }


    public boolean matches(Class<?>[] parameterTypes, String name) {
        if(method.getName().matches(name)) {
            loadParameterTypes();
            return equals(this.parameterTypes, parameterTypes);
        }
        return false;
    }

    public boolean matches(String name) {
        if(method.getName().equals(name)) {
            return method.getParameterCount() == 0;
        }
        return false;
    }

    @Override
    public Void run() {
        method.setAccessible(true);
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MethodDescriptor<?, ?> that = (MethodDescriptor<?, ?>) o;
        if (!method.equals(that.method)) return false;
        if (!owner.equals(that.owner)) return false;
        return Arrays.equals(parameterTypes, that.parameterTypes);
    }

    @Override
    public int hashCode() {
        int result = method.hashCode();
        result = 31 * result + owner.hashCode();
        result = 31 * result + Arrays.hashCode(parameterTypes);
        return result;
    }

    private void loadParameterTypes() {
        Class<?>[] r = parameterTypes;
        if(r == null) {
            synchronized (this) {
                r = parameterTypes;
                if(r == null) {
                    this.parameterTypes =
                            method.getParameterTypes();
                }
            }
        }
    }



    private boolean equals(Class<?>[] parameterTypes, Class<?>[] theirParameterTypes) {
        if(parameterTypes == null && theirParameterTypes == null) return true;
        if(parameterTypes == null || theirParameterTypes == null) {
            return false;
        }

        int len = parameterTypes.length;
        int theirLen = theirParameterTypes.length;
        if(len != theirLen) {
            return false;
        }
        for(int i = 0; i < len; ++i) {
            final Class<?> mine = parameterTypes[i];
            final Class<?> theirs = parameterTypes[i];
            if(!mine.equals(theirs) || !theirs.isAssignableFrom(mine)) {
                return false;
            }
        }
        return true;
    }


}
