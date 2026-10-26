import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class WordFrequencyCounter {

    private static final String DELIMITERS = "[^\\p{L}\\p{Nd}]+";
    private static final int CHUNK_LIMIT = 8192;

    public static void main(String[] args) {
        System.out.print("Введите имя файла: ");
        Path file = Path.of(new Scanner(System.in).nextLine().trim());
        WordFrequencyCounter counter = new WordFrequencyCounter();
        try {
            System.out.println("____Загрузка файла целиком____");
            counter.printFrequencies(counter.countWords(file));

            System.out.println("\n____Потоковая обработка____ ");
            counter.printFrequencies(counter.countWordsStreaming(file));
        } catch (IOException e) {
            System.err.println("Не удалось прочитать файл: " + e.getMessage());
        }
    }

    // Вариант 1: весь текст читается в одну строку и сразу разбирается
    public Map<String, Integer> countWords(Path filePath) throws IOException {
        Map<String, Integer> result = new TreeMap<>();
        collect(Files.readString(filePath), result);
        return result;
    }

    // Вариант 2: символы копятся во фрагмент, который разбирается на переводе строки
    // или, если он длиннее CHUNK_LIMIT, на ближайшем разделителе (слово не разрежется).
    // Расход памяти не зависит от размера файла.
    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> result = new TreeMap<>();
        StringBuilder chunk = new StringBuilder();

        try (BufferedReader in = Files.newBufferedReader(filePath)) {
            for (int ch = in.read(); ch != -1; ch = in.read()) {
                chunk.append((char) ch);
                boolean overflow = chunk.length() > CHUNK_LIMIT && !Character.isLetterOrDigit(ch);
                if (ch == '\n' || overflow) {
                    collect(chunk.toString(), result);
                    chunk.setLength(0);
                }
            }
        }
        collect(chunk.toString(), result);
        return result;
    }

    private void collect(String text, Map<String, Integer> result) {
        for (String word : text.toLowerCase().split(DELIMITERS)) {
            if (!word.isEmpty()) {
                result.merge(word, 1, Integer::sum);
            }
        }
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        frequencies.forEach((word, count) -> System.out.println(word + " - " + count));
        System.out.println("Различных слов: " + frequencies.size());
    }
}
