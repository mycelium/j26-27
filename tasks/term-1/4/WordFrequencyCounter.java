import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        String text = Files.readString(filePath);
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();

        for (int i = 0; i < text.length(); i++) {
            char character = text.charAt(i);

            boolean hyphenInsideWord = character == '-'
                    && !word.isEmpty()
                    && i + 1 < text.length()
                    && Character.isLetterOrDigit(text.charAt(i + 1));

            if (Character.isLetterOrDigit(character) || hyphenInsideWord) {
                word.append(character);
            } else {
                normalizeAndAddWord(frequencies, word);
            }
        }

        normalizeAndAddWord(frequencies, word);
        return frequencies;
    }

    public Map<String, Integer> countWordsByChunks(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            int current = reader.read();

            while (current != -1) {
                int next = reader.read();
                char character = (char) current;

                boolean hyphenInsideWord = character == '-'
                        && !word.isEmpty()
                        && next != -1
                        && Character.isLetterOrDigit((char) next);

                if (Character.isLetterOrDigit(character) || hyphenInsideWord) {
                    word.append(character);
                } else {
                    normalizeAndAddWord(frequencies, word);
                }

                current = next;
            }
        }

        normalizeAndAddWord(frequencies, word);
        return frequencies;
    }

    private void normalizeAndAddWord(Map<String, Integer> frequencies, StringBuilder word) {
        if (word.isEmpty()) {
            return;
        }

        String normalizedWord = word.toString().toLowerCase();
        frequencies.put(
                normalizedWord,
                frequencies.getOrDefault(normalizedWord, 0) + 1);
        word.setLength(0);
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println(
                    "Укажите файл и режим подсчёта: <файл> <default|chunks>");
            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of(args[0]);

        try {
            Map<String, Integer> frequencies;

            if (args[1].equalsIgnoreCase("default")) {
                frequencies = counter.countWords(filePath);
            } else if (args[1].equalsIgnoreCase("chunks")) {
                frequencies = counter.countWordsByChunks(filePath);
            } else {
                System.out.println("Режим должен быть default или chunks.");
                return;
            }

            counter.printFrequencies(frequencies);
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл: " + e.getMessage());
        }
    }
}
