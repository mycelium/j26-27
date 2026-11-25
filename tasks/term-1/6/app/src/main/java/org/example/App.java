package org.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class App {
    record Point(double x, double y) {
        Point {
            if (!Double.isFinite(x) || !Double.isFinite(y)) {
                throw new IllegalArgumentException("Coordinates must be finite numbers.");
            }
        }

        double distanceTo(Point other) {
            return Math.hypot(x - other.x, y - other.y);
        }
    }

    record Sample(Point point, String label) {}

    private static final List<Sample> SAMPLES = List.of(
            new Sample(new Point(1, 2), "A"),
            new Sample(new Point(2, 1), "A"),
            new Sample(new Point(2, 3), "A"),
            new Sample(new Point(5, 7), "B"),
            new Sample(new Point(6, 8), "B"),
            new Sample(new Point(7, 7), "B"),
            new Sample(new Point(8, 2), "C"),
            new Sample(new Point(9, 3), "C"),
            new Sample(new Point(9, 1), "C")
    );

    static String classify(List<Sample> samples, Point query, int k) {
        if (k < 1 || k > samples.size()) {
            throw new IllegalArgumentException("k must be between 1 and " + samples.size() + ".");
        }

        List<Sample> neighbors = samples.stream()
                .sorted(Comparator.comparingDouble(sample -> sample.point().distanceTo(query)))
                .limit(k)
                .toList();

        // In a vote tie, prefer the class whose closest neighbor comes first.
        Map<String, Integer> votes = new LinkedHashMap<>();
        for (Sample neighbor : neighbors) {
            votes.merge(neighbor.label(), 1, Integer::sum);
        }

        String winner = neighbors.getFirst().label();
        for (var vote : votes.entrySet()) {
            if (vote.getValue() > votes.get(winner)) {
                winner = vote.getKey();
            }
        }
        return winner;
    }

    private static BufferedImage plot(Point query, String label) {
        int width = 640;
        int height = 480;
        int margin = 55;
        int right = 510;
        int bottom = height - margin;
        double minX = Math.min(0, query.x());
        double maxX = Math.max(10, query.x());
        double minY = Math.min(0, query.y());
        double maxY = Math.max(9, query.y());

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, width, height);
            graphics.setColor(Color.DARK_GRAY);
            graphics.drawString("KNN classification", margin, 27);
            graphics.drawRect(margin, margin, right - margin, bottom - margin);
            graphics.drawString("x", right, bottom + 24);
            graphics.drawString("y", margin - 22, margin);
            graphics.drawString(String.format("x: %.4g to %.4g", minX, maxX), margin, bottom + 24);
            graphics.drawString(String.format("y: %.4g to %.4g", minY, maxY), margin + 210, bottom + 24);

            Map<String, Color> colors = Map.of(
                    "A", new Color(35, 100, 180),
                    "B", new Color(185, 65, 50),
                    "C", new Color(30, 130, 85)
            );
            for (Sample sample : SAMPLES) {
                drawPoint(graphics, sample.point(), colors.get(sample.label()),
                        minX, maxX, minY, maxY, margin, right, bottom, false);
            }
            drawPoint(graphics, query, Color.BLACK,
                    minX, maxX, minY, maxY, margin, right, bottom, true);

            int legendY = margin + 15;
            for (String classLabel : List.of("A", "B", "C")) {
                graphics.setColor(colors.get(classLabel));
                graphics.fillOval(right + 18, legendY - 9, 10, 10);
                graphics.drawString("Class " + classLabel, right + 35, legendY);
                legendY += 25;
            }
            graphics.setColor(Color.BLACK);
            graphics.drawString("Query: " + label, right + 18, legendY + 5);
        } finally {
            graphics.dispose();
        }
        return image;
    }

    private static void drawPoint(Graphics2D graphics, Point point, Color color,
                                  double minX, double maxX, double minY, double maxY,
                                  int margin, int right, int bottom, boolean query) {
        int x = margin + (int) Math.round(fraction(point.x(), minX, maxX) * (right - margin));
        int y = bottom - (int) Math.round(fraction(point.y(), minY, maxY) * (bottom - margin));
        graphics.setColor(color);
        if (query) {
            graphics.setStroke(new BasicStroke(2));
            graphics.drawOval(x - 8, y - 8, 16, 16);
        } else {
            graphics.fillOval(x - 5, y - 5, 10, 10);
        }
    }

    private static double fraction(double value, double min, double max) {
        return (value / 2 - min / 2) / (max / 2 - min / 2);
    }

    public static void main(String[] args) {
        if (args.length < 2 || args.length > 3) {
            System.err.println("Usage: App <x> <y> [k]");
            return;
        }

        try {
            Point query = new Point(Double.parseDouble(args[0]), Double.parseDouble(args[1]));
            int k = args.length == 3 ? Integer.parseInt(args[2]) : 3;
            String label = classify(SAMPLES, query, k);
            System.out.println(label);
            BufferedImage image = plot(query, label);
            SwingUtilities.invokeLater(() -> {
                JFrame window = new JFrame("KNN");
                window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                window.add(new JLabel(new ImageIcon(image)));
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
            });
        } catch (IllegalArgumentException error) {
            System.err.println(error.getMessage());
        }
    }
}
