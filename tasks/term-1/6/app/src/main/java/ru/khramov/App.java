package ru.khramov;

import ru.khramov.data.ClassSpec;
import ru.khramov.data.DataGenerator;
import ru.khramov.plot.PlotRenderer;
import ru.khramov.point.LabeledPoint;
import ru.khramov.point.Point;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class App {
	private static final int K = 5;
	private static final long SEED = 42;
	private static final String OUTPUT_FILE = "plot.png";
	private static final Point DEFAULT_POINT = new Point(4, 4);
	private static final List<ClassSpec> CLASS_SPECS = List.of(
			new ClassSpec("A", new Point(2, 2), 1.5, 40),
			new ClassSpec("B", new Point(8, 2), 1.4, 40),
			new ClassSpec("C", new Point(5, 8), 1.3, 40)
	);

	public static void main(String[] args) throws IOException {
		Point point;
		try {
			point = parsePoint(args);
		} catch (IllegalArgumentException e) {
			System.err.println(e.getMessage());
			System.err.println("Usage: App [x y]");
			System.exit(1);
			return;
		}

		List<LabeledPoint> labeledPoints = DataGenerator.generate(CLASS_SPECS, new Random(SEED));
		KnnClassifier classifier = new KnnClassifier(K, labeledPoints);

		String label = classifier.classify(point);
		System.out.println("Point (" + point.x() + ", " + point.y() + ") -> class " + label);

		new PlotRenderer().render(labeledPoints, point, label, new File(OUTPUT_FILE));
		System.out.println("Plot saved to " + OUTPUT_FILE);
	}

	static Point parsePoint(String[] args) {
		if (args.length == 0) {
			return DEFAULT_POINT;
		}
		if (args.length != 2) {
			throw new IllegalArgumentException("Expected two arguments: x and y");
		}

		try {
			return new Point(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("x and y must be numbers", e);
		}
	}
}
