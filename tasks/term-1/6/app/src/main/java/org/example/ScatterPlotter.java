package org.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.imageio.ImageIO;


public final class ScatterPlotter {

    private static final Color[] PALETTE = {
        new Color(0xE74C3C), new Color(0x2E86DE), new Color(0x27AE60),
        new Color(0xF39C12), new Color(0x8E44AD), new Color(0x16A085)
    };

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 760;
    private static final int LEFT = 80;
    private static final int RIGHT = 170;
    private static final int TOP = 60;
    private static final int BOTTOM = 70;

    private final KnnClassifier classifier;
    private final List<LabeledPoint> queries = new ArrayList<>();
    private final Map<String, Color> colors = new LinkedHashMap<>();
    private final double minX, maxX, minY, maxY;

    public ScatterPlotter(KnnClassifier classifier) {
        this.classifier = classifier;
        for (LabeledPoint lp : classifier.getTraining()) {
            colors.computeIfAbsent(lp.label(), l -> PALETTE[colors.size() % PALETTE.length]);
        }
        double x0 = Double.MAX_VALUE, x1 = -Double.MAX_VALUE;
        double y0 = Double.MAX_VALUE, y1 = -Double.MAX_VALUE;
        for (LabeledPoint lp : classifier.getTraining()) {
            x0 = Math.min(x0, lp.point().x());
            x1 = Math.max(x1, lp.point().x());
            y0 = Math.min(y0, lp.point().y());
            y1 = Math.max(y1, lp.point().y());
        }
        double padX = Math.max((x1 - x0) * 0.08, 0.5);
        double padY = Math.max((y1 - y0) * 0.08, 0.5);
        this.minX = x0 - padX;
        this.maxX = x1 + padX;
        this.minY = y0 - padY;
        this.maxY = y1 + padY;
    }

    public void addQuery(Point p, String label) {
        queries.add(new LabeledPoint(p, label));
    }

    public void save(File file) throws IOException {
        BufferedImage img = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = img.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WIDTH, HEIGHT);

            int plotW = WIDTH - LEFT - RIGHT;
            int plotH = HEIGHT - TOP - BOTTOM;

