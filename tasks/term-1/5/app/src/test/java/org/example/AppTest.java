package org.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class AppTest {
    @TempDir
    Path directory;

    @Test
    void printsUsageWithoutAnExistingDataset() {
        assertUsageError(new String[0], "provide one dataset filename");
        assertUsageError(new String[]{directory.resolve("missing.csv").toString()}, "dataset file not found");
    }

    @Test
    void createsACsvReportAndPrintsTheSummary() throws Exception {
        Path input = directory.resolve("movie reviews.csv");
        String positiveReview = "I loved this movie!<br /><br />\"It was wonderful, funny and moving!\"\n";
        try (CSVPrinter csv = CSVFormat.DEFAULT.builder().setHeader("review", "sentiment").get()
                .print(Files.newBufferedWriter(input, StandardCharsets.UTF_8))) {
            csv.printRecord(positiveReview, "positive");
            csv.printRecord("This was a terrible movie. I hated it.", "negative");
            csv.printRecord("The film was released in 2001.", "positive");
        }

        ByteArrayOutputStream stdout = new ByteArrayOutputStream();
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        int exitCode = App.run(new String[]{input.toString()}, new PrintStream(stdout), new PrintStream(stderr));

        assertEquals(0, exitCode, stderr.toString());
        Path report = directory.resolve("movie reviews-sentiments.csv");
        try (CSVParser csv = CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).get()
                .parse(Files.newBufferedReader(report, StandardCharsets.UTF_8))) {
            assertEquals(List.of("id", "review", "original_sentiment", "predicted_sentiment"), csv.getHeaderNames());
            List<CSVRecord> reviews = csv.getRecords();
            assertEquals(3, reviews.size());
            assertEquals("1", reviews.get(0).get("id"));
            assertEquals(positiveReview, reviews.get(0).get("review"));
            assertEquals("positive", reviews.get(0).get("predicted_sentiment"));
            assertEquals("negative", reviews.get(1).get("predicted_sentiment"));
            assertEquals("positive", reviews.get(2).get("original_sentiment"));
            assertEquals("neutral", reviews.get(2).get("predicted_sentiment"));
        }
        String output = stdout.toString();
        assertTrue(output.contains("Processed 3 reviews."));
        assertTrue(output.contains("Matches with original labels: 2 / 3 (66.7%)"));
        assertTrue(output.contains("Generated " + report));
    }

    @Test
    void rejectsAnInvalidHeaderWithoutCreatingAReport() throws Exception {
        Path input = directory.resolve("invalid.csv");
        Files.writeString(input, "text,sentiment\nGreat film.,positive\n");
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        int exitCode = App.run(new String[]{input.toString()}, new PrintStream(new ByteArrayOutputStream()),
                new PrintStream(stderr));

        assertEquals(1, exitCode);
        assertTrue(stderr.toString().contains("must contain the 'review' and 'sentiment' columns"));
        assertFalse(Files.exists(directory.resolve("invalid-sentiments.csv")));
    }

    private static void assertUsageError(String[] args, String message) {
        ByteArrayOutputStream stderr = new ByteArrayOutputStream();
        int exitCode = App.run(args, new PrintStream(new ByteArrayOutputStream()), new PrintStream(stderr));

        assertEquals(2, exitCode);
        assertTrue(stderr.toString().contains(message));
        assertTrue(stderr.toString().contains("Usage: sentiments <dataset.csv>"));
    }
}
