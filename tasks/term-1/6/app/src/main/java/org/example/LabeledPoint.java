package org.example;

public record LabeledPoint(Point point, String label) {

    public LabeledPoint(double x, double y, String label) {
        this(new Point(x, y), label);
    }
}