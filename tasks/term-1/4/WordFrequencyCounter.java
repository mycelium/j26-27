import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.nio.file.Files;

public class WordFrequencyCounter {
    String text = "";

    public Map<String, Integer> countWords(Path filePath) {
        // read file, tokenize words, update map
        try{
           text = Files.readString(filePath);
        }
        catch (Exception e) {
            System.out.println("Не удалось найти файл");
            return new HashMap<>();
        }

        String [] splitText = text.toLowerCase().split("[^\\p{L}]+");

        Map<String, Integer> counterWorldsMap = Arrays.stream(splitText)
            //.map(String::toLowerCase)
            .filter(world -> !world.isEmpty())
            .distinct()
            .collect(Collectors.toMap(world -> world, world -> 0));

        for (String world : splitText){
            if (world.isEmpty()) continue;
            counterWorldsMap.merge(world, 1, Integer::sum);
            
        }

        return counterWorldsMap;
    }



    public Map<String, Integer> countWordsline_by_line(Path filePath) {
        // read file, tokenize words, update map
        try(Stream<String> line = Files.lines(filePath);){
            return line
            .map(l -> l.split("[^\\p{L}]+"))
            .flatMap(Arrays::stream)
            .map(String::toLowerCase)
            .filter(world -> !world.isEmpty())
            .collect(Collectors.groupingBy(world -> world, Collectors.summingInt(w -> 1)));          
        }
        catch (Exception e) {
            System.out.println("Не удалось найти файл");
            return new HashMap<>();
        }

        
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
        frequencies.entrySet().stream()
        .forEach(w -> System.out.println(w.getKey() + " : " + w.getValue()));
    }

    public static void main(String[] args) {
        // run word frequency counter

        if (args.length < 1) {
            System.out.println("Укажите путь к файлу");
            return;
            }
        Path filePath = Path.of(args[0]);

        WordFrequencyCounter counter = new WordFrequencyCounter();
        
        counter.printFrequencies(counter.countWords(filePath));
        counter.printFrequencies(counter.countWordsline_by_line(filePath));

    }
}

