import java.util.*;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

public class WordFrequencyCounter {

    private void addWords(String text, Map<String, Integer> frequencies){
        String[] words = text.toLowerCase().split("[^\\p{L}]+");
        for (String word : words){
            if (word.isEmpty()){
                continue;
            }
            frequencies.merge(word, 1, Integer::sum);
        }
    }

    public Map<String, Integer> countWords(Path filePath) {
        // read file, tokenize words, update map
        Map<String, Integer> frequencies = new HashMap<>();
        try {
            String text = Files.readString(filePath);
            addWords(text, frequencies);

        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл: " + e.getMessage());
        }

        return frequencies;
    }

    public Map<String, Integer> countWordsStreaming(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            StringBuilder word = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {                 
                if (Character.isLetter(ch)) {
                    word.append(Character.toLowerCase((char) ch)); 
                } else if (word.length() > 0) {
                    frequencies.merge(word.toString(), 1, Integer::sum); 
                    word.setLength(0);                             
                }  
            }
            if (word.length() > 0) {                               
                frequencies.merge(word.toString(), 1, Integer::sum);
            }
        } catch (IOException e) {
            System.out.println("Не удалось прочитать файл: " + e.getMessage());
        }
 
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());
        for (Map.Entry<String, Integer> pair : entries){
            System.out.printf("%s: %d%n", pair.getKey(), pair.getValue());
        }
    }

    public static void main(String[] args) {
        // run word frequency counter
       if (args.length < 2) {
            System.out.println("Использование: java WordFrequencyCounter <файл> <full|stream>");
            return;
        }

        Path path = Path.of(args[0]);
        WordFrequencyCounter counter = new WordFrequencyCounter();
        Map<String, Integer> result;

        long start = System.nanoTime();
        if (args[1].equals("full")) {
            result = counter.countWords(path);           
        } else {
            result = counter.countWordsStreaming(path);            
        }
        long end = System.nanoTime();

        counter.printFrequencies(result);
        System.out.printf("Время: %.2f ms%n", (end - start) / 1_000_000.0);
    }
}
