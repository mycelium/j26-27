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

            boolean hyphenInsideWord =
                    character == '-'
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

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Использование: java WordFrequencyCounter <файл>");
            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Map<String, Integer> frequencies = counter.countWords(Path.of(args[0]));

        counter.printFrequencies(frequencies);
    }
}
