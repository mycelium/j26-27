package ru.khramov;

import org.junit.Test;
import ru.khramov.point.Point;

import static org.junit.Assert.assertEquals;

public class AppTest {

	@Test
	public void parsePointDefault() {
		assertEquals(new Point(5, 5), App.parsePoint(new String[]{}));
	}

	@Test
	public void parsePointFromArgs() {
		assertEquals(new Point(1.5, -2), App.parsePoint(new String[]{"1.5", "-2"}));
	}

	@Test(expected = IllegalArgumentException.class)
	public void parsePointWrongCount() {
		App.parsePoint(new String[]{"1"});
	}

	@Test(expected = IllegalArgumentException.class)
	public void parsePointNotNumbers() {
		App.parsePoint(new String[]{"a", "b"});
	}
}
