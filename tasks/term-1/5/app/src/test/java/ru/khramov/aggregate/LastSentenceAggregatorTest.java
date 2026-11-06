package ru.khramov.aggregate;

import org.junit.jupiter.api.Test;
import ru.khramov.score.ScoredSentence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LastSentenceAggregatorTest {
	private final LastSentenceAggregator aggregator = new LastSentenceAggregator();

	@Test
	void lastNegativeSentenceGivesNegative() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(4, 7),
				new ScoredSentence(0, 6)
		);

		assertEquals(Sentiment.NEGATIVE, aggregator.aggregate(input));
	}

	@Test
	void lastPositiveSentenceGivesPositive() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(0, 5),
				new ScoredSentence(0, 7),
				new ScoredSentence(4, 6)
		);

		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(input));
	}

	@Test
	void lastNeutralSentenceGivesNeutral() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(0, 7),
				new ScoredSentence(2, 6)
		);

		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(input));
	}

	@Test
	void mildScoresMapToSameClassAsStrongOnes() {
		assertEquals(Sentiment.NEGATIVE, aggregator.aggregate(List.of(new ScoredSentence(1, 5))));
		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(List.of(new ScoredSentence(3, 5))));
	}

	@Test
	void singleSentenceGivesItsOwnSentiment() {
		assertEquals(Sentiment.NEGATIVE, aggregator.aggregate(List.of(new ScoredSentence(0, 5))));
		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(List.of(new ScoredSentence(2, 5))));
		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(List.of(new ScoredSentence(4, 5))));
	}

	@Test
	void orderMatters() {
		List<ScoredSentence> positiveLast = List.of(
				new ScoredSentence(0, 5),
				new ScoredSentence(4, 5)
		);
		List<ScoredSentence> negativeLast = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(0, 5)
		);

		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(positiveLast));
		assertEquals(Sentiment.NEGATIVE, aggregator.aggregate(negativeLast));
	}

	@Test
	void lengthDoesNotAffectResult() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(0, 100),
				new ScoredSentence(4, 1)
		);

		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(input));
	}

	@Test
	void emptySentencesGiveNeutral() {
		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(List.of()));
	}

	@Test
	void nullSentencesGiveNeutral() {
		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(null));
	}
}
