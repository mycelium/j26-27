package ru.khramov.aggregate;

import ru.khramov.score.ScoredSentence;

import java.util.List;

public class VotingAggregator implements Aggregator {
	private static Sentiment findMax(int negativeCount, int neutralCount, int positiveCount) {
		if (negativeCount > neutralCount && negativeCount > positiveCount) {
			return Sentiment.NEGATIVE;
		}

		if (positiveCount > neutralCount && positiveCount > negativeCount) {
			return Sentiment.POSITIVE;
		}

		return Sentiment.NEUTRAL;
	}

	@Override
	public Sentiment aggregate(List<ScoredSentence> sentences) {
		if (sentences == null || sentences.isEmpty()) {
			return Sentiment.NEUTRAL;
		}

		int negativeCount = 0;
		int neutralCount = 0;
		int positiveCount = 0;

		for (ScoredSentence sentence : sentences) {
			switch (Sentiment.fromScore(sentence.score())) {
				case NEGATIVE -> negativeCount++;
				case NEUTRAL -> neutralCount++;
				case POSITIVE -> positiveCount++;
			}
		}

		return findMax(negativeCount, neutralCount, positiveCount);
	}

	@Override
	public String getName() {
		return "Voting";
	}

}

