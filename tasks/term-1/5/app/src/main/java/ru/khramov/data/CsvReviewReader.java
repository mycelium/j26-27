package ru.khramov.data;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import ru.khramov.aggregate.Sentiment;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvReviewReader {
	private static final CSVFormat FORMAT = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build();

	private final TextCleaner cleaner = new TextCleaner();

	public List<Review> read(Path file, int limit) throws IOException {
		List<Review> reviews = new ArrayList<>();
		try (Reader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8);
			 CSVParser parser = FORMAT.parse(reader)) {
			for (CSVRecord record : parser) {
				if (reviews.size() >= limit) {
					break;
				}
				String text = cleaner.clean(record.get("review"));
				Sentiment expected = Sentiment.fromLabel(record.get("sentiment"));
				reviews.add(new Review(text, expected));
			}
		}
		return reviews;
	}
}
