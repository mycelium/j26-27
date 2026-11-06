package ru.khramov.aggregate;

import org.junit.jupiter.api.Test;
import ru.khramov.score.ScoredSentence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VotingAggregatorTest {
	private final VotingAggregator aggregator = new VotingAggregator();

	@Test
	public void positiveSentencesGivePositive() {
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
	void neutralSentencesGiveNeutral() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(2, 5),
				new ScoredSentence(2, 7)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEUTRAL, sentiment);
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
	void majorityPositiveBeatsMinorityNegative() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(3, 5),
				new ScoredSentence(4, 5),
				new ScoredSentence(0, 5),
				new ScoredSentence(0, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.POSITIVE, sentiment);
	}

	@Test
	void majorityNegativeBeatsMinorityPositive() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(3, 5),
				new ScoredSentence(0, 5),
				new ScoredSentence(0, 5),
				new ScoredSentence(0, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEGATIVE, sentiment);
	}

	@Test
	void positiveAndNeutralSentencesGivePositive() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(3, 5),
				new ScoredSentence(2, 7)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.POSITIVE, sentiment);
	}

	@Test
	void negativeAndNeutralSentencesGiveNegative() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(0, 5),
				new ScoredSentence(1, 5),
				new ScoredSentence(2, 7)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEGATIVE, sentiment);
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

	@Test
	void neutralMajorityBeatsEmotionalMinority() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(2, 5),
				new ScoredSentence(2, 5),
				new ScoredSentence(4, 5),
				new ScoredSentence(0, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEUTRAL, sentiment);
	}

	@Test
	void tieBetweenPositiveAndNegativeGivesNeutralEvenWithFewerNeutral() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 5),
				new ScoredSentence(0, 5),
				new ScoredSentence(2, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEUTRAL, sentiment);
	}

	@Test
	void tieBetweenPositiveAndNeutralGivesNeutral() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(3, 5),
				new ScoredSentence(2, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEUTRAL, sentiment);
	}

	@Test
	void tieBetweenNegativeAndNeutralGivesNeutral() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(1, 5),
				new ScoredSentence(2, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEUTRAL, sentiment);
	}

	@Test
	void mildAndStrongScoresAreCountedAsSameClass() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(0, 5),
				new ScoredSentence(1, 5),
				new ScoredSentence(3, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEGATIVE, sentiment);
	}

	@Test
	void sentenceLengthDoesNotAffectVote() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(4, 100),
				new ScoredSentence(0, 1),
				new ScoredSentence(0, 1)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEGATIVE, sentiment);
	}

	@Test
	void singleNegativeSentenceGivesNegative() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(0, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEGATIVE, sentiment);
	}

	@Test
	void singleNeutralSentenceGivesNeutral() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(2, 5)
		);

		Sentiment sentiment = aggregator.aggregate(input);
		assertEquals(Sentiment.NEUTRAL, sentiment);
	}

	@Test
	void scoreOutOfRangeThrows() {
		List<ScoredSentence> input = List.of(
				new ScoredSentence(5, 5)
		);

		assertThrows(IllegalArgumentException.class, () -> aggregator.aggregate(input));
	}
}
