import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try (Scanner fileScanner = new Scanner(filePath, "UTF-8")) {
            fileScanner.useDelimiter("\\A");
            if (!fileScanner.hasNext()) {
                return frequencies;
            }
            String fileContent = fileScanner.next();
            try (Scanner wordScanner = new Scanner(fileContent)) {
                wordScanner.useDelimiter("[^\\p{L}\\p{N}']+");
                while (wordScanner.hasNext()) {
                    String word = wordScanner.next().toLowerCase(Locale.ROOT);
                    if (frequencies.containsKey(word)) {
                        frequencies.put(word, frequencies.get(word) + 1);
                    } else {
                        frequencies.put(word, 1);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't read file: " + filePath, e);
        }
        return frequencies;
    }

    public Map<String, Integer> countWordsByStream(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try (Scanner scanner = new Scanner(filePath, "UTF-8")) {
            scanner.useDelimiter("[^\\p{L}\\p{N}']+");
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase(Locale.ROOT);
                if (frequencies.containsKey(word)) {
                    frequencies.put(word, frequencies.get(word) + 1);
                } else {
                    frequencies.put(word, 1);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Can't read file: " + filePath, e);
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        Path path = Path.of(args.length > 0 ? args[0] : "java4.txt");
        WordFrequencyCounter counter = new WordFrequencyCounter();
        System.out.println("Reading in-memory");
        Map<String, Integer> inMemory = counter.countWords(path);
        counter.printFrequencies(inMemory);
        System.out.println();
        System.out.println("Reading by stream");
        Map<String, Integer> byStream = counter.countWordsByStream(path);
        counter.printFrequencies(byStream);
        System.out.println();
        System.out.println("Is equal: " + inMemory.equals(byStream));
    }
}
