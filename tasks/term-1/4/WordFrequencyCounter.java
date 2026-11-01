import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFrequencyCounter {
    private static final Pattern WORD_PATTERN = Pattern.compile("[\\p{L}\\p{N}][\\p{L}\\p{M}\\p{N}]*");
    private static final int BUFFER_SIZE = 64 * 1024;

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        String text = Files.readString(filePath, StandardCharsets.UTF_8);
        Map<String, Integer> frequencies = new HashMap<>();
        countWordsInText(text, frequencies, true);
        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        char[] buffer = new char[BUFFER_SIZE];
        String pending = "";

        try (Reader reader = new InputStreamReader(
                new BufferedInputStream(Files.newInputStream(filePath), 1024 * 1024),
                StandardCharsets.UTF_8.newDecoder())) {
            int length;
            while ((length = reader.read(buffer)) != -1) {
                String text = pending + new String(buffer, 0, length);
                pending = countWordsInText(text, frequencies, false);
            }
        }

        countWordsInText(pending, frequencies, true);
        return frequencies;
    }

    private String countWordsInText(String text, Map<String, Integer> frequencies, boolean endOfFile) {
        Matcher matcher = WORD_PATTERN.matcher(text);

        while (matcher.find()) {
            if (!endOfFile && (matcher.end() == text.length()
                    || (matcher.end() == text.length() - 1
                    && Character.isHighSurrogate(text.charAt(text.length() - 1))))) {
                return text.substring(matcher.start());
            }

            String word = matcher.group().toLowerCase(Locale.ROOT);
            frequencies.merge(word, 1, Math::addExact);
        }

        if (!endOfFile && !text.isEmpty() && Character.isHighSurrogate(text.charAt(text.length() - 1))) {
            return text.substring(text.length() - 1);
        }

        return "";
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        frequencies.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.printf("%s: %d%n", entry.getKey(), entry.getValue()));

        long total = frequencies.values().stream().mapToLong(Integer::longValue).sum();
        System.out.printf("Всего слов: %d, различных слов: %d%n", total, frequencies.size());
    }

    public static void main(String[] args) {
        if (args.length != 0 && args.length != 2) {
            System.err.println("Использование: java WordFrequencyCounter [маленький_файл большой_файл]");
            System.exit(1);
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();

        try {
            Path smallFile = Path.of(args.length == 2 ? args[0] : "sample.txt");
            Path largeFile = Path.of(args.length == 2 ? args[1] : "large-random.txt");

            System.out.println("Полная загрузка файла: " + smallFile);
            long start = System.nanoTime();
            Map<String, Integer> fullResult = counter.countWords(smallFile);
            double fullSeconds = (System.nanoTime() - start) / 1_000_000_000.0;
            counter.printFrequencies(fullResult);
            System.out.printf("Время подсчёта: %.3f с%n", fullSeconds);

            System.out.println("\nПотоковая обработка файла: " + largeFile);
            start = System.nanoTime();
            Map<String, Integer> streamingResult = counter.countWordsStreaming(largeFile);
            double streamingSeconds = (System.nanoTime() - start) / 1_000_000_000.0;
            counter.printFrequencies(streamingResult);
            System.out.printf("Время подсчёта: %.3f с%n", streamingSeconds);
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Ошибка: " + exception.getMessage());
            System.exit(1);
        } catch (ArithmeticException exception) {
            System.err.println("Ошибка: число появлений одного слова превысило предел Integer.");
            System.exit(1);
        }
    }
}
