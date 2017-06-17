package io.sunshower.lang.tuple

import spock.lang.Specification
import spock.lang.Unroll

import static io.sunshower.lang.tuple.Pair.*;
/**
 * Created by haswell on 4/6/16.
 */
class PairTest extends Specification {

    @Unroll
    def "equal pairs must be equivalent"() {
        expect:
            e == v
        where:
            e << [of("1", "2"), of(null, null), of("a", null), of(null, "a")]
            v << [of("1", "2"), of(null, null), of("a", null), of(null, "a")]
    }

    @Unroll
    def "non-equivalent pairs must not be equivalent"() {
        expect:
            e != v
        where:
            e << [
                    of("1", "2"), of(null, null), of("a", null), of(null, "a")
            ]
            v << [
                    of(null, null),
                    of("1", "2"),
                    of(null, "a"),
                    of("a", null)
            ]

    }

    @Unroll
    def "non-equivalent pairs must not be equivalent for snd"() {
        expect:
            e.toString() != v.toString()
            e.hashCode() != v.hashCode()
            e.snd != v.snd
        where:
            e << [
                    of("1", "2"), of(null, null), of("a", null), of(null, "a")
            ]
            v << [
                    of(null, null),
                    of("1", "2"),
                    of(null, "a"),
                    of("a", null)
            ]
    }

    @Unroll
    def "non-equivalent pairs must not be equivalent for fst"() {
        expect:
            e.fst != v.fst
        where:
            e << [
                    of("1", "2"), of(null, null), of("a", null), of(null, "a")
            ]
            v << [
                    of(null, null),
                    of("1", "2"),
                    of(null, "a"),
                    of("a", null)
            ]
    }

}
