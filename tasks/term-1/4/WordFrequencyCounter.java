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

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
    }

    public static void main(String[] args) {
        // run word frequency counter
    }
}
