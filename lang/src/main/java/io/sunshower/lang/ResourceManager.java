package io.sunshower.lang;


/**
 * Created by haswell on 3/29/16.
 */
public interface ResourceManager {


    boolean exists(String s);

    Resource forName(String s);

    Resource getResource(String s);

    ClassLoader getClassLoader();

    <T> Class<T> getClass(String name);

    <T> Class<T> getClass(String name, boolean load);


    default Package[] getPackages() {
        return Package.getPackages();
    }


}
