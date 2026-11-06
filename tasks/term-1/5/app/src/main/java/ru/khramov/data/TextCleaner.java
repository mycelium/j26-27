package ru.khramov.data;

public class TextCleaner {
	public String clean(String text) {
		return text.replaceAll("<br\\s*/?>", " ").replaceAll("\\s+", " ").trim();
	}
}
