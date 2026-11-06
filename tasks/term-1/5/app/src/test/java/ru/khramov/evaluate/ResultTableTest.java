package ru.khramov.evaluate;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResultTableTest {
	private final ResultTable table = new ResultTable();

	@Test
	void containsHeaderAndOneLinePerResult() {
		String text = table.format(List.of(
				new AggregatorResult("A", 10, 6, 2),
				new AggregatorResult("B", 10, 5, 0)
		));

		List<String> lines = text.lines().toList();

		assertEquals(3, lines.size());
		assertTrue(lines.getFirst().contains("Aggregator"));
		assertTrue(lines.get(1).startsWith("A"));
		assertTrue(lines.get(2).startsWith("B"));
	}

	@Test
	void formatsPercentagesWithDotAndOneDecimal() {
		String text = table.format(List.of(new AggregatorResult("A", 10, 6, 2)));

		String row = text.lines().toList().get(1);

		assertTrue(row.contains("60.0%"));
		assertTrue(row.contains("20.0%"));
		assertTrue(row.contains("75.0%"));
	}

	@Test
	void emptyResultsGiveOnlyHeader() {
		assertEquals(1, table.format(List.of()).lines().count());
	}
}
