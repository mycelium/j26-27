package org.example;

import java.util.EnumMap;
import java.util.Map;


public final class Evaluator {
    private final Map<Sentiment, Map<Sentiment, Integer>> matrix = new EnumMap<>(Sentiment.class);
    private int total;

    public void add(Sentiment expected, Sentiment predicted) {
        if (expected == null || predicted == null) return;
        matrix.computeIfAbsent(expected, k -> new EnumMap<>(Sentiment.class)).merge(predicted, 1, Integer::sum);
        total++;
    }

    public int total() { return total; }

    private int count(Sentiment e, Sentiment p) {
        return matrix.getOrDefault(e, Map.of()).getOrDefault(p, 0);
    }

    public double accuracyAll() {
        int ok = 0;
        for (Sentiment s : Sentiment.values()) ok += count(s, s);
        return total == 0 ? 0 : (double) ok / total;
    }

    public double accuracyDecisive() {
        int ok = 0, n = 0;
        for (Sentiment e : Sentiment.values()) {
            for (Sentiment p : new Sentiment[]{Sentiment.POSITIVE, Sentiment.NEGATIVE}) {
                int c = count(e, p);
                n += c;
                if (e == p) ok += c;
            }
        }
        return n == 0 ? 0 : (double) ok / n;
    }

    public String report() {
        if (total == 0) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("\nСравнение с эталонной разметкой (строки - эталон, столбцы - предсказание):\n");
        sb.append(String.format("%-10s %10s %10s %10s%n", "", "positive", "negative", "neutral"));
        for (Sentiment e : new Sentiment[]{Sentiment.POSITIVE, Sentiment.NEGATIVE, Sentiment.NEUTRAL}) {
            if (!matrix.containsKey(e)) continue;
            sb.append(String.format("%-10s %10d %10d %10d%n", e.label(),
                    count(e, Sentiment.POSITIVE), count(e, Sentiment.NEGATIVE), count(e, Sentiment.NEUTRAL)));
        }
        sb.append(String.format("Accuracy (все отзывы, neutral = ошибка): %.1f%%%n", accuracyAll() * 100));
        sb.append(String.format("Accuracy (только positive/negative):    %.1f%%%n", accuracyDecisive() * 100));
        return sb.toString();
    }
}
