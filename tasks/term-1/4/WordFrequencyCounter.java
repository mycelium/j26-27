import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordFrequencyCounter {

    private static final int TOP = 20;

    public Map<String, Integer> countWords(Path filePath) {
        try {
            String text = new String(Files.readAllBytes(filePath), StandardCharsets.UTF_8);
            Map<String, Integer> frequencies = new HashMap<>();
            StringBuilder word = new StringBuilder();
            for (int i = 0; i < text.length(); i++) {
                feed(text.charAt(i), word, frequencies);
            }
            flush(word, frequencies);
            return frequencies;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public Map<String, Integer> countWordsStreaming(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();
        char[] buffer = new char[8192];
        try (Reader reader = new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8)) {
            int n;
            while ((n = reader.read(buffer)) != -1) {
                for (int i = 0; i < n; i++) {
                    feed(buffer[i], word, frequencies);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        flush(word, frequencies);
        return frequencies;
    }


    private static void feed(char c, StringBuilder word, Map<String, Integer> frequencies) {
        if (Character.isLetterOrDigit(c)) {
            word.append(Character.toLowerCase(c));
        } else {
            flush(word, frequencies);
        }
    }

    private static void flush(StringBuilder word, Map<String, Integer> frequencies) {
        if (word.length() > 0) {
            frequencies.merge(word.toString(), 1, Integer::sum);
            word.setLength(0);
        }
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        long total = 0;
        for (int count : frequencies.values()) {
            total += count;
        }
        System.out.println("total words: " + total + ", unique words: " + frequencies.size());

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        entries.sort((a, b) -> {
            int byCount = Integer.compare(b.getValue(), a.getValue());
            return byCount != 0 ? byCount : a.getKey().compareTo(b.getKey());
        });
        for (int i = 0; i < Math.min(TOP, entries.size()); i++) {
            System.out.printf("%-20s %d%n", entries.get(i).getKey(), entries.get(i).getValue());
        }
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java WordFrequencyCounter <file> [full|stream]");
            return;
        }
        Path path = Paths.get(args[0]);
        String mode = args.length > 1 ? args[1] : "both";
        WordFrequencyCounter counter = new WordFrequencyCounter();

        Map<String, Integer> full = null;
        Map<String, Integer> stream = null;

        if (!mode.equals("stream")) {
            long start = System.nanoTime();
            try {
                full = counter.countWords(path);
                System.out.printf("full load: %d ms%n", (System.nanoTime() - start) / 1_000_000);
            } catch (OutOfMemoryError e) {
                System.out.println("full load: OutOfMemoryError (file does not fit into memory)");
            }
        }
        if (!mode.equals("full")) {
            long start = System.nanoTime();
            stream = counter.countWordsStreaming(path);
            System.out.printf("streaming: %d ms%n", (System.nanoTime() - start) / 1_000_000);
        }
        if (full != null && stream != null) {
            System.out.println("results equal: " + full.equals(stream));
        }

        Map<String, Integer> result = stream != null ? stream : full;
        if (result != null) {
            System.out.println();
            counter.printFrequencies(result);
        }
    }
}