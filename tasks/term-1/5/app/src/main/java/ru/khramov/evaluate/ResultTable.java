package ru.khramov.evaluate;

import java.util.List;
import java.util.Locale;

public class ResultTable {
	public String format(List<AggregatorResult> results) {
		StringBuilder table = new StringBuilder();
		table.append(String.format("%-16s %10s %10s %18s%n", "Aggregator", "Accuracy", "Neutral", "Acc. w/o neutral"));
		for (AggregatorResult result : results) {
			table.append(String.format(Locale.US, "%-16s %9.1f%% %9.1f%% %17.1f%%%n",
					result.name(),
					result.accuracy() * 100,
					result.neutralShare() * 100,
					result.accuracyWithoutNeutral() * 100));
		}
		return table.toString();
	}
}
