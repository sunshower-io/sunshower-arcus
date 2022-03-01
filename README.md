# Build

![example workflow](https://github.com/sunshower-io/sunshower-arcus/actions/workflows/gradle.yml/badge.svg)


# Sunshower-Arcus
Sunshower-Arcus is the collection of common libraries underlying most of Sunshower.io. 
Arcus provides utilities for:

1. Distributed configuration
1. Tenancy-aware transaction-routing
1. high-performance, index-sympathetic database IDs integrated with technologies like Hibernate/JPA and JAX-RS
1. Multitenancy
1. Security 


## Configuration

Annotating a configuration class with `@io.sunshower.arcus.config.Configure(ConfigurationClass.class)` will
create an injectable, statically-typed configuration object. Intrinsically-supported formats are:

1. JSON
1. XML
1. YAML

but adding support for a new format is as simple as implementing `io.sunshower.arcus.config.ConfigurationReader`,
adding the configuration reader implementation to `META-INF/services/io.sunshower.arcus.config.ConfigurationReader`,
and placing the implementation on the runtime classpath of the consuming project.


Given a class with the simple-name `ExampleConfiguration`, Arcus will search:

1. The system environment for a `ARCUS_EXAMPLE_CONFIGURATION` environment variable pointing to the desired configuration
1. The system properties for `configuration.example-configuration` pointing to the desired configuration
1. The classpath for `classpath:/configuration/example-configuration.{ext}` where `{ext}` is any extension
contributed by a `ConfigurationReader` available on the classpath
   


 




