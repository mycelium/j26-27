import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class WordFrequencyCounter {

    // Вариант 1: Загрузка всего содержимого файла с последующей обработкой
    public Map<String, Integer> countWords(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);

            String[] words = content.toLowerCase().split("[^\\p{L}\\p{N}]+");

            for (String word : words) {
                if (!word.isEmpty()) {
                    frequencies.merge(word, 1, Integer::sum);
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return frequencies;
    }

    // Вариант 2: Потоковая обработка файла.
    public Map<String, Integer> countWordsStreaming(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("[^\\p{L}\\p{N}]+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        frequencies.merge(word, 1, Integer::sum);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        frequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry ->
                        System.out.printf("%-20s %d%n", entry.getKey(), entry.getValue()));
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Без --stream - загрузка всего файла в память");
            System.err.println("С --stream - потоковая обработка");
            System.exit(1);
        }

        Path filePath = Path.of(args[0]);
        if (!Files.isReadable(filePath)) {
            System.err.println("Файл не найден или недоступен: " + filePath);
            System.exit(1);
        }

        boolean streaming = args.length > 1 && "--stream".equals(args[1]);

        WordFrequencyCounter counter = new WordFrequencyCounter();
        long start = System.nanoTime();

        Map<String, Integer> frequencies = streaming
                ? counter.countWordsStreaming(filePath)
                : counter.countWords(filePath);

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;

        System.out.println("Режим: " + (streaming ? "потоковый" : "полная загрузка"));
        System.out.println("Файл: " + filePath);
        System.out.println("Время: " + elapsedMs + " мс");
        System.out.println("Уникальных слов: " + frequencies.size());
        counter.printFrequencies(frequencies);
    }
}