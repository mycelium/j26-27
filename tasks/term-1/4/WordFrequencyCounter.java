import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.io.BufferedReader;

public class WordFrequencyCounter {

    private void addWords(String text, Map<String, Integer> counts) {
        for(String w: tokenize(text)){
            if(counts.containsKey(w)){
                counts.put(w, counts.get(w) + 1);
            }
            else counts.put(w, 1);
        }
    }

    public Map<String, Integer> countWordsInMemory(Path filePath) throws IOException {
        // read file, tokenize words, update map
        String text = Files.readString(filePath);
//        List<String> words = tokenize(text);
        Map<String, Integer> counts = new HashMap<>();

        addWords(text, counts);
        
        return counts;
    }

    public Map<String, Integer> countWordsStreaming(Path filepath) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(filepath)) {
            Map<String, Integer> counts = new HashMap<>();
            String line;
            while((line = reader.readLine()) != null){
                addWords(line, counts);
            }
            
            return counts;
        }
        
    }

    public void printFrequencies(Map<String, Integer> frequencies, int limit) { 
        // print word counts

        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        
        /* entries.sort((a, b) -> Integer.compare(b.getValue(), a.getValue())); */

        entries.sort((a, b) -> {
            int c = Integer.compare(b.getValue(), a.getValue());
            if(c != 0) return c;
            return a.getKey().compareTo(b.getKey());
        });

        int total = 0;
        for(int c: frequencies.values()) total += c;

        System.out.println("Всего слов: " + total + "; Уникальных: " + frequencies.size());

        int letLimit = Math.min(limit, frequencies.size());

        System.out.println("First " + letLimit + " words");

        for(int i = 0; i < letLimit; ++i){
            Map.Entry<String, Integer> e = entries.get(i);
            System.out.printf("%-20s %d%n", e.getKey(), e.getValue());
        }
        
    }

    public static List<String> tokenize(String text){
        String[] words = text.split("[^\\p{L}'-]+");  
        List<String> new_words = new ArrayList<>(); 
        for(String w: words){
            if(w.isEmpty()) continue;
            String new_w = w.toLowerCase();
            new_words.add(new_w);
        }
        return new_words;
    }

    private boolean compareModes(WordFrequencyCounter counter, Path path) throws IOException {
        Map<String, Integer> memory = counter.countWordsInMemory(path);
        Map<String, Integer> streaming = counter.countWordsStreaming(path);
        return memory.equals(streaming);
    }


    public static void main(String[] args) {
        // run word frequency counter
        if (args.length == 0) {
            System.err.println("Использование: java WordFrequencyCounter <файл> [memory|stream|compare] [N]");
            System.exit(1);
        }

        String mode = args.length > 1 ? args[1] : "stream";
        int limit = 25;

        if(args.length > 2){
            try{
                limit = Integer.parseInt(args[2]);
            } catch (NumberFormatException e){
                System.err.println("N должно быть целым числом: " + args[2]);
                System.exit(1);
            }
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();

        try{
            
            Path path = Path.of(args[0]).toAbsolutePath();

            switch (mode) {
                case "memory" -> counter.printFrequencies(counter.countWordsInMemory(path), limit);
                case "stream" -> counter.printFrequencies(counter.countWordsStreaming(path), limit);
                case "compare" -> System.out.println("Результаты совпадают: " + counter.compareModes(counter, path));
                default -> {
                    System.err.println("Неизвестный режим: " + mode + " (ожидается memory, stream или compare)");
                    System.exit(1);
                }
            }
            
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
