package io.sunshower.arcus.incant;

import io.sunshower.lang.Refreshable;

/**
 * Created by haswell on 4/10/16.
 */
public interface InvocationContext extends Refreshable {


    <T> ServiceDescriptor<T> resolve(String name);

    <T> ServiceDescriptor<T> resolve(Class<T> type, String name);


}
