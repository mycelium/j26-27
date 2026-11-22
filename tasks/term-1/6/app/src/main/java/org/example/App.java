package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

public class App {
    private static final int K = 3;
    private static final String COORDINATE_ERROR = "Error: enter two finite numbers for x and y, for example 5.5 1.0.";
    private static final List<LabeledPoint> TRAINING_POINTS = List.of(
            new LabeledPoint(new Point(0, 0), "A"),
            new LabeledPoint(new Point(1, 1), "A"),
            new LabeledPoint(new Point(1, 2), "A"),
            new LabeledPoint(new Point(-0.7, 0.4), "A"),
            new LabeledPoint(new Point(-0.4, 1.4), "A"),
            new LabeledPoint(new Point(0.3, 0.9), "A"),
            new LabeledPoint(new Point(0.5, 1.7), "A"),
            new LabeledPoint(new Point(0.8, -0.4), "A"),
            new LabeledPoint(new Point(1.6, 0.2), "A"),
            new LabeledPoint(new Point(1.8, 1.1), "A"),

            new LabeledPoint(new Point(3, 3), "B"),
            new LabeledPoint(new Point(4, 3), "B"),
            new LabeledPoint(new Point(4, 4), "B"),
            new LabeledPoint(new Point(2.8, 4.4), "B"),
            new LabeledPoint(new Point(3.3, 3.8), "B"),
            new LabeledPoint(new Point(3.5, 4.8), "B"),
            new LabeledPoint(new Point(4.1, 5.3), "B"),
            new LabeledPoint(new Point(4.6, 4.6), "B"),
            new LabeledPoint(new Point(4.8, 3.6), "B"),
            new LabeledPoint(new Point(5.2, 5.0), "B"),

            new LabeledPoint(new Point(4.4, 0.6), "C"),
            new LabeledPoint(new Point(4.7, -0.3), "C"),
            new LabeledPoint(new Point(4.8, 1.7), "C"),
            new LabeledPoint(new Point(5.1, 0.9), "C"),
            new LabeledPoint(new Point(5.4, 0.1), "C"),
            new LabeledPoint(new Point(5.5, 1.3), "C"),
            new LabeledPoint(new Point(5.8, 1.9), "C"),
            new LabeledPoint(new Point(6.1, 0.6), "C"),
            new LabeledPoint(new Point(6.4, 1.2), "C"),
            new LabeledPoint(new Point(6.6, 0.0), "C")
    );

    public static void main(String[] args) throws IOException {
        Point query;
        try {
            query = args.length == 0 ? readPoint() : parsePoint(args);
        } catch (IllegalArgumentException exception) {
            System.err.println("Usage: App <x> <y>");
            System.err.println(COORDINATE_ERROR);
            System.exit(2);
            return;
        }
        if (query == null) {
            System.err.println("Error: no coordinates were entered.");
            System.exit(2);
            return;
        }

        KnnClassifier classifier = new KnnClassifier(TRAINING_POINTS, K);
        String predictedClass = classifier.predict(query);
        System.out.println("Predicted class: " + predictedClass);

        Path image = PointPlot.savePlot(TRAINING_POINTS, query, predictedClass);
        System.out.println("Image saved successfully: " + image.toAbsolutePath());
    }

    private static Point readPoint() throws IOException {
        BufferedReader input = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        while (true) {
            System.out.println("Enter x and y separated by a space (for example 5.5 1.0):");
            String line = input.readLine();
            if (line == null) {
                return null;
            }

            try {
                return parsePoint(line.strip().split("\\s+"));
            } catch (IllegalArgumentException exception) {
                System.err.println(COORDINATE_ERROR);
            }
        }
    }

    private static Point parsePoint(String[] coordinates) {
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("Expected two coordinates.");
        }
        return new Point(Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]));
    }
}
