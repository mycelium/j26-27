import java.util.*;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();

        String text = Files.readString(filePath).toLowerCase();
        String[] words = text.split("[^\\p{L}\\p{N}]+");

        for (String word : words) {
            frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
        }

        return frequencies;
    }


    public void printFrequencies(Map<String, Integer> frequencies) {
        System.out.println("Word: frequency (times):");
        int count = 0;
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            count += 1;
            System.out.println(count + ". " + entry.getKey() + ": " + entry.getValue() + " times");
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide a file path to count words.");
            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of(args[0]);

        Map<String, Integer> frequencies = counter.countWords(filePath);
        counter.printFrequencies(frequencies);
    }
}

// regex: "[^\\p{L}\\p{N}]+"
