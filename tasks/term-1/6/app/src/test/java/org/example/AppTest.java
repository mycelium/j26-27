package org.example;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AppTest {
    private final List<App.Sample> samples = List.of(
            new App.Sample(new App.Point(1, 0), "A"),
            new App.Sample(new App.Point(2, 0), "B"),
            new App.Sample(new App.Point(3, 0), "B"),
            new App.Sample(new App.Point(9, 0), "C")
    );

    @Test
    public void majorityAmongNearestNeighborsWins() {
        assertEquals("B", App.classify(samples, new App.Point(0, 0), 3));
    }

    @Test
    public void closestNeighborBreaksVoteTie() {
        assertEquals("A", App.classify(samples, new App.Point(0, 0), 2));
    }
}
