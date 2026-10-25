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
        // run word frequency counter
    }
}

// regex: "[^\\p{L}\\p{N}]+"
