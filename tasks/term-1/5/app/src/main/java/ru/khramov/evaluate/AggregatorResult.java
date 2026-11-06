package ru.khramov.evaluate;

public record AggregatorResult(String name, int total, int correct, int neutral) {
	public double accuracy() {
		if (total == 0) {
			return 0;
		}
		return (double) correct / total;
	}

	public double neutralShare() {
		if (total == 0) {
			return 0;
		}
		return (double) neutral / total;
	}

	public double accuracyWithoutNeutral() {
		if (total - neutral == 0) {
			return 0;
		}
		
		return (double) correct / (total - neutral);
	}
}
