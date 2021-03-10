package io.sunshower.lang.common.hash.integers

import io.sunshower.lang.common.hash.HashFunction
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by haswell on 4/4/16.
 */
class Murmur3Spec extends Specification {

    def "murmur3's utility functions must work"() {
        expect:
            m.hashCode() != null
            m.toString() != null
            m.bits() == 32

        where:
            m = HashFunction.murmur3(3);

    }

    @Unroll
    def "murmur3 must hash integers effectively"() {
        when:
            def b = IntegerHashFunction.murmur3(a).apply(a)
        then:
            b != a
        where:
            a << [1,2,3,4,5]
    }

    @Unroll
    def "murmur3 must return different hash values for colliding keys with different seeds"() {
        when:
            def hash1 = HashFunction.murmur3(s1)
            def hash2 = HashFunction.murmur3(s2)
            def h1 = hash1.apply(fst)
            def h2 = hash2.apply(snd)
        then:
            h1 != h2
        where:
            s1 << [101, 2142341]
            s2 << [406, 423141]
            fst << [
                    "Hello",
                    "World"
            ]
            snd << [
                    "Hello",
                    "World"

            ]

    }

    @Unroll
    def "murmur3 must return different hash values for non-colliding keys"() {
        when:
            def hash = HashFunction.murmur3(seed)
            def h1 = hash.apply(fst)
            def h2 = hash.apply(snd)
        then:
            h1 != h2
        where:
            seed << [1,2,3,4,5,6, 7, 8, 9, 10, 11, 12, 13, 14]
            fst << [
                    "Hello",
                    "world",
                    "how",
                    "are",
                    "you?",
                    "the",
                    "quick",
                    "brown",
                    "fox",
                    "jumped",
                    "over",
                    "the",
                    "lazy",
                    "dog"
            ]

            snd << [
                    "one",
                    "two",
                    "three",
                    "four",
                    "five",
                    "six",
                    "seven",
                    "eight",
                    "nine",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14"
            ]

    }
}
