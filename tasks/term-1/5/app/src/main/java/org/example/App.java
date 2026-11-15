package org.example;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        String fileName = "IMDB Dataset.csv";
        Path filePath = Paths.get(fileName);

        if (!Files.exists(filePath)) {
            filePath = Paths.get("..", fileName);
        }

        try {
            analyzeSentiments(filePath);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
            System.err.println("Программа искала файл по пути: " + filePath.toAbsolutePath());
        }
    }

    private static void analyzeSentiments(Path filePath) throws IOException {
        System.out.println("Инициализация CoreNLP (это может занять некоторое время при первом запуске)...");
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
        props.setProperty("quiet", "true");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        System.out.println("CoreNLP успешно инициализирован.\n");
        System.out.println("Найден файл, начинаем чтение: " + filePath.toAbsolutePath());

        int totalReviews = 0;
        int correctPredictions = 0;

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;
            StringBuilder currentReview = new StringBuilder();

            while ((line = reader.readLine()) != null) {

                if (currentReview.length() == 0 && (line.trim().isEmpty() || line.trim().equals("review,sentiment"))) {
                    continue;
                }
                
                if (currentReview.length() > 0) {
                    currentReview.append("\n");
                }
                currentReview.append(line);

                String currentStr = currentReview.toString().trim();
                
                if (currentStr.endsWith("\",positive") || currentStr.endsWith("\",negative") || currentStr.endsWith("\",neutral")) {
                    
                    int lastComma = currentStr.lastIndexOf(',');
                    String actualSentiment = currentStr.substring(lastComma + 1).trim();
                    
                    String reviewWithQuotes = currentStr.substring(0, lastComma).trim();
                    
                    String reviewText = reviewWithQuotes;
                    if (reviewText.startsWith("\"")) reviewText = reviewText.substring(1);
                    if (reviewText.endsWith("\"")) reviewText = reviewText.substring(0, reviewText.length() - 1);
                    
                    reviewText = reviewText.replace("\"\"", "\"");

                    String cleanText = reviewText.replaceAll("<[^>]*>", " ").replaceAll("\\s+", " ").trim();

                    totalReviews++;
                    String predictedSentiment = getSentiment(pipeline, cleanText);

                    if (totalReviews % 50 == 0 || totalReviews <= 5) {
                        System.out.println("Отзыв #" + totalReviews + " (фрагмент): " +
                                (cleanText.length() > 60 ? cleanText.substring(0, 60) + "..." : cleanText));
                        System.out.println("Ожидаемая оценка: " + actualSentiment);
                        System.out.println("Предсказанная:    " + predictedSentiment);

                        if (actualSentiment.equals(predictedSentiment)) {
                            System.out.println("Результат: ВЕРНО\n");
                        } else {
                            System.out.println("Результат: ОШИБКА\n");
                        }
                    }

                    if (actualSentiment.equals(predictedSentiment)) {
                        correctPredictions++;
                    }

                    currentReview.setLength(0);
                }
            }
        }

        System.out.println("=========================================");
        System.out.println("Всего отзывов обработано: " + totalReviews);

        if (totalReviews > 0) {
            System.out.println("Точность модели: " + correctPredictions + "/" + totalReviews +
                    String.format(" (%.2f%%)", (double) correctPredictions / totalReviews * 100));
        } else {
            System.out.println("Отзывы не найдены. Проверьте формат файла.");
        }
    }

    private static String getSentiment(StanfordCoreNLP pipeline, String text) {
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        if (sentences == null || sentences.isEmpty()) {
            return "neutral";
        }

        int score = 0;
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);

            if (sentiment.contains("Positive")) {
                score++;
            } else if (sentiment.contains("Negative")) {
                score--;
            }
        }

        if (score > 0) return "positive";
        if (score < 0) return "negative";
        return "neutral";
    }
}