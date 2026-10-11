# 1. Java: Matrix multiplication

Метод замера времени был взят отсюда: https://www.geeksforgeeks.org/java/java-system-nanotime-vs-system-currenttimemillis/

Результат до оптимизации:

```bash
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 4227618 ms
Time for original method: 7967496 ms
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 4076182 ms
Time for original method: 8176210 ms
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 3977196 ms
Time for original method: 8884756 ms
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 4263426 ms
Time for original method: 9308657 ms
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 4044833 ms
Time for original method: 9431759 ms
```

### Попытка оптимизации №1

Была попытка превратить двумерные массивы в линейные и умножить их так. 

Время считалось как общее для метода, так и локально для умножения, чтобы сравнить эффективность именно подхода с умножением, если допустить, что на вход будут подаваться массивы линейные.

```bash
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 3921120 ms
Time for original method: 9033688 ms
Time for optimized multiplication: 3730239 ms
Time for linear method: 4275359 ms
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 4074148 ms
Time for original method: 9850339 ms
Time for optimized multiplication: 3745418 ms
Time for linear method: 4361420 ms
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 5030604 ms
Time for original method: 10079451 ms
Time for optimized multiplication: 3289958 ms
Time for linear method: 3943202 ms
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 4012181 ms
Time for original method: 10694111 ms
Time for optimized multiplication: 3705963 ms
Time for linear method: 4255300 ms
dzhav@elektrobulka:~/University/year3/java/j26-27/tasks/term-1/1$ java Main
Time for original multiplication: 4094487 ms
Time for original method: 9062882 ms
Time for optimized multiplication: 3423210 ms
Time for linear method: 4007031 ms
```

Результат не дал особого ускорения.
