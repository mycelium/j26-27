import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class WordFrequencyCounter {

    // A word is a group of letters and digits. Everything else (space, comma,
    // dot, new line...) is a separator. Words are counted in lower case,
    // so "The" and "the" are the same word.

    // Variant 1: load the whole file into memory, then count the words.
    public Map<String, Integer> countWords(Path filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(filePath);
        String text = new String(bytes, StandardCharsets.UTF_8);
        char[] chars = text.toCharArray();

        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();
        addChars(chars, chars.length, word, frequencies);
        addWord(word, frequencies); // the file can end with a word
        return frequencies;
    }

    // Variant 2: read the file in small parts (8192 characters) and count the
    // words on the go. The file is never fully in memory, so it can be bigger
    // than the RAM. Only the map with different words is kept.
    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        StringBuilder word = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(Files.newInputStream(filePath), StandardCharsets.UTF_8))) {
            char[] buffer = new char[8192];
            int length;
            while ((length = reader.read(buffer)) != -1) {
                // "word" is kept between the parts, because a word can be cut
                // in the middle by the end of the buffer
                addChars(buffer, length, word, frequencies);
            }
        }
        addWord(word, frequencies); // the file can end with a word
        return frequencies;
    }

    // Goes through the first "length" characters. Letters and digits are
    // collected in "word", any other character finishes the word.
    // Both variants use this method, so they follow the same rules.
    private static void addChars(char[] chars, int length, StringBuilder word,
                                 Map<String, Integer> frequencies) {
        for (int i = 0; i < length; i++) {
            char c = chars[i];
            if (Character.isLetterOrDigit(c)) {
                word.append(c);
            } else {
                addWord(word, frequencies);
            }
        }
    }

    // If there is a word collected, add 1 to its counter and clear "word".
    private static void addWord(StringBuilder word, Map<String, Integer> frequencies) {
        if (word.length() > 0) {
            // Locale.ROOT, so the result does not depend on the language of the computer
            String w = word.toString().toLowerCase(Locale.ROOT);
            frequencies.put(w, frequencies.getOrDefault(w, 0) + 1);
            word.setLength(0);
        }
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        printFrequencies(frequencies, frequencies.size());
    }

    // Prints the "limit" most frequent words. Same counts go in alphabetical order.
    public void printFrequencies(Map<String, Integer> frequencies, int limit) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        entries.sort((a, b) -> {
            if (!a.getValue().equals(b.getValue())) {
                return Integer.compare(b.getValue(), a.getValue());
            }
            return a.getKey().compareTo(b.getKey());
        });

        for (int i = 0; i < entries.size() && i < limit; i++) {
            System.out.println(entries.get(i).getKey() + ": " + entries.get(i).getValue());
        }
        System.out.println("Different words: " + frequencies.size());
    }

    // Usage: java WordFrequencyCounter [file] [whole | stream]
    // Without the second argument both variants are run.
    // Without arguments a small test text is used.
    public static void main(String[] args) throws IOException {
        WordFrequencyCounter counter = new WordFrequencyCounter();

        Path file;
        if (args.length == 0) {
            String text = "The cat and the dog. The DOG ran, and the cat sat!\n"
                    + "Кот и собака. Кот сидит, собака бежит.\n"
                    + "One, two, three... one two; ONE!";
            file = Files.createTempFile("words", ".txt");
            Files.write(file, text.getBytes(StandardCharsets.UTF_8));
            file.toFile().deleteOnExit();
            System.out.println("No file given, using a small test text.");
        } else {
            file = Paths.get(args[0]);
        }

        String mode = "both";
        if (args.length >= 2) {
            mode = args[1];
        }
        if (!mode.equals("whole") && !mode.equals("stream") && !mode.equals("both")) {
            System.out.println("The second argument must be whole or stream");
            return;
        }

        Map<String, Integer> wholeResult = null;
        Map<String, Integer> streamResult = null;

        if (mode.equals("whole") || mode.equals("both")) {
            long start = System.currentTimeMillis();
            wholeResult = counter.countWords(file);
            long time = System.currentTimeMillis() - start;
            System.out.println("--- Variant 1: whole file in memory (" + time + " ms)");
            counter.printFrequencies(wholeResult, 10);
        }

        if (mode.equals("stream") || mode.equals("both")) {
            long start = System.currentTimeMillis();
            streamResult = counter.countWordsStreaming(file);
            long time = System.currentTimeMillis() - start;
            System.out.println("--- Variant 2: streaming (" + time + " ms)");
            counter.printFrequencies(streamResult, 10);
        }

        if (wholeResult != null && streamResult != null) {
            System.out.println("Both results are the same: " + wholeResult.equals(streamResult));
        }
    }
}
