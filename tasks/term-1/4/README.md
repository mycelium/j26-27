# Lab 4: Word Frequency Counter

Group: 40102
Name: Ovi Md Shamin Yasir

## Files

`WordFrequencyCounter.java` has:

- `countWords` - variant 1. It loads the whole file into memory and then counts the words.
- `countWordsStreaming` - variant 2. It reads the file in small parts (8192 characters) and counts the words on the go.
- `printFrequencies` - prints the words, the most frequent first. Words with the same count are in alphabetical order. With a second argument it prints only the first N words.
- `main` - runs the counter, prints the 10 most frequent words, the number of different words and the time.

## What is a word

A word is a group of letters and digits (letters of any language, so Russian words work too). Everything else (space, dot, comma, new line, `_`) is a separator. Words are counted in lower case, so `The` and `the` are the same word.

Both variants use the same method (`addChars`) for this, so they must give the same result. `main` checks it.

## How to run

```
javac WordFrequencyCounter.java
java WordFrequencyCounter                   (small test text, both variants)
java WordFrequencyCounter file.txt          (both variants)
java WordFrequencyCounter file.txt whole    (only variant 1)
java WordFrequencyCounter file.txt stream   (only variant 2)
```

## The two variants

**Variant 1 (whole file).** `Files.readAllBytes` reads the file, then the bytes are turned into a `String` and into a `char[]`. So the program needs several times more memory than the size of the file. A file bigger than the free memory can not be processed.

**Variant 2 (streaming).** The file is read with a `BufferedReader` into a buffer of 8192 characters. After each part the characters are checked one by one. The program keeps only three things in memory: the buffer, the word that is being read now and the map with the counters. The memory does not depend on the size of the file. It depends only on the number of different words.

One small detail: the end of the buffer can cut a word in the middle. That is why the `StringBuilder word` lives outside the reading loop: the rest of the word comes in the next part and it continues in the same `word`.

The map is the only thing that grows. If a file has so many different words that the map does not fit into memory, variant 2 will fail too.

## Results

Small test text (it has a Russian line too):

```
--- Variant 1: whole file in memory
the: 4
one: 3
and: 2
cat: 2
dog: 2
two: 2
кот: 2
собака: 2
ran: 1
sat: 1
Different words: 14
Both results are the same: true
```

Big file test. I made a text file of about 257 MB from random words (5812 different words, some Russian, some with digits). To make a big file from any text you can copy it many times, for example in Linux `for i in $(seq 1 300); do cat book.txt; done > big.txt`, or in cmd `for /L %i in (1,1,300) do type book.txt >> big.txt`.

I limited the memory of Java to 64 MB with `-Xmx64m`. The file is 4 times bigger than that.

```
java -Xmx64m WordFrequencyCounter big.txt whole
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.base/java.nio.file.Files.readAllBytes(Files.java:3288)
	at WordFrequencyCounter.countWords(WordFrequencyCounter.java:18)

java -Xmx64m WordFrequencyCounter big.txt stream
--- Variant 2: streaming (3921 ms)
emubcrd: 3601911
sbqgbcn: 1800655
chcrnbsd: 1199165
uusbs: 900467
bhbrejne: 718732
dsjrvfdssu: 599346
ldrwc: 515273
tg: 450013
vrnykosol: 400486
hzfwyh: 358942
Different words: 5812
```

Variant 1 fails with `OutOfMemoryError`, variant 2 works with the same 64 MB.

With a big memory (`-Xmx3g`) both variants work on the same file:

```
Variant 1: 6405 ms
Variant 2: 4425 ms
Both results are the same: true
```

So streaming is not slower: variant 1 also has to read and copy the whole file first. The time depends on the computer, so the numbers can be different on another one.

