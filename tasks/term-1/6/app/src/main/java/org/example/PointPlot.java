package org.example;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.knowm.xchart.ChartEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;

final class PointPlot {
    static Path savePlot(List<LabeledPoint> points, Point query, String predictedClass) throws IOException {
        return saveImage(buildChart(points, query, predictedClass));
    }

    private static XYChart buildChart(List<LabeledPoint> points, Point query, String predictedClass) {
        Map<String, List<Point>> pointsByClass = new LinkedHashMap<>();
        for (LabeledPoint labeledPoint : points) {
            pointsByClass.computeIfAbsent(labeledPoint.label(), label -> new ArrayList<>())
                    .add(labeledPoint.point());
        }

        XYChart chart = new XYChartBuilder().width(850).height(760)
                .title("KNN Point Classification").xAxisTitle("x").yAxisTitle("y").build();
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setLegendLayout(Styler.LegendLayout.Horizontal);
        chart.getStyler().setMarkerSize(11);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotGridLinesColor(new Color(0xE1E6ED));
        chart.getStyler().setSeriesColors(new Color[]{new Color(0x2563EB), new Color(0xC46808), new Color(0x0F766E)});

        for (Map.Entry<String, List<Point>> series : pointsByClass.entrySet()) {
            double[] x = series.getValue().stream().mapToDouble(Point::x).toArray();
            double[] y = series.getValue().stream().mapToDouble(Point::y).toArray();
            chart.addSeries("Class " + series.getKey(), x, y);
        }

        String queryLabel = "Query (" + query.x() + ", " + query.y() + ") \u2192 " + predictedClass;
        XYSeries querySeries = chart.addSeries(queryLabel, new double[]{query.x()}, new double[]{query.y()});
        querySeries.setMarker(SeriesMarkers.CROSS);
        querySeries.setMarkerColor(Color.BLACK);
        return chart;
    }

    private static Path saveImage(XYChart chart) throws IOException {
        Path directory = Files.createDirectories(Path.of("images"));
        byte[] image = ChartEncoder.getBytes(chart, "png");
        for (int index = 0; ; index++) {
            String filename = index == 0 ? "points.png" : "points-" + index + ".png";
            Path file = directory.resolve(filename);
            OutputStream output;
            try {
                output = Files.newOutputStream(file, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
            } catch (FileAlreadyExistsException exception) {
                continue;
            }

            try (output) {
                output.write(image);
            } catch (IOException exception) {
                Files.deleteIfExists(file);
                throw exception;
            }
            return file;
        }
    }
}
