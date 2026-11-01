import java.util.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WordFrequencyCounter {

    // everything except letters and digits separates words
    private static final String DELIMITER = "[^\\p{L}\\p{N}]+";

    // variant 1: load the whole file into memory, then process it
    public Map<String, Integer> countWords(Path filePath) {
        String text;
        try {
            text = Files.readString(filePath, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        Map<String, Integer> frequencies = new HashMap<>();
        for (String word : text.split(DELIMITER)) {
            addWord(frequencies, word);
        }
        return frequencies;
    }

    private void addWord(Map<String, Integer> frequencies, String word) {
        if (!word.isEmpty()) {
            frequencies.merge(word.toLowerCase(), 1, Integer::sum);
        }
    }

    // variant 2: streaming, Scanner reads the file in small chunks and returns one word at a time,
    // so only the map of unique words is kept in memory
    public Map<String, Integer> countWordsStreaming(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try (Scanner scanner = new Scanner(filePath, StandardCharsets.UTF_8)) {
            scanner.useDelimiter(DELIMITER);
            while (scanner.hasNext()) {
                addWord(frequencies, scanner.next());
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // most frequent words first, equal counts in alphabetical order
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue().reversed()
                .thenComparing(Map.Entry.comparingByKey()));
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Unique words: " + frequencies.size());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java WordFrequencyCounter <file> [full|stream]");
            return;
        }
        Path filePath = Path.of(args[0]);
        String mode = args.length > 1 ? args[1] : "stream";

        WordFrequencyCounter counter = new WordFrequencyCounter();
        long start = System.nanoTime();
        Map<String, Integer> frequencies = mode.equals("full")
                ? counter.countWords(filePath)
                : counter.countWordsStreaming(filePath);
        long elapsed = (System.nanoTime() - start) / 1_000_000;

        counter.printFrequencies(frequencies);
        System.out.println("Mode: " + mode + ", time: " + elapsed + " ms");
    }
}
