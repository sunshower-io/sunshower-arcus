package io.sunshower.lang.common.version;

/**
 * Created by haswell on 5/26/16.
 */
public final class Version implements Comparable<Version> {
    private final String value;
    private final boolean closed;

    public Version(boolean closed, String value) {
        this.value = value;
        this.closed = closed;
    }

    public Version(String value) {
        this(true, value);
    }

    public String getValue() {
        return value;
    }

    boolean isClosed() {
        return closed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Version version = (Version) o;

        if (closed != version.closed) return false;
        return value != null ? value.equals(version.value) : version.value == null;

    }

    @Override
    public int hashCode() {
        int result = value != null ? value.hashCode() : 0;
        result = 31 * result + (closed ? 1 : 0);
        return result;
    }

    @Override
    public int compareTo(Version o) {
        if(o == null) return 1;

        /**
         * mine: [0, 1]
         * theirs: (0, 1]
         */
        if(this.value.equals(o.value)) {
            if(this.closed && !o.closed) {
                return 1;
            } else if(!this.closed && o.closed) {
                return -1;
            }
            return 0;
        }
        return this.value.compareTo(o.value);

//        if(this.value.equals(o.value)) {
//            if(closed && !o.closed) {
//                return -1;
//            } else if(!closed && o.closed) {
//                return 1;
//            } else {
//                return 0;
//            }
//        } else {
//            return this.value.compareTo(o.value);
//        }
    }
}
