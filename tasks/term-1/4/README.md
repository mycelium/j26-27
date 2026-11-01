# WordFrequencyCounter

Word frequency counter for a text file.

Two modes:
- **FULL-LOAD** — loads the entire file into memory
- **STREAMING** — line-by-line reading (works for files larger than available RAM)

## Usage

```powershell
java WordFrequencyCounter <file> [--stream] [--by-freq]
```

| Flag        | Description                                          |
|-------------|------------------------------------------------------|
| `--stream`  | Streaming mode (line-by-line)                        |
| `--by-freq` | Sort by frequency descending instead of alphabetically |

### Examples

```powershell
# Full-load mode (entire file in memory), alphabetical order
java WordFrequencyCounter sample.txt

# Streaming mode
java WordFrequencyCounter sample.txt --stream

# Sort by frequency
java WordFrequencyCounter sample.txt --by-freq

# Both flags
java WordFrequencyCounter sample.txt --stream --by-freq
```

### Create a test file

```powershell
"Hello world! Hello Java. World of Java and streams." | Out-File -Encoding utf8 sample.txt
```

### Example output

```
Using FULL-LOAD mode ...
Word frequencies (5 unique words):
----------------------------------------
and                            1
hello                          2
java                           2
of                             1
streams                        1
world                          2
```
```