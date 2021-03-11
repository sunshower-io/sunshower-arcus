package io.sunshower.lang.io

import io.sunshower.lambda.Option
import spock.lang.Specification
import spock.lang.Unroll

class FilesTest extends Specification {
    @Unroll
    def "Files must extract extensions correctly for string parameters"() {
        expect:
            Files.getExtension(f) == Option.of(e)
        where:
            f << ["test.yaml", "frapper.y", "ext.xml"]
            e << ["yaml", "y", "xml"]
    }
}
