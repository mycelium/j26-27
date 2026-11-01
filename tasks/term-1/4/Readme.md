# 4. Java: Word Frequency Counter

**Файл:** `WordFrequencyCounter.java` — подсчёт частоты слов в текстовом файле двумя способами.

## Запуск

```bash
javac WordFrequencyCounter.java
java WordFrequencyCounter <файл>            # оба варианта, время и сравнение результатов
java WordFrequencyCounter <файл> full       # только загрузка всего файла
java WordFrequencyCounter <файл> stream     # только потоковая обработка
```

## Реализация

- `countWords(Path)` — **вариант 1**: весь файл читается в память (`Files.readAllBytes`), затем разбивается на слова.
- `countWordsStreaming(Path)` — **вариант 2**: файл читается блоками по 8192 символа, слово собирается
  посимвольно (в том числе через границы блоков). В памяти находятся только буфер и словарь слов,
  поэтому размер файла не ограничен объёмом оперативной памяти (память зависит от числа уникальных слов).
- `printFrequencies(Map)` — выводит общее число слов, число уникальных слов и 20 самых частых слов
  (по убыванию частоты, при равенстве — по алфавиту).

Слово — непрерывная последовательность букв и цифр (Unicode), регистр не учитывается, кодировка UTF-8.
Оба варианта используют одну и ту же функцию разбора, поэтому результаты совпадают (программа это проверяет: `results equal: true`).

## Проверка на файле больше доступной памяти

Создаём файл 300 МБ и ограничиваем память JVM до 64 МБ:

```bash
yes "the quick brown fox jumps over the lazy dog" | head -c 300000000 > big.txt
java -Xmx64m WordFrequencyCounter big.txt
```

Ожидаемый результат:

```
full load: OutOfMemoryError (file does not fit into memory)
streaming: ... ms

total words: 61363637, unique words: 9
the                  13636364
brown                6818182
fox                  6818182
...
```
