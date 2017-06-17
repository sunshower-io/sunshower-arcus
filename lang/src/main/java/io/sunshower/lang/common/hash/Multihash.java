package io.sunshower.lang.common.hash;


import java.util.Arrays;

/**
 * Created by haswell on 5/25/16.
 */
public final class Multihash {

    public enum Type {

        SHA1(0x11, 20),
        SHA256(0x12, 32),
        SHA512(0x13, 64),
        SHA3(0x14, 64),
        BLAKE2B(0x40, 64),
        BLAKE2S(0x41, 32);

        final int length;
        final int value;
        Type(final int value, final int length) {
            this.value = value;
            this.length = length;
        }



        static Type get(int value) {
            switch(value) {
                case 0x11 :return SHA1;
                case 0x12 : return SHA256;
                case 0x13 : return SHA512;
                case 0x14 : return SHA3;
                case 0x40 : return BLAKE2B;
                case 0x41 : return BLAKE2S;
            }
            throw new InvalidHashException("No hash identified by '" + value + "' was found!");
        }
    }



    final Type type;
    final byte[] data;



    private Multihash(Type type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public String encode(Encoding encoding) {
        return new String(encoding.encode(data));
    }


    public Type getType() {
        return this.type;
    }


    public static Multihash decode(byte[] data) {
        check(data);
        final Type type = Type.get(data[0]);
        final int length = data[1] & 0xFF;
        final byte[] digest = new byte[length];
        System.arraycopy(data, 2, digest, 0, length);
        return new Multihash(type, digest);
    }


    public static byte[] encode(Multihash hash) {
        return hash.toByteArray();
    }

    public byte[] toByteArray() {
        final byte[] result = new byte[data.length + 2];
        result[0] = (byte) type.value;
        result[1] = (byte) data.length;
        System.arraycopy(data, 0, result, 2, data.length);
        return result;
    }


    @Override
    public String toString() {
        return "Multihash{" +
                "type=" + type +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    public boolean equals(Object o) {
        if(o == null) return false;
        if(o == this) return true;
        if(Multihash.class.equals(o.getClass())) {
            final Multihash that = (Multihash) o;
            return that.type == this.type &&
                    Arrays.equals(this.data, that.data);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(data);
        return result;
    }

    private static void check(byte[] data) {
        if(data == null) {
            throw new InvalidHashException("Hash cannot be null");
        }
        final int length = data.length;
        if(length < 3) {
            throw new InvalidHashException("Hash must be at least 3 bytes long");
        }
        if(length > 129) {
            throw new InvalidHashException("Hash must not be longer than 129 bytes");
        }
    }


    interface Encoding {

        byte[] decode(byte[] input);

        byte[] encode(byte[] input);

        default byte[] encode(String input) {
            return encode(input.getBytes());
        }
    }
}
