package io.sunshower.arcus.incant;

import java.lang.reflect.Method;

public final class Invoker<T> {
    
    final Class<T> type;
    final String methodName;

    public Invoker(Class<T> type, String methodName) {
        this.type = type;
        this.methodName = methodName;
    }

    public <U> Invocation<U> createInvocation(T instance) {
        return new DefaultInvocation<>(instance, Methods.named(type, methodName));
    }


    private class DefaultInvocation<U> implements Invocation<U> {
        
        final Method method;
        final Object instance;
        public DefaultInvocation(T instance, Method method) {
            this.instance = instance;
            method.setAccessible(true);
            this.method = method;
        }
       
        @SuppressWarnings("unchecked")
        public U invoke(Object...args) {
            try {
                return (U) method.invoke(instance, args);
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
