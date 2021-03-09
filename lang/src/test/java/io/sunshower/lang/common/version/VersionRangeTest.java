package io.sunshower.lang.common.version;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class VersionRangeTest {

    @Test
    void ensureVersionRangeParsesSingleVersionCorrectly() {

        VersionRange range = VersionRange.parse("1.0");
        Version lowerBound = range.getLowerBound();
        Version upperBound = range.getUpperBound();
        assertEquals(lowerBound.isClosed(), (true));
        assertEquals(lowerBound, (upperBound));
    }

    @Test
    void ensureVersionRangeParsingTwoOpenRangesProducesExpectedResults() {
        VersionRange range = VersionRange.parse("(1.0, 1.1)");
        Version lowerBound = range.getLowerBound();
        assertEquals(lowerBound.getValue(), ("1.0"));
        assertEquals(lowerBound.isClosed(), (false));

        Version upperBound = range.getUpperBound();
        assertEquals(upperBound.getValue(), ("1.1"));
        assertEquals(upperBound.isClosed(), (false));
    }

    @Test
    void ensureVersionRangeParsingOpenLowerBoundAndClosedUpperBoundProducesExpectedResult() {
        VersionRange range = VersionRange.parse("(1.0, 1.1]");
        Version lowerBound = range.getLowerBound();
        assertEquals(lowerBound.getValue(), ("1.0"));
        assertEquals(lowerBound.isClosed(), (false));

        Version upperBound = range.getUpperBound();
        assertEquals(upperBound.getValue(), ("1.1"));
        assertEquals(upperBound.isClosed(), (true));
    }

    @Test
    void ensureVersionRangeParsingClosedLowerBoundAndOpenUpperBoundProducesExpectedResult() {
        VersionRange range = VersionRange.parse("[1.0, 1.1]");
        Version lowerBound = range.getLowerBound();
        assertEquals(lowerBound.getValue(), ("1.0"));
        assertEquals(lowerBound.isClosed(), (true));
        Version upperBound = range.getUpperBound();
        assertEquals(upperBound.getValue(), ("1.1"));
        assertEquals(upperBound.isClosed(), (true));
    }

    @Test
    void ensureVersionRangeContainsValueThatsBetweenThem() {

        VersionRange range = VersionRange.parse("[1.0, 1.1]");
        assertEquals(range.contains(new Version("1.0")), (true));
    }

    @Test
    void ensureOpenLowerRangeDoesNotContainLowerValue() {
        VersionRange range = VersionRange.parse("(1.0, 1.1]");
        assertEquals(range.contains(new Version("1.0")), (false));
    }
}
