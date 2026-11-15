package org.example;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


public final class CsvParser {
    private CsvParser() {}

    public static List<String[]> parse(Reader in) throws IOException {
        List<String[]> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        boolean fieldStarted = false;
        int c;
        int next = -2;
        boolean first = true;

        while (true) {
            if (next != -2) { c = next; next = -2; } else { c = in.read(); }
            if (c == -1) break;
            if (first) {
                first = false;
                if (c == '\uFEFF') continue;
            }
            if (quoted) {
                if (c == '"') {
                    next = in.read();
                    if (next == '"') { field.append('"'); next = -2; }
                    else quoted = false;
                } else {
                    field.append((char) c);
                }
            } else if (c == '"' && field.length() == 0) {
                quoted = true;
                fieldStarted = true;
            } else if (c == ',') {
                row.add(field.toString());
                field.setLength(0);
                fieldStarted = true;
            } else if (c == '\n' || c == '\r') {
                if (c == '\r') {
                    next = in.read();
                    if (next == '\n') next = -2;
                }
                if (fieldStarted || field.length() > 0 || !row.isEmpty()) {
                    row.add(field.toString());
                    rows.add(row.toArray(new String[0]));
                }
                row.clear();
                field.setLength(0);
                fieldStarted = false;
            } else {
                field.append((char) c);
                fieldStarted = true;
            }
        }
        if (fieldStarted || field.length() > 0 || !row.isEmpty()) {
            row.add(field.toString());
            rows.add(row.toArray(new String[0]));
        }
        return rows;
    }
}

