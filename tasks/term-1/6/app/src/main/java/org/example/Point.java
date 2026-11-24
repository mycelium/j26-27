package org.example;


public record Point(double x, double y) {
    public double distanceTo(Point other) {
        double dx = x - other.x;
        double dy = y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String toString() {
        return String.format(java.util.Locale.ROOT, "(%.2f; %.2f)", x, y);
    }
}

