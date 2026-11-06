package ru.khramov.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.khramov.aggregate.Sentiment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CsvReviewReaderTest {
	private final CsvReviewReader reader = new CsvReviewReader();

	private Path write(Path dir, String content) throws IOException {
		return Files.writeString(dir.resolve("test.csv"), content);
	}

	@Test
	void skipsHeaderAndReadsLabels(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\nGood movie,positive\nBad movie,negative\n");

		List<Review> result = reader.read(file, 10);

		assertEquals(List.of(
				new Review("Good movie", Sentiment.POSITIVE),
				new Review("Bad movie", Sentiment.NEGATIVE)), result
		);
	}

	@Test
	void keepsCommaInsideQuotes(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\n\"Good, very good\",positive\n");

		assertEquals("Good, very good", reader.read(file, 10).getFirst().text());
	}

	@Test
	void unescapesDoubledQuotes(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\n\"He said \"\"wow\"\"\",positive\n");

		assertEquals("He said \"wow\"", reader.read(file, 10).getFirst().text());
	}

	@Test
	void cleansHtmlTags(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\na<br /><br />b,negative\n");

		assertEquals("a b", reader.read(file, 10).getFirst().text());
	}

	@Test
	void respectsLimit(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\nA,positive\nB,positive\nC,negative\n");

		assertEquals(2, reader.read(file, 2).size());
	}

	@Test
	void emptyFileBodyGivesNoReviews(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\n");

		assertEquals(List.of(), reader.read(file, 10));
	}

	@Test
	void zeroLimitGivesNoReviews(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\nA,positive\n");

		assertEquals(List.of(), reader.read(file, 0));
	}

	@Test
	void keepsNewlineInsideQuotesAsSpace(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\n\"line1\nline2\",positive\n");

		assertEquals("line1 line2", reader.read(file, 10).getFirst().text());
	}

	@Test
	void readsNonAsciiText(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\nCafé — très bien,positive\n");

		assertEquals("Café — très bien", reader.read(file, 10).getFirst().text());
	}

	@Test
	void unknownLabelThrows(@TempDir Path dir) throws IOException {
		Path file = write(dir, "review,sentiment\nA,unknown\n");

		assertThrows(IllegalArgumentException.class, () -> reader.read(file, 10));
	}
}
