import java.util.*;
import java.nio.file.*;
import java.io.*;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();

        String text = Files.readString(filePath);
        String[] words = text.toLowerCase().split("[^\\p{L}\\p{N}]+");

        for (String word : words) {
            if (!word.isEmpty()) {
                frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
            }
        }

        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("[^\\p{L}\\p{N}]+");

                for (String word : words) {
                    if (!word.isEmpty()) {
                        frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }

        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        for (Map.Entry<String, Integer> entry : new TreeMap<>(frequencies).entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        WordFrequencyCounter counter = new WordFrequencyCounter();

        Path filePath = Path.of(args.length > 0 ? args[0] : "input.txt");

        try {
            System.out.println("Обычный способ:");
            Map<String, Integer> result1 = counter.countWords(filePath);
            counter.printFrequencies(result1);

            System.out.println("\nПотоковый способ:");
            Map<String, Integer> result2 = counter.countWordsStreaming(filePath);
            counter.printFrequencies(result2);

        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
    }
}
