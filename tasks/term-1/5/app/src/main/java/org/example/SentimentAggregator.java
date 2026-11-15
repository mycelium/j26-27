package org.example;

public final class SentimentAggregator {
    private SentimentAggregator() {}

    public static int net(int[] sentenceClasses) {
        int net = 0;
        for (int c : sentenceClasses) net += c - 2;
        return net;
    }

    public static Sentiment aggregate(int[] sentenceClasses, double threshold) {
        int net = net(sentenceClasses);
        if (net > threshold) return Sentiment.POSITIVE;
        if (net < -threshold) return Sentiment.NEGATIVE;
        return Sentiment.NEUTRAL;
    }
}