package ru.khramov.data;

import ru.khramov.point.Point;

public record ClassSpec(String label, Point center, double spread, int count) {
	public ClassSpec {
		if (spread < 0) {
			throw new IllegalArgumentException("spread must not be negative");
		}
		if (count < 0) {
			throw new IllegalArgumentException("count must not be negative");
		}
	}
}
