import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

public class WordFrequencyCounter {

    // Максимальная длина "строки" при потоковой обработке
    private static final int MAX_LINE_LENGTH = 10_000;

    // Вариант 1: весь файл загружается в память, затем обрабатывается
    public Map<String, Integer> countWords(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new TreeMap<>();
        String text = Files.readString(filePath);
        addWords(frequencies, text);
        return frequencies;
    }

    // Вариант 2: потоковая обработка, файл читается по символам, в памяти только текущая "строка".
    // Строка заканчивается на символе перевода строки или, если строка слишком длинная (более MAX_LINE_LENGTH символов),
    // на первом символе, который не является буквой или цифрой (чтобы не разрезать слово)
    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new TreeMap<>();
        StringBuilder line = new StringBuilder();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            int c;
            while ((c = reader.read()) != -1) {
                line.append((char) c);
                boolean newLine = c == '\n';
                boolean tooLong = line.length() >= MAX_LINE_LENGTH && !Character.isLetterOrDigit(c);
                if (newLine || tooLong) {
                    addWords(frequencies, line.toString());
                    line.setLength(0);
                }
            }
        }
        addWords(frequencies, line.toString());
        return frequencies;
    }

    // Разбивает текст на слова (буквы и цифры) и увеличивает счётчик каждого слова
    private void addWords(Map<String, Integer> frequencies, String text) {
        for (String word : text.toLowerCase().split("[^\\p{L}\\p{Nd}]+")) {
            if (!word.isEmpty()) {
                frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
            }
        }
    }

    // Печатает слова по алфавиту (TreeMap хранит ключи отсортированными)
    public void printFrequencies(Map<String, Integer> frequencies) {
        for (Map.Entry<String, Integer> entry : frequencies.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) throws IOException {
        Path path = Path.of(args[0]);
        WordFrequencyCounter counter = new WordFrequencyCounter();

        if (args.length > 1 && args[1].equals("stream")) {
            counter.printFrequencies(counter.countWordsStreaming(path));
        } else {
            counter.printFrequencies(counter.countWords(path));
        }
    }
}