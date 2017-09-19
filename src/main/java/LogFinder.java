import java.util.*;
import java.util.stream.*;
import java.io.*;
import java.nio.file.*;

import org.apache.commons.cli.*;

public class LogFinder {
    private final List<String> paths;
    private final String extensions;

    public LogFinder(List<String> paths, String extensions) {
        this.paths = paths;
        this.extensions = extensions;
    }

    public List<String> getPaths() {
        return paths;
    }

    public String getExtensions() {
        return extensions;
    }

    public List<File> find(List<String> words, Criteria criteria) throws IOException {
        List<File> result = new ArrayList<>();
        for (String path : paths) {
            List<File> files = getFiles(path);
            for (File file : files) {
                if (findInFile(file, words, criteria)) {
                    result.add(file);
                }
            }
        }
        return result;
    }

    public boolean findInFile(File file, List<String> words, Criteria criteria) throws IOException {
        Map<String, Boolean> wordsFound = new HashMap<>();
        for (String word : words) {
            wordsFound.put(word, false);
        }
        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = in.readLine()) != null) {
                for (String word : words) {
                    if (findInLine(line, word)) {
                        wordsFound.put(word, true);
                    }
                }
            }
        }
        return criteria.find(wordsFound);
    }

    public boolean findInLine(String line, String word) {
        return line.indexOf(word) != -1;
    }

    public List<File> getFiles(String path) throws IOException {
        List<File> result = new ArrayList<>();
        Path dir = Paths.get(path);
        try (DirectoryStream<Path> files = Files.newDirectoryStream(dir, this.extensions)) {
            for (Path file : files) {
                result.add(file.toFile());
            }
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        // set up cmd line options
        Options options = new Options();
        options.addOption("path", true, "The search path");
        options.addOption("and", false, "Use 'and' semantics when searching words in a file");
        options.addOption("extensions", true, "The file extensions to search");
        CommandLineParser parser = new BasicParser();
        CommandLine cmd = parser.parse(options, args);

        // get cmd line values
        String paths = cmd.getOptionValue("path");
        String exts = cmd.getOptionValue("extensions");
        boolean useAnd = cmd.hasOption("and");
        Criteria criteria = useAnd ? Criteria.AND : Criteria.OR;
        List<String> words = cmd.getArgList();

        // execute log finder
        LogFinder logFinder = new LogFinder(Arrays.asList(paths), exts);
        for (File file : logFinder.find(words, criteria)) {
            System.out.println(file.getPath());
        }
    }
}
