import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFrequencyCounter {

    // Вариант 1: Загрузка всего файла в память (подходит для небольших файлов)
    public Map<String, Integer> countWordsInMemory(Path filePath) throws IOException {
        // Читаем все байты файла и преобразуем в строку
        String content = new String(Files.readAllBytes(filePath));
        return processText(content);
    }

    // Вариант 2: Потоковая обработка
    public Map<String, Integer> countWordsStreaming(Path filePath) throws IOException {
        Map<String, Integer> frequencies = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("[^a-zA-Z]+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        if (frequencies.containsKey(word)) {
                            frequencies.put(word, frequencies.get(word) + 1);
                        } else {
                            frequencies.put(word, 1);
                        }
                    }
                }
            }
        }
        return frequencies;
    }

    private Map<String, Integer> processText(String text) {
        Map<String, Integer> frequencies = new HashMap<>();
        String[] words = text.toLowerCase().split("[^a-zA-Z]+");
        for (String word : words) {
            if (!word.isEmpty()) {
                if (frequencies.containsKey(word)) {
                    frequencies.put(word, frequencies.get(word) + 1);
                } else {
                    frequencies.put(word, 1);
                }
            }
        }
        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // 1. Преобразуем Map в список записей (Entry), чтобы можно было их отсортировать
        List<Map.Entry<String, Integer>> list = new ArrayList<>(frequencies.entrySet());

        // 2. сортируем список по убыванию частоты
        list.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // 3.  топ-20 слов
        int limit = Math.min(20, list.size());
        for (int i = 0; i < limit; i++) {
            Map.Entry<String, Integer> entry = list.get(i);
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        WordFrequencyCounter counter = new WordFrequencyCounter();
        
        Path testFile = Paths.get("test.txt");
        try {
            String testContent = "Hello world! This is a test. Hello again, world. This is only a test to check word frequencies.";
            Files.write(testFile, testContent.getBytes());

            System.out.println("--- Вариант 1: Загрузка всего файла ---");
            Map<String, Integer> inMemoryFreq = counter.countWordsInMemory(testFile);
            counter.printFrequencies(inMemoryFreq);

            System.out.println("\n--- Вариант 2: Потоковая обработка ---");
            Map<String, Integer> streamingFreq = counter.countWordsStreaming(testFile);
            counter.printFrequencies(streamingFreq);

            Files.deleteIfExists(testFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}