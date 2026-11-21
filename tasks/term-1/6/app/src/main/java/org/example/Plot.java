package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class Plot {
    static final Path OUTPUT = Path.of("data", "knn.png");

    private static final Map<String, Color> COLORS = Map.of(
            "A", new Color(0x2463B6),
            "B", new Color(0xBA4A2B),
            "C", new Color(0x29805B)
    );

    static void save(List<App.Point> points, App.Point query) throws IOException {
        int width = 500;
        int height = 500;
        int left = 64;
        int right = 424;
        int top = 54;
        int bottom = 414;
        double min = Math.min(0, Math.min(query.x(), query.y()));
        double max = Math.max(10, Math.max(query.x(), query.y()));

        var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        var g = image.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
            g.drawString("Ближайшие соседи", left, 32);
            g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

            for (int tick = 0; tick <= 5; tick++) {
                double fraction = tick / 5.0;
                int x = left + (int) Math.round((right - left) * fraction);
                int y = bottom - (int) Math.round((bottom - top) * fraction);

                g.setColor(new Color(0xDEE3EB));
                g.drawLine(x, top, x, bottom);
                g.drawLine(left, y, right, y);

                g.setColor(Color.DARK_GRAY);
                String label = String.format(Locale.ROOT, "%.2g", min * (1 - fraction) + max * fraction);
                int labelWidth = g.getFontMetrics().stringWidth(label);
                g.drawString(label, x - labelWidth / 2, bottom + 19);
                g.drawString(label, left - labelWidth - 8, y + 4);
            }
            g.drawRect(left, top, right - left, bottom - top);
            g.drawString("x", right + 16, bottom + 4);
            g.drawString("y", left - 4, top - 14);

            for (var point : points) {
                int x = pixel(point.x(), min, max, left, right);
                int y = pixel(point.y(), min, max, bottom, top);
                g.setColor(COLORS.get(point.label()));
                g.fillOval(x - 6, y - 6, 12, 12);
            }

            int x = pixel(query.x(), min, max, left, right);
            int y = pixel(query.y(), min, max, bottom, top);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(2));
            g.drawOval(x - 9, y - 9, 18, 18);
            g.drawLine(x - 4, y, x + 4, y);
            g.drawLine(x, y - 4, x, y + 4);

            int legendX = left;
            for (String label : List.of("A", "B", "C")) {
                g.setColor(COLORS.get(label));
                g.fillOval(legendX, 460, 12, 12);
                g.setColor(Color.DARK_GRAY);
                g.drawString("Класс " + label, legendX + 19, 470);
                legendX += 90;
            }
            g.setColor(Color.BLACK);
            g.drawOval(legendX, 460, 12, 12);
            g.drawString("Новая точка · " + query.label(), legendX + 19, 470);
        } finally {
            g.dispose();
        }
        Files.createDirectories(OUTPUT.getParent());
        ImageIO.write(image, "png", OUTPUT.toFile());
    }

    private static int pixel(double value, double min, double max, int start, int end) {
        double fraction = (value / 2 - min / 2) / (max / 2 - min / 2);
        return start + (int) Math.round(fraction * (end - start));
    }
}
