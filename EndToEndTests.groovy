class EndToEndTests extends GroovyTestCase {
    void "test logfinder finds 1 word in 1 file"() {
        def cmd = [ 'java', '-jar', 'build/libs/logfinder-java-all.jar', '-path', '.', '-extensions', '*.txt', 'Rafael' ]
        def proc = cmd.execute()
        def output = []
        proc.text.eachLine { output << it }
        assert '.\\test.txt' in output
    }

    void "test logfinder finds 2 words in a file using 'and' semantics"() {
        def cmd = [ 'java', '-jar', 'build/libs/logfinder-java-all.jar', '-path', '.', '-and', '-extensions', '*.txt', 'Rafael', 'Lee']
        def proc = cmd.execute()
        def output = []
        proc.text.eachLine { output << it }
        assert output.size == 0
    }
}
