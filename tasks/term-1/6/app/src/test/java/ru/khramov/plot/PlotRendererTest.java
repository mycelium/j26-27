package ru.khramov.plot;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.khramov.plot.PlotRenderer.Bounds;
import ru.khramov.point.LabeledPoint;
import ru.khramov.point.Point;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlotRendererTest {
	private final Bounds bounds = new Bounds(0, 10, 0, 10);

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Test
	public void toPixelXCenter() {
		assertEquals(400, PlotRenderer.toPixelX(5, bounds));
	}

	@Test
	public void toPixelXEdges() {
		assertEquals(PlotRenderer.MARGIN, PlotRenderer.toPixelX(0, bounds));
		assertEquals(PlotRenderer.WIDTH - PlotRenderer.MARGIN, PlotRenderer.toPixelX(10, bounds));
	}

	@Test
	public void toPixelYIsFlipped() {
		assertEquals(PlotRenderer.HEIGHT - PlotRenderer.MARGIN, PlotRenderer.toPixelY(0, bounds));
		assertEquals(PlotRenderer.MARGIN, PlotRenderer.toPixelY(10, bounds));
	}

	@Test
	public void findBoundsIncludesNewPoint() {
		List<LabeledPoint> points = List.of(
				new LabeledPoint("A", new Point(1, 1)),
				new LabeledPoint("B", new Point(3, 4))
		);

		Bounds result = PlotRenderer.findBounds(points, new Point(10, -5));

		assertEquals(0, result.minX(), 1e-9);
		assertEquals(11, result.maxX(), 1e-9);
		assertEquals(-6, result.minY(), 1e-9);
		assertEquals(5, result.maxY(), 1e-9);
	}

	@Test
	public void renderCreatesNonEmptyFile() throws IOException {
		List<LabeledPoint> points = List.of(
				new LabeledPoint("A", new Point(1, 1)),
				new LabeledPoint("B", new Point(8, 2)),
				new LabeledPoint("C", new Point(5, 8))
		);
		File output = new File(folder.getRoot(), "plot.png");

		new PlotRenderer().render(points, new Point(4, 4), "A", output);

		assertTrue(output.exists());
		assertTrue(output.length() > 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void renderEmptyPointsTest() throws IOException {
		new PlotRenderer().render(List.of(), null, null, new File(folder.getRoot(), "plot.png"));
	}
}
