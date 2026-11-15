package org.example;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public final class ReviewReader {
    private static final List<String> TEXT_COLUMNS = List.of("review", "text", "content", "comment");
    private static final List<String> LABEL_COLUMNS = List.of("sentiment", "label");

    private ReviewReader() {}

    public static List<Review> read(Path file, String textColumn, int limit) throws IOException {
        String name = file.getFileName().toString().toLowerCase(Locale.ROOT);
        return name.endsWith(".csv") ? readCsv(file, textColumn, limit) : readLines(file, limit);
    }

    static List<Review> readLines(Path file, int limit) throws IOException {
        List<Review> out = new ArrayList<>();
        for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
            if (line.isBlank()) continue;
            out.add(new Review(out.size() + 1, TextCleaner.clean(line), null));
            if (limit > 0 && out.size() >= limit) break;
        }
        return out;
    }

    static List<Review> readCsv(Path file, String textColumn, int limit) throws IOException {
        List<String[]> rows;
        try (Reader r = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            rows = CsvParser.parse(r);
        }
        return fromRows(rows, textColumn, limit);
    }

    static List<Review> fromRows(List<String[]> rows, String textColumn, int limit) {
        if (rows.isEmpty()) return List.of();
        String[] header = rows.get(0);
        int textIdx = find(header, textColumn != null ? List.of(textColumn.toLowerCase(Locale.ROOT)) : TEXT_COLUMNS);
        if (textIdx < 0) {
            throw new IllegalArgumentException("Не нашёл колонку с текстом отзыва. Колонки в файле: "
                    + Arrays.toString(header) + ". Укажите нужную через --column.");
        }
        int labelIdx = find(header, LABEL_COLUMNS);

        List<Review> out = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length <= textIdx) continue;
            String text = TextCleaner.clean(row[textIdx]);
            if (text.isEmpty()) continue;
            Sentiment expected = (labelIdx >= 0 && row.length > labelIdx) ? Sentiment.fromLabel(row[labelIdx]) : null;
            out.add(new Review(out.size() + 1, text, expected));
            if (limit > 0 && out.size() >= limit) break;
        }
        return out;
    }

    private static int find(String[] header, List<String> names) {
        for (int i = 0; i < header.length; i++) {
            if (names.contains(header[i].trim().toLowerCase(Locale.ROOT))) return i;
        }
        return -1;
    }
}