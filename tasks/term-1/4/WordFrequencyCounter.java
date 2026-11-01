import java.util.*;
import java.nio.file.Path;
import java.io.*;

public class WordFrequencyCounter {

	public Map<String, Integer> countWords(Path filePath) throws IOException {
		// read file, tokenize words, update map
	    Map<String, Integer> frequencies = new HashMap<>();

	    Scanner scanner = new Scanner(filePath.toFile());
	    String text = "";

	    while (scanner.hasNextLine()) {
	        text = text + scanner.nextLine() + " ";
	    }

	    scanner.close();
	    addWords(text, frequencies);

	    return frequencies;
	}

    public Map<String, Integer> countWordsLineByLine(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addWords(line, frequencies);
            }
        }

        return frequencies;
    }

    private void addWords(String text, Map<String, Integer> frequencies) {
        text = text.toLowerCase(Locale.ROOT);
        String[] words = text.split("[^\\p{L}\\p{N}]+");

        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }

            if (frequencies.containsKey(word)) {
                int count = frequencies.get(word);
                frequencies.put(word, count + 1);
            } else {
                frequencies.put(word, 1);
            }
        }
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
        for (String word : frequencies.keySet()) {
            System.out.println(word + " = " + frequencies.get(word));
        }
    }

    public static void main(String[] args) throws IOException {
        // run word frequency counter
        if (args.length == 0) {
            System.out.println("Usage: java WordFrequencyCounter <file.txt>");
            return;
        }

        Path filePath = Path.of(args[0]);
        WordFrequencyCounter counter = new WordFrequencyCounter();

        System.out.println("Method 1: Read whole file");
        Map<String, Integer> result1 = counter.countWords(filePath);
        counter.printFrequencies(result1);

        System.out.println();
        System.out.println("Method 2: Read line by line");
        Map<String, Integer> result2 = counter.countWordsLineByLine(filePath);
        counter.printFrequencies(result2);
    }
}