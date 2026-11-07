package ru.khramov.point;

public record Point(double x, double y) {
	public double distanceTo(Point point) {
		return Math.sqrt(Math.pow(this.x - point.x, 2) + Math.pow(this.y - point.y, 2));
	}
}
