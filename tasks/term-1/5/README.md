# Sentiment analysis отзывов на фильмы (Stanford CoreNLP)

Приложение читает набор отзывов и для каждого ставит `positive`, `negative` или `neutral`.


## Запуск

Все команды выполняются из корня проекта. Пути в --input и --output считаются от корня проекта.
Первая сборка скачивает CoreNLP и модели.

```bash

# первые 500 отзывов IMDB, 4 потока
./gradlew run --args="--input 'data/IMDB Dataset.csv' --limit 500 --threads 4 --output result.csv"

./gradlew test

# показать справку приложения 
./gradlew run --args="--help"
```

Вход: `.csv` (колонка `review`/`text`/`content`/`comment`, либо `--column имя`) или текстовый файл,
один отзыв на строку. Если в CSV есть колонка `sentiment`/`label`, в конце печатается сравнение с эталоном.

Результат (`sentiments.csv` по умолчанию): `id, predicted, net_score, sentences, [expected], review_preview`.

## Как ставится оценка

CoreNLP (`tokenize, ssplit, parse, sentiment`) даёт каждому предложению класс 0..4
(very negative .. very positive). Для отзыва считается `net = сумма (класс - 2)` по всем предложениям:

* `net > 0` — positive
* `net < 0` — negative
* `net == 0` — neutral (эмоционально окрашенных предложений нет или они уравновешивают друг друга)

Порог настраивается `--threshold` (`|net| <= порога` → neutral).
