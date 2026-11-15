package org.example;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Properties;


public final class CoreNlpScorer implements SentenceScorer {
    private static final int NEUTRAL_CLASS = 2;

    private final StanfordCoreNLP pipeline;

    public CoreNlpScorer() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,parse,sentiment");
        props.setProperty("parse.binaryTrees", "true");
        props.setProperty("parse.maxlen", "100");
        this.pipeline = new StanfordCoreNLP(props);
    }

    @Override
    public int[] score(String text) {
        Annotation doc = new Annotation(text);
        pipeline.annotate(doc);
        List<CoreMap> sentences = doc.get(CoreAnnotations.SentencesAnnotation.class);
        if (sentences == null) return new int[0];
        int[] classes = new int[sentences.size()];
        for (int i = 0; i < classes.length; i++) {
            Tree tree = sentences.get(i).get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            classes[i] = tree == null ? NEUTRAL_CLASS : RNNCoreAnnotations.getPredictedClass(tree);
        }
        return classes;
    }
}
