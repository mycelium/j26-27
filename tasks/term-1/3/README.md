# Functional Sets

Лабораторная работа по Scala.

В работе реализованы функциональные множества.

Множество задается функцией:

```scala
type Set = Int => Boolean
```

Если элемент принадлежит множеству, функция возвращает `true`, иначе `false`.

## Реализованные функции

- `singletonSet`
- `union`
- `intersect`
- `diff`
- `filter`
- `forall`
- `exists`
- `map`

Для функций `forall` и `exists` проверяются значения от `-1000` до `1000`.

## Запуск

```bash
sbt run
```