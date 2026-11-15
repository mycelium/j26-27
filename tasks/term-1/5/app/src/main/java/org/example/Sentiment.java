package org.example;

import java.util.Locale;

public enum Sentiment {
    POSITIVE, NEGATIVE, NEUTRAL;

    public String label() {
        return name().toLowerCase(Locale.ROOT);
    }

    public static Sentiment fromLabel(String s) {
        if (s == null) return null;
        return switch (s.trim().toLowerCase(Locale.ROOT)) {
            case "positive", "pos" -> POSITIVE;
            case "negative", "neg" -> NEGATIVE;
            case "neutral" -> NEUTRAL;
            default -> null;
        };
    }
}

