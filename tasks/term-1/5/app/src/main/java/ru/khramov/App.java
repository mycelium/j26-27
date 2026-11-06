package ru.khramov;

import ru.khramov.aggregate.*;
import ru.khramov.data.CsvReviewReader;
import ru.khramov.data.Review;
import ru.khramov.evaluate.AggregatorResult;
import ru.khramov.evaluate.Evaluator;
import ru.khramov.evaluate.ResultTable;
import ru.khramov.score.SentenceScorer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class App {
	private static final String DEFAULT_FILE = "IMDB Dataset.csv";
	private static final int DEFAULT_LIMIT = 100;
	private static final String DEFAULT_OUTPUT = "results.txt";

	public static void main(String[] args) throws IOException {
		Path file = args.length > 0 ? Path.of(args[0]) : findDefaultFile();
		int limit = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_LIMIT;
		Path output = Path.of(args.length > 2 ? args[2] : DEFAULT_OUTPUT);

		List<Review> reviews = new CsvReviewReader().read(file, limit);
		System.out.printf("Read %d reviews from %s%n", reviews.size(), file);

		SentenceScorer scorer = new SentenceScorer();
		List<Aggregator> aggregators = List.of(
				new ArithmeticMeanAggregator(),
				new VotingAggregator(),
				new LengthWeightedAggregator(),
				new LastSentenceAggregator()
		);
		Evaluator evaluator = new Evaluator(scorer::score, aggregators);

		long start = System.nanoTime();
		List<AggregatorResult> results = evaluator.evaluate(reviews);
		double seconds = (System.nanoTime() - start) / 1e9;

		String report = String.format(Locale.US, "Reviews: %d (%s)%n%n%s%nEvaluated in %.1f s%n",
				reviews.size(), file.getFileName(), new ResultTable().format(results), seconds);
		System.out.print(report);
		Files.writeString(output, report);
		System.out.println("Saved to " + output.toAbsolutePath());
	}

	private static Path findDefaultFile() {
		Path local = Path.of(DEFAULT_FILE);
		return Files.exists(local) ? local : Path.of("..", DEFAULT_FILE);
	}
}
