package ru.khramov.aggregate;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
			"Positive, POSITIVE"
	})
	void fromLabel(String label, Sentiment sentiment) {
		assertEquals(sentiment, Sentiment.fromLabel(label));
	}

	@ParameterizedTest
	@ValueSource(doubles = {-0.1, 4.1, Double.NaN})
	void fromScoreOutOfRangeThrows(double score) {
		assertThrows(IllegalArgumentException.class, () -> Sentiment.fromScore(score));
	}

	@Test
	void fromScoreAcceptsRangeBounds() {
		assertEquals(Sentiment.NEGATIVE, Sentiment.fromScore(0));
		assertEquals(Sentiment.POSITIVE, Sentiment.fromScore(4));
	}

	@Test
	void fromLabelUnknownThrows() {
		assertThrows(IllegalArgumentException.class, () -> Sentiment.fromLabel("unknown"));
	}
}
