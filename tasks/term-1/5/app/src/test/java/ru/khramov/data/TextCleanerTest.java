package ru.khramov.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextCleanerTest {
	private final TextCleaner cleaner = new TextCleaner();

	@Test
	void replacesBrTagsWithSpace() {
		assertEquals("a b", cleaner.clean("a<br /><br />b"));
	}

	@Test
	void handlesBrWithoutSlash() {
		assertEquals("a b", cleaner.clean("a<br>b"));
	}

	@Test
	void leavesPlainTextUnchanged() {
		assertEquals("Just text.", cleaner.clean("Just text."));
	}
}
