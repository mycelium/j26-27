import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordFrequencyCounter {

    public Map<String, Long> countWords(Path filePath) throws IOException {
        // read file, tokenize words, update map
        String text = Files.readString(filePath, StandardCharsets.UTF_8);
        Map<String, Long> frequencies = new HashMap<>();
        Matcher words = Pattern.compile("[\\p{L}\\p{Nd}]+").matcher(text);

        while (words.find()) {
            addWord(frequencies, words.group());
        }
        return frequencies;
    }

    public Map<String, Long> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Long> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            int character;
            while ((character = readCodePoint(reader)) != -1) {
                if (Character.isLetterOrDigit(character)) {
                    word.appendCodePoint(character);
                } else if (word.length() > 0) {
                    addWord(frequencies, word.toString());
                    word.setLength(0);
                }
            }
        }

        //the file might end without a space or newline
        if (word.length() > 0) {
            addWord(frequencies, word.toString());
        }
        return frequencies;
    }

    private void addWord(Map<String, Long> frequencies, String word) {
        String lower = word.toLowerCase(Locale.ROOT);
        frequencies.merge(lower, 1L, Long::sum);
    }

    private int readCodePoint(BufferedReader reader) throws IOException {
        int first = reader.read();
        if (first == -1 || !Character.isHighSurrogate((char) first)) {
            return first;
        }

        // some unicode characters use two chars
        reader.mark(1);
        int second = reader.read();
        if (second != -1 && Character.isLowSurrogate((char) second)) {
            return Character.toCodePoint((char) first, (char) second);
        }
        reader.reset();
        return first;
    }

    public void printFrequencies(Map<String, Long> frequencies) {
        // print word counts
        List<Map.Entry<String, Long>> entries = new ArrayList<>(frequencies.entrySet());
        entries.sort((first, second) -> {
            int order = second.getValue().compareTo(first.getValue());
            return order != 0 ? order : first.getKey().compareTo(second.getKey());
        });

        for (Map.Entry<String, Long> entry : entries) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        // run word frequency counter
        if (args.length < 1 || args.length > 2) {
            System.out.println("Usage: java WordFrequencyCounter <file> [memory|stream]");
            return;
        }

        String mode = args.length == 2 ? args[1] : "stream";
        if (!mode.equals("memory") && !mode.equals("stream")) {
            System.out.println("Mode must be memory or stream.");
            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path file = Path.of(args[0]);
        try {
            Map<String, Long> frequencies = mode.equals("memory")
                    ? counter.countWords(file)
                    : counter.countWordsStreaming(file);
            counter.printFrequencies(frequencies);
        } catch (IOException e) {
            System.err.println("Cannot read file: " + e.getMessage());
        }
    }
}
