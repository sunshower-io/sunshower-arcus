package io.sunshower.arcus.incant;

import java.util.Set;

/**
 * Created by haswell on 4/10/16.
 */
public interface OperationScanner {


    Set<ServiceDescriptor<?>> scan(Class<?> type);

}
