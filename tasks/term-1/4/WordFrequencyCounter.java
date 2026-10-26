import java.util.*;
import java.nio.file.Path;

public class WordFrequencyCounter {

public Map<String, Integer> countWordsMemory(Path filePath) {
        Map<String, Integer> frequencies = new HashMap<>();
        try {
            String text = java.nio.file.Files.readString(filePath).toLowerCase();
            for (String word : text.split("[\\s\\p{Punct}]+")) {
                if (!word.isEmpty()) frequencies.put(word, frequencies.getOrDefault(word, 0) + 1);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return frequencies;
    }

    public Map<String, Integer> countWords(Path filePath) {
        Map<String, Integer> dictionary = new HashMap<>();
        try (Scanner scanner = new Scanner(filePath).useDelimiter("[\\s\\p{Punct}]+")) {
            while (scanner.hasNext()) {
                String word = scanner.next().toLowerCase();
                if (!word.isEmpty()) dictionary.put(word, dictionary.getOrDefault(word, 0) + 1);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при чтении файла: " + e.getMessage());
        }
        return dictionary;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        if (frequencies == null || frequencies.isEmpty()) {
            return;
        }
        
        List<Map.Entry<String, Integer>> list = new ArrayList<>(frequencies.entrySet());
        list.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        
        for (Map.Entry<String, Integer> entry : list) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    public static void main(String[] args) {
        WordFrequencyCounter counter = new WordFrequencyCounter();
        
        try {
            Path filePath = java.nio.file.Paths.get("test.txt");
            java.nio.file.Files.writeString(filePath, "На сегодняшний момент язык Java является одним из самых распространенных и популярных языков программирования. Первая версия языка появилась 23 мая в 1995 году в недрах компании Sun Microsystems, впоследствии поглощенной компанией Oracle. Спустя год вышла первая версия JDK (Java Development Kit) - набора инструментов для разработчиков для программирования на языке Java.\r\n" + //
                                "\r\n" + //
                                "Java задумывался как универсальный язык программирования, который можно применять для различного рода задач. В итоге выход первой версии языка Java (1.0) в 1995 году стал знаковым событием для индустрии разработки программного обеспечения.");

            System.out.println("Потоковая обработка");
            Map<String, Integer> streamResult = counter.countWords(filePath);
            counter.printFrequencies(streamResult);

            System.out.println("\nЗагрузка в память");
            Map<String, Integer> memoryResult = counter.countWordsMemory(filePath);
            counter.printFrequencies(memoryResult);
            
            java.nio.file.Files.deleteIfExists(filePath);
        } catch (Exception e) {
            System.out.println("Ошибка в тестовом запуске: " + e.getMessage());
        }
    }
}