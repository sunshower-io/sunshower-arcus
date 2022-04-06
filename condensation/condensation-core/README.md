# Object Mapper

The Aire object mapper is intended to be a lightweight and (reasonably) fast,
external-dependency-free interchange format (JSON, XML, etc.) binder.

## Why another mapper?
Condensation seeks to provide first-class support for dynamic polymorphism.  For instance, in JAXB,
one must enumerate all of the subtypes that an element-type may have, or rely on implementation-specific details.
Libraries like Jackson have better support, but can be heavy depending on the features required.  Condensation aims to
be fast, simple, and lightweight


## Usage (JSON)

### Simple Scenario

The annotation `@RootElement` denotes a type that can be bound to a document element. For instance:

```json
{
  "firstName": "Josiah",
  "lastName": "Haswell"
}

```

could correspond to the class:

```java

@RootElement
public class Person {

  @Attribute
  private String firstName;

  @Attribute
  private String lastName;
}
```

You can then bind the JSON document to an instance of the object as follows:

```java

class Example {

  public Example() {
    String document = "{"
                      + "\"firstName\":\"Josiah\","
                      + "\"lastName\":\"Haswell\""
                      + "}";
    Condensation condensation = Condensation.create("json");
    Person person = condensation.read(Person.class, document);
  }
}
```

Condensation supports arbitrarily-nested objects, Arrays, Lists, Maps, etc.

### Arrays

Condensation natively supports arrays of objects, primitive arrays, etc.

#### Primitive Arrays

The default numeric type for JSON/JavaScript is 8-byte IEEE 754 floating-point. Any primitive
numeric type (or their wrappers) may be used and conversions are automatically applied

```java

class ExampleInts {

  public Example() {
    Condensation condensation = Condensation.create("json");
    int[] values = condensation.read(int[].class, "[1,2,3,4]");
  }
}

/**
 *
 */
class ExampleDoubles {

  public Example() {
    Condensation condensation = Condensation.create("json");
    double[] values = condensation.read(double[].class, "[1,2,3,4]");
  }
}


class ExampleStrings {

  public Example() {
    Condensation condensation = Condensation.create("json");
    String[] values = condensation.read(String[].class, "[\"1\",\"2\",\"3\",\"4\"]");
  }
}

```

### Arrays of Objects

Condensation also supports arrays of objects.  The `Person` type mapped above may be read as follows:
```java

class Example {
  public Example() {
    Condensation condensation = Condensation.create("json");
    String value = "[\n"
                   + "  {\n"
                   + "    \"firstName\": \"Josiah\",\n"
                   + "    \"lastName\": \"Haswell\"\n"
                   + "  },\n"
                   + "\n"
                   + "  {\n"
                   + "    \"firstName\": \"Bob\",\n"
                   + "    \"lastName\": \"Porgnorgler\"\n"
                   + "  }\n"
                   + "]";
    Person[] values = condensation.read(Person[].class, value);
  }
}
```

### Maps

Condensation supports maps whose values may be any mapped type or primitive,
and whose keys may be mapped to strings.  

```java
    class StringToIntegerConverter implements Function<String, Integer> {

      @Override
      public Integer apply(String s) {
        return Integer.parseInt(s);
      }
    }
    @RootElement
    class KV {

      @Element
      @Convert(key = StringToIntegerConverter.class)
      Map<Integer, Integer> elements;

    }
    val value = "{" + "\"elements\": {" + "\"1\": 1," + "\"2\": 3}" + "} ";
    KV kv = condensation.read(KV.class, value);
```

Converters may be applied for both the key and the value of a map:

```java

class IntegerToStringConverter implements Function<Integer, String> {

  @Override
  public String apply(Integer s) {
    return s.toString();
  }
}
class StringToIntegerConverter implements Function<String, Integer> {

  @Override
  public Integer apply(String s) {
    return Integer.parseInt(s);
  }
}
@RootElement
class KV {

  @Element
  @Convert(
      key = StringToIntegerConverter.class, 
      value = IntegerToStringConverter.class
  )
  Map<Integer, Integer> elements;

}
val value = "{" + "\"elements\": {" + "1: \"1\"," + "2: \"3\"}" + "} ";
KV kv = condensation.read(KV.class, value);
```


