package org.example;

import java.io.PrintStream;
import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;
import org.example.SentimentAnalyzer.Sentiment;

final class ReviewStatistics {
    private final Map<Sentiment, Long> predictions = new EnumMap<>(Sentiment.class);
    private long total;
    private long labeled;
    private long matches;

    void add(String originalSentiment, Sentiment prediction) {
        total++;
        predictions.merge(prediction, 1L, Long::sum);
        if (!originalSentiment.isBlank()) {
            labeled++;
            if (prediction.label().equalsIgnoreCase(originalSentiment.strip())) {
                matches++;
            }
        }
    }

    long total() {
        return total;
    }

    void printSummary(PrintStream out, double seconds) {
        out.println("\nSummary:");
        out.printf(Locale.ROOT, "Total reviews: %,d%n", total);
        for (Sentiment sentiment : Sentiment.values()) {
            long count = predictions.getOrDefault(sentiment, 0L);
            out.printf(Locale.ROOT, "  %s: %,d (%.1f%%)%n", sentiment.label(), count, percentage(count, total));
        }
        if (labeled > 0) {
            out.printf(Locale.ROOT, "Matches with original labels: %,d / %,d (%.1f%%)%n",
                    matches, labeled, percentage(matches, labeled));
        } else {
            out.println("Matches with original labels: not available (no labeled reviews).");
        }
        out.printf(Locale.ROOT, "Elapsed time: %.2f seconds%n", seconds);
    }

    private static double percentage(long count, long base) {
        return base == 0 ? 0.0 : 100.0 * count / base;
    }
}
