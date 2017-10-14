package io.sunshower.arcus.incant;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by haswell on 4/10/16.
 */
public class TrieServiceRegistry implements InvocationContext {

    private final OperationScanner scanner;
    private final ServiceResolver serviceResolver;
    private final Map<String, ServiceDescriptor<?>> serviceDescriptors;


    public TrieServiceRegistry(
            final OperationScanner scanner,
            final ServiceResolver serviceResolver
    ) {
        this.scanner = scanner;
        this.serviceResolver = serviceResolver;
        this.serviceDescriptors = new HashMap<>();
    }


    @Override
    @SuppressWarnings("unchecked")
    public <T> ServiceDescriptor<T> resolve(String name) {
        return (ServiceDescriptor<T>) this.serviceDescriptors.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ServiceDescriptor<T> resolve(Class<T> type, String name) {
        return (ServiceDescriptor<T>) this.serviceDescriptors.get(name);
    }

    private void scan() {
        final Set<Class<?>> serviceTypes =
                serviceResolver.resolveServiceTypes();
        for(Class<?> serviceType : serviceTypes) {
            register(serviceType);
        }
    }

    private void register(Class<?> serviceType) {
        final Set<ServiceDescriptor<?>>
                registeredServices = scanner.scan(serviceType);
        for(ServiceDescriptor<?> descriptor : registeredServices) {
            this.serviceDescriptors.put(descriptor.getIdentifier(), descriptor);
        }
    }

    @Override
    public void refresh() {
        scan();
    }
}
