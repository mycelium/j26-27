package ru.khramov.aggregate;

import ru.khramov.score.ScoredSentence;

import java.util.List;

public class LengthWeightedAggregator implements Aggregator {
	@Override
	public Sentiment aggregate(List<ScoredSentence> sentences) {
		if (sentences == null || sentences.isEmpty()) {
			return Sentiment.NEUTRAL;
		}

		double weightedSum = 0;
		double sumWeight = 0;
		for (ScoredSentence sentence : sentences) {
			weightedSum += sentence.score() * sentence.length();
//			weightedSum += sentence.score() * log(1 + sentence.length());

			sumWeight += sentence.length();
//			sumWeight += log(1 + sentence.length());
		}

		if (sumWeight == 0) {
			return Sentiment.NEUTRAL;
		}

		return Sentiment.fromScore(weightedSum / sumWeight);
	}

	@Override
	public String getName() {
		return "LengthWeighted";
	}

}
