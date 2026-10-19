import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.io.BufferedReader;

public class WordFrequencyCounter {

	public static Map<String, Integer> countWords(Path filePath) throws IOException {
		String file = Files.readString(filePath);

		if (file.isBlank()) {
			return Map.of();
		}

		Map<String, Integer> wordFrequencies = new HashMap<>();

		String[] words = file.toLowerCase().split("[^a-zA-Zа-яА-Я0-9]+");

		for (String word : words) {
			if (word.isEmpty()) {
				continue;
			}

			wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
		}

		return wordFrequencies;
	}

	public static Map<String, Integer> countWordsStream(Path filePath) throws IOException {
		Map<String, Integer> wordFrequencies = new HashMap<>();

		try (BufferedReader file = Files.newBufferedReader(filePath)) {
			String line;
			while ((line = file.readLine()) != null) {
				String[] words = line.toLowerCase().split("[^a-zA-Zа-яА-Я0-9]+");

				for (String word : words) {
					if (word.isEmpty()) {
						continue;
					}

					wordFrequencies.put(word, wordFrequencies.getOrDefault(word, 0) + 1);
				}
			}
		}

		return wordFrequencies;
	}

	public static void printFrequencies(Map<String, Integer> frequencies) {
		if (frequencies.isEmpty()) {
			System.out.println("Map is empty");
			return;
		}

		for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}

	public static void main(String[] args) {
		try {
			Path filePath = Path.of("data/russian.txt");

			System.out.println("Сounting words using the function 'countWords'");
			long currentTime = System.currentTimeMillis();
			Map<String, Integer> wordFrequencies = countWords(filePath);
			long endTime = System.currentTimeMillis();
			System.out.println("Number of words: " + wordFrequencies.size());
			System.out.println("Average execution time: " + (endTime - currentTime) + " ms");
//			printFrequencies(wordFrequencies);
			System.out.println();

			System.out.println("Сounting words using the function 'countWordsStream'");
			currentTime = System.currentTimeMillis();
			wordFrequencies = countWordsStream(filePath);
			endTime = System.currentTimeMillis();
			System.out.println("Number of words: " + wordFrequencies.size());
			System.out.println("Average execution time: " + (endTime - currentTime) + " ms");
//			printFrequencies(wordFrequencies);

		} catch (IOException e) {
			System.err.println("Error read file: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Unforeseen error: " + e.getMessage());
		}
	}
}
