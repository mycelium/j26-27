import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.*;
public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try{
            String content = Files.readString(filePath);
            String[] words = content.toLowerCase().split("[^a-zа-я]+");
            
            for(String word:words){
                if(!word.isEmpty()){
                    frequencies.put(word, frequencies.getOrDefault(word,0)+1);
                }
            }
        }catch(IOException e){
            System.out.println("ошибка чтения файла"+e.getMessage());
        }
        return frequencies;
    }
    
    
    public Map<String, Integer> countWordsStreaming(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))){
            String line;
            while((line=reader.readLine())!=null){
                String[] words = line.toLowerCase().split("[^a-zа-я]+");
                for(String word : words){
                    if(!word.isEmpty()){
                        frequencies.put(word, frequencies.getOrDefault(word,0)+1);
                    }
                }
            }
        }catch(IOException e){
            System.out.println("ошибка чтения файла"+e.getMessage());
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        frequencies.entrySet().stream().sorted((a,b)->b.getValue().compareTo(a.getValue()))
        .forEach(entry->System.out.println(entry.getKey() + ": "+entry.getValue()));
    }

    public static void main(String[] args) {
        if(args.length == 0){
            System.out.println("укажите путь к файлу при запуске");
            return;
        }
        
        Path filePath = Path.of(args[0]);
        WordFrequencyCounter counter = new WordFrequencyCounter();
        
        
        System.out.println("Полная закгрузка файла");
        Map<String, Integer> fullLoadResult = counter.countWords(filePath);
        counter.printFrequencies(fullLoadResult);
        
        
        System.out.println("\nПотоковая обработка");
        Map<String, Integer> streamingResult = counter.countWordsStreaming(filePath);
        counter.printFrequencies(streamingResult);
    }
}
