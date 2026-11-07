package ru.khramov;

import org.junit.Test;
import ru.khramov.point.LabeledPoint;
import ru.khramov.point.Point;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class KnnClassifierTest {

	private final List<LabeledPoint> labeledPoints = List.of(
			new LabeledPoint("B", new Point(1, 1)),
			new LabeledPoint("A", new Point(2, 2)),
			new LabeledPoint("A", new Point(2, 3)),
			new LabeledPoint("A", new Point(3, 2))
	);

	@Test
	public void classifyk1Test() {
		KnnClassifier knnClassifier = new KnnClassifier(1, labeledPoints);

		Point point = new Point(0, 0);

		assertEquals("B", knnClassifier.classify(point));
	}

	@Test
	public void classifyk3Test() {
		KnnClassifier knnClassifier = new KnnClassifier(3, labeledPoints);

		Point point = new Point(0, 0);

		assertEquals("A", knnClassifier.classify(point));
	}

	@Test
	public void classifyTieTest() {
		List<LabeledPoint> labeledPoints = List.of(
				new LabeledPoint("A", new Point(1, 1)),
				new LabeledPoint("B", new Point(2, 2)),
				new LabeledPoint("C", new Point(3, 3))
		);

		KnnClassifier knnClassifier = new KnnClassifier(3, labeledPoints);

		Point point = new Point(0, 0);

		assertEquals("A", knnClassifier.classify(point));
	}

	@Test
	public void classifyNearestIsNotFirstTest() {
		List<LabeledPoint> labeledPoints = List.of(
				new LabeledPoint("C", new Point(5, 5)),
				new LabeledPoint("A", new Point(1, 1)),
				new LabeledPoint("B", new Point(3, 3))
		);

		KnnClassifier knnClassifier = new KnnClassifier(1, labeledPoints);

		Point point = new Point(0, 0);

		assertEquals("A", knnClassifier.classify(point));
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorZeroKTest() {
		new KnnClassifier(0, labeledPoints);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorNegativeKTest() {
		new KnnClassifier(-1, labeledPoints);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorKGreaterThanSizeTest() {
		new KnnClassifier(labeledPoints.size() + 1, labeledPoints);
	}

	@Test(expected = IllegalArgumentException.class)
	public void constructorEmptyListTest() {
		new KnnClassifier(1, List.of());
	}

}
