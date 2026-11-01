import java.util.*;
import java.nio.file.*;
import java.io.*;
import java.util.stream.*;

public class WordFrequencyCounter {

    private String cleanWord(String word) {
        return word.toLowerCase().replaceAll("[^a-zа-яё0-9]", "");
    }

    /**
     * Вариант 1: Загрузка всего файла в память
     */
    public Map<String, Integer> countWords(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try {
            // Читаем все строки целиком
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                String[] words = line.split("\\s+");
                for (String w : words) {
                    String word = cleanWord(w);
                    if (!word.isEmpty()) {
                        frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return frequencies;
    }

    /**
     * Вариант 2: Потоковая обработка (читает по строчке, не загружая всё в память)
     */
    public Map<String, Integer> countWordsStream(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\s+");
                for (String w : words) {
                    String word = cleanWord(w);
                    if (!word.isEmpty()) {
                        frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }
        return frequencies;
    }

    /**
     * Вывод результатов (сортировка по убыванию частоты)
     */
    public void printFrequencies(Map<String, Integer> frequencies) {
        if (frequencies.isEmpty()) {
            System.out.println("Файл пуст или не содержит слов.");
            return;
        }
        
        frequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    public static void main(String[] args) {
        WordFrequencyCounter counter = new WordFrequencyCounter();

        Path testFile = Paths.get("test.txt");
        try {
            Files.writeString(testFile, "Hello world! Hello Java. This is a test. Java is great.");
        } catch (IOException e) {
            System.err.println("Не удалось создать тестовый файл: " + e.getMessage());
            return;
        }

        System.out.println("--- Вариант 1: Загрузка в память ---");
        long start = System.nanoTime();
        Map<String, Integer> freq1 = counter.countWords(testFile);
        long end = System.nanoTime();
        counter.printFrequencies(freq1);
        System.out.printf("Время: %.4f сек.%n%n", (end - start) / 1e9);

        System.out.println("--- Вариант 2: Потоковая обработка ---");
        start = System.nanoTime();
        Map<String, Integer> freq2 = counter.countWordsStream(testFile);
        end = System.nanoTime();
        counter.printFrequencies(freq2);
        System.out.printf("Время: %.4f сек.%n", (end - start) / 1e9);
    }
}