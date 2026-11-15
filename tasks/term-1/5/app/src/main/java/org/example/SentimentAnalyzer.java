package org.example;

import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations.SentimentAnnotatedTree;
import edu.stanford.nlp.util.CoreMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

final class SentimentAnalyzer {
    private static final Pattern HTML_BREAK = Pattern.compile("<br\\s*/?\\s*>", Pattern.CASE_INSENSITIVE);
    private final StanfordCoreNLP pipeline;

    SentimentAnalyzer() {
        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize,pos,parse,sentiment");
        properties.setProperty("ssplit.newlineIsSentenceBreak", "two");
        properties.setProperty("parse.model", "edu/stanford/nlp/models/srparser/englishSR.ser.gz");
        properties.setProperty("parse.binaryTrees", "true");
        properties.setProperty("parse.buildgraphs", "false");
        pipeline = new StanfordCoreNLP(properties);
    }

    Sentiment classify(String review) {
        String text = HTML_BREAK.matcher(review).replaceAll("\n");
        if (text.isBlank()) {
            return Sentiment.NEUTRAL;
        }

        Annotation document = pipeline.process(text);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
        double average = sentences.stream()
                .mapToInt(sentence -> RNNCoreAnnotations.getPredictedClass(sentence.get(SentimentAnnotatedTree.class)) - 2)
                .average().orElse(0.0);

        if (average >= 0.5) {
            return Sentiment.POSITIVE;
        }
        if (average <= -0.5) {
            return Sentiment.NEGATIVE;
        }
        return Sentiment.NEUTRAL;
    }

    enum Sentiment {
        POSITIVE, NEGATIVE, NEUTRAL;

        String label() {
            return name().toLowerCase(Locale.ROOT);
        }
    }
}
