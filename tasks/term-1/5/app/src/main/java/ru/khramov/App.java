package ru.khramov;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

public class App {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

		CoreDocument document = new CoreDocument("This movie was great. The ending was awful.");
		pipeline.annotate(document);
		for (CoreSentence sentence : document.sentences()) {
			System.out.println(sentence.sentiment() + ": " + sentence.text());
		}
	}
}
