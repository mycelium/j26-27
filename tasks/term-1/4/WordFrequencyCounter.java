import java.util.*;
import java.nio.file.Path;

public class WordFrequencyCounter {
    // добавлен также учет апострофов
    private static final Pattern wordPattern = Pattern.compile("[\\p{L}\\p{N}]+(?:'\\p{L}+)?");

    private static void countToMap(String text, Map<String, Integer> frequencies) {
        Matcher matcher = wordPattern.matcher(text);
        while (matcher.find()) {
            frequencies.merge(matcher.group().toLowerCase(), 1, Integer::sum);
        }
    }

    public Map<String, Integer> countWords(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        String oneStringFile = Files.readString(filePath);
        countWords(oneStringFile, frequencies);
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
    }

    public static void main(String[] args) {
        WordFrequencyCounter counter = new WordFrequencyCounter();

        for (String arg : args) {
            Path path = Path.of(arg);
            System.out.println(String.format("Reading path: %s", path));

            try {
                System.out.println("Full load:");
                counter.printFrequencies(counter.countWords(path));

            } catch (NoSuchFileException e) {
                System.err.println("File not found: " + e.getFile());
            } catch (IOException e) {
                System.err.println("Reading error: " + e.getMessage());
            }
        }
    }
}
