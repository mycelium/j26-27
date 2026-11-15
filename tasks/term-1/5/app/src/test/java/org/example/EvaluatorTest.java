package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {
    @Test void computesBothAccuracies() {
        Evaluator e = new Evaluator();
        e.add(Sentiment.POSITIVE, Sentiment.POSITIVE);
        e.add(Sentiment.POSITIVE, Sentiment.NEGATIVE);
        e.add(Sentiment.NEGATIVE, Sentiment.NEGATIVE);
        e.add(Sentiment.NEGATIVE, Sentiment.NEUTRAL);
        e.add(null, Sentiment.POSITIVE); // без эталона - игнорируется
        assertEquals(4, e.total());
        assertEquals(0.5, e.accuracyAll(), 1e-9);
        assertEquals(2.0 / 3.0, e.accuracyDecisive(), 1e-9);
    }
}
