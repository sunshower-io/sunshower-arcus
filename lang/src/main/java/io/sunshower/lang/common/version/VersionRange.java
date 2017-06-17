package io.sunshower.lang.common.version;

import io.sunshower.lang.common.Strings;

import java.util.function.Predicate;

/**
 * Created by haswell on 5/26/16.
 */
public class VersionRange {


    public static VersionRange parse(String s) {
        if(Strings.isBlank(s)) {
            throw new IllegalArgumentException("Version must not be null or empty!");
        }
        final String version = s.trim();
        if(version.charAt(0) == '[' || version.charAt(0) == '(') {
            return parseRange(version);
        } else {
            return parseFixed(version);
        }

    }

    private final Version lowerBound;
    private final Version upperBound;

    private VersionRange(Version version) {
        this(version, version);
    }

    public boolean contains(Version v) {
        return lowerBound.compareTo(v) >= 0 &&
                v.compareTo(upperBound) <= 0;
    }


    private VersionRange(Version lowerBound, Version upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    private static VersionRange parseFixed(String version) {
        return new VersionRange(new Version(true, version));
    }

    private static VersionRange parseRange(String version) {
        final String lowerBound = consumeUntil(version, 1, VersionRange::isWhitespaceOrSeparator, true);
        boolean closedUpperBound = version.charAt(version.length() - 1) == ']';
        boolean closedLowerBound = version.charAt(0) == '[';


        return new VersionRange(
                new Version(closedLowerBound, lowerBound),
                new Version(closedUpperBound,
                        consumeUntil(version,
                                version.length() - 2,
                                VersionRange::isWhitespaceOrSeparator,
                                false)
                ));
    }

    private static boolean isWhitespaceOrSeparator(char ch) {
        return Character.isWhitespace(ch)
                || ch == ','
                || ch == '[' || ch == ']' ||
                ch == '(' || ch == ')';
    }

    private static String consumeUntil(String version, int beginIndex, Predicate<Character> p, boolean direction) {
        final StringBuilder b = new StringBuilder();
        int i = beginIndex;
        char ch;
        do {
            ch = version.charAt(i);
            if(!p.test(ch)) {
                b.append(ch);
            } else {
                break;
            }
            if(direction) {
                ++i;
            } else {
                --i;
            }
        } while(true);
        return b.toString();
    }


    public Version getLowerBound() {
        return lowerBound;
    }

    public Version getUpperBound() {
        return upperBound;
    }
}