### CSS Selector Support
Elements may be queried via CSS selector and bound to Java objects.  For instance:

```json

[


  {
    "_id": "6168a85db4a66c5cfb0d89a4",
    "index": 0,
    "guid": "d208ecef-8294-4022-aa6f-c334e62fc305",
    "isActive": false,
    "balance": "$2,497.99",
    "picture": "http://placehold.it/32x32",
    "age": 37,
    "eyeColor": "brown",
    "name": "Ola Mcintosh",
    "gender": "female",
    "company": "TASMANIA",
    "email": "olamcintosh@tasmania.com",
    "phone": "+1 (859) 573-2954",
    "address": "342 Ebony Court, Denio, American Samoa, 9004",
    "about": "Labore elit consequat officia veniam pariatur ex incididunt cupidatat ex. Deserunt aliquip est velit esse quis consectetur dolore mollit in. Dolore elit nostrud incididunt do officia voluptate cillum ea.\r\n",
    "registered": "2019-06-28T05:48:36 +06:00",
    "latitude": 85.324825,
    "longitude": -138.070366,
    "tags": [
      "qui proident reprehenderit veniam occaecat pariatur aliquip veniam est sit fugiat aliquip amet qui consequat officia nulla laboris velit minim",
      "anim et cillum laboris laborum ex qui sint labore est qui veniam labore ut anim esse pariatur duis dolore anim",
      "consectetur amet exercitation velit ipsum eiusmod elit eu occaecat proident sint dolor enim amet ad quis sunt elit eu ea",
      "dolore mollit ut qui mollit mollit veniam anim amet voluptate cupidatat ex et consectetur anim sunt id proident est qui",
      "reprehenderit fugiat velit mollit in eu velit minim labore pariatur nostrud amet anim dolor elit reprehenderit duis aliqua amet nulla",
      "officia sint id do deserunt aliquip aliqua adipisicing sunt mollit excepteur consequat id aliqua sint nostrud cupidatat quis excepteur consequat",
      "ullamco irure do ipsum enim nulla excepteur nulla officia esse magna enim nulla excepteur sit quis quis sint cupidatat nostrud",
      "culpa proident sit pariatur aliquip dolore incididunt commodo exercitation sunt do voluptate consectetur voluptate nulla enim labore sint est aliquip",
      "magna pariatur pariatur nostrud incididunt anim dolore quis commodo magna ex nisi nisi minim do proident veniam eu velit cillum",
      "amet quis veniam enim adipisicing sit cillum excepteur eiusmod deserunt incididunt labore dolore ad voluptate duis velit fugiat culpa sint"
    ],
    "friends": [
      {
        "id": 0,
        "name": "Walsh Fisher"
      },
      {
        "id": 1,
        "name": "Tricia Caldwell"
      },
      {
        "id": 2,
        "name": "Virgie Sheppard"
      },
      {
        "id": 3,
        "name": "Eula Fleming"
      },
      {
        "id": 4,
        "name": "Ford Cherry"
      },
      {
        "id": 5,
        "name": "Ayala Perry"
      },
      {
        "id": 6,
        "name": "Nixon Cook"
      },
      {
        "id": 7,
        "name": "Houston Wilson"
      },
      {
        "id": 8,
        "name": "Loretta Jordan"
      },
      {
        "id": 9,
        "name": "Moss Mendoza"
      }
    ],
    "greeting": "Hello, Ola Mcintosh! You have 7 unread messages.",
    "favoriteFruit": "strawberry"
  },
  {
    "_id": "6168a85d695b7e83b1e00999",
    "index": 1,
    "guid": "3f835576-acee-4a62-8de8-7bd28460116a",
    "isActive": false,
    "balance": "$1,094.09",
    "picture": "http://placehold.it/32x32",
    "age": 30,
    "eyeColor": "blue",
    "name": "Key Holloway",
    "gender": "male",
    "company": "JOVIOLD",
    "email": "keyholloway@joviold.com",
    "phone": "+1 (948) 593-3313",
    "address": "293 Ridgewood Avenue, Maplewood, Vermont, 868",
    "about": "In id eiusmod ut qui velit laborum mollit culpa commodo consequat. Ea in eiusmod sint reprehenderit nulla nulla reprehenderit nostrud dolor esse reprehenderit officia. Tempor enim id sit veniam. In mollit ipsum mollit excepteur dolore occaecat mollit. Nostrud sit cillum mollit et exercitation culpa incididunt dolore mollit reprehenderit.\r\n",
    "registered": "2018-08-22T01:48:18 +06:00",
    "latitude": 13.325747,
    "longitude": 168.177381,
    "tags": [
      "fugiat nostrud ipsum enim nostrud sint ea ipsum occaecat ea laboris nisi qui consequat nulla qui ipsum qui aliquip excepteur",
      "consequat exercitation adipisicing ipsum pariatur ullamco nisi quis nulla proident adipisicing sit cillum enim esse reprehenderit pariatur ipsum eiusmod sunt",
      "do ea veniam exercitation incididunt irure ea eiusmod ad est eu et labore nulla fugiat ut reprehenderit voluptate velit nostrud",
      "aute aliqua exercitation exercitation eu consectetur commodo dolore excepteur esse adipisicing excepteur cupidatat anim anim anim ex do id ea",
      "commodo dolor pariatur pariatur anim elit labore ea labore nostrud ex ut ex irure culpa veniam commodo commodo esse enim",
      "esse quis labore ex do nostrud excepteur cupidatat aliqua aliquip mollit officia proident dolore consectetur minim laborum ad dolore ex",
      "exercitation eu aliquip enim culpa qui id sit non incididunt duis exercitation fugiat ut pariatur et est esse ullamco cupidatat",
      "id esse ullamco qui eiusmod qui proident reprehenderit duis sunt quis minim sint nulla sit ex do qui minim non",
      "aliquip pariatur nulla magna id velit occaecat ea enim ea pariatur eiusmod occaecat ipsum pariatur et in est eiusmod minim",
      "id consectetur mollit ullamco ad sint elit labore consectetur sint Lorem et Lorem adipisicing quis pariatur ut ea et esse"
    ],
    "friends": [
      {
        "id": 0,
        "name": "Crosby Camacho"
      },
      {
        "id": 1,
        "name": "Myra Villarreal"
      },
      {
        "id": 2,
        "name": "Stein Slater"
      },
      {
        "id": 3,
        "name": "Cherry Gray"
      },
      {
        "id": 4,
        "name": "Delia Garza"
      },
      {
        "id": 5,
        "name": "Alta Garrison"
      },
      {
        "id": 6,
        "name": "Tabatha Franklin"
      },
      {
        "id": 7,
        "name": "Debora Talley"
      },
      {
        "id": 8,
        "name": "Waters Kinney"
      },
      {
        "id": 9,
        "name": "Cecile Beard"
      }
    ],
    "greeting": "Hello, Key Holloway! You have 5 unread messages.",
    "favoriteFruit": "strawberry"
  }
]

```
```java
    @RootElement
    class Person {
      @Element
      String email;
      @Element String phone;

      @Element String address;

      @Element
      Collection<String> tags;
      
      @Element
      Collection<Friend> friends;

    }

    @RootElement class Friend {
      @Element int id;
      @Element String name;
    }
  val result = Condensation
      .parse("json", read("friends.json"))
      .selectAll(".friends > .id, .friends > .name, .tags", Person.class);


```

Condensation will automatically compute an "expected" document structure from the bound type structures 
that alters its parsing behavior to avoid parsing the entire document (reading it into an Abstract 
Syntax Tree then binding it).  This is an important optimization in many cases. The document is still
broken into its constituent token stream


### Architecture

#### Parser
tk: document com.aire.ux.condensation.Parser
#### Lexer
tk: document
1. com.aire.ux.parsing.ast.Symbol
1. com.aire.ux.parsing.ast.Token
#### Tree Hom Functor
tk: document 
1. com.aire.ux.test.NodeAdapter
1. com.aire.ux.parsing.ast.SyntaxNode
#### Extending CSS support
Support for additional CSS selectors and functionality may be implemented by providing additional
com.aire.ux.plan.evaluator.Evaluator implementations.  





