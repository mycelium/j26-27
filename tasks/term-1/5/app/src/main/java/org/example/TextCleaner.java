package org.example;

import java.util.regex.Pattern;

public final class TextCleaner {
    private static final Pattern TAG = Pattern.compile("</?[a-zA-Z][^>]*>");
    private static final Pattern SPACES = Pattern.compile("\\s+");

    private TextCleaner() {}

    public static String clean(String s) {
        if (s == null) return "";
        String t = TAG.matcher(s).replaceAll(" ");
        t = t.replace("&quot;", "\"").replace("&#39;", "'").replace("&lt;", "<")
             .replace("&gt;", ">").replace("&amp;", "&");
        return SPACES.matcher(t).replaceAll(" ").trim();
    }
}