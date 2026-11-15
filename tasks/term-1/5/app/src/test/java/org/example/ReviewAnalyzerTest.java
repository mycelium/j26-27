package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReviewAnalyzerTest {
    private static ReviewAnalyzer withClasses(int... classes) {
        return new ReviewAnalyzer(text -> classes, 0);
    }

    @Test void positiveSentencesGivePositive() {
        assertEquals(Sentiment.POSITIVE, withClasses(3, 2, 4).analyze("x").sentiment());
    }

    @Test void negativeSentencesGiveNegative() {
        assertEquals(Sentiment.NEGATIVE, withClasses(1, 0, 2).analyze("x").sentiment());
    }

    @Test void onlyNeutralSentencesGiveNeutral() {
        assertEquals(Sentiment.NEUTRAL, withClasses(2, 2, 2).analyze("x").sentiment());
    }

    @Test void balancedOpinionsGiveNeutral() {
        assertEquals(Sentiment.NEUTRAL, withClasses(3, 1).analyze("x").sentiment());
    }

    @Test void emptyTextGivesNeutral() {
        assertEquals(Sentiment.NEUTRAL, withClasses().analyze("").sentiment());
    }

    @Test void thresholdWidensNeutralZone() {
        ReviewAnalyzer a = new ReviewAnalyzer(t -> new int[]{3, 2, 2}, 1);
        assertEquals(Sentiment.NEUTRAL, a.analyze("x").sentiment());
    }

    @Test void scorerFailureIsReportedNotThrown() {
        ReviewAnalyzer a = new ReviewAnalyzer(t -> { throw new IllegalStateException("boom"); }, 0);
        ReviewResult r = a.analyze("x");
        assertNull(r.sentiment());
        assertTrue(r.error().contains("boom"));
    }
}
