package ru.khramov.aggregate;

import ru.khramov.score.ScoredSentence;

import java.util.List;

public class ArithmeticMeanAggregator implements Aggregator {
	@Override
	public Sentiment aggregate(List<ScoredSentence> sentences) {
		if (sentences == null || sentences.isEmpty()) {
			return Sentiment.NEUTRAL;
		}

		double sumScore = 0;
		for (ScoredSentence sentence : sentences) {
			sumScore += sentence.score();
		}
		return Sentiment.fromScore(sumScore / sentences.size());
	}

	@Override
	public String getName() {
		return "ArithmeticMean";
	}
}
