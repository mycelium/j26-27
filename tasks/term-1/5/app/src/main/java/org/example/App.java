package org.example;

import edu.stanford.nlp.ling.CoreAnnotations.*;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import org.apache.commons.csv.CSVFormat;

import java.io.IOException;
import java.nio.file.*;
import java.util.Properties;

import static java.nio.charset.StandardCharsets.UTF_8;

public class App {
    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2) {
            throw new IllegalArgumentException("Использование: input.csv [limit]");
        }
        Path input = Path.of(args[0]);
        Path output = Path.of("data", "results.csv");
        int limit = args.length == 2 ? Integer.parseInt(args[1]) : 0;
        if (args.length == 2 && limit <= 0) {
            throw new IllegalArgumentException("Количество отзывов должно быть больше нуля.");
        }

        if (Files.exists(output) && Files.isSameFile(input, output)) {
            throw new IllegalArgumentException("Нельзя записывать результат поверх входного файла.");
        }

        var inputFormat = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setMaxRows(limit)
                .get();
        var outputFormat = CSVFormat.DEFAULT.builder()
                .setHeader("review", "original_sentiment", "predicted_sentiment")
                .get();

        try (var reader = Files.newBufferedReader(input);
             var reviews = inputFormat.parse(reader)) {
            if (!reviews.getHeaderMap().containsKey("review")) {
                throw new IllegalArgumentException("В CSV нужен столбец review.");
            }

            System.out.println("Загрузка CoreNLP...");
            Properties properties = new Properties();
            properties.setProperty("annotators", "tokenize,ssplit,pos,parse,sentiment");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

            Files.createDirectories(output.getParent());
            int count = 0;
            try (var result = outputFormat.print(output, UTF_8)) {
                for (var record : reviews) {
                    String review = record.get("review");
                    String original = record.isMapped("sentiment") ? record.get("sentiment") : "";
                    result.printRecord(review, original, analyze(review, pipeline));
                    count++;
                }
            }
            System.out.println("Обработано отзывов: " + count);
            System.out.println("Результат: " + output);
        }
    }

    private static String analyze(String review, StanfordCoreNLP pipeline) {
        String text = review.replaceAll("<[^>]+>", " ").trim();
        if (text.isEmpty()) {
            return "neutral";
        }

        Annotation document = new Annotation(text);
        pipeline.annotate(document);

        double weightedScore = 0;
        int totalTokens = 0;
        for (var sentence : document.get(SentencesAnnotation.class)) {
            var tree = sentence.get(SentimentAnnotatedTree.class);
            int score = RNNCoreAnnotations.getPredictedClass(tree);
            int tokens = sentence.get(TokensAnnotation.class).size();
            weightedScore += score * tokens;
            totalTokens += tokens;
        }

        double average = totalTokens == 0 ? 2 : weightedScore / totalTokens;
        if (average < 1.5) {
            return "negative";
        }
        if (average > 2.5) {
            return "positive";
        }
        return "neutral";
    }
}
