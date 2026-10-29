import java.util.*;
import java.nio.file.Path;
import java.nio.file.Files;
import java.io.IOException;
import java.io.BufferedReader;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) throws IOException {
        
        Map<String, Integer> frequencies = new HashMap<>();

        String text = Files.readString(filePath);
        String[] words = text.split("[^\\p{L}\\p{N}']+");

        for (String word : words){
            if (word.isEmpty()) continue;
            frequencies.merge(word.toLowerCase(), 1, Integer::sum);
        }

        return frequencies;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        System.out.println("Word : frequency");
        
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(frequencies.entrySet());
        
        Collections.sort(entries, new Comparator<Map.Entry<String, Integer>>(){
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b){
                int cmp = b.getValue() - a.getValue();
                if (cmp != 0){
                    return cmp;
                }
                return a.getKey().compareTo(b.getKey());
            }
        });

        for (Map.Entry<String, Integer> entry : entries){
            System.out.println(entry.getKey() + ": " + entry.getValue() + "times");
        }
    }

    public Map<String, Integer> countWordsStream(Path filePath) throws IOException{
        Map<String, Integer> frequencies = new HashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(filePath)){
            String line;
            while ((line = reader.readLine()) != null){
                String[] words = line.split("[^\\p{L}\\p{N}']+");
                for (String word : words){
                    if (word.isEmpty()) continue;
                    String w = word.toLowerCase();
                    if (frequencies.containsKey(w)){
                        frequencies.put(w, frequencies.get(w) + 1);
                    }
                    else{
                        frequencies.put(w, 1);
                    }
                }
            }
        }

        return frequencies;
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage ");
            System.out.println("java WordFrequencyCounter.java --file <path>");
            System.out.println("java WordFrequencyCounter.java --stream <path>");
            return;
        }

        WordFrequencyCounter counter = new WordFrequencyCounter();
        Path filePath = Path.of(args[1]);

        try{
            Map<String, Integer> frequencies;
            if (args[0].equals("--file")){
                frequencies = counter.countWords(filePath);
            }
            else if (args[0].equals("--stream")){
                frequencies = counter.countWordsStream(filePath);
            }
            else{
                System.out.println("Unknown mode, choose '--file' or '--stream'");
                return;
            }
            counter.printFrequencies(frequencies);
        }
        catch (IOException e){
            System.out.println("error while working with files" + filePath);
        }
    }
}
