# WordFrequencyCounter

Считает частоту слов в текстовом файле. Два режима: полная загрузка файла в память и потоковое чтение построчно

---

## Требования

- Java 11+ (`Path.of`, `Files.readString`, `Files.newBufferedReader`)

---

## Компиляция

```bash
javac WordFrequencyCounter.java