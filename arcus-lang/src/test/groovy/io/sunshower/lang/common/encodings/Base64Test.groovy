package io.sunshower.lang.common.encodings

import spock.lang.Shared
import spock.lang.Specification

import java.nio.charset.Charset

class Base64Test extends Specification {
    @Shared
    def javaencoding = java.util.Base64.getEncoder()

    def "encoding must work on streams"() {
        expect:
        def inputStream = new ByteArrayInputStream(value.getBytes())
        def outputStream = new ByteArrayOutputStream()


        def encoding = Encodings.create(Encodings.Type.Base64)
        encoding.encode(inputStream, outputStream)

        javaencoding.encodeToString(value.getBytes()) == outputStream.toString()


        where:


        value << [
                "h",
                "hello world",
                "this is some fun stuff"
        ]
    }

    def "encoding must work on strings"() {
        expect:
        def encoding = Encodings.create(Encodings.Type.Base64)

        encoding.encode(value) == javaencoding.encodeToString(value.getBytes(Charset.defaultCharset()))
        where:
        value << [
                "h",
                "hello world",
                "this is some fun stuff"
        ]
    }
}
