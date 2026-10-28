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
            return null;
        }

        String [] splitText = text.split("[.,!-?\\s]+");

        Map<String, Integer> counterWorldsMap = Arrays.stream(splitText)
            .map(String::toLowerCase)
            .filter(world -> !world.isEmpty())
            .distinct()
            .collect(Collectors.toMap(world -> world, world -> 0));

        for (String world : splitText){
            counterWorldsMap.merge(world, 1, Integer::sum);
            
        }

        return counterWorldsMap;
    }



    public Map<String, Integer> countWordsline_by_line(Path filePath) {
        // read file, tokenize words, update map
        try(Stream<String> line = Files.lines(filePath);){
            return line
            .map(l -> l.split("[.,!-?\\s]+"))
            .flatMap(Arrays::stream)
            .map(String::toLowerCase)
            .filter(world -> !world.isEmpty())
            .collect(Collectors.groupingBy(world -> world, Collectors.summingInt(w -> 1)));          
        }
        catch (Exception e) {
            System.out.println("Не удалось найти файл");
            return null;
        }

        
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
        frequencies.entrySet().stream()
        .forEach(w -> System.out.println(w.getKey() + " : " + w.getValue()));
    }

    public static void main(String[] args) {
        // run word frequency counter

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of("C:\\Users\\User\\Desktop\\V\\3 курс 5 сем\\JAVA\\Text.txt") ;

        counter.printFrequencies(counter.countWords(filePath));
        counter.printFrequencies(counter.countWordsline_by_line(filePath));

    }
}

