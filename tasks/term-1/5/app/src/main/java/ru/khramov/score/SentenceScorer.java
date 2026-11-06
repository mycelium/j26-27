package ru.khramov.score;

import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SentenceScorer {
	private final StanfordCoreNLP pipeline;

	public SentenceScorer() {
		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		this.pipeline = new StanfordCoreNLP(properties);
	}

	public List<ScoredSentence> score(String text) {
		List<ScoredSentence> result = new ArrayList<>();
		CoreDocument coreDocument = new CoreDocument(text);
		pipeline.annotate(coreDocument);

		for (CoreSentence sentence : coreDocument.sentences()) {
			Tree tree = sentence.sentimentTree();
			int score = RNNCoreAnnotations.getPredictedClass(tree);
			int length = sentence.tokens().size();
			result.add(new ScoredSentence(score, length));
		}
		return result;
	}
}
