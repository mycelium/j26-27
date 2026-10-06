import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.nio.file.Path;
import java.util.stream.Stream;

public class WordFrequencyCounter {

    public Map<String, Integer> countWordsFile(Path filePath) throws IOException {
        String fileText = Files.readString(filePath);
        Map<String, Integer> freq = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        countChar(sb, fileText, freq);

        return freq;
    }

    public Map<String, Integer> countWordsStream(Path filePath) throws IOException {
        Map<String, Integer> freq = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        try (Stream<String> stream = Files.lines(filePath)) {
            stream.forEach(line -> {
                countChar(sb, line, freq);
            });
        }

        return freq;
    }

    public void countChar(StringBuilder sb, String s, Map<String, Integer> freq) {
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            } else {
                addWord(sb, freq);
            }
        }
        addWord(sb, freq);
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

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("не указан путь к файлу");
            System.out.println("пример:");
            System.out.println("java WordFrequencyCounter --file input.txt");
            System.out.println("или");
            System.out.println("java WordFrequencyCounter --stream input.txt");

            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of(args[1]);

        Map<String, Integer> freq = new HashMap<>();

        if (args[0].equals("--file")) {
            freq = counter.countWordsFile(filePath);
        } else if (args[0].equals("--stream")) {
            freq = counter.countWordsStream(filePath);
        } else {
            System.out.println("неизвестный флаг");
            return;
        }

        counter.printFrequencies(freq);
    }
}
