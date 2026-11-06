package ru.khramov.aggregate;

import org.junit.jupiter.api.Test;
import ru.khramov.score.ScoredSentence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LengthWeightedAggregatorTest {
	private final LengthWeightedAggregator aggregator = new LengthWeightedAggregator();

	@Test
	void positiveSentencesGivePositive() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(3, 7)
		);

		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(input));
	}

	@Test
	void negativeSentencesGiveNegative() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(0, 5),
				new ScoredSentence(1, 7)
		);

		assertEquals(Sentiment.NEGATIVE, aggregator.aggregate(input));
	}

	@Test
	void equalLengthsBehaveLikeArithmeticMean() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(0, 5)
		);

		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(input));
	}

	@Test
	void longNegativeSentenceOutweighsShortPositiveOnes() {
		// weighted: (4*2 + 1*10 + 0*3) / 15 = 1.2 -> NEGATIVE, plain mean: 1.67 -> NEUTRAL
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 2),
				new ScoredSentence(1, 10),
				new ScoredSentence(0, 3)
		);

		assertEquals(Sentiment.NEGATIVE, aggregator.aggregate(input));
		assertNotEquals(new ArithmeticMeanAggregator().aggregate(input), aggregator.aggregate(input));
	}

	@Test
	void longPositiveSentenceOutweighsShortNegativeOnes() {
		// weighted: (0*2 + 1*3 + 4*10) / 15 = 2.87 -> POSITIVE, plain mean: 1.67 -> NEUTRAL
		List<ScoredSentence> input = List.of(
				new ScoredSentence(0, 2),
				new ScoredSentence(1, 3),
				new ScoredSentence(4, 10)
		);

		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(input));
		assertNotEquals(new ArithmeticMeanAggregator().aggregate(input), aggregator.aggregate(input));
	}

	@Test
	void resultIsOnBoundaryOfNeutralAndPositive() {
		// (4*1 + 1*1) / 2 = 2.5 -> POSITIVE
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 1),
				new ScoredSentence(1, 1)
		);

		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(input));
	}

	@Test
	void singleSentenceGivesItsOwnSentiment() {
		assertEquals(Sentiment.NEGATIVE, aggregator.aggregate(List.of(new ScoredSentence(0, 12))));
		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(List.of(new ScoredSentence(2, 12))));
		assertEquals(Sentiment.POSITIVE, aggregator.aggregate(List.of(new ScoredSentence(4, 12))));
	}

	@Test
	void scalingAllLengthsDoesNotChangeResult() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 2),
				new ScoredSentence(1, 10)
		);
		List<ScoredSentence> scaled = List.of(
				new ScoredSentence(4, 20),
				new ScoredSentence(1, 100)
		);

		assertEquals(aggregator.aggregate(input), aggregator.aggregate(scaled));
	}

	@Test
	void emptySentencesGiveNeutral() {
		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(List.of()));
	}

	@Test
	void nullSentencesGiveNeutral() {
		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(null));
	}

	@Test
	void zeroTotalLengthGivesNeutral() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 0),
				new ScoredSentence(0, 0)
		);

		assertEquals(Sentiment.NEUTRAL, aggregator.aggregate(input));
	}

	@Test
	void zeroLengthSentenceIsIgnored() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 0),
				new ScoredSentence(0, 5)
		);

		assertEquals(Sentiment.NEGATIVE, aggregator.aggregate(input));
	}
}
