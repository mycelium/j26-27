package ru.khramov.evaluate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AggregatorResultTest {
	private static final double DELTA = 1e-9;

	@Test
	void typicalCase() {
		AggregatorResult result = new AggregatorResult("Test", 10, 6, 2);

		assertEquals(0.6, result.accuracy(), DELTA);
		assertEquals(0.2, result.neutralShare(), DELTA);
		assertEquals(0.75, result.accuracyWithoutNeutral(), DELTA);
	}

	@Test
	void allCorrect() {
		AggregatorResult result = new AggregatorResult("Test", 5, 5, 0);

		assertEquals(1.0, result.accuracy(), DELTA);
		assertEquals(0.0, result.neutralShare(), DELTA);
		assertEquals(1.0, result.accuracyWithoutNeutral(), DELTA);
	}

	@Test
	void noneCorrect() {
		AggregatorResult result = new AggregatorResult("Test", 5, 0, 0);

		assertEquals(0.0, result.accuracy(), DELTA);
		assertEquals(0.0, result.accuracyWithoutNeutral(), DELTA);
	}

	@Test
	void allNeutral() {
		AggregatorResult result = new AggregatorResult("Test", 4, 0, 4);

		assertEquals(0.0, result.accuracy(), DELTA);
		assertEquals(1.0, result.neutralShare(), DELTA);
		assertEquals(0.0, result.accuracyWithoutNeutral(), DELTA);
	}

	@Test
	void zeroTotalGivesZeros() {
		AggregatorResult result = new AggregatorResult("Test", 0, 0, 0);

		assertEquals(0.0, result.accuracy(), DELTA);
		assertEquals(0.0, result.neutralShare(), DELTA);
		assertEquals(0.0, result.accuracyWithoutNeutral(), DELTA);
	}

	@Test
	void neutralDecreasesAccuracyButNotAccuracyWithoutNeutral() {
		AggregatorResult withoutNeutral = new AggregatorResult("Test", 10, 5, 0);
		AggregatorResult withNeutral = new AggregatorResult("Test", 10, 5, 4);

		assertEquals(withoutNeutral.accuracy(), withNeutral.accuracy(), DELTA);
		assertEquals(0.5, withoutNeutral.accuracyWithoutNeutral(), DELTA);
		assertEquals(5.0 / 6, withNeutral.accuracyWithoutNeutral(), DELTA);
	}

	@Test
	void divisionIsNotInteger() {
		AggregatorResult result = new AggregatorResult("Test", 3, 1, 0);

		assertEquals(1.0 / 3, result.accuracy(), DELTA);
	}

	@Test
	void keepsName() {
		assertEquals("Voting", new AggregatorResult("Voting", 1, 1, 0).name());
	}
}
