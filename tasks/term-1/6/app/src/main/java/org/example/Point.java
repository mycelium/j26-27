package org.example;

public record Point(double x, double y) {
    public Point {
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("Point coordinates must be finite.");
        }
    }

    public double distanceTo(Point other) {
        return Math.hypot(x - other.x, y - other.y);
    }
}
