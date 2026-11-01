import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WordFrequencyCounter {

    private static final int BUFFER_SIZE = 64 * 1024;

    /**
     * Вариант 1: загружает файл целиком в память, затем обрабатывает.
     * Подходит только для файлов, которые помещаются в оперативную память.
     */
    public Map<String, Integer> countWords(Path filePath) {
        try {
            String content = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            Map<String, Integer> frequencies = new HashMap<>();
            StringBuilder word = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                processChar(content.charAt(i), word, frequencies);
            }
            flushWord(word, frequencies);
            return frequencies;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read file: " + filePath, e);
        }
    }

    /**
     * Вариант 2: потоковая обработка. Файл читается небольшими блоками,
     * в памяти хранится только буфер, текущее слово и словарь частот.
     * Расход памяти зависит от числа РАЗНЫХ слов, а не от размера файла,
     * поэтому файл может быть больше доступной оперативной памяти.
     * Слово, разорванное границей двух блоков, корректно склеивается
     * (текущее слово хранится между чтениями).
     */
    public Map<String, Integer> countWordsStreaming(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();
        char[] buffer = new char[BUFFER_SIZE];
        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            int read;
            while ((read = reader.read(buffer)) != -1) {
                for (int i = 0; i < read; i++) {
                    processChar(buffer[i], word, frequencies);
                }
            }
            flushWord(word, frequencies);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read file: " + filePath, e);
        }
        return frequencies;
    }

    /** Слово - непрерывная последовательность букв и цифр, регистр не учитывается. */
    private static void processChar(char c, StringBuilder word, Map<String, Integer> frequencies) {
        if (Character.isLetterOrDigit(c)) {
            word.append(Character.toLowerCase(c));
        } else {
            flushWord(word, frequencies);
        }
    }

    private static void flushWord(StringBuilder word, Map<String, Integer> frequencies) {
        if (word.length() > 0) {
            frequencies.merge(word.toString(), 1, Integer::sum);
            word.setLength(0);
        }
    }

    /** Печатает все слова: сначала самые частые, при равенстве - по алфавиту. */
    public void printFrequencies(Map<String, Integer> frequencies) {
        printFrequencies(frequencies, Integer.MAX_VALUE);
    }

    /** Печатает не более limit самых частых слов. */
    public void printFrequencies(Map<String, Integer> frequencies, int limit) {
        frequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .forEach(e -> System.out.printf("%-30s %d%n", e.getKey(), e.getValue()));
    }

    /**
     * Использование: java WordFrequencyCounter <файл> [all|stream|both] [top N]
     * По умолчанию: both, top 20.
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java WordFrequencyCounter <file> [all|stream|both] [top]");
            return;
        }
        Path path = Path.of(args[0]);
        String mode = args.length > 1 ? args[1] : "both";
        int top = args.length > 2 ? Integer.parseInt(args[2]) : 20;

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Map<String, Integer> full = null;
        Map<String, Integer> streamed = null;

        if (mode.equals("all") || mode.equals("both")) {
            long start = System.nanoTime();
            full = counter.countWords(path);
            long ms = (System.nanoTime() - start) / 1_000_000;
            System.out.println("=== Full load: " + ms + " ms, unique words: " + full.size() + " ===");
            counter.printFrequencies(full, top);
        }

        if (mode.equals("stream") || mode.equals("both")) {
            long start = System.nanoTime();
            streamed = counter.countWordsStreaming(path);
            long ms = (System.nanoTime() - start) / 1_000_000;
            System.out.println("=== Streaming: " + ms + " ms, unique words: " + streamed.size() + " ===");
            counter.printFrequencies(streamed, top);
        }

        if (full != null && streamed != null) {
            System.out.println("Results equal: " + full.equals(streamed));
        }
    }
}