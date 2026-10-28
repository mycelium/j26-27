import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

public class WordFrequencyCounter {

    public Map<String, Integer> countWords(Path filePath) {
        // read file, tokenize words, update map
        return null;
    }

    public void printFrequencies(Map<String, Integer> frequencies) {
        // print word counts
    }

    public static void main(String[] args) {
        try{
            Path p = Path.of(args[0]);
            Path path = p.toAbsolutePath();
                String text = Files.readString(path);      
                String head = text.substring(0, Math.min(42, text.length()));
                System.out.println(head);

            String[] words = head.split("\\P{L}+"); 
            for(String w: words){
                String new_w = w.toLowerCase();
                System.out.println(new_w);
            }
        }
        catch (NoSuchFileException e) {
            System.err.println("Файл не найден: " + e.getMessage());
            System.exit(1);
        }
        catch (IOException e) {
            System.err.println("Ошибка чтения: " + e.getMessage());
            System.exit(1);
        }



        
        // run word frequency counter
        /* System.out.println("agrs count: " + args.length);
        if(args.length == 0){
            System.err.println("Аргумент не передан!");
            System.exit(1);
        }
        else if(args.length == 1){
            Path p = Path.of(args[0]);
            String str = p.toAbsolutePath().toString();
            System.out.println(str + " is here");
        } */

        
    }
}
