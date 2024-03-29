package io.sunshower.crypt;

import static java.lang.String.format;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.crypt.core.DecryptedValue;
import io.sunshower.crypt.core.EncryptedValue;
import io.sunshower.crypt.core.EncryptionService;
import io.sunshower.lang.common.encodings.Encoding;
import io.sunshower.lang.common.encodings.Encodings;
import io.sunshower.lang.common.encodings.Encodings.Type;
import io.sunshower.lang.primitives.Rope;
import io.sunshower.lang.tuple.Pair;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.function.Function;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;

@SuppressWarnings("PMD")
@SuppressFBWarnings
public class JCAEncryptionService implements EncryptionService {

  /** default key length */
  public static final int DEFAULT_LENGTH = 256;
  /** default number of rounds */
  public static final int DEFAULT_ROUNDS = 65_536;

  public static final String AES_CTR_NO_PADDING = "AES/CTR/NoPadding";
  final Charset charset = StandardCharsets.UTF_8;
  /** the salt for this service */
  private final CharSequence salt;
  /** the algorithm for this service */
  private final String algorithm;

  private final int size;
  private final int rounds;
  /** the password for this service. Probably better to not use a string */
  private final CharSequence password;

  private final Object lock;
  /** the encoding to use--base58, base64, etc. */
  private final Encoding encoding;

  private volatile SecretKey key;
  private volatile IvParameterSpec initializationVector;

  {
    lock = new Object();
  }

  public JCAEncryptionService(
      int rounds,
      int size,
      @NonNull final CharSequence salt,
      @NonNull final String algorithm,
      @NonNull final Encoding encoding,
      @NonNull final CharSequence password) {
    this.salt = salt;
    this.size = size;
    this.rounds = rounds;
    this.encoding = encoding;
    this.password = password;
    this.algorithm = algorithm;
    this.key = generatePassword(password);
  }

  public JCAEncryptionService(Encoding encoding, CharSequence salt, CharSequence password) {
    this(DEFAULT_ROUNDS, DEFAULT_LENGTH, salt, "PBKDF2WithHmacSHA256", encoding, password);
  }

  public JCAEncryptionService(CharSequence salt, CharSequence password) {
    this(Encodings.create(Type.Base58), salt, password);
  }

  public static EncryptionService serviceFrom(EncryptedValue value, CharSequence password) {
    val encoding = value.getEncoding();
    val decodedSalt = new String(encoding.decode(value.getSalt()));
    val decodedInitializationVector = encoding.decode(value.getInitializationVector());
    val service = new JCAEncryptionService(value.getEncoding(), decodedSalt, password);
    service.initializationVector = new IvParameterSpec(decodedInitializationVector);
    return service;
  }

  private byte[] bytesFor(CharSequence sequence) {
    return StandardCharsets.UTF_8.encode(CharBuffer.wrap(sequence)).array();
  }

  @Override
  @SneakyThrows
  public CharSequence encrypt(CharSequence input, Function<byte[], CharSequence> transformer) {
    synchronized (lock) {
      val algorithm = AES_CTR_NO_PADDING;
      val secretKey = createSecretKey();
      val cipher = Cipher.getInstance(algorithm);
      val parameterSpec = generateInitializationVector();
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
      val charBuffer = CharBuffer.wrap(input);
      return transformer.apply(
          encoding
              .encode(cipher.doFinal(charset.encode(charBuffer).array()))
              .getBytes(StandardCharsets.UTF_8));
    }
  }

  @Override
  @SneakyThrows
  public CharSequence decrypt(CharSequence input, Function<byte[], CharSequence> transformer) {
    synchronized (lock) {
      val decoded = encoding.decode(input);
      val cipher = Cipher.getInstance(AES_CTR_NO_PADDING);
      val key = createSecretKey();
      val initializationVector = generateInitializationVector();
      cipher.init(Cipher.DECRYPT_MODE, key, initializationVector);
      val plainText = cipher.doFinal(decoded);
      return new String(plainText);
    }
  }

  @Override
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public <T extends Serializable> SealedObject encrypt(T object) {
    synchronized (lock) {
      val algorithm = AES_CTR_NO_PADDING;
      val secretKey = createSecretKey();
      val cipher = Cipher.getInstance(algorithm);
      val parameterSpec = generateInitializationVector();
      cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
      return new SealedObject(object, cipher);
    }
  }

  @Override
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public <T extends Serializable> T decrypt(SealedObject object) {
    synchronized (lock) {
      val cipher = Cipher.getInstance(AES_CTR_NO_PADDING);
      val key = createSecretKey();
      val initializationVector = generateInitializationVector();
      cipher.init(Cipher.DECRYPT_MODE, key, initializationVector);
      return (T) object.getObject(cipher);
    }
  }

