import java.util.*;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Files;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        String text = Files.readString(filePath);
        text = text.toLowerCase();
        String[] words = text.split("[^\\p{L}\\p{N}]+");
        for (String word : words) {
            if (!word.isEmpty()) {
                if (frequencies.containsKey(word)) {
                    int count = frequencies.get(word);
                    frequencies.put(word, count + 1);
                } else {
                    frequencies.put(word, 1);
                }
            }
        }
    return frequencies;
    }
    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
            Map<String, Integer> frequencies = new HashMap<>();
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line= reader.readLine();
                while (line != null) {
                    line = line.toLowerCase();
                    String[] words = line.split("[^\\p{L}\\p{N}]+");
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            if (frequencies.containsKey(word)) {
                                int count = frequencies.get(word);
                                frequencies.put(word, count + 1);
                            } else {
                                frequencies.put(word, 1);
                            }
                        }
                    }
                    line = reader.readLine();
                }
            }
            return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    public static void main(String[] args) throws IOException {
        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of("text.txt");
        System.out.println("Full file reading");
        Map<String, Integer> result1 = counter.countWords(filePath);
        counter.printFrequencies(result1);
        System.out.println("\nStreaming reading");
        Map<String, Integer> result2 = counter.countWordsStreaming(filePath);
        counter.printFrequencies(result2);
        System.out.println("\nResults are equal: "+ result1.equals(result2));
}
}
