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
        // print word counts
    }

    public static void main(String[] args) {
        // run word frequency counter
    }
}
