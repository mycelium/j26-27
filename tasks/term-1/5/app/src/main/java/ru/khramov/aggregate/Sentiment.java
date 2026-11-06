package ru.khramov.aggregate;

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
}
