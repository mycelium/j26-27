import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException{
        // read file, tokenize words, update map
        String content = Files.readString(filePath, StandardCharsets.UTF_8);
        Map<String, Integer> frequencies = new LinkedHashMap<>();
        for (String raw : content.split("[^\\p{L}\\p{N}']+")) {
            if (!raw.isEmpty()) {
                String word = raw.toLowerCase(Locale.ROOT);
                frequencies.merge(word, 1, Integer::sum);
            }
        }
        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new LinkedHashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String raw : line.split("[^\\p{L}\\p{N}']+")) {
                    if (!raw.isEmpty()) {
                        String word = raw.toLowerCase(Locale.ROOT);
                        frequencies.merge(word, 1, Integer::sum);
                    }
                }
            }
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
        frequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.printf("%-20s %d%n", e.getKey(), e.getValue()));
    }

    public static void main(String[] args) {
        // run word frequency counter
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
