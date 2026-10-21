import java.util.*;
import java.nio.file.Path;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) {

        Map<String, Integer> frequencies = new HashMap<>();

        try{
            long fileSize = Files.size(filePath);
            long maxMemory = Runtime.getRuntime().maxMemory();
            long safe = (long) (maxMemory * 0.02); 
            
            if (fileSize < safe) {
                String content = Files.readString(filePath);
                String[] words = content.toLowerCase().split("[^\\p{L}\\p{Nd}]+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
                    }
                }
            } 
            else {
                try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                    
                    char[] buffer = new char[8192]; 
                    int charsRead;
                    StringBuilder wordBuilder = new StringBuilder();

                    while ((charsRead = reader.read(buffer)) != -1) {
                        for (int i = 0; i < charsRead; i++) {
                            char c = buffer[i];
                            
                            if (Character.isLetterOrDigit(c)) {
                                wordBuilder.append(Character.toLowerCase(c));
                            } 
                            else {
                                if (wordBuilder.length() > 0) {
                                    String word = wordBuilder.toString();
                                    frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
                                    wordBuilder.setLength(0);
                                }
                            }
                        }
                    }
                    if (wordBuilder.length() > 0) {
                        String word = wordBuilder.toString();
                        frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } 
        catch (IOException e) {
            throw new UncheckedIOException("Ошибка чтения файла: " + filePath, e);
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        frequencies.forEach((word, count) -> 
            System.out.println(word + ": " + count)
        );
    }

    public static void main(String[] args) {
        
        Path path = Path.of("test.txt");
        
        try {
            if (!Files.exists(path)) {
                Files.writeString(path, "Make some different words for example: dog dog dog -- dog, Cat cat, mouse\n mouse mouse mouse РАЗ два три Раз два Три");
            }

            WordFrequencyCounter counter = new WordFrequencyCounter();
            System.out.println("Результат:");
            counter.printFrequencies(counter.countWords(path));
            
        } 
        catch (IOException | UncheckedIOException e) {
            System.err.println("Не удалось создать или прочитать файл: " + e.getMessage());
        }
        
    }
}
