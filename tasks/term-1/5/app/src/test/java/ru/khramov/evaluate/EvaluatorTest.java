package ru.khramov.evaluate;

import org.junit.jupiter.api.Test;
import ru.khramov.aggregate.Aggregator;
import ru.khramov.aggregate.Sentiment;
import ru.khramov.data.Review;
import ru.khramov.score.ScoredSentence;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EvaluatorTest {
	private static final List<ScoredSentence> ANY = List.of(new ScoredSentence(2, 5));

	private static Aggregator constant(String name, Sentiment answer) {
		return new Aggregator() {
			@Override
			public Sentiment aggregate(List<ScoredSentence> sentences) {
				return answer;
			}

			@Override
			public String getName() {
				return name;
			}
		};
	}

	private static Function<String, List<ScoredSentence>> anyScorer() {
		return text -> ANY;
	}

	@Test
	void allCorrect() {
		Evaluator evaluator = new Evaluator(anyScorer(), List.of(constant("Pos", Sentiment.POSITIVE)));
		List<Review> reviews = List.of(
				new Review("a", Sentiment.POSITIVE),
				new Review("b", Sentiment.POSITIVE)
		);

		AggregatorResult result = evaluator.evaluate(reviews).getFirst();

		assertEquals(new AggregatorResult("Pos", 2, 2, 0), result);
	}

	@Test
	void allWrong() {
		Evaluator evaluator = new Evaluator(anyScorer(), List.of(constant("Neg", Sentiment.NEGATIVE)));
		List<Review> reviews = List.of(
				new Review("a", Sentiment.POSITIVE),
				new Review("b", Sentiment.POSITIVE)
		);

		AggregatorResult result = evaluator.evaluate(reviews).getFirst();

		assertEquals(new AggregatorResult("Neg", 2, 0, 0), result);
	}

	@Test
	void partiallyCorrect() {
		Evaluator evaluator = new Evaluator(anyScorer(), List.of(constant("Pos", Sentiment.POSITIVE)));
		List<Review> reviews = List.of(
				new Review("a", Sentiment.POSITIVE),
				new Review("b", Sentiment.NEGATIVE),
				new Review("c", Sentiment.POSITIVE),
				new Review("d", Sentiment.NEGATIVE)
		);

		AggregatorResult result = evaluator.evaluate(reviews).getFirst();

		assertEquals(new AggregatorResult("Pos", 4, 2, 0), result);
	}

	@Test
	void neutralAnswersAreCountedSeparatelyAndNeverCorrect() {
		Evaluator evaluator = new Evaluator(anyScorer(), List.of(constant("Neu", Sentiment.NEUTRAL)));
		List<Review> reviews = List.of(
				new Review("a", Sentiment.POSITIVE),
				new Review("b", Sentiment.NEGATIVE)
		);

		AggregatorResult result = evaluator.evaluate(reviews).getFirst();

		assertEquals(new AggregatorResult("Neu", 2, 0, 2), result);
	}

	@Test
	void resultsFollowAggregatorOrder() {
		Evaluator evaluator = new Evaluator(anyScorer(), List.of(
				constant("First", Sentiment.POSITIVE),
				constant("Second", Sentiment.NEGATIVE),
				constant("Third", Sentiment.NEUTRAL)
		));
		List<Review> reviews = List.of(new Review("a", Sentiment.POSITIVE));

		List<AggregatorResult> results = evaluator.evaluate(reviews);

		assertEquals(List.of("First", "Second", "Third"), results.stream().map(AggregatorResult::name).toList());
		assertEquals(List.of(1, 0, 0), results.stream().map(AggregatorResult::correct).toList());
		assertEquals(List.of(0, 0, 1), results.stream().map(AggregatorResult::neutral).toList());
	}

	@Test
	void scorerIsCalledOncePerReviewRegardlessOfAggregatorCount() {
		AtomicInteger calls = new AtomicInteger();
		Evaluator evaluator = new Evaluator(
				text -> {
					calls.incrementAndGet();
					return ANY;
				},
				List.of(
						constant("A", Sentiment.POSITIVE),
						constant("B", Sentiment.NEGATIVE),
						constant("C", Sentiment.NEUTRAL)
				));
		List<Review> reviews = List.of(
				new Review("a", Sentiment.POSITIVE),
				new Review("b", Sentiment.NEGATIVE)
		);

		evaluator.evaluate(reviews);

		assertEquals(2, calls.get());
	}

	@Test
	void aggregatorReceivesScoresOfExactlyThatReview() {
		Map<String, List<ScoredSentence>> scores = Map.of(
				"good", List.of(new ScoredSentence(4, 3)),
				"bad", List.of(new ScoredSentence(0, 3))
		);
		Aggregator byFirstScore = new Aggregator() {
			@Override
			public Sentiment aggregate(List<ScoredSentence> sentences) {
				return Sentiment.fromScore(sentences.getFirst().score());
			}

			@Override
			public String getName() {
				return "ByFirstScore";
			}
		};
		Evaluator evaluator = new Evaluator(scores::get, List.of(byFirstScore));
		List<Review> reviews = List.of(
				new Review("good", Sentiment.POSITIVE),
				new Review("bad", Sentiment.NEGATIVE)
		);

		assertEquals(new AggregatorResult("ByFirstScore", 2, 2, 0), evaluator.evaluate(reviews).getFirst());
	}

	@Test
	void emptyReviewsGiveZeroTotals() {
		Evaluator evaluator = new Evaluator(anyScorer(), List.of(constant("A", Sentiment.POSITIVE)));

		assertEquals(new AggregatorResult("A", 0, 0, 0), evaluator.evaluate(List.of()).getFirst());
	}

	@Test
	void noAggregatorsGiveNoResults() {
		Evaluator evaluator = new Evaluator(anyScorer(), List.of());

		assertEquals(List.of(), evaluator.evaluate(List.of(new Review("a", Sentiment.POSITIVE))));
	}

	@Test
	void evaluatingTwiceGivesSameResultsWithoutAccumulating() {
		Evaluator evaluator = new Evaluator(anyScorer(), List.of(constant("A", Sentiment.POSITIVE)));
		List<Review> reviews = List.of(new Review("a", Sentiment.POSITIVE));

		assertEquals(evaluator.evaluate(reviews), evaluator.evaluate(reviews));
	}
}
