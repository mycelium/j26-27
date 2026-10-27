import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.*;
import java.nio.file.Files;
import java.io.BufferedReader;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WordFrequencyCounter {
    // добавлен также учет апострофов
    private static final Pattern wordPattern = Pattern.compile("[\\p{L}\\p{N}]+(?:'\\p{L}+)?");

    private static void countToMap(String text, Map<String, Integer> frequencies) {
        Matcher matcher = wordPattern.matcher(text);
        while (matcher.find()) {
            frequencies.merge(matcher.group().toLowerCase(), 1, Integer::sum);
        }
    }

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        String oneStringFile = Files.readString(filePath);
        countToMap(oneStringFile, frequencies);
        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                countToMap(line, frequencies);
            }
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies)  {
        frequencies.entrySet().stream() // формируем пары (key, value) и пихаем в поток для сортировки
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(element -> System.out.println(element.getKey() + ": " + element.getValue()));
        System.out.println();
    }

    public static void main(String[] args) {
        WordFrequencyCounter counter = new WordFrequencyCounter();

        for (String arg : args) {
            Path path = Path.of(arg);
            System.out.println(String.format("Reading path: %s", path));

            try {
                System.out.println("Full load:\n");
                counter.printFrequencies(counter.countWords(path));

                System.out.println("Streaming:\n");
                counter.printFrequencies(counter.countWordsStreaming(path));

            } catch (NoSuchFileException e) {
                System.err.println("File not found: " + e.getFile());
            } catch (IOException e) {
                System.err.println("Reading error: " + e.getMessage());
            }
        }
    }
}
