package ru.khramov.aggregate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SentimentTest {
	@ParameterizedTest
	@CsvSource({
			"0, NEGATIVE",
			"1, NEGATIVE",
			"2, NEUTRAL",
			"3, POSITIVE",
			"4, POSITIVE",
			"1.4, NEGATIVE",
			"1.5, NEUTRAL",
			"2.4, NEUTRAL",
			"2.5, POSITIVE"
	})
	void fromScore(double score, Sentiment sentiment) {
		assertEquals(sentiment, Sentiment.fromScore(score));
	}

	@ParameterizedTest
	@CsvSource({
			"negative, NEGATIVE",
			"NEGATIVE, NEGATIVE",
			"neutral, NEUTRAL",
			"NEUTRAL, NEUTRAL",
			"positive, POSITIVE",
			"POSITIVE, POSITIVE",
	})
	void fromLabel(String label, Sentiment sentiment) {
		assertEquals(sentiment, Sentiment.fromLabel(label));
	}
}
