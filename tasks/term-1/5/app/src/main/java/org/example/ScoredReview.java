package org.example;

import java.util.List;

public record ScoredReview(List<Integer> scores, Sentiment expected) {

    public double mean() {
        if (scores.size() == 0) return -1;
        return scores.stream().mapToInt(Integer::intValue).average().orElse(2.0);
    }
}