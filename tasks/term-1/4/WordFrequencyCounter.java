import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {

        String text = Files.readString(filePath, StandardCharsets.UTF_8).toLowerCase(Locale.ROOT);

        String formattedText = text.replaceAll("\\p{P}+", " ");

        Map<String, Integer> wordFrequency = new HashMap<>();

        String[] words = formattedText.split("\\s+");

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            if (!wordFrequency.containsKey(word)) {
                wordFrequency.put(word, 1);
            } else {
                wordFrequency.put(word, wordFrequency.get(word) + 1);
            }
        }

        return wordFrequency;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {

        Map<String, Integer> wordFrequency = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {

            String line = reader.readLine();

            while (line != null) {

                String formattedLine = line.toLowerCase(Locale.ROOT).replaceAll("\\p{P}+", " ");
                String[] words = formattedLine.split("\\s+");

                for (String word : words) {
                    if (word.isEmpty()) {
                        continue;
                    }
                    if (!wordFrequency.containsKey(word)) {
                        wordFrequency.put(word, 1);
                    } else {
                        wordFrequency.put(word, wordFrequency.get(word) + 1);
                    }
                }
                line = reader.readLine();
            }
        }
        return wordFrequency;
    }

    public void printFrequencies(Map<String, Integer> wordFrequency) {
        for (String word : wordFrequency.keySet()) {
            System.out.println(word + ": " + wordFrequency.get(word));
        }
    }

    public static void main(String[] args) throws IOException {
        WordFrequencyCounter counter = new WordFrequencyCounter();

        Path filePath = Path.of("file.txt");

        Map<String, Integer> frequencies = counter.countWords(filePath);

        Map<String, Integer> frequenciesStream = counter.countWordsStreaming(filePath);

        System.out.println("Загрузка всего файла:");
        counter.printFrequencies(frequencies);

        System.out.println("\nПотоковая обработка:");
        counter.printFrequencies(frequenciesStream);

        if (frequencies.equals(frequenciesStream)) {
            System.out.println("\nПроверка пройдена: результаты совпадают.");
        } else {
            System.out.println("\nОшибка: результаты отличаются.");
        }

    }
}