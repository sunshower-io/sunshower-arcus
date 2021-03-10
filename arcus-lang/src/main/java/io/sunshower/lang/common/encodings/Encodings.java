package io.sunshower.lang.common.encodings;

public class Encodings {

    public enum Type {
        Base58,
        Base64
    }

    public static Encoding create(Type type) {
        switch (type) {
            case Base64:
                return Instances.BASE64_INSTANCE;
            case Base58:
                return Base58.getInstance(Base58.Alphabets.Default);
        }
        throw new IllegalArgumentException("Unknown encoding type: " + type);
    }

    static final class Instances {
        static final Encoding BASE64_INSTANCE = new Base64();
    }
}
