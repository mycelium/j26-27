package org.example;

import java.util.Locale;

public enum Sentiment {
    POSITIVE, NEGATIVE, NEUTRAL;

    public static Sentiment fromLabel(String label) {
        return valueOf(label.trim().toUpperCase(Locale.ROOT));
    }
}