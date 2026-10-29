import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {  // был нестатисечкий метод
        // read file, tokenize words, update map
        String text = Files.readString(filePath);
        List<String> words = tokenize(text);
        Map<String, Integer> counts = new HashMap<>();
        for(String w: words){
            if(counts.containsKey(w)){
                counts.put(w, counts.get(w) + 1);
            }
            else{
                counts.put(w, 1);
            }
        }
        
        return counts;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {  // был нестатисечкий метод
        // print word counts
        for(Map.Entry<String, Integer> e : frequencies.entrySet()){
            System.out.println(e.getKey() + ": " + e.getValue());
        }
        
    }

    public static List<String> tokenize(String text){
        String[] words = text.split("\\P{L}+");
        List<String> new_words = new ArrayList<>(); 
        for(String w: words){
            if(w.isEmpty()) continue;
            String new_w = w.toLowerCase();
            new_words.add(new_w);
        }
        return new_words;
    }

    public static void main(String[] args) {
        // run word frequency counter
        if(args.length == 0){
            System.err.println("Ошибка: введите параметр командной строки!");
            System.exit(1);
        }
        WordFrequencyCounter counter = new WordFrequencyCounter();
        try{
            Path p = Path.of(args[0]);
            Path path = p.toAbsolutePath();    
            Map<String, Integer> cnts = counter.countWords(path);      
            counter.printFrequencies(cnts);
        }
        catch (NoSuchFileException e) {
            System.err.println("Файл не найден: " + e.getMessage());
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Ошибка чтения: " + e.getMessage());
            System.exit(1);
        }
        
    }
}
