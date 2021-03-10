package io.sunshower.persistence.id;

import java.io.Serializable;

/**
 * Created by haswell on 7/21/17.
 */
public interface Persistable<ID extends Serializable> {

    ID getId();

    int hashCode();

    boolean equals(Object o);

    String toString();

}
