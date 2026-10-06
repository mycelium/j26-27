import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWordsFile(Path filePath) throws IOException {
        String fileText = Files.readString(filePath);
        Map<String, Integer> freq = new HashMap<>();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < fileText.length(); i++) {
            char c = fileText.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            } else {
                addWord(sb, freq);
            }
        }

        addWord(sb, freq);

        return freq;
    }

    public Map<String, Integer> countWordsStream(Path filePath) throws IOException {
        // read file, tokenize words, update map
        return null;
    }

    public void addWord(StringBuilder sb, Map<String, Integer> freq) {
        if (sb.isEmpty()) {
            return;
        }

        String word = sb.toString();
        int count = freq.getOrDefault(word, 0);
        freq.put(word, count + 1);

        sb.setLength(0);
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()){
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("не указан путь к файлу");
            System.out.println("пример:");
            System.out.println("java WordFrequencyCounter --file input.txt");
            System.out.println("или");
            System.out.println("java WordFrequencyCounter --stream input.txt");

            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of(args[0]);

        Map<String, Integer> freq = new HashMap<>();

        if (args[1].equals("--file")) {
            freq = counter.countWordsFile(filePath);
        } else if (args[1].equals("--stream")) {
            freq = counter.countWordsStream(filePath);
        }

        counter.printFrequencies(freq);
    }
}
