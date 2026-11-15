package org.example;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class App {

    private static final String USAGE = """
            Использование: gradlew run --args="--input reviews.csv [--output result.csv] [--limit N] [--threads N] [--column review] [--threshold 0]"
              --input,  -i   CSV (колонка review/text/content/comment, опционально sentiment/label)
                             или текстовый файл: один отзыв на строку
              --output, -o   куда писать результат (по умолчанию sentiments.csv)
              --limit,  -n   обработать только первые N отзывов (парсер медленный, для проверки берите 200-1000)
              --threads,-t   число потоков (по умолчанию min(4, число ядер))
              --column       имя колонки с текстом, если не угадалось автоматически
              --threshold    порог для net-оценки: |net| <= порога -> neutral (по умолчанию 0)
            """;

    public static void main(String[] args) throws Exception {
        Path input = null;
        Path output = Path.of("sentiments.csv");
        int limit = 0;
        int threads = Math.max(1, Math.min(4, Runtime.getRuntime().availableProcessors()));
        String column = null;
        double threshold = 0;

        try {
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "--input", "-i" -> input = Path.of(args[++i]);
                    case "--output", "-o" -> output = Path.of(args[++i]);
                    case "--limit", "-n" -> limit = Integer.parseInt(args[++i]);
                    case "--threads", "-t" -> threads = Math.max(1, Integer.parseInt(args[++i]));
                    case "--column" -> column = args[++i];
                    case "--threshold" -> threshold = Double.parseDouble(args[++i]);
                    case "--help", "-h" -> { System.out.print(USAGE); return; }
                    default -> {
                        if (args[i].startsWith("-") || input != null) throw new IllegalArgumentException("Неизвестный аргумент: " + args[i]);
                        input = Path.of(args[i]);
                    }
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Ошибка в аргументах: " + e.getMessage());
            System.err.print(USAGE);
            System.exit(2);
        }
        if (input == null) {
            System.err.print(USAGE);
            System.exit(2);
        }

        List<Review> reviews = ReviewReader.read(input, column, limit);
        if (reviews.isEmpty()) {
            System.err.println("В файле нет отзывов.");
            System.exit(1);
        }
        System.out.printf("Отзывов: %d, потоков: %d. Загружаю модели CoreNLP (около минуты)...%n", reviews.size(), threads);

        ReviewAnalyzer analyzer = new ReviewAnalyzer(new CoreNlpScorer(), threshold);
        run(reviews, analyzer, threads, output);
    }

    static void run(List<Review> reviews, ReviewAnalyzer analyzer, int threads, Path output) throws IOException, InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<ReviewResult>> futures = new ArrayList<>(reviews.size());
        for (Review r : reviews) futures.add(pool.submit(() -> analyzer.analyze(r.text())));
        pool.shutdown();

        Map<Sentiment, Integer> counts = new EnumMap<>(Sentiment.class);
        Evaluator evaluator = new Evaluator();
        boolean hasLabels = reviews.stream().anyMatch(r -> r.expected() != null);
        boolean verbose = reviews.size() <= 20;
        int failures = 0;
        long start = System.currentTimeMillis();

        try (BufferedWriter w = Files.newBufferedWriter(output, StandardCharsets.UTF_8)) {
            w.write("id,predicted,net_score,sentences" + (hasLabels ? ",expected" : "") + ",review_preview\n");
            for (int i = 0; i < reviews.size(); i++) {
                Review review = reviews.get(i);
                ReviewResult res;
                try {
                    res = futures.get(i).get();
                } catch (ExecutionException e) {
                    res = ReviewResult.failed(String.valueOf(e.getCause()));
                }
                if (res.sentiment() == null) {
                    failures++;
                    System.err.printf("Отзыв %d: ошибка анализа: %s%n", review.id(), res.error());
                } else {
                    counts.merge(res.sentiment(), 1, Integer::sum);
                    evaluator.add(review.expected(), res.sentiment());
                }

                String predicted = res.sentiment() == null ? "" : res.sentiment().label();
                String preview = preview(review.text());
                w.write(review.id() + "," + predicted + "," + res.net() + "," + res.sentences()
                        + (hasLabels ? "," + (review.expected() == null ? "" : review.expected().label()) : "")
                        + "," + csv(preview) + "\n");

                if (verbose) {
                    System.out.printf("[%-8s] (net=%+d) %s%n", predicted.isEmpty() ? "error" : predicted, res.net(), preview);
                } else if ((i + 1) % 100 == 0 || i + 1 == reviews.size()) {
                    double sec = (System.currentTimeMillis() - start) / 1000.0;
                    System.out.printf("\rОбработано %d / %d (%.1f отзывов/с)", i + 1, reviews.size(), (i + 1) / Math.max(sec, 0.001));
                }
            }
        }
        if (!verbose) System.out.println();

        System.out.println("\nИтог:");
        for (Sentiment s : new Sentiment[]{Sentiment.POSITIVE, Sentiment.NEGATIVE, Sentiment.NEUTRAL}) {
            System.out.printf("  %-8s %d%n", s.label(), counts.getOrDefault(s, 0));
        }
        if (failures > 0) System.out.printf("  ошибок анализа: %d (в результате у них пустая оценка)%n", failures);
        System.out.print(evaluator.report());
        System.out.println("\nРезультат записан в " + output.toAbsolutePath());
    }

    static String preview(String text) {
        return text.length() <= 100 ? text : text.substring(0, 100) + "...";
    }

    static String csv(String s) {
        return "\"" + s.replace("\"", "\"\"") + "\"";
    }
}
