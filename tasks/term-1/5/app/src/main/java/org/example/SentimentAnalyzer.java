package org.example;

import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SentimentAnalyzer {
    private final StanfordCoreNLP pipeline;

    public SentimentAnalyzer() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");
        this.pipeline = new StanfordCoreNLP(props);   // дорого: создаём один раз
    }

    /** Оценки всех предложений отзыва: от 0 (very negative) до 4 (very positive). */
    public List<Integer> sentenceScores(String text) {
        CoreDocument doc = new CoreDocument(text);
        pipeline.annotate(doc);
        List<Integer> scores = new ArrayList<>();
        for (CoreSentence s : doc.sentences()) {
            Tree tree = s.coreMap().get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            scores.add(RNNCoreAnnotations.getPredictedClass(tree));
        }
        return scores;
    }
}
