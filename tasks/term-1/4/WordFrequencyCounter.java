import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        String text = Files.readString(filePath, StandardCharsets.UTF_8);
        String[] words = text.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+");
        for (String word : words) {
            if (!word.isEmpty()) {
                frequencies.merge(word, 1, Integer::sum);
            }
        }
        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder currentWord = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            char[] buffer = new char[8192];
            int charsRead;
            while ((charsRead = reader.read(buffer)) != -1) {
                for (int i = 0; i < charsRead; i++) {
                    char ch = Character.toLowerCase(buffer[i]);
                    if (Character.isLetterOrDigit(ch)) {
                        currentWord.append(ch);
                    } else if (currentWord.length() > 0) {
                        frequencies.merge(currentWord.toString(), 1, Integer::sum);
                        currentWord.setLength(0);
                    }
                }
            }
            if (currentWord.length() > 0) {
                frequencies.merge(currentWord.toString(), 1, Integer::sum);
            }
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        Map<String, Integer> sorted = new TreeMap<>(frequencies);
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java WordFrequencyCounter --file <path> | --stream <path>");
            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of(args[1]);

        try {
            Map<String, Integer> frequencies;
            if (args[0].equals("--file")) {
                frequencies = counter.countWords(filePath);
            } else if (args[0].equals("--stream")) {
                frequencies = counter.countWordsStreaming(filePath);
            } else {
                System.out.println("Unknown mode. Use --file or --stream.");
                return;
            }
            counter.printFrequencies(frequencies);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
