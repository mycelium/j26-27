package ru.khramov;

import ru.khramov.score.ScoredSentence;
import ru.khramov.score.SentenceScorer;

import java.util.List;

public class App {

	public static void main(String[] args) {
		SentenceScorer sentenceScorer = new SentenceScorer();
		List<ScoredSentence> scored = sentenceScorer.score("This movie was great. The ending was awful.");
		for (ScoredSentence sentence : scored) {
			System.out.println(sentence);
		}
	}
}
