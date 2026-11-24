package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public final class DatasetUtils {

    private DatasetUtils() { }

    public static List<LabeledPoint> generateDemo(int perClass, long seed) {
        Random rnd = new Random(seed);
        double[][] centers = {{2.5, 2.5}, {7.5, 3.0}, {5.0, 7.5}};
        String[] labels = {"A", "B", "C"};
        double sigma = 1.0;

        List<LabeledPoint> result = new ArrayList<>();
        for (int c = 0; c < centers.length; c++) {
            for (int i = 0; i < perClass; i++) {
                double x = centers[c][0] + rnd.nextGaussian() * sigma;
                double y = centers[c][1] + rnd.nextGaussian() * sigma;
                result.add(new LabeledPoint(x, y, labels[c]));
            }
        }
        return result;
    }

    public static List<LabeledPoint> loadCsv(Path path) throws IOException {
        List<LabeledPoint> result = new ArrayList<>();
        int lineNo = 0;
        for (String line : Files.readAllLines(path)) {
            lineNo++;
            line = line.trim();
            if (line.isEmpty() || line.startsWith("#")) {
                continue;
            }
            String[] parts = line.split("[,;]");
            if (parts.length < 3) {
                throw new IOException("Строка " + lineNo + ": ожидается x,y,label");
            }
            try {
                double x = Double.parseDouble(parts[0].trim());
                double y = Double.parseDouble(parts[1].trim());
                result.add(new LabeledPoint(x, y, parts[2].trim()));
            } catch (NumberFormatException e) {
                if (lineNo == 1) {
                    continue; // заголовок
                }
                throw new IOException("Строка " + lineNo + ": некорректное число", e);
            }
        }
        return result;
    }

    public static String format(Point p) {
        return String.format(Locale.ROOT, "%.2f %.2f", p.x(), p.y());
    }
}
