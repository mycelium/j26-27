package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class KnnClassifier {
    private final List<LabeledPoint> trainingPoints;
    private final int k;

    public KnnClassifier(List<LabeledPoint> trainingPoints, int k) {
        this.trainingPoints = List.copyOf(trainingPoints);
        if (k < 1 || k > this.trainingPoints.size()) {
            throw new IllegalArgumentException("k must be between 1 and the number of training points.");
        }
        this.k = k;
    }

    public String predict(Point query) {
        Objects.requireNonNull(query, "Query point is required.");
        List<Neighbor> neighbors = new ArrayList<>(trainingPoints.size());
        for (LabeledPoint example : trainingPoints) {
            neighbors.add(new Neighbor(example.label(), query.distanceTo(example.point())));
        }
        neighbors.sort(Comparator.comparingDouble(Neighbor::distance));

        Map<String, Integer> votes = new LinkedHashMap<>();
        for (Neighbor neighbor : neighbors.subList(0, k)) {
            votes.merge(neighbor.label(), 1, Integer::sum);
        }

        String predictedLabel = neighbors.getFirst().label();
        int mostVotes = votes.get(predictedLabel);
        for (Map.Entry<String, Integer> vote : votes.entrySet()) {
            if (vote.getValue() > mostVotes) {
                predictedLabel = vote.getKey();
                mostVotes = vote.getValue();
            }
        }
        return predictedLabel;
    }

    private record Neighbor(String label, double distance) {
    }
}
