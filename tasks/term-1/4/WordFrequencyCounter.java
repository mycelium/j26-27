import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedReader;

public class WordFrequencyCounter {
    public Map<String, Integer> countWords(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try {
            String all = Files.readString(filePath);
            String[] words = all.toLowerCase().split("\\W+"); // разбитие текста по символам, которые не являются буквами или символами
            for (String word : words) {
                if (!word.isEmpty())
                    frequencies.merge(word, 1, Integer::sum); // если слово еще нет - положить 1, если уже встречалось, то +1
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении: " + e.getMessage());
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public Map<String, Integer> countWords2(Path filePath){
        Map<String, Integer> frequencies = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)){
            String line;
            while ((line = reader.readLine()) != null){
                String[] words = line.toLowerCase().split("\\W+");

                for (int i = 0; i < words.length; i++){
                    String word = words[i];
                    if(!word.isEmpty()){
                        if(frequencies.containsKey(word)){
                            frequencies.put(word, frequencies.get(word) + 1); // если слово уже было +1
                        } else {
                            frequencies.put(word, 1); // иначе встретили впервые
                        }
                    }
                }
            }
        } catch (IOException e){
            System.out.println("Ошибка: " + e.getMessage());
        }
        return frequencies;
    }

    public static void main(String[] args) {
        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path path = Path.of("text.txt");
        Map<String, Integer> result = counter.countWords(path);
        counter.printFrequencies(result);

        Map<String, Integer> result2 = counter.countWords2(path);
        counter.printFrequencies(result2);
    }
}
