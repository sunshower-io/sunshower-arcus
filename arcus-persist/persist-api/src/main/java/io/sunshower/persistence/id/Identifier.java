package io.sunshower.persistence.id;

import static io.sunshower.lang.common.encodings.Base58.Alphabets.Default;
import static java.lang.String.format;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.lang.common.encodings.Base58;
import io.sunshower.lang.common.encodings.Encoding;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

public class Identifier implements Comparable<Identifier>, Serializable {

    static final transient Encoding base58 = Base58.getInstance(Default);

    static final transient Sequence<Identifier> randomSequence = Identifiers.randomSequence();

    volatile byte[] id;

    protected Identifier() {}

    @SuppressFBWarnings
    public byte[] value() {
        return id;
    }

    public static Identifier valueOf(UUID id) {
        final ByteBuffer bb = ByteBuffer.allocate(16);
        bb.putLong(id.getMostSignificantBits());
        bb.putLong(id.getLeastSignificantBits());
        return new Identifier(bb.array());
    }

    public static Identifier valueOf(byte[] id) {
        return new Identifier(id);
    }

    Identifier(byte[] id) {
        if (id == null || id.length != 16) {
            throw new IllegalArgumentException("Argument cannot possibly be a valid identifier");
        }
        this.id = id;
    }

    @Override
    public int compareTo(Identifier o) {
        if (o == null) {
            return 1;
        }

        for (int i = 0; i < 16; i++) {
            final byte v = id[i];
            final byte tv = o.id[i];

            if (v > tv) {
                return 1;
            } else if (tv > v) {
                return -1;
            }
        }
        return 0;
    }

    @Override
    @SuppressFBWarnings
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (o.getClass().equals(Identifier.class)) {
            final Identifier other = (Identifier) o;
            return Arrays.equals(id, other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : Arrays.hashCode(id);
    }

    @Override
    public String toString() {
        return toString(Default);
    }

    public static final Identifier random() {
        return randomSequence.next();
    }

    public String toString(Base58.Alphabets alphabet) {
        if (id == null) {
            return "id[null]";
        }
        switch (alphabet) {
            case Default:
                return base58.encode(Arrays.copyOf(id, id.length));
            default:
                return Base58.getInstance(alphabet).encode(Arrays.copyOf(id, id.length));
        }
    }

    public static Identifier valueOf(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Identifier cannot be null");
        }
        return new Identifier(base58.decode(id));
    }

    public static Identifier copyOf(Identifier id) {
        return new Identifier(id.id);
    }

    public byte[] getBytes() {
        return Arrays.copyOf(id, id.length);
    }

    public static boolean isIdentifier(String id) {
        byte[] decode = base58.decode(id);
        return id != null && decode.length == 16 && base58.test(id);
    }

    public static Identifier decode(String s) {
        if (isIdentifier(s)) {
            return new Identifier(base58.decode(s));
        }
        throw new IllegalArgumentException(format("Invalid identifier: '%s'", s));
    }

    public void setId(String id) {
        this.id = base58.decode(id);
    }

    @SuppressFBWarnings
    public byte[] getId() {
        return id;
    }
}
