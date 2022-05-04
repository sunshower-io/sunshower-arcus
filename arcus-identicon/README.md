# Sunshower-Identicon

this library implements [JDenticon-flavored identicons](https://jdenticon.com/) in pure Java with no
external (non-Arcus) dependencies.

## Build

Locate the latest version
of [Sunshower-Arcus in Maven Central](https://mvnrepository.com/search?q=io.sunshower.arcus)
and include the artifact `arcus-identicon`  in your build.

Maven

```xml

<dependency>
  <groupId>io.sunshower.arcus</groupId>
  <artifactId>arcus-identicon</artifactId>
  <version>${arcus.version}</version>
</dependency>
```

Gradle

```groovy
dependencies {
    implementation 'io.sunshower.arcus:arcus-identicon'
}
```

This artifact is included in the Arcus exported Bill-Of-Materials for use in the Spring Gradle
plugin or Maven imports project:

Maven

```xml

<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>io.sunshower.arcus</groupId>
      <artifactId>bom-exported</artifactId>
      <version>${arcus.version}</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependencyManagement>
```

## Usage

Arcus-Identicon supports creating identicons from both arbitrary objects and SHA-1 hashes.

```java
import io.sunshower.arcus.identicon.Jdenticon;
String svgFile=Jdenticon.toSvg("Josiah Haswell")
```

will create:

```svg

<svg
  xmlns="http://www.w3.org/2000/svg"
  width="100"
  height="100"
  viewBox="0 0 100 100"
  preserveAspectRatio="xMidYMid meet"
>
  <path
    fill="#E6E6E6"
    d="M29 12a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M54 12a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M54 87a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M29 87a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M4 37a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M79 37a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M79 62a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M4 62a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0"
    fill-opacity="1.0"
  ></path>
  <path
    fill="#4D4D4D"
    d="M25 0L25 25L12 25ZM100 25L75 25L75 12ZM75 100L75 75L87 75ZM0 75L25 75L25 87Z"
    fill-opacity="1.0"
  ></path>
  <path
    fill="#D175BA"
    d="M25 25L50 25L50 29L39 50L25 50ZM75 25L75 50L71 50L50 39L50 25ZM75 75L50 75L50 71L60 50L75 50ZM25 75L25 50L29 50L50 60L50 75Z"
    fill-opacity="1.0"
  ></path>
</svg>
```

You can access the structure of the document via the `Identicon` class, which returns a tree-like
structure with all of the attributes. This may be written to arbitrary formats via
the `io.sunshower.arcus.markup.TagWriter` class

Additionally, you may support new formats (e.g. raster formats) by implementing some combination of:

1. `io.sunshower.arcus.identicon.Renderer`
2. `io.sunshower.arcus.identicon.Path`
3. `io.sunshower.arcus.identicon.IconWriter`

The relevant geometric data supplied as a list of points (polygons) or circles (center, diameter)

### Configuration

You may control all aspects of identicon generation through
the `io.sunshower.arcus.identicon.Configuration`
class, including color and greyscale lightness mappings.

```java
// override opacity, saturation, and padding
Configuration cfg=Configuration.defaultBuilder()
    .withOpacity(0.8f)
    .withSaturation(0.7f)
    .withPadding(2)
    .withSize(128)
    .build();

```

Identicons may be create for arbitrary objects. It is possible to support non-SHA-1 object digests,
although this is untested currently

### Examples

*Defaults*
<p align="center">
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/default-0.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/default-1.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/default-2.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/default-3.svg" />
</p>

*Opacity/Saturation*
<p align="center">
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/opacity-sat-bg-0.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/opacity-sat-bg-1.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/opacity-sat-bg-2.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/opacity-sat-bg-3.svg" />
</p>

*Padding: 2*
<p align="center">
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/padding-opacity-saturation0.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/padding-opacity-saturation1.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/padding-opacity-saturation2.svg" />
    <img alt="example-1" src="https://raw.githubusercontent.com/sunshower-io/sunshower-arcus/master/arcus-identicon/src/test/resources/padding-opacity-saturation3.svg" />
</p>





