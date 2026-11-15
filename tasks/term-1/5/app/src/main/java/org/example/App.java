package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

public class App {
    private static final int PROGRESS_INTERVAL = 1_000;
    private static final long PROGRESS_PERIOD = TimeUnit.SECONDS.toNanos(10);
    private static final CSVFormat INPUT_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader().setSkipHeaderRecord(true).get();
    private static final CSVFormat OUTPUT_FORMAT = CSVFormat.DEFAULT.builder()
            .setHeader("id", "review", "original_sentiment", "predicted_sentiment").get();

    public static void main(String[] args) {
        int exitCode = run(args, System.out, System.err);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    static int run(String[] args, PrintStream out, PrintStream err) {
        if (args.length == 1 && (args[0].equals("--help") || args[0].equals("-h"))) {
            printUsage(out);
            return 0;
        }
        if (args.length != 1 || args[0].isBlank()) {
            err.println("Error: provide one dataset filename.");
            printUsage(err);
            return 2;
        }

        Path input;
        try {
            input = Path.of(args[0]).toAbsolutePath().normalize();
        } catch (InvalidPathException exception) {
            err.println("Error: invalid dataset filename: " + args[0]);
            printUsage(err);
            return 2;
        }
        if (!Files.isRegularFile(input)) {
            err.println("Error: dataset file not found: " + input);
            printUsage(err);
            return 2;
        }

        String filename = input.getFileName().toString();
        String stem = filename.toLowerCase(Locale.ROOT).endsWith(".csv")
                ? filename.substring(0, filename.length() - 4) : filename;
        Path output = input.resolveSibling(stem + "-sentiments.csv");
        try {
            long started = System.nanoTime();
            ReviewStatistics statistics = processDataset(input, output, out);
            statistics.printSummary(out, (System.nanoTime() - started) / 1_000_000_000.0);
            out.printf("Generated %s with sentiment predictions.%n", output);
            return 0;
        } catch (IOException | RuntimeException exception) {
            err.println("Error: could not complete the analysis: " + exception.getMessage());
            return 1;
        }
    }

    private static ReviewStatistics processDataset(Path input, Path output, PrintStream out) throws IOException {
        ReviewStatistics statistics = new ReviewStatistics();
        Path temporary = Files.createTempFile(output.getParent(), ".sentiments-", ".tmp");
        long lastReported = -1;

        try {
            try (BufferedReader reader = Files.newBufferedReader(input, StandardCharsets.UTF_8)) {
                reader.mark(1);
                if (reader.read() != '\uFEFF') {
                    reader.reset();
                }

                try (CSVParser records = INPUT_FORMAT.parse(reader)) {
                    if (!records.getHeaderNames().containsAll(List.of("review", "sentiment"))) {
                        throw new IOException("Dataset must contain the 'review' and 'sentiment' columns.");
                    }

                    out.println("Loading the English sentiment model...");
                    SentimentAnalyzer analyzer = new SentimentAnalyzer();
                    out.println("Processing reviews from " + input.getFileName() + "...");
                    long lastProgress = System.nanoTime();

                    try (CSVPrinter csv = OUTPUT_FORMAT.print(Files.newBufferedWriter(temporary, StandardCharsets.UTF_8))) {
                        for (CSVRecord record : records) {
                            if (!record.isConsistent()) {
                                throw new IOException("Invalid CSV record " + record.getRecordNumber() + ".");
                            }
                            String review = record.get("review");
                            String originalSentiment = record.get("sentiment");
                            SentimentAnalyzer.Sentiment prediction = analyzer.classify(review);
                            statistics.add(originalSentiment, prediction);
                            csv.printRecord(statistics.total(), review, originalSentiment, prediction.label());
                            long currentTime = System.nanoTime();
                            if (statistics.total() % PROGRESS_INTERVAL == 0 || currentTime - lastProgress >= PROGRESS_PERIOD) {
                                printProgress(out, statistics.total());
                                lastReported = statistics.total();
                                lastProgress = currentTime;
                            }
                        }
                    }
                }
            }

            Files.move(temporary, output, StandardCopyOption.REPLACE_EXISTING);
            if (statistics.total() != lastReported) {
                printProgress(out, statistics.total());
            }
            return statistics;
        } finally {
            Files.deleteIfExists(temporary);
        }
    }

    private static void printProgress(PrintStream out, long count) {
        out.printf(Locale.ROOT, "Processed %,d reviews.%n", count);
    }

    private static void printUsage(PrintStream out) {
        out.println("Usage: sentiments <dataset.csv>");
        out.println("Example: sentiments \"IMDB Dataset.csv\"");
        out.println("The UTF-8 CSV must contain the 'review' and 'sentiment' columns.");
    }
}
