package ru.khramov.score;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SentenceScorerTest {
	private static final SentenceScorer scorer = new SentenceScorer();

	@Test
	void splitsTextIntoSentences() {
		List<ScoredSentence> result = scorer.score("This movie was great. The ending was awful.");
		assertEquals(2, result.size());
	}

	@Test
	void positiveSentenceGivesHighScore() {
		List<ScoredSentence> result = scorer.score("This movie was great.");
		assertTrue(result.getFirst().score() >= 3);
	}
}
