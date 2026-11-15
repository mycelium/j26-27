package org.example;

public record ReviewResult(Sentiment sentiment, int net, int sentences, String error) {

    public static ReviewResult ok(Sentiment s, int net, int sentences) {
        return new ReviewResult(s, net, sentences, null);
    }

    public static ReviewResult failed(String error) {
        return new ReviewResult(null, 0, 0, error);
    }
}