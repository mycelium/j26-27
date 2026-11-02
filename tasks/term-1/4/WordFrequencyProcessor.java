import java.util.HashMap;
import java.util.Map;

/**
 * конечный автомат для выделения слов и подсчёта их частоты
 * собирает слова посимвольно, склеивает переносы и сохраняет состояние между блоками текста
 */
final class WordFrequencyProcessor {
    private enum State {
        BETWEEN_WORDS,
        READING_WORD,
        AFTER_HYPHEN,
        AFTER_HYPHEN_CARRIAGE_RETURN,
        AFTER_HYPHEN_LINE_BREAK
    }

    private final Map<String, Integer> frequencies = new HashMap<>();
    private final StringBuilder currentWord = new StringBuilder();
    private State state = State.BETWEEN_WORDS;

    public void process(char[] buffer, int length) {
        for (int index = 0; index < length; index++) {
            accept(buffer[index]);
        }
    }

    public Map<String, Integer> finish() {
        saveCurrentWord();
        return frequencies;
    }

    private void accept(char symbol) {
        switch (state) {
            case BETWEEN_WORDS -> acceptBetweenWords(symbol);
            case READING_WORD -> acceptReadingWord(symbol);
            case AFTER_HYPHEN -> acceptAfterHyphen(symbol);
            case AFTER_HYPHEN_CARRIAGE_RETURN -> acceptAfterHyphenCarriageReturn(symbol);
            case AFTER_HYPHEN_LINE_BREAK -> acceptAfterHyphenLineBreak(symbol);
        }
    }

    private void acceptBetweenWords(char symbol) {
        if (Character.isLetterOrDigit(symbol)) {
            appendToCurrentWord(symbol);
            state = State.READING_WORD;
        }
    }

    private void acceptReadingWord(char symbol) {
        if (Character.isLetterOrDigit(symbol)) {
            appendToCurrentWord(symbol);
        } else if (symbol == '-') {
            state = State.AFTER_HYPHEN;
        } else {
            saveCurrentWord();
            state = State.BETWEEN_WORDS;
        }
    }

    private void acceptAfterHyphen(char symbol) {
        if (Character.isLetterOrDigit(symbol)) {
            currentWord.append('-');
            appendToCurrentWord(symbol);
            state = State.READING_WORD;
        } else if (symbol == '\r') {
            state = State.AFTER_HYPHEN_CARRIAGE_RETURN;
        } else if (symbol == '\n') {
            state = State.AFTER_HYPHEN_LINE_BREAK;
        } else {
            saveCurrentWord();
            state = State.BETWEEN_WORDS;
        }
    }

    private void acceptAfterHyphenCarriageReturn(char symbol) {
        if (symbol == '\n') {
            state = State.AFTER_HYPHEN_LINE_BREAK;
        } else {
            acceptAfterHyphenLineBreak(symbol);
        }
    }

    private void acceptAfterHyphenLineBreak(char symbol) {
        if (Character.isLetterOrDigit(symbol)) {
            appendToCurrentWord(symbol);
            state = State.READING_WORD;
        } else {
            saveCurrentWord();
            state = State.BETWEEN_WORDS;
        }
    }

    private void appendToCurrentWord(char symbol) {
        currentWord.append(Character.toLowerCase(symbol));
    }

    private void saveCurrentWord() {
        if (currentWord.isEmpty()) {
            return;
        }

        frequencies.merge(currentWord.toString(), 1, Integer::sum);
        currentWord.setLength(0);
    }
}
