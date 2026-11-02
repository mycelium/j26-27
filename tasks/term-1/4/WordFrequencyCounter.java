import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class WordFrequencyCounter {
    private static final int BUFFER_SIZE = 8192;
    private static final int OUTPUT_LIMIT = 20;

    private record CountResult(Map<String, Integer> frequencies, long elapsedMilliseconds) {
    }

    public Map<String, Integer> countWordsInMemory(Path filePath) throws IOException {
        String text = Files.readString(filePath, StandardCharsets.UTF_8);
        char[] buffer = text.toCharArray();

        WordFrequencyProcessor processor = new WordFrequencyProcessor();
        processor.process(buffer, buffer.length);
        return processor.finish();
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        WordFrequencyProcessor processor = new WordFrequencyProcessor();
        char[] buffer = new char[BUFFER_SIZE];

        try (Reader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            int length;

            while ((length = reader.read(buffer)) != -1) {
                processor.process(buffer, length);
            }
        }

        return processor.finish();
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        printFrequencies(frequencies, frequencies.size());
    }

    public void printFrequencies(Map<String, Integer> frequencies, int limit) {
        frequencies.entrySet().stream()
                .sorted((first, second) -> {
                    int frequencyComparison = Integer.compare(second.getValue(), first.getValue());
                    return frequencyComparison != 0
                            ? frequencyComparison
                            : first.getKey().compareTo(second.getKey());
                })
                .limit(limit)
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    private CountResult count(Path filePath, boolean streaming) throws IOException {
        long startedAt = System.nanoTime();
        Map<String, Integer> frequencies = streaming
                ? countWordsStreaming(filePath)
                : countWordsInMemory(filePath);
        long elapsedMilliseconds = (System.nanoTime() - startedAt) / 1_000_000;
        return new CountResult(frequencies, elapsedMilliseconds);
    }

    private void printResult(String title, CountResult result) {
        long totalWords = result.frequencies().values().stream()
                .mapToLong(Integer::longValue)
                .sum();

        System.out.println(title);
        System.out.println("Всего слов: " + totalWords);
        System.out.println("Уникальных слов: " + result.frequencies().size());
        System.out.println("Время: " + result.elapsedMilliseconds() + " мс");
        System.out.println("Самые частые слова:");
        printFrequencies(result.frequencies(), OUTPUT_LIMIT);
    }

    private void compare(Path filePath) throws IOException {
        CountResult inMemory = count(filePath, false);
        CountResult streaming = count(filePath, true);

        printResult("Загрузка файла целиком", inMemory);
        System.out.println();
        printResult("Потоковая обработка", streaming);
        System.out.println();
        System.out.println("Результаты совпадают: "
                + inMemory.frequencies().equals(streaming.frequencies()));
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Использование: java WordFrequencyCounter <memory|stream|compare> <файл>");
            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of(args[1]);

        try {
            switch (args[0]) {
                case "memory" -> counter.printResult(
                        "Загрузка файла целиком",
                        counter.count(filePath, false)
                );
                case "stream" -> counter.printResult(
                        "Потоковая обработка",
                        counter.count(filePath, true)
                );
                case "compare" -> counter.compare(filePath);
                default -> System.err.println("Неизвестный режим: " + args[0]);
            }
        } catch (IOException exception) {
            System.err.println("Ошибка чтения файла: " + exception.getMessage());
        }
    }
}
