package ru.khramov.aggregate;

import ru.khramov.score.ScoredSentence;

import java.util.List;

public class LastSentenceAggregator implements Aggregator {
	@Override
	public Sentiment aggregate(List<ScoredSentence> sentences) {
		if (sentences == null || sentences.isEmpty()) {
			return Sentiment.NEUTRAL;
		}

		return Sentiment.fromScore(sentences.getLast().score());
	}

	@Override
	public String getName() {
		return "LastSentence";
	}
}
