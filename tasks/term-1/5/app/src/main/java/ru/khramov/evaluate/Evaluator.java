package ru.khramov.evaluate;

import ru.khramov.aggregate.Aggregator;
import ru.khramov.aggregate.Sentiment;
import ru.khramov.data.Review;
import ru.khramov.score.ScoredSentence;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Evaluator {
	private final Function<String, List<ScoredSentence>> scorer;
	private final List<Aggregator> aggregators;

	public Evaluator(Function<String, List<ScoredSentence>> scorer, List<Aggregator> aggregators) {
		this.scorer = scorer;
		this.aggregators = List.copyOf(aggregators);
	}

	public List<AggregatorResult> evaluate(List<Review> reviews) {
		int[] correct = new int[aggregators.size()];
		int[] neutral = new int[aggregators.size()];

		for (Review review : reviews) {
			List<ScoredSentence> scored = scorer.apply(review.text());
			for (int i = 0; i < aggregators.size(); i++) {
				Sentiment predicted = aggregators.get(i).aggregate(scored);
				if (predicted == review.expected()) {
					correct[i]++;
				}
				if (predicted == Sentiment.NEUTRAL) {
					neutral[i]++;
				}
			}
		}

		List<AggregatorResult> results = new ArrayList<>();
		for (int i = 0; i < aggregators.size(); i++) {
			results.add(new AggregatorResult(aggregators.get(i).getName(), reviews.size(), correct[i], neutral[i]));
		}
		return results;
	}
}
