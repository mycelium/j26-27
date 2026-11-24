package org.example;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

public class KnnClassifierTest {

    private final List<LabeledPoint> data = List.of(
            new LabeledPoint(0, 0, "A"), new LabeledPoint(1, 0, "A"), new LabeledPoint(0, 1, "A"),
            new LabeledPoint(10, 10, "B"), new LabeledPoint(11, 10, "B"), new LabeledPoint(10, 11, "B"),
            new LabeledPoint(0, 10, "C"), new LabeledPoint(1, 10, "C"), new LabeledPoint(0, 11, "C"));

    @Test
    public void classifiesNearClusters() {
        KnnClassifier knn = new KnnClassifier(data, 3);
        assertEquals("A", knn.classify(new Point(0.5, 0.5)));
        assertEquals("B", knn.classify(new Point(10.5, 10.5)));
        assertEquals("C", knn.classify(new Point(0.5, 10.5)));
    }

    @Test
    public void oneNeighbourReturnsNearestLabel() {
        KnnClassifier knn = new KnnClassifier(data, 1);
        assertEquals("B", knn.classify(new Point(9, 9)));
    }

    @Test
    public void tieIsResolvedByDistance() {
        List<LabeledPoint> d = List.of(new LabeledPoint(0, 0, "X"), new LabeledPoint(10, 0, "Y"));
        KnnClassifier knn = new KnnClassifier(d, 2);
        assertEquals("X", knn.classify(new Point(2, 0)));
        assertEquals("Y", knn.classify(new Point(8, 0)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void kLargerThanDatasetIsRejected() {
        new KnnClassifier(data, 100);
    }
}
