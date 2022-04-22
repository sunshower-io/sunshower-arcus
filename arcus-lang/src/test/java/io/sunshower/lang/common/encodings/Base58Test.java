package io.sunshower.lang.common.encodings;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.lang.common.encodings.Encodings.Type;
import java.util.UUID;
import lombok.val;
import org.junit.jupiter.api.Test;

/** Created by haswell on 10/16/17. */
public class Base58Test {

  @Test
  void testCase1() {
    val string = "The quick brown fox jumps over the lazy dog.";
    val encoding = Encodings.create(Type.Base58);
    assertEquals(
        "USm3fpXnKG5EUBx2ndxBDMPVciP5hGey2Jh4NDv6gmeo1LkMeiKrLJUUBk6Z", encoding.encode(string));
  }

  @Test
  public void ensureEncodingStringWithDefaultEncodingWorksForSimpleString() {

    String input = "hello";

    Encoding instance = Base58.getInstance(Base58.Alphabets.Default);
    String result = instance.encode(input.getBytes());
    String expected = "Cn8eVZg";
    assertEquals(result, (expected));
  }

  @Test
  public void ensureDecodingWorksForSimpleString() {
    Encoding instance = Base58.getInstance(Base58.Alphabets.Default);

    String input = "Cn8eVZg";

    assertEquals(new String(instance.decode(input)), ("hello"));
  }

  @Test
  public void ensureRandomUUIDsAllDecodeAndEncodeCorrectly() {
    Encoding instance = Base58.getInstance(Base58.Alphabets.Ripple);
    for (int i = 0; i < 100; i++) {
      UUID id = UUID.randomUUID();
      String input = id.toString();
      String encoded = instance.encode(input);
      assertEquals(new String(instance.decode(encoded)), (input));
    }
  }

  @Test
  public void ensureIdsStringIsEncodedAndDecodedCorrectly() {
    String result = "11W6rgS6AQZMn5FKaK59";

    Encoding instance = Base58.getInstance(Base58.Alphabets.Default);

    byte[] decode = instance.decode(result);
    assertEquals(instance.encode(decode), (result));
  }

  @Test
  public void ensureEncodingIsIdempotent() {
    Encoding instance = Base58.getInstance(Base58.Alphabets.Default);

    String value = "k5UyzPLWuTANuE9a1xdx6W";
    assertEquals(instance.encode(instance.decode(value)), (value));
  }

  @Test
  public void ensureCommonPunctuationCharactersAreEncoded() {

    String input = "!@#$%^&*()_+";

    Encoding instance = Base58.getInstance(Base58.Alphabets.Default);
    String result = instance.encode(input.getBytes());

    assertEquals(result, "dPoCNNENvGW2ngdG");
  }
}
