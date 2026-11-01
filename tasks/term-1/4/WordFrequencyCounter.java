import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        String text = Files.readString(filePath);
        addWords(map, text);

        return map;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> map = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            char[] buffer = new char[8192];       
            StringBuilder word = new StringBuilder();
            int length;
            while ((length = reader.read(buffer)) != -1) {
                for (int i = 0; i < length; i++) {
                    char c = buffer[i];
                    if (Character.isLetterOrDigit(c)) {
                        word.append(c);              
                    } else if (word.length() > 0) {
                        addWord(map, word.toString().toLowerCase()); 
                        word.setLength(0);            
                    }
                }
            }
            if (word.length() > 0) {
                addWord(map, word.toString().toLowerCase());
            }
        }
        return map;
    }

    private void addWords(Map<String, Integer> map, String text) {
        String lower = text.toLowerCase();
        String[] words = lower.split("[^\\p{L}\\p{N}]+");
        for (String word : words) {
            if (!word.isEmpty()) {
                addWord(map, word);
            }
        }
    }
 
    private void addWord(Map<String, Integer> map, String word) {
        Integer count = map.get(word);
        if (count == null) {
            map.put(word, 1);
        } else {
            map.put(word, count + 1);
        }
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        for (String word : frequencies.keySet()) {
            System.out.println(word + ": " + frequencies.get(word));
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage: java WordFrequencyCounter <file> <full|stream>");
            return;
        }
        Path path = Path.of(args[0]);
        String mode = args[1];
        WordFrequencyCounter counter = new WordFrequencyCounter();
 
        if (mode.equals("full")) {
            System.out.println("Mode 1: whole file in memory");
            counter.printFrequencies(counter.countWords(path));
        } else if (mode.equals("stream")) {
            System.out.println("Mode 2: block-by-block streaming");
            counter.printFrequencies(counter.countWordsStreaming(path));
        } else {
            System.out.println("Unknown mode: " + mode + " (expected full or stream)");
        }
    }

}
