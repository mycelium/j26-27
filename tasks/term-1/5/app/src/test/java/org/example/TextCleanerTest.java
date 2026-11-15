package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextCleanerTest {
    @Test void stripsTagsAndNormalizesWhitespace() {
        assertEquals("One Two", TextCleaner.clean("One<br /><br />\n Two"));
    }

    @Test void keepsPlainLessThanSigns() {
        assertEquals("rated 2 < 3 stars", TextCleaner.clean("rated 2 < 3 stars"));
    }
}
