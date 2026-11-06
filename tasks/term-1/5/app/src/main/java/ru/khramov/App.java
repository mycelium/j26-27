package ru.khramov;

import ru.khramov.aggregate.Aggregator;
import ru.khramov.aggregate.ArithmeticMeanAggregator;
import ru.khramov.aggregate.LastSentenceAggregator;
import ru.khramov.aggregate.LengthWeightedAggregator;
import ru.khramov.aggregate.VotingAggregator;
import ru.khramov.data.CsvReviewReader;
import ru.khramov.data.Review;
import ru.khramov.evaluate.AggregatorResult;
import ru.khramov.evaluate.Evaluator;
import ru.khramov.score.SentenceScorer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

public class App {
	private static final String DEFAULT_FILE = "IMDB Dataset.csv";
	private static final int DEFAULT_LIMIT = 100;

	public static void main(String[] args) throws IOException {
		Path file = args.length > 0 ? Path.of(args[0]) : findDefaultFile();
		int limit = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_LIMIT;

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

		printTable(results);
		System.out.printf(Locale.US, "Evaluated in %.1f s%n", seconds);
	}

	private static Path findDefaultFile() {
		Path local = Path.of(DEFAULT_FILE);
		return Files.exists(local) ? local : Path.of("..", DEFAULT_FILE);
	}

	private static void printTable(List<AggregatorResult> results) {
		System.out.printf("%-16s %10s %10s %18s%n", "Aggregator", "Accuracy", "Neutral", "Acc. w/o neutral");
		for (AggregatorResult result : results) {
			System.out.printf(Locale.US, "%-16s %9.1f%% %9.1f%% %17.1f%%%n",
					result.name(),
					result.accuracy() * 100,
					result.neutralShare() * 100,
					result.accuracyWithoutNeutral() * 100);
		}
	}
}
