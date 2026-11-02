import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        // read file, tokenize words, update map

        String text = Files.readString(filePath, StandardCharsets.UTF_8);
        Map<String, Integer> frequencies = new HashMap<>();
        Matcher words = Pattern.compile("[\\p{L}\\p{Nd}]+").matcher(text);

        while (words.find()) {
            addWord(frequencies, words.group());
        }
        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        Pattern word = Pattern.compile("[\\p{L}\\p{Nd}]+");

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher m = word.matcher(line);
                while (m.find()) {
                    addWord(frequencies, m.group());
                }
            }
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        entries.sort((a, b) -> {
            int byCount = Integer.compare(b.getValue(), a.getValue());
            return byCount != 0 ? byCount : a.getKey().compareTo(b.getKey());
        });

        for (Map.Entry<String, Integer> entry : entries) {
            System.out.printf("%-20s %d%n", entry.getKey(), entry.getValue());
        }
    }

    private void addWord(Map<String, Integer> frequencies, String word) {
        frequencies.merge(word, 1, Integer::sum);
    }

    public static void main(String[] args) {
        // run word frequency counter

        if (args.length < 2) {
            System.err.println("usage: java WordFrequencyCounter <whole|stream> <file>");
            return;
        }
        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path file = Path.of(args[1]);
        try {
            Map<String, Integer> frequencies;
            if (args[0].equals("whole")) {
                frequencies = counter.countWords(file);
            } else if (args[0].equals("stream")) {
                frequencies = counter.countWordsStreaming(file);
            } else {
                System.err.println("unknown mode: " + args[0] + " (expected whole or stream)");
                return;
            }
            counter.printFrequencies(frequencies);
        } catch (IOException e) {
            System.err.println("Unable to read the file: " + e.getMessage());
        }
    }
}
