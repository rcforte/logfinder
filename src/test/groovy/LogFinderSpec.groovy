import spock.lang.Specification
import java.nio.file.*;

class LogFinderSpec extends Specification {
  def "log finder is created with paths and extensions"() {
    given:
    def paths = ['.']
    def extensions = '*.txt'

    when:
    def logFinder = new LogFinder(paths, extensions)

    then:
    logFinder.paths == paths
    logFinder.extensions == extensions
  }

  def "log finder returns empty list of files when looking for empty list of words"() {
    given:
    def logFinder = new LogFinder(['.'], '*.txt')

    when:
    def files = logFinder.find([], Criteria.OR)

    then:
    files != null
    files.isEmpty()
  }

  def "log finder finds word in line"() {
    given:
    def logFinder = new LogFinder(['.'], '*.txt') 

    when:
    def found = logFinder.findInLine('this is a line', 'is')

    then:
    found == true
  }

  def "log finder gets the list of files in a given path"() {
    given:
    def dir = new File('tmp')
    dir.mkdirs()

    def txtFile = new File(dir, 'my-test.txt')
    txtFile << 'test'

    def pdfFile = new File(dir, 'my-test.pdf')
    pdfFile << 'test'

    def logFinder = new LogFinder(['tmp'], '*.pdf')

    when:
    def files = logFinder.getFiles(dir.path)

    then:
    files != null
    files.size == 1
    files[0] == pdfFile

    cleanup:
    Files.delete(txtFile.toPath())
    Files.delete(pdfFile.toPath())
    Files.delete(dir.toPath())
  }

  def "log finder finds word in given file"() {
    given:
    def words = ['Rafael']

    def dir = new File('tmp')
    dir.mkdirs()

    def file = new File(dir, 'my-test.txt')
    words.forEach { file << it }

    def logFinder = new LogFinder(['.'], '*.txt')

    when:
    def found = logFinder.findInFile(file, words, Criteria.OR)

    then:
    found == true

    cleanup:
    Files.delete(file.toPath())
    Files.delete(dir.toPath())
  }

  def "log finder finds 1 word in 1 file"() {
    given:
    def logFinder = new LogFinder(['.'], '*.tmp')
    def words = ['Rafael']
    def tempFile = new File('./test.tmp')
    words.forEach { tempFile << it }

    when:
    def files = logFinder.find(words, Criteria.OR)

    then:
    files != null
    files.size == 1
    files[0] == tempFile

    cleanup:
    tempFile.delete()
  }

  def "log finder does not find file using and"() {
    given:
    def logFinder = new LogFinder(['.'], '*.tmp')

    def tempFile = new File('./test.tmp')
    tempFile << 'Rafael'
    tempFile << 'Forte'

    when:
    def files = logFinder.find(['Rafael', 'Lee'], Criteria.AND)

    then:
    files != null
    files.size == 0

    cleanup:
    tempFile.delete()
  }

  def "log finder finds file using and"() {
    given:
    def logFinder = new LogFinder(['.'], '*.tmp')

    def tempFile = new File('./test.tmp')
    tempFile << 'Rafael'
    tempFile << 'Forte'

    when:
    def files = logFinder.find(['Rafael', 'Forte'], Criteria.AND)

    then:
    files != null
    files.size == 1
    files[0] == tempFile

    cleanup:
    tempFile.delete()
  }
}
