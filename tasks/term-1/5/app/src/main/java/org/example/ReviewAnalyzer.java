package org.example;

public final class ReviewAnalyzer {
    private final SentenceScorer scorer;
    private final double threshold;

    public ReviewAnalyzer(SentenceScorer scorer, double threshold) {
        this.scorer = scorer;
        this.threshold = threshold;
    }

    public ReviewResult analyze(String text) {
        try {
            int[] classes = scorer.score(text);
            return ReviewResult.ok(SentimentAggregator.aggregate(classes, threshold),
                    SentimentAggregator.net(classes), classes.length);
        } catch (RuntimeException | StackOverflowError e) {
            return ReviewResult.failed(String.valueOf(e));
        }
    }
}