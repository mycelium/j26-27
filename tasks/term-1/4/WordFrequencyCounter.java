import java.util.*;
import java.nio.file.Path;
import java.io.IOException;
import java.util.stream.Stream;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        String content = Files.readString(filePath);
        Map<String, Integer> frequencies = new HashMap<>();
        String[] words = content.toLowerCase().split("[^\\p{L}\\p{N}]+");

        for (String word : words) {
            if (!word.isEmpty()) {
                frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
            }
        }
        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        
        try (java.io.BufferedReader reader = Files.newBufferedReader(filePath)) {
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
        for (String word : frequencies.keySet()) {
            System.out.println(word + ": " + frequencies.get(word));
        }
    }

    public static void main(String[] args) {
        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of("input.txt");
        try {
            System.out.println("Весь файл");
            Map<String, Integer> firstResult = counter.countWords(filePath);
            counter.printFrequencies(firstResult);
            System.out.println("\nПострочно");
            Map<String, Integer> secondResult = counter.countWordsStreaming(filePath);
            counter.printFrequencies(secondResult);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
        }
    }
}
