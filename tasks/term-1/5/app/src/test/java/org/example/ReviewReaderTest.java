package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReviewReaderTest {
    @Test void readsReviewAndLabelColumnsAndCleansHtml() {
        List<String[]> rows = List.of(
                new String[]{"review", "sentiment"},
                new String[]{"Good.<br /><br />Really good.", "positive"},
                new String[]{"Bad &amp; boring", "negative"});
        List<Review> reviews = ReviewReader.fromRows(rows, null, 0);
        assertEquals(2, reviews.size());
        assertEquals("Good. Really good.", reviews.get(0).text());
        assertEquals(Sentiment.POSITIVE, reviews.get(0).expected());
        assertEquals("Bad & boring", reviews.get(1).text());
    }

    @Test void respectsLimit() {
        List<String[]> rows = List.of(new String[]{"review"}, new String[]{"a"}, new String[]{"b"}, new String[]{"c"});
        assertEquals(2, ReviewReader.fromRows(rows, null, 2).size());
    }

    @Test void unknownColumnGivesHelpfulError() {
        List<String[]> rows = List.of(new String[]{"foo", "bar"}, new String[]{"x", "y"});
        assertThrows(IllegalArgumentException.class, () -> ReviewReader.fromRows(rows, null, 0));
    }
}
