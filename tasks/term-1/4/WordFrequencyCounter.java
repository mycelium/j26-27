import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class WordFrequencyCounter {
    private static final int TOP = 20;

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        String text = Files.readString(filePath);
        String[] words = text.split("[^\\p{L}\\p{Nd}]+"); 

        Map<String, Integer> frequencies = new HashMap<>();
        for (String word : words) {
            if (!word.isEmpty()) {
                addWord(frequencies, word);
            }
        }
        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            int c;
            while ((c = reader.read()) != -1) {
                if (Character.isLetterOrDigit(c)) {
                    word.append((char) c);
                } else if (word.length() > 0) {
                    addWord(frequencies, word.toString());
                    word.setLength(0);
                }
            }
        }
        if (word.length() > 0) { 
            addWord(frequencies, word.toString());
        }
        return frequencies;
    }

    private void addWord(Map<String, Integer> frequencies, String word) {
        String w = word.toLowerCase();
        frequencies.put(w, frequencies.getOrDefault(w, 0) + 1);
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        entries.sort((a, b) -> {
            if (!a.getValue().equals(b.getValue())) {
                return Integer.compare(b.getValue(), a.getValue());
            }
            return a.getKey().compareTo(b.getKey());
        });

        long total = 0;
        for (int count : frequencies.values()) {
            total += count;
        }

        System.out.println("Всего слов: " + total);
        System.out.println("Разных слов: " + frequencies.size());
        System.out.println("Самые частые слова:");
        for (int i = 0; i < entries.size() && i < TOP; i++) {
            System.out.println("  " + entries.get(i).getKey() + ": " + entries.get(i).getValue());
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Использование: java WordFrequencyCounter <full|stream> <файл>");
            return;
        }
        String mode = args[0];
        Path path = Path.of(args[1]);
        WordFrequencyCounter counter = new WordFrequencyCounter();

        long start = System.nanoTime();
        Map<String, Integer> result;
        if (mode.equals("full")) {
            result = counter.countWords(path);
        } else if (mode.equals("stream")) {
            result = counter.countWordsStreaming(path);
        } else {
            System.out.println("Неизвестный режим: " + mode + " (нужно full или stream)");
            return;
        }
        long time = (System.nanoTime() - start) / 1_000_000;

        counter.printFrequencies(result);
        System.out.println("Время: " + time + " ms");
    }
}