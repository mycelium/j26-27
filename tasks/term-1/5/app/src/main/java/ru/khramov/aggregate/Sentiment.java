package ru.khramov.aggregate;

import java.util.Locale;

public enum Sentiment {
	NEGATIVE,
	NEUTRAL,
	POSITIVE;

	public static Sentiment fromScore(double score) {
		if (score >= 0 && score < 1.5) {
			return NEGATIVE;
		} else if (score >= 1.5 && score < 2.5) {
			return NEUTRAL;
		} else if (score >= 2.5 && score <= 4) {
			return POSITIVE;
		} else {
			throw new IllegalArgumentException("score out of range: " + score);
		}
	}

	public static Sentiment fromLabel(String label) {
		return switch (label.toLowerCase(Locale.ROOT)) {
			case "negative" -> NEGATIVE;
			case "positive" -> POSITIVE;
			case "neutral" -> NEUTRAL;
			default -> throw new IllegalArgumentException("unknow label: " + label);
		};
	}
}
