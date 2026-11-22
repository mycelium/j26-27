
# 5. Java: Sentiment Analysis with CoreNLP

**Файлы:**
- `app/src/main/java/org/example/App.java` — основная логика анализа тональности.
- `app/build.gradle.kts` — зависимости (Stanford CoreNLP + models, OpenCSV).
- `README.md` — описание решения.

## Постановка задачи
Используя датасет отзывов на фильмы (IMDB Dataset) и библиотеку Stanford CoreNLP, разработать приложение, которое для каждого отзыва проставляет оценку:
- positive
- negative
- neutral

## Детали реализации

### Pipeline
Создаётся один раз при запуске программы:
annotators = tokenize, ssplit, pos, parse, sentiment


### Анализ тональности
CoreNLP возвращает оценку для **каждого предложения** в диапазоне 0–4:
- 0 — Very negative
- 1 — Negative
- 2 — Neutral
- 3 — Positive
- 4 — Very positive

Для всего отзыва берётся **среднее арифметическое** score по всем предложениям.

Пороги:
- avg ≤ 1.4 → negative
- avg ≥ 2.6 → positive
- иначе → neutral

### Очистка текста
Перед анализом из отзывов удаляются HTML-теги `<br />` и лишние пробелы.

### Запуск
```bash
# обработать 10 отзывов
./gradlew run --args="IMDB Dataset.csv 10"

# обработать 30 отзывов (по умолчанию 20)
./gradlew run --args="IMDB Dataset.csv 30"
```

Первый запуск скачивает модели (~500 МБ) и инициализирует pipeline (30–60 секунд).

## Результаты работы
На 10 отзывах:
- Positive  : 2
- Negative  : 1
- Neutral   : 7
