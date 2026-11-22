package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.List;
import org.junit.Test;

public class AppTest {
    @Test
    public void predictsTheClassWithTheMostVotesAmongTheNearestPoints() {
        List<LabeledPoint> points = List.of(
                new LabeledPoint(new Point(10, 0), "B"),
                new LabeledPoint(new Point(0, 2), "A"),
                new LabeledPoint(new Point(0, 0.2), "B"),
                new LabeledPoint(new Point(1, 0), "A")
        );

        assertEquals("A", new KnnClassifier(points, 3).predict(new Point(0, 0)));
        assertEquals("B", new KnnClassifier(points, 1).predict(new Point(0, 0)));
    }

    @Test
    public void breaksAVoteTieUsingTheClosestNeighbor() {
        List<LabeledPoint> points = List.of(
                new LabeledPoint(new Point(1, 1), "A"),
                new LabeledPoint(new Point(0, -1), "B")
        );

        assertEquals("B", new KnnClassifier(points, 2).predict(new Point(0, 0)));
    }

    @Test
    public void preservesTrainingOrderForEqualDistances() {
        List<LabeledPoint> points = List.of(
                new LabeledPoint(new Point(-1, 0), "B"),
                new LabeledPoint(new Point(1, 0), "A")
        );

        assertEquals("B", new KnnClassifier(points, 1).predict(new Point(0, 0)));
        assertEquals("B", new KnnClassifier(points, 2).predict(new Point(0, 0)));
    }

    @Test
    public void rejectsAnInvalidNumberOfNeighbors() {
        List<LabeledPoint> points = List.of(new LabeledPoint(new Point(0, 0), "A"));

        assertThrows(IllegalArgumentException.class, () -> new KnnClassifier(points, 0));
        assertThrows(IllegalArgumentException.class, () -> new KnnClassifier(points, 2));
        assertThrows(IllegalArgumentException.class, () -> new KnnClassifier(List.of(), 1));
    }
}
