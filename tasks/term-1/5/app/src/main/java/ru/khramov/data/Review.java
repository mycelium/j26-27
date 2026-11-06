package ru.khramov.data;

import ru.khramov.aggregate.Sentiment;

public record Review(String text, Sentiment expected) {}
