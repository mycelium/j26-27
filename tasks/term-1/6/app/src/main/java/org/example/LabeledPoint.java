package org.example;

import java.util.Objects;

public record LabeledPoint(Point point, String label) {
    public LabeledPoint {
        Objects.requireNonNull(point, "Point is required.");
        if (label == null || label.isBlank()) {
            throw new IllegalArgumentException("Class label must not be blank.");
        }
    }
}
