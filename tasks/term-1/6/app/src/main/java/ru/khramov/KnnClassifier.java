package ru.khramov;

import ru.khramov.point.LabeledPoint;
import ru.khramov.point.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KnnClassifier {
	private final int k;
	private final List<LabeledPoint> labeledPoints;

	public KnnClassifier(int k, List<LabeledPoint> labeledPoints) {
		if (k <= 0 || k > labeledPoints.size()) {
			throw new IllegalArgumentException("k must be between 1 and " + (labeledPoints.size()));
		}

		this.k = k;
		this.labeledPoints = List.copyOf(labeledPoints);
	}

	public String classify(Point point) {
		List<LabeledPoint> nearestPoints = findNearestPoints(point);
		Map<String, Integer> votes = new HashMap<>();
		for (LabeledPoint nearestPoint : nearestPoints) {
			votes.merge(nearestPoint.label(), 1, Integer::sum);
		}

		String bestLabel = null;
		int bestCount = 0;
		for (LabeledPoint neighbor : nearestPoints) {
			int count = votes.get(neighbor.label());
			if (count > bestCount) {
				bestCount = count;
				bestLabel = neighbor.label();
			}
		}
		return bestLabel;
	}

	private List<LabeledPoint> findNearestPoints(Point point) {
		List<LabeledPoint> remainingPoints = new ArrayList<>(labeledPoints);
		List<LabeledPoint> nearestPoints = new ArrayList<>();

		for (int i = 0; i < k; i++) {
			LabeledPoint nearestPoint = remainingPoints.getFirst();
			double nearestDistance = point.distanceTo(nearestPoint.point());
			for (LabeledPoint remainingPoint : remainingPoints) {
				double distance = point.distanceTo(remainingPoint.point());
				if (distance < nearestDistance) {
					nearestPoint = remainingPoint;
					nearestDistance = distance;
				}
			}
			nearestPoints.add(nearestPoint);
			remainingPoints.remove(nearestPoint);
		}
		return nearestPoints;
	}
}
