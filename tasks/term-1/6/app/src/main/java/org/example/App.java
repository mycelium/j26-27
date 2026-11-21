package org.example;

import java.io.IOException;
import java.util.*;

public class App {
    record Point(double x, double y, String label) {}

    private static final int K = 3;
    private static final List<Point> POINTS = List.of(
            new Point(1, 2, "A"), new Point(2, 1, "A"),
            new Point(2.5, 3, "A"), new Point(3, 2, "A"),
            new Point(6.5, 2, "B"), new Point(7, 4, "B"),
            new Point(8, 2.5, "B"), new Point(8.5, 3.5, "B"),
            new Point(4, 7.5, "C"), new Point(5.5, 7, "C"),
            new Point(5, 9, "C"), new Point(6, 8.2, "C")
    );

    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Укажи две координаты: x y");
        }
        double x = Double.parseDouble(args[0]);
        double y = Double.parseDouble(args[1]);
        if (!Double.isFinite(x) || !Double.isFinite(y)) {
            throw new IllegalArgumentException("Координаты должны быть конечными числами");
        }

        String label = classify(x, y);
        Plot.save(POINTS, new Point(x, y, label));
        System.out.println("Класс: " + label);
        System.out.println("График: " + Plot.OUTPUT);
    }

    private static String classify(double x, double y) {
        var neighbors = POINTS.stream()
                .sorted(Comparator.comparingDouble(p -> Math.hypot(p.x() - x, p.y() - y)))
                .limit(K)
                .toList();

        var votes = new HashMap<String, Integer>();
        for (var point : neighbors) {
            votes.merge(point.label(), 1, Integer::sum);
        }

        String best = neighbors.getFirst().label();
        for (var point : neighbors) {
            if (votes.get(point.label()) > votes.get(best)) {
                best = point.label();
            }
        }
        return best;
    }
}
