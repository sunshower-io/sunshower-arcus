package io.sunshower.lang.common.encodings

import spock.lang.Specification
import spock.lang.Unroll

class CharArraySliceTest extends Specification {


    @Unroll
    def "char array slices must work as expected"() {
        expect:
        lhs.toString() == rhs.toString()
        where:
        lhs << [
                "hello",
                "world",
                "how",
                "are",
                "this is a longer slice"
        ].collect { str -> new CharArraySlice(str.toCharArray()) }

        rhs << [
                "hello",
                "world",
                "how",
                "are",
                "this is a longer slice"
        ]
    }


    @Unroll
    def "expect substrings to work for repeated substring values"() {
        expect:

        def end = rhs.length() - 3
        def start = 1

        def end1 = end - 2
        def start1 = start + 3;

        def lhss = lhs.subSequence(start, end).subSequence(start1, end1).toString();
        def rhss = rhs.subSequence(start, end).subSequence(start1, end1).toString();
        lhss == rhss

        where:
        lhs << [
                "hello adfadfadfadfq adfadfadfadfadf",
                "world adfafasdfa  adfasdfdasf  adfadfadfadf",
                "howcoobadfafda adfadfadfadf",
                "are asdfadfadfadfasf adfadfadfad",
                "this is a longer slice adfadfasdf"
        ].collect { str -> new CharArraySlice(str.toCharArray()) }

        rhs << [
                "hello adfadfadfadfq adfadfadfadfadf",
                "world adfafasdfa  adfasdfdasf  adfadfadfadf",
                "howcoobadfafda adfadfadfadf",
                "are asdfadfadfadfasf adfadfadfad",
                "this is a longer slice adfadfasdf"
        ]

    }

    @Unroll
    def "expect substrings to work for values"() {
        expect:

        def start = 2
        def end = 5

        def lhss = lhs.subSequence(start, end).toString();
        def rhss = rhs.subSequence(start, end).toString();
        lhss == rhss

        where:
        lhs << [
                "hello adfadfadfadfq",
                "world adfafasdfa  adfasdfdasf ",
                "howcoobadfafda",
                "are asdfadfadfadfasf",
                "this is a longer slice"
        ].collect { str -> new CharArraySlice(str.toCharArray()) }

        rhs << [
                "hello adfadfadfadfq",
                "world adfafasdfa  adfasdfdasf ",
                "howcoobadfafda",
                "are asdfadfadfadfasf",
                "this is a longer slice"
        ]

    }

    @Unroll
    def "expect substrings to work correctly"() {
        expect:
        def start = 2;
        def end = rhs.length() - 3;
        def lhss = lhs.subSequence(start, end).toString();
        def rhss = rhs.subSequence(start, end).toString();
        lhss == rhss

        where:
        lhs << [
                "hello adfadfadfadfq",
                "world adfafasdfa  adfasdfdasf ",
                "howcoobadfafda",
                "are asdfadfadfadfasf",
                "this is a longer slice"
        ].collect { str -> new CharArraySlice(str.toCharArray()) }

        rhs << [
                "hello adfadfadfadfq",
                "world adfafasdfa  adfasdfdasf ",
                "howcoobadfafda",
                "are asdfadfadfadfasf",
                "this is a longer slice"
        ]

    }
}
