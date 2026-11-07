package ru.khramov.point;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PointTest {

	@Test
	public void getDistanceBetweenPoints() {
		Point point1 = new Point(0, 0);
		Point point2 = new Point(3, 4);

		assertEquals(5, (int) point1.distanceTo(point2));

	}

	@Test
	public void getDistanceToOneself() {
		Point point1 = new Point(0, 0);
		Point point2 = new Point(0, 0);

		assertEquals(0, (int) point1.distanceTo(point2));

	}
}
