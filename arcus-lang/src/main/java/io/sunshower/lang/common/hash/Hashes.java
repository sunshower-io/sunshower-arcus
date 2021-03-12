package io.sunshower.lang.common.hash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.val;

public class Hashes {

  public interface HashCode {

    Algorithm getAlgorithm();

    default byte[] digest(Object... os) {
      try (val outputStream = new ByteArrayOutputStream();
          val objectOutputStream = new ObjectOutputStream(outputStream)) {
        for (Object o : os) {
          objectOutputStream.writeObject(o);
        }
        return digest(outputStream.toByteArray());
      } catch (IOException ex) {
        throw new IllegalStateException("Encountered unexpected error digesting objects", ex);
      }
    }

    default byte[] digest(ByteBuffer buf) {
      return digest(buf.array());
    }

    default byte[] digest(byte[] data) {
      try {
        return MessageDigest.getInstance(getAlgorithm().getName()).digest(data);
      } catch (NoSuchAlgorithmException e) {
        throw new IllegalStateException(e);
      }
    }
  }

  public enum Algorithm {
    MD2("MD2"),
    MD5("MD5"),
    SHA1("SHA-1"),
    SHA256("SHA-256"),
    SHA384("SHA-384"),
    SHA512("SHA-512"),
    BLAKE2B("BLAKE-2b"),
    BLAKE2S("BLAKE-2s");

    private final String name;

    Algorithm(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

  public static HashCode hashCode(Algorithm algorithm) {
    return new DefaultHashCode(algorithm);
  }

  private static class DefaultHashCode implements HashCode {
    final Algorithm algorithm;

    private DefaultHashCode(final Algorithm algorithm) {
      this.algorithm = algorithm;
    }

    @Override
    public Algorithm getAlgorithm() {
      return algorithm;
    }
  }
}
