package io.sunshower.lang.common.version;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by haswell on 5/26/16.
 */
public class VersionRangeTest {


    @Test
    public void ensureVersionRangeParsesSingleVersionCorrectly() {

        VersionRange range = VersionRange.parse("1.0");
        Version lowerBound = range.getLowerBound();
        Version upperBound = range.getUpperBound();
        assertThat(lowerBound.isClosed(), is(true));
        assertThat(lowerBound, is(upperBound));
    }

    @Test
    public void ensureVersionRangeParsingTwoOpenRangesProducesExpectedResults() {
        VersionRange range = VersionRange.parse("(1.0, 1.1)");
        Version lowerBound = range.getLowerBound();
        assertThat(lowerBound.getValue(), is("1.0"));
        assertThat(lowerBound.isClosed(), is(false));

        Version upperBound = range.getUpperBound();
        assertThat(upperBound.getValue(), is("1.1"));
        assertThat(upperBound.isClosed(), is(false));
    }

    @Test
    public void ensureVersionRangeParsingOpenLowerBoundAndClosedUpperBoundProducesExpectedResult() {
        VersionRange range = VersionRange.parse("(1.0, 1.1]");
        Version lowerBound = range.getLowerBound();
        assertThat(lowerBound.getValue(), is("1.0"));
        assertThat(lowerBound.isClosed(), is(false));

        Version upperBound = range.getUpperBound();
        assertThat(upperBound.getValue(), is("1.1"));
        assertThat(upperBound.isClosed(), is(true));
    }

    @Test
    public void ensureVersionRangeParsingClosedLowerBoundAndOpenUpperBoundProducesExpectedResult() {
        VersionRange range = VersionRange.parse("[1.0, 1.1]");
        Version lowerBound = range.getLowerBound();
        assertThat(lowerBound.getValue(), is("1.0"));
        assertThat(lowerBound.isClosed(), is(true));
        Version upperBound = range.getUpperBound();
        assertThat(upperBound.getValue(), is("1.1"));
        assertThat(upperBound.isClosed(), is(true));
    }


    @Test
    public void ensureVersionRangeContainsValueThatsBetweenThem() {

        VersionRange range = VersionRange.parse("[1.0, 1.1]");
        assertThat(range.contains(new Version("1.0")), is(true));
    }

    @Test
    public void ensureOpenLowerRangeDoesNotContainLowerValue() {

        VersionRange range = VersionRange.parse("(1.0, 1.1]");
        assertThat(range.contains(new Version("1.0")), is(false));
    }

}