package ru.khramov.data;

import ru.khramov.point.LabeledPoint;
import ru.khramov.point.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {
	public static List<LabeledPoint> generate(List<ClassSpec> classSpecs, Random random) {
		List<LabeledPoint> points = new ArrayList<>();
		for (ClassSpec classSpec : classSpecs) {
			points.addAll(generate(classSpec, random));
		}
		return points;
	}

	public static List<LabeledPoint> generate(ClassSpec classSpec, Random random) {
		List<LabeledPoint> points = new ArrayList<>();
		for (int i = 0; i < classSpec.count(); i++) {
			String label = classSpec.label();
			double x = classSpec.center().x() + random.nextGaussian() * classSpec.spread();
			double y = classSpec.center().y() + random.nextGaussian() * classSpec.spread();
			points.add(new LabeledPoint(label, new Point(x, y)));
		}
		return points;
	}
}
