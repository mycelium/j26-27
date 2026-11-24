package org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;


public class App {

    public static void main(String[] args) throws IOException {
        System.setProperty("java.awt.headless", "true");
        Locale.setDefault(Locale.ROOT);

        int k = 5;
        String dataPath = null;
        String outPath = "knn_plot.png";
        List<String> positional = new ArrayList<>();

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--k" -> k = Integer.parseInt(args[++i]);
                case "--data" -> dataPath = args[++i];
                case "--out" -> outPath = args[++i];
                default -> positional.add(args[i]);
            }
        }

        List<LabeledPoint> training = (dataPath == null)
                ? DatasetUtils.generateDemo(40, 42)
                : DatasetUtils.loadCsv(Path.of(dataPath));
        KnnClassifier knn = new KnnClassifier(training, k);
        ScatterPlotter plotter = new ScatterPlotter(knn);

        System.out.printf("Обучающая выборка: %d точек, k = %d%n", training.size(), k);

        if (positional.size() >= 2) {
            Point p = new Point(parse(positional.get(0)), parse(positional.get(1)));
            classifyAndPrint(knn, plotter, p);
        } else {
            interactive(knn, plotter);
        }

        File out = new File(outPath);
        plotter.save(out);
        System.out.println("График сохранён: " + out.getAbsolutePath());
    }

    private static void interactive(KnnClassifier knn, ScatterPlotter plotter) {
        System.out.println("Введите точку как \"x y\" (пустая строка или q — завершить):");
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();
            if (line.isEmpty() || line.equalsIgnoreCase("q")) {
                break;
            }
            String[] parts = line.split("[\\s,;]+");
            if (parts.length != 2) {
                System.out.println("Ожидается два числа: x y");
                continue;
            }
            try {
                classifyAndPrint(knn, plotter, new Point(parse(parts[0]), parse(parts[1])));
            } catch (NumberFormatException e) {
                System.out.println("Не удалось разобрать число: " + e.getMessage());
            }
        }
    }

    private static void classifyAndPrint(KnnClassifier knn, ScatterPlotter plotter, Point p) {
        String label = knn.classify(p);
        plotter.addQuery(p, label);
        System.out.println("Точка " + p + " -> класс " + label);
    }

    private static double parse(String s) {
        return Double.parseDouble(s.replace(',', '.'));
    }
}
