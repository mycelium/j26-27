import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class WordFrequencyCounter {

    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}']+");

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        return countWordsFromText(content);
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                countWordsFromText(line, frequencies);
            }
        }

        return frequencies;
    }

    private Map<String, Integer> countWordsFromText(String text) {
        Map<String, Integer> frequencies = new HashMap<>();
        countWordsFromText(text, frequencies);
        return frequencies;
    }

    private void countWordsFromText(String text, Map<String, Integer> frequencies) {
        var matcher = WORD_PATTERN.matcher(text.toLowerCase());
        while (matcher.find()) {
            String word = matcher.group();
            if (!word.isEmpty() && !word.equals("'")) {
                frequencies.merge(word, 1, Integer::sum);
            }
        }
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        printFrequencies(frequencies, true);
    }

    public void printFrequencies(Map<String, Integer> frequencies, boolean sortByWord) {
        if (frequencies.isEmpty()) {
            System.out.println("No words found.");
            return;
        }

        System.out.println("Word frequencies (" + frequencies.size() + " unique words):");
        System.out.println("----------------------------------------");

        if (sortByWord) {
            new TreeMap<>(frequencies).forEach((word, count) ->
                    System.out.printf("%-30s %d%n", word, count));
        } else {
            frequencies.entrySet().stream()
                    .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                            .thenComparing(Map.Entry.comparingByKey()))
                    .forEach(e -> System.out.printf("%-30s %d%n", e.getKey(), e.getValue()));
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java WordFrequencyCounter <file> [--stream] [--by-freq]");
            System.err.println("  --stream   use streaming (memory-efficient) mode");
            System.err.println("  --by-freq  sort output by frequency (descending) instead of alphabetically");
            System.exit(1);
        }

        Path filePath = Path.of(args[0]);
        boolean useStreaming = false;
        boolean sortByFreq = false;

        for (int i = 1; i < args.length; i++) {
            if ("--stream".equals(args[i])) {
                useStreaming = true;
            } else if ("--by-freq".equals(args[i])) {
                sortByFreq = true;
            }
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();

        try {
            Map<String, Integer> frequencies;

            if (useStreaming) {
                System.out.println("Using STREAMING mode (line-by-line) ...");
                frequencies = counter.countWordsStreaming(filePath);
            } else {
                System.out.println("Using FULL-LOAD mode ...");
                frequencies = counter.countWords(filePath);
            }

            counter.printFrequencies(frequencies, !sortByFreq);

        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(2);
        }
    }
}