            drawDecisionRegions(g, plotW, plotH);
            drawGridAndAxes(g, plotW, plotH);
            drawTrainingPoints(g, plotW, plotH);
            drawQueries(g, plotW, plotH);
            drawLegend(g, plotW);
            drawTitle(g);
        } finally {
            g.dispose();
        }
        File parent = file.getAbsoluteFile().getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        ImageIO.write(img, "png", file);
    }

    private double toPx(double x, int plotW) {
        return LEFT + (x - minX) / (maxX - minX) * plotW;
    }

    private double toPy(double y, int plotH) {
        return TOP + plotH - (y - minY) / (maxY - minY) * plotH;
    }

    private void drawDecisionRegions(Graphics2D g, int plotW, int plotH) {
        int cell = 8;
        for (int px = 0; px < plotW; px += cell) {
            for (int py = 0; py < plotH; py += cell) {
                double x = minX + (px + cell / 2.0) / plotW * (maxX - minX);
                double y = maxY - (py + cell / 2.0) / plotH * (maxY - minY);
                Color c = colors.get(classifier.classify(new Point(x, y)));
                g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 45));
                g.fillRect(LEFT + px, TOP + py, Math.min(cell, plotW - px), Math.min(cell, plotH - py));
            }
        }
    }

    private void drawGridAndAxes(Graphics2D g, int plotW, int plotH) {
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
        FontMetrics fm = g.getFontMetrics();
        int ticks = 10;
        for (int i = 0; i <= ticks; i++) {
            double vx = minX + (maxX - minX) * i / ticks;
            int px = (int) Math.round(toPx(vx, plotW));
            g.setColor(new Color(0, 0, 0, 25));
            g.drawLine(px, TOP, px, TOP + plotH);
            g.setColor(Color.DARK_GRAY);
            String s = String.format(Locale.ROOT, "%.1f", vx);
            g.drawString(s, px - fm.stringWidth(s) / 2, TOP + plotH + 18);

            double vy = minY + (maxY - minY) * i / ticks;
            int py = (int) Math.round(toPy(vy, plotH));
            g.setColor(new Color(0, 0, 0, 25));
            g.drawLine(LEFT, py, LEFT + plotW, py);
            g.setColor(Color.DARK_GRAY);
            s = String.format(Locale.ROOT, "%.1f", vy);
            g.drawString(s, LEFT - 8 - fm.stringWidth(s), py + 4);
        }
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRect(LEFT, TOP, plotW, plotH);

        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        fm = g.getFontMetrics();
        String xl = "x";
        g.drawString(xl, LEFT + plotW / 2 - fm.stringWidth(xl) / 2, TOP + plotH + 45);
        String yl = "y";
        g.drawString(yl, LEFT - 55, TOP + plotH / 2);
    }

    private void drawTrainingPoints(Graphics2D g, int plotW, int plotH) {
        double r = 5;
        for (LabeledPoint lp : classifier.getTraining()) {
            double cx = toPx(lp.point().x(), plotW);
            double cy = toPy(lp.point().y(), plotH);
            Ellipse2D dot = new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r);
            g.setColor(colors.get(lp.label()));
            g.fill(dot);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(1f));
            g.draw(dot);
        }
    }

    private void drawQueries(Graphics2D g, int plotW, int plotH) {
        for (LabeledPoint q : queries) {
            double cx = toPx(q.point().x(), plotW);
            double cy = toPy(q.point().y(), plotH);
            Color c = colors.getOrDefault(q.label(), Color.BLACK);

            g.setColor(new Color(0, 0, 0, 90));
            g.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f, 
                    new float[] {4f, 4f}, 0f));
            for (KnnClassifier.Neighbor n : classifier.nearestNeighbors(q.point())) {
                g.drawLine((int) cx, (int) cy, (int) toPx(n.point().point().x(), plotW), (int) toPy(n.point().point().y(), plotH));
            }

            Path2D star = star(cx, cy, 13, 5.5);
            g.setColor(c);
            g.fill(star);
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1.8f));
            g.draw(star);
        }
    }

    private static Path2D star(double cx, double cy, double outer, double inner) {
        Path2D p = new Path2D.Double();
        for (int i = 0; i < 10; i++) {
            double ang = -Math.PI / 2 + i * Math.PI / 5;
            double rad = (i % 2 == 0) ? outer : inner;
            double x = cx + rad * Math.cos(ang);
            double y = cy + rad * Math.sin(ang);
            if (i == 0) {
                p.moveTo(x, y);
            } else {
                p.lineTo(x, y);
            }
        }
        p.closePath();
        return p;
    }

    private void drawLegend(Graphics2D g, int plotW) {
        int x = LEFT + plotW + 25;
        int y = TOP + 10;
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        g.setColor(Color.BLACK);
        g.drawString("Классы", x, y);
        y += 25;
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 13));
        for (Map.Entry<String, Color> e : colors.entrySet()) {
            g.setColor(e.getValue());
            g.fill(new Ellipse2D.Double(x, y - 10, 12, 12));
            g.setColor(Color.BLACK);
            g.drawString(e.getKey(), x + 20, y);
            y += 22;
        }
        if (!queries.isEmpty()) {
            y += 8;
            g.setColor(Color.LIGHT_GRAY);
            g.fill(star(x + 6, y - 4, 8, 3.5));
            g.setColor(Color.BLACK);
            g.setStroke(new BasicStroke(1.2f));
            g.draw(star(x + 6, y - 4, 8, 3.5));
            g.drawString("новая точка", x + 20, y);
        }
    }

    private void drawTitle(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));
        g.drawString("KNN-классификация (k = " + classifier.getK() + ", точек в выборке: "
                + classifier.getTraining().size() + ")", LEFT, 36);
    }
}
