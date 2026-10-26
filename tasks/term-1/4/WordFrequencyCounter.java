import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class WordFrequencyCounter {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private final boolean ignoreCase;
    private final int bufferSize;

    public WordFrequencyCounter() {
        this(true, DEFAULT_BUFFER_SIZE);
    }

    public WordFrequencyCounter(boolean ignoreCase) {
        this(ignoreCase, DEFAULT_BUFFER_SIZE);
    }

    public WordFrequencyCounter(boolean ignoreCase, int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Buffer size must be positive.");
        }

        this.ignoreCase = ignoreCase;
        this.bufferSize = bufferSize;
    }

    // метод грузит весь файл в сбилженную строку, потом считает слова
    public Map<String, Integer> countWordsWholeFile(Path filePath) throws IOException {
        String text = Files.readString(filePath, StandardCharsets.UTF_8);
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();

        processText(text, word, frequencies);
        finishWord(word, frequencies);
        return frequencies;
    }

    // тут читаем файл батчами через буффер и считаем слова
    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();
        char[] buffer = new char[bufferSize];

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            int charactersRead;
            while ((charactersRead = reader.read(buffer)) != -1) {
                processText(buffer, charactersRead, word, frequencies);
            }
        }

        finishWord(word, frequencies);
        return frequencies;
    }

    // метод чтения текста под уже сбилженную строку
    private void processText(String text, StringBuilder word, Map<String, Integer> frequencies) {
        for (int i = 0; i < text.length(); i++) {
            processCharacter(text.charAt(i), word, frequencies);
        }
    }

    // метод-близнец с другой сигнатурой под буффер
    private void processText(
            char[] buffer,
            int charactersRead,
            StringBuilder word,
            Map<String, Integer> frequencies
    ) {
        for (int i = 0; i < charactersRead; i++) {
            processCharacter(buffer[i], word, frequencies);
        }
    }

    // тут по символу собираем слово, все остальное считаем разделителями
    private void processCharacter(
            char character,
            StringBuilder word,
            Map<String, Integer> frequencies
    ) {
        char ch = ignoreCase ? Character.toLowerCase(character) : character;

        if (Character.isLetterOrDigit(ch)) {
            word.append(ch);
        } else {
            finishWord(word, frequencies);
        }
    }

    // добиваем последнее слово, если файл кончился без разделителя
    private static void finishWord(StringBuilder word, Map<String, Integer> frequencies) {
        if (word.isEmpty()) {
            return;
        }

        String wordText = word.toString();
        frequencies.merge(wordText, 1, Integer::sum);
        word.setLength(0);
    }

    private static void writeFrequencies(Path outputPath, Map<String, Integer> frequencies)
            throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
            // сортируем резы, чтобы вывод не был хаотичным
            for (Map.Entry<String, Integer> entry : new TreeMap<>(frequencies).entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Path filePath = Path.of("artifacts", "text.txt");
        boolean fileWasProvided = false;
        boolean streamingOnly = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--file")) {
                if (i + 1 >= args.length || fileWasProvided) {
                    throw new IllegalArgumentException(
                            "Usage: java WordFrequencyCounter [--file <path>] [--streaming-only]"
                    );
                }
                filePath = Path.of(args[++i]);
                fileWasProvided = true;
            } else if (args[i].equals("--streaming-only")) {
                streamingOnly = true;
            } else {
                throw new IllegalArgumentException(
                        "Unknown argument: " + args[i]
                                + ". Usage: java WordFrequencyCounter [--file <path>] [--streaming-only]"
                );
            }
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path artifactsDirectory = Path.of("artifacts");
        Files.createDirectories(artifactsDirectory);

        Path streamingResult = artifactsDirectory.resolve("streaming_result.txt");
        if (streamingOnly) {
            writeFrequencies(streamingResult, counter.countWordsStreaming(filePath));
            System.out.println("Streaming result: " + streamingResult);
            return;
        }

        Map<String, Integer> wholeFileFrequencies = counter.countWordsWholeFile(filePath);
        Map<String, Integer> streamingFrequencies = counter.countWordsStreaming(filePath);
        Path wholeFileResult = artifactsDirectory.resolve("whole_file_result.txt");
        writeFrequencies(wholeFileResult, wholeFileFrequencies);
        writeFrequencies(streamingResult, streamingFrequencies);

        System.out.println("Whole-file result: " + wholeFileResult);
        System.out.println("Streaming result: " + streamingResult);
        System.out.println("Results are equal: " + wholeFileFrequencies.equals(streamingFrequencies));
    }
}
