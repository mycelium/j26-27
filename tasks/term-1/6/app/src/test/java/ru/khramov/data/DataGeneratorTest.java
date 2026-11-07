package ru.khramov.data;

import org.junit.Test;
import ru.khramov.point.LabeledPoint;
import ru.khramov.point.Point;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static ru.khramov.data.DataGenerator.generate;

public class DataGeneratorTest {
	private final ClassSpec classSpecA = new ClassSpec("A", new Point(2, 2), 1.5, 50);
	private final ClassSpec classSpecB = new ClassSpec("B", new Point(8, 2), 1.4, 40);
	private final ClassSpec classSpecC = new ClassSpec("C", new Point(5, 8), 1.3, 30);

	@Test
	public void generatePoints() {
		List<LabeledPoint> points = generate(List.of(classSpecA, classSpecB, classSpecC), new Random(50));

		assertEquals(120, points.size());
	}

	@Test
	public void generateSingleClassCount() {
		List<LabeledPoint> points = generate(classSpecA, new Random(50));

		assertEquals(50, points.size());
	}

	@Test
	public void generateKeepsLabel() {
		List<LabeledPoint> points = generate(classSpecB, new Random(50));

		for (LabeledPoint point : points) {
			assertEquals("B", point.label());
		}
	}

	@Test
	public void generateMeanIsNearCenter() {
		ClassSpec classSpec = new ClassSpec("A", new Point(2, 5), 1.5, 5000);

		List<LabeledPoint> points = generate(classSpec, new Random(50));

		double sumX = 0;
		double sumY = 0;
		for (LabeledPoint point : points) {
			sumX += point.point().x();
			sumY += point.point().y();
		}

		assertEquals(2.0, sumX / points.size(), 0.2);
		assertEquals(5.0, sumY / points.size(), 0.2);
	}

	@Test
	public void generateSameSeedGivesSameData() {
		List<LabeledPoint> first = generate(classSpecA, new Random(50));
		List<LabeledPoint> second = generate(classSpecA, new Random(50));

		assertEquals(first, second);
	}

	@Test
	public void generateSharedRandomGivesDifferentShapes() {
		List<LabeledPoint> points = generate(List.of(classSpecA, classSpecA), new Random(50));

		List<LabeledPoint> firstHalf = points.subList(0, 50);
		List<LabeledPoint> secondHalf = points.subList(50, 100);

		assertNotEquals(firstHalf, secondHalf);
	}

	@Test(expected = IllegalArgumentException.class)
	public void classSpecNegativeSpreadTest() {
		new ClassSpec("A", new Point(0, 0), -1, 10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void classSpecNegativeCountTest() {
		new ClassSpec("A", new Point(0, 0), 1, -1);
	}
}
