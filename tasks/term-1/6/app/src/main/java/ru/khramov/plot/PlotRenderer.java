package ru.khramov.plot;

import ru.khramov.point.LabeledPoint;
import ru.khramov.point.Point;

import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PlotRenderer {
	static {
		System.setProperty("java.awt.headless", "true");
	}

	static final int WIDTH = 800;
	static final int HEIGHT = 600;
	static final int MARGIN = 60;

	private static final int POINT_RADIUS = 5;
	private static final int NEW_POINT_RADIUS = 9;
	private static final int TICKS = 5;
	private static final int TICK_LENGTH = 5;
	private static final double PADDING = 1.0;
	private static final Color[] PALETTE = {
			new Color(220, 50, 47),
			new Color(38, 139, 210),
			new Color(60, 160, 80),
			new Color(230, 140, 20),
			new Color(140, 80, 190)
	};

	record Bounds(double minX, double maxX, double minY, double maxY) {
	}

	public void render(List<LabeledPoint> points, Point newPoint, String newLabel, File output) throws IOException {
		if (points.isEmpty()) {
			throw new IllegalArgumentException("No points to render");
		}

		Bounds bounds = findBounds(points, newPoint);
		Map<String, Color> colors = assignColors(points);

		BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		try {
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			drawAxes(g, bounds);
			drawPoints(g, points, colors, bounds);
			if (newPoint != null) {
				drawNewPoint(g, newPoint, newLabel, colors, bounds);
			}
			drawLegend(g, colors);
		} finally {
			g.dispose();
		}

		ImageIO.write(image, "png", output);
	}

	static int toPixelX(double x, Bounds bounds) {
		double ratio = (x - bounds.minX()) / (bounds.maxX() - bounds.minX());
		return (int) Math.round(MARGIN + ratio * (WIDTH - 2 * MARGIN));
	}

	static int toPixelY(double y, Bounds bounds) {
		double ratio = (y - bounds.minY()) / (bounds.maxY() - bounds.minY());
		return (int) Math.round(HEIGHT - MARGIN - ratio * (HEIGHT - 2 * MARGIN));
	}

	static Bounds findBounds(List<LabeledPoint> points, Point newPoint) {
		double minX = Double.POSITIVE_INFINITY;
		double maxX = Double.NEGATIVE_INFINITY;
		double minY = Double.POSITIVE_INFINITY;
		double maxY = Double.NEGATIVE_INFINITY;

		for (LabeledPoint labeledPoint : points) {
			minX = Math.min(minX, labeledPoint.point().x());
			maxX = Math.max(maxX, labeledPoint.point().x());
			minY = Math.min(minY, labeledPoint.point().y());
			maxY = Math.max(maxY, labeledPoint.point().y());
		}
		if (newPoint != null) {
			minX = Math.min(minX, newPoint.x());
			maxX = Math.max(maxX, newPoint.x());
			minY = Math.min(minY, newPoint.y());
			maxY = Math.max(maxY, newPoint.y());
		}

		return new Bounds(minX - PADDING, maxX + PADDING, minY - PADDING, maxY + PADDING);
	}

	private Map<String, Color> assignColors(List<LabeledPoint> points) {
		Map<String, Color> colors = new LinkedHashMap<>();
		for (LabeledPoint labeledPoint : points) {
			colors.computeIfAbsent(labeledPoint.label(), label -> PALETTE[colors.size() % PALETTE.length]);
		}
		return colors;
	}

	private void drawAxes(Graphics2D g, Bounds bounds) {
		FontMetrics metrics = g.getFontMetrics();

		for (int i = 0; i <= TICKS; i++) {
			double x = bounds.minX() + (bounds.maxX() - bounds.minX()) * i / TICKS;
			double y = bounds.minY() + (bounds.maxY() - bounds.minY()) * i / TICKS;
			int pixelX = toPixelX(x, bounds);
			int pixelY = toPixelY(y, bounds);

			g.setColor(new Color(230, 230, 230));
			g.drawLine(pixelX, MARGIN, pixelX, HEIGHT - MARGIN);
			g.drawLine(MARGIN, pixelY, WIDTH - MARGIN, pixelY);

			g.setColor(Color.BLACK);
			g.drawLine(pixelX, HEIGHT - MARGIN, pixelX, HEIGHT - MARGIN + TICK_LENGTH);
			g.drawLine(MARGIN - TICK_LENGTH, pixelY, MARGIN, pixelY);

			String xLabel = String.format(Locale.ROOT, "%.1f", x);
			String yLabel = String.format(Locale.ROOT, "%.1f", y);
			g.drawString(xLabel, pixelX - metrics.stringWidth(xLabel) / 2,
					HEIGHT - MARGIN + TICK_LENGTH + metrics.getAscent() + 2);
			g.drawString(yLabel, MARGIN - TICK_LENGTH - metrics.stringWidth(yLabel) - 4,
					pixelY + metrics.getAscent() / 2 - 1);
		}

		g.setColor(Color.BLACK);
		g.drawRect(MARGIN, MARGIN, WIDTH - 2 * MARGIN, HEIGHT - 2 * MARGIN);
	}

	private void drawPoints(Graphics2D g, List<LabeledPoint> points, Map<String, Color> colors, Bounds bounds) {
		for (LabeledPoint labeledPoint : points) {
			int pixelX = toPixelX(labeledPoint.point().x(), bounds);
			int pixelY = toPixelY(labeledPoint.point().y(), bounds);

			g.setColor(colors.get(labeledPoint.label()));
			g.fillOval(pixelX - POINT_RADIUS, pixelY - POINT_RADIUS, 2 * POINT_RADIUS, 2 * POINT_RADIUS);
		}
	}

	private void drawNewPoint(Graphics2D g, Point newPoint, String newLabel, Map<String, Color> colors, Bounds bounds) {
		int pixelX = toPixelX(newPoint.x(), bounds);
		int pixelY = toPixelY(newPoint.y(), bounds);
		int diameter = 2 * NEW_POINT_RADIUS;

		g.setColor(colors.getOrDefault(newLabel, Color.GRAY));
		g.fillOval(pixelX - NEW_POINT_RADIUS, pixelY - NEW_POINT_RADIUS, diameter, diameter);
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(2));
		g.drawOval(pixelX - NEW_POINT_RADIUS, pixelY - NEW_POINT_RADIUS, diameter, diameter);
		g.setStroke(new BasicStroke(1));
		g.drawString("New: " + newLabel, pixelX + NEW_POINT_RADIUS + 4, pixelY - NEW_POINT_RADIUS);
	}

	private void drawLegend(Graphics2D g, Map<String, Color> colors) {
		int x = WIDTH - MARGIN - 90;
		int y = MARGIN + 15;

		g.setColor(new Color(255, 255, 255, 220));
		g.fillRect(x - 10, y - 15, 90, 10 + 22 * colors.size());
		g.setColor(Color.BLACK);
		g.drawRect(x - 10, y - 15, 90, 10 + 22 * colors.size());

		for (Map.Entry<String, Color> entry : colors.entrySet()) {
			g.setColor(entry.getValue());
			g.fillOval(x, y - 6, 12, 12);
			g.setColor(Color.BLACK);
			g.drawString(entry.getKey(), x + 20, y + 5);
			y += 22;
		}
	}
}
