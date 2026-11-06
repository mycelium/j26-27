package ru.khramov.aggregate;

import ru.khramov.score.ScoredSentence;

import java.util.List;

public interface Aggregator {
	Sentiment aggregate(List<ScoredSentence> sentences);

	String getName();
}
