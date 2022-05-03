package io.sunshower.crypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import io.sunshower.crypt.core.EncryptionService;
import java.io.IOException;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JCAEncryptionServiceTest {

  private String salt;
  private String password;
  private String plaintext;
  private EncryptionService encryptionService;

  @BeforeEach
  void setUp() {
    salt = "hello world";
    password = "How are you";
    encryptionService = new JCAEncryptionService(salt, password);
    plaintext = "whaddup";
  }

  @Test
  void ensureSaltAndIvAreEncodedAndDecodedCorrectly() {
    val siv = encryptionService.createSaltAndInitializationVector();
    val siv2 = encryptionService.decodeSaltAndInitializationVector(siv);
    assertEquals(64, siv2.fst().length);
    assertEquals(16, siv2.snd().length);
  }

  @Test
  void ensureEncryptingWorks() {
    val result = encryptionService.encrypt((CharSequence) plaintext);
    assertNotEquals(plaintext, result);
  }

  @Test
  void ensureDecryptingWorks() {
    val result = encryptionService.decrypt(encryptionService.encrypt((CharSequence) plaintext));
    assertEquals(plaintext, result.toString());
  }

  @Test
  void ensureEncryptingAndDecryptingValueWorks() throws IOException {
    val result = encryptionService.encryptText(plaintext);
    val decrypted = encryptionService.decryptText(result);
    assertEquals(decrypted.getValue().toString(), plaintext);
  }
}
