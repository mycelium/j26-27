import java.util.*;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) {
        // read file, tokenize words, update map
        return null;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        frequencies.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue()));
    }

    public static void main(String[] args) {
        String text = """
                Java — строго типизированный объектно-ориентированный язык програм-
                мирования общего назначения.
                Приложения Java транслируются в байт-код и выполняются виртуальной машиной Java!
                """;

        WordFrequencyProcessor processor = new WordFrequencyProcessor();
        char[] buffer = text.toCharArray();
        processor.process(buffer, buffer.length);

        WordFrequencyCounter counter = new WordFrequencyCounter();
        counter.printFrequencies(processor.finish());
    }
}
