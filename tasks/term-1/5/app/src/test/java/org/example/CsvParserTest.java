package org.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CsvParserTest {
    @Test void handlesQuotesCommasAndNewlines() throws IOException {
        String csv = "\uFEFFreview,sentiment\r\n\"Great, really \"\"great\"\"\nmovie\",positive\r\nplain,negative\r\n";
        List<String[]> rows = CsvParser.parse(new StringReader(csv));
        assertEquals(3, rows.size());
        assertArrayEquals(new String[]{"review", "sentiment"}, rows.get(0));
        assertArrayEquals(new String[]{"Great, really \"great\"\nmovie", "positive"}, rows.get(1));
        assertArrayEquals(new String[]{"plain", "negative"}, rows.get(2));
    }

    @Test void lastRowWithoutTrailingNewline() throws IOException {
        List<String[]> rows = CsvParser.parse(new StringReader("a,b\n1,2"));
        assertEquals(2, rows.size());
        assertArrayEquals(new String[]{"1", "2"}, rows.get(1));
    }
}
