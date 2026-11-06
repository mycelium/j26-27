package ru.khramov.aggregate;

import org.junit.jupiter.api.Test;
import ru.khramov.score.ScoredSentence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArithmeticMeanAggregatorTest {
	private final ArithmeticMeanAggregator aggregator = new ArithmeticMeanAggregator();

	@Test
	void positiveSentencesGivePositive() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(3, 7)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.POSITIVE, sentiment);
	}

	@Test
	void negativeSentencesGiveNegative() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(0, 5),
				new ScoredSentence(1, 7)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEGATIVE, sentiment);
	}

	@Test
	void positiveAndNegativeSentencesGiveNeutral() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(0, 7)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEUTRAL, sentiment);
	}

	@Test
	void emptySentencesGiveNeutral() {
		List<ScoredSentence> input = List.of();

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEUTRAL, sentiment);
	}

	@Test
	void nullSentencesGiveNeutral() {
		Sentiment sentiment = aggregator.aggregate(null);
		assertEquals(Sentiment.NEUTRAL, sentiment);
	}

	@Test
	void singlePositiveSentenceGivesPositive() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.POSITIVE, sentiment);
	}
}
