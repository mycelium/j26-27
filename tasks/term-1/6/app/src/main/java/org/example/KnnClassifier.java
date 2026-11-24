package org.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;



public class KnnClassifier {

    private final int k;
    private final List<LabeledPoint> training;

    public KnnClassifier(List<LabeledPoint> training, int k) {
        if (training == null || training.isEmpty()) {
            throw new IllegalArgumentException("Обучающая выборка пуста");
        }
        if (k <= 0) {
            throw new IllegalArgumentException("k должно быть положительным, получено: " + k);
        }
        if (k > training.size()) {
            throw new IllegalArgumentException(
                    "k (" + k + ") больше размера выборки (" + training.size() + ")");
        }
        this.k = k;
        this.training = List.copyOf(training);
    }

    public int getK() {
        return k;
    }

    public List<LabeledPoint> getTraining() {
        return training;
    }


    public String classify(Point query) {
        List<Neighbor> neighbors = nearestNeighbors(query);

        Map<String, Integer> votes = new LinkedHashMap<>();
        Map<String, Double> distanceSum = new LinkedHashMap<>();
        for (Neighbor n : neighbors) {
            votes.merge(n.point().label(), 1, Integer::sum);
            distanceSum.merge(n.point().label(), n.distance(), Double::sum);
        }

        String best = null;
        for (String label : votes.keySet()) {
            if (best == null) {
                best = label;
                continue;
            }
            int cmp = Integer.compare(votes.get(label), votes.get(best));
            if (cmp > 0 || (cmp == 0 && distanceSum.get(label) < distanceSum.get(best))) {
                best = label;
            }
        }
        return best;
    }


    public List<Neighbor> nearestNeighbors(Point query) {
        List<Neighbor> all = new ArrayList<>(training.size());
        for (LabeledPoint lp : training) {
            all.add(new Neighbor(lp, query.distanceTo(lp.point())));
        }
        all.sort(Comparator.comparingDouble(Neighbor::distance));
        return all.subList(0, k);
    }


    public record Neighbor(LabeledPoint point, double distance) { }
}

