import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedReader;

public class WordFrequencyCounter {
    
    public Map<String, Integer> countWords(Path filePath) throws IOException {
        // read file, tokenize words, update map
        String text = Files.readString(filePath);
        Map<String, Integer> result = new HashMap<>();
        String[] words = text.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+");
        for (String word : words) {
            if (!word.isEmpty()) {
                result.merge(word, 1, Integer::sum);
            }
        }
        return result;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> result = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase(Locale.ROOT).split("[^\\p{L}\\p{N}]+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        result.merge(word, 1, Integer::sum);
                    }
                }
            }
        }
        return result;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
        for (Map.Entry<String, Integer> pair : frequencies.entrySet()) {
            System.out.println(pair.getKey() + " " + pair.getValue());
        }
    }

    public static void main(String[] args) throws IOException {
        // run word frequency counter
        if (args.length == 0) {
        System.out.println("Использование: java WordFrequencyCounter.java <файл>");
        return;
        }
        Path path = Path.of(args[0]);
        WordFrequencyCounter counter = new WordFrequencyCounter();
        System.out.println("Подсчет слов с помощью загрузки всего содержимого файла:");
        try {
            Map<String, Integer> inMemory = counter.countWords(path);
            counter.printFrequencies(inMemory);
        } catch (OutOfMemoryError e) {
            System.out.println("Не хватило памяти для загрузки файла целиком");
        }
        System.out.println("Подсчет слов с помощью потоковой обработки файла:");
        Map<String, Integer> streaming = counter.countWordsStreaming(path);
        counter.printFrequencies(streaming);
    }
}
