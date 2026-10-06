import java.util.*;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWordsFile(Path filePath) {
        // read file, tokenize words, update map
        return null;
    }

    public Map<String, Integer> countWordsStream(Path filePath) {
        // read file, tokenize words, update map
        return null;
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

        Map<String, Integer> freq;

        if (args[1].equals("--file")) {
            freq = counter.countWordsFile(filePath);
        } else if (args.equals("--stream")) {
            freq = counter.countWordsStream(filePath);
        }

        counter.printFrequencies(freq);
    }
}
