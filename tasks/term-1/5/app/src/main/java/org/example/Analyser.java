package org.example;
import java.util.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.io.BufferedReader;


public class Analyser {


    private List<String> atributes;

    

    public static Review parseLine(String line) {
        line = deleteHtml(line);
        int ind = line.lastIndexOf(',');
        String review = line.substring(0, ind);
        if(review.startsWith("\"", 0) && review.endsWith("\""))
            review = review.substring(1, review.length() - 1).replace("\"\"", "\"");
        Sentiment sentiment = Sentiment.fromLabel(line.substring(ind+1));
        Review ans = new Review(review, sentiment);
        return ans;
    }

    public static String deleteHtml(String line) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < line.length(); ++i){
            if(line.startsWith("<br />", i)) { sb.append(' '); i += 5; continue; }
            sb.append(line.charAt(i));
        }
        return sb.toString();
    }


    public static List<Review> parseCSV(Path filePath, int limit) throws IOException {
        List<Review> ans = new ArrayList<>();


        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            reader.readLine();

            for(int i = 0; i < limit; ++i){
                line = reader.readLine();
                if(line == null) break;
                Review prod = parseLine(line);
                ans.add(prod);
            }
            /* while ((line = reader.readLine()) != null) {
                Review prod = parseLine(line);
                ans.add(prod);
            } */
        
        }
        return ans;
    }

    /* public static List<Pair<Integer, Sentiment>> calc_pairs(SentimentAnalyzer analyzer, List<Review> reviews) {
        List<Pair<Integer, Sentiment>> ans = new ArrayList<>();

        return ans;
    } */

    public static List<ScoredReview> calc_pairs(
        SentimentAnalyzer analyzer,
        List<Review> reviews) {

            List<ScoredReview> result = new ArrayList<>();  // это пары <список оценок, сентимент>


            for (int i = 0; i < reviews.size(); i++) {
                Review review = reviews.get(i);
                List<Integer> scores = analyzer.sentenceScores(review.text()); // зависит от вашего API
                ScoredReview el = new ScoredReview(scores, review.mark());
                result.add(el);
            }


            return result;
    }


    public static double calcBound(List<ScoredReview> pairs) {
        List<ScoredReview> sorted_pairs = new ArrayList<>(pairs);
        sorted_pairs.sort(Comparator.comparingDouble(ScoredReview::mean));

        int errs = 0;
        for (ScoredReview pair: sorted_pairs) {
            if (pair.expected() == Sentiment.NEGATIVE) errs++;
        }

        int bestErrs = errs;
        double bestBound = sorted_pairs.get(0).mean();

        for(int i = 0; i < sorted_pairs.size(); ++i){
            if(sorted_pairs.get(i).expected() == Sentiment.POSITIVE) errs++;
            else if(sorted_pairs.get(i).expected() == Sentiment.NEGATIVE) errs--;

            boolean last = (i == sorted_pairs.size() - 1);
            if (!last && Double.compare(sorted_pairs.get(i).mean(), sorted_pairs.get(i + 1).mean()) == 0) {
                continue;                       // одинаковые средние: границу здесь ставить нельзя
            }
            if (errs < bestErrs) {
                bestErrs = errs;
                bestBound = last ? Math.nextUp(sorted_pairs.get(i).mean())
                            : (sorted_pairs.get(i).mean() + sorted_pairs.get(i + 1).mean()) / 2.0;
            }
        }

        return bestBound;
    }


    public static double accuracy(List<ScoredReview> pairs, double t) {
        if (pairs.isEmpty()) return 0.0;
        int correct = 0;
        for(int i = 0; i < pairs.size(); ++i){
            Sentiment curValue = pairs.get(i).mean() < t ? Sentiment.NEGATIVE : Sentiment.POSITIVE;
            if(curValue == pairs.get(i).expected()) correct++;
        }

        return (double)correct / pairs.size();
    }


    public static void main(String[] args) {
        System.out.println("Hello, world!");

        if (args.length < 1) {
            System.err.println("Использование: <файл> [limit]");
            System.exit(1);
        }

        int limit = 10;

        if(args.length > 1){
            try {
                limit = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("limit должен быть целым числом: " + e.getMessage());
                System.exit(1);
            }
        }

        if (limit <= 0) { System.err.println("limit должен быть положительным числом"); System.exit(1); }



        try{

            Path path = Path.of(args[0]);
            List<Review> reviews = parseCSV(path, limit);

            SentimentAnalyzer analyzer = new SentimentAnalyzer();   // создаём ОДИН раз, до цикла

            long start = System.currentTimeMillis();
            List<ScoredReview> pairs = calc_pairs(analyzer, reviews);
            long elapsed = System.currentTimeMillis() - start;

            for (int i = 0; i < pairs.size(); ++i){
                System.out.println(pairs.get(i).scores() + " " + pairs.get(i).expected());
            }

            System.out.println("Обработано отзывов: " + reviews.size());
            System.out.println("Время: " + elapsed + " мс");


            Double bound = calcBound(pairs);
            System.out.println("Сходится: " + accuracy(pairs, bound));





        } catch (NoSuchFileException e){
            System.err.println("Файл не найден" + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Ошибка чтения" + e.getMessage());
            System.exit(1);
        }


    }

}