  @Override
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public <T extends Serializable> byte[] encryptToBytes(T object) {
    synchronized (lock) {
      val cipher = Cipher.getInstance(AES_CTR_NO_PADDING);
      val key = createSecretKey();
      val initializationVector = generateInitializationVector();
      cipher.init(Cipher.ENCRYPT_MODE, key, initializationVector);
      try (val outputStream = new ByteArrayOutputStream();
          val cipherOutputStream = new CipherOutputStream(outputStream, cipher);
          val objectOutputStream = new ObjectOutputStream(cipherOutputStream)) {
        objectOutputStream.writeObject(object);
        return outputStream.toByteArray();
      }
    }
  }

  @Override
  @SneakyThrows
  @SuppressWarnings("unchecked")
  public <T extends Serializable> T decryptFromBytes(byte[] data) {
    synchronized (lock) {
      val secretKey = createSecretKey();
      val cipher = Cipher.getInstance(AES_CTR_NO_PADDING);
      val parameterSpec = generateInitializationVector();
      cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

      try (val inputStream = new ByteArrayInputStream(data);
          val cipherInputStream = new CipherInputStream(inputStream, cipher);
          val objectInputStream = new ObjectInputStream(cipherInputStream)) {
        return (T) objectInputStream.readObject();
      }
    }
  }

  @Override
  public EncryptedValue encryptText(CharSequence input) {
    val encryptedText = encrypt(input);
    val salt = saltAsBytes();
    val initializationVector = this.initializationVector.getIV();
    return new DefaultEncryptedValue(
        encoding, encryptedText.toString(), salt, initializationVector);
  }

  @Override
  @SneakyThrows
  public DecryptedValue decryptText(EncryptedValue encryptedValue) {
    val service = (JCAEncryptionService) serviceFrom(encryptedValue, password);
    service.key = this.key;
    val plaintext = service.decrypt(new Rope(encryptedValue.getCipherText()));
    return new DefaultDecryptedValue(plaintext);
  }

  @Override
  @SneakyThrows
  public SecretKey generatePassword(CharSequence input) {
    val factory = SecretKeyFactory.getInstance(algorithm);
    val keySpec = new PBEKeySpec(passwordAsCharacters(), saltAsBytes(), rounds, size);
    return new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "AES");
  }

  @Override
  @SneakyThrows
  public byte[] createSalt() {
    val initializationVector = new byte[64];
    val instance = SecureRandom.getInstance("SHA1PRNG");
    instance.nextBytes(initializationVector);
    return initializationVector;
  }

  @Override
  @SneakyThrows
  public byte[] createInitializationVector() {
    val initializationVector = new byte[16];
    val instance = SecureRandom.getInstance("SHA1PRNG");
    instance.nextBytes(initializationVector);
    return initializationVector;
  }

  @Override
  public CharSequence createSaltAndInitializationVector() {
    val salt = createSalt();
    val iv = createInitializationVector();

    val encodedSalt = encoding.encode(salt);
    val encodedIv = encoding.encode(iv);
    return encoding.encode(encodedSalt + ":" + encodedIv);
  }

  @Override
  public Pair<byte[], byte[]> decodeSaltAndInitializationVector(CharSequence saltAndIv) {
    val siv = new String(encoding.decode(saltAndIv), StandardCharsets.UTF_8).split(":");
    if (siv.length != 2) {
      throw new IllegalArgumentException(
          format("Error: invalid salt and initialization vector: %s", saltAndIv));
    }
    val encodedIv = siv[1];
    val encodedSalt = siv[0];
    return Pair.of(encoding.decode(encodedSalt), encoding.decode(encodedIv));
  }

  @SneakyThrows
  private IvParameterSpec generateInitializationVector() {
    var iv = initializationVector;
    if (iv != null) {
      return iv;
    }
    synchronized (lock) {
      iv = initializationVector;
      if (iv != null) {
        return iv;
      } else {
        byte[] initializationVector = createInitializationVector();
        return (this.initializationVector = new IvParameterSpec(initializationVector));
      }
    }
  }

  private char[] passwordAsCharacters() {
    val result = new char[password.length()];
    for (int i = 0; i < result.length; i++) {
      result[i] = password.charAt(i);
    }
    return result;
  }

  private byte[] saltAsBytes() {
    val cbuffer = CharBuffer.wrap(salt);
    return charset.encode(cbuffer).array();
  }

  private SecretKey createSecretKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    var k = key;
    if (k != null) {
      return key;
    } else {
      synchronized (lock) {
        k = key;
        if (k == null) {
          return key = generatePassword(password);
        }
        return k;
      }
    }
  }

  public void setInitializationVector(CharSequence initializationVector) {
    val iv = encoding.decode(initializationVector);
    this.initializationVector = new IvParameterSpec(iv);
  }
}
