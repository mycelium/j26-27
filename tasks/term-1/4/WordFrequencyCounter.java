import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedReader;

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

    public Map<String, Integer> countWordsStream(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("[^\\p{L}\\p{N}]+");

                for (String word : words) {
                    frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
                }
            }
        }

        return frequencies;
    }


    public static void main(String[] args) {
        if (args.length == 0) {
            return;
        }

        if (args.length < 2) {
            System.out.println("Please provide the program mode and the file path.");
            System.out.println("Usage:");
            System.out.println("java WordFrequencyCounter.java --file <path>");
            System.out.println("java WordFrequencyCounter.java --stream <path>");
            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of(args[1]);

        try {
            Map<String, Integer> frequencies;
            if (args[0].equals("--file")) {
                frequencies = counter.countWords(filePath);
            } else if (args[0].equals("--stream")) {
                frequencies = counter.countWordsStream(filePath);
            } else {
                System.out.println("Unknown mode. Please choose '--file' or '--stream'.");
                return;
            }
            counter.printFrequencies(frequencies);
        } catch (IOException e) {
            System.out.println("Error while working with the file: " + filePath);
        }
    }
}

// regex: "[^\\p{L}\\p{N}]+"
