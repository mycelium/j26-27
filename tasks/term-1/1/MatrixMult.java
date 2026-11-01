import java.util.Arrays;
import java.util.Random;

public class MatrixMult {

    @FunctionalInterface
    private interface MatrixMultiplier {
        double[][] multiply(double[][] firstMatrix, double[][] secondMatrix);
    }

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        return multiplyOptimized(firstMatrix, secondMatrix);
    }

    public static double[][] multiplyClassic(double[][] firstMatrix, double[][] secondMatrix) {
        int sharedDimension = columnCount(firstMatrix, "Первая матрица");
        int resultColumns = columnCount(secondMatrix, "Вторая матрица");

        if (sharedDimension != secondMatrix.length) {
            throw new IllegalArgumentException(
                    "Количество столбцов первой матрицы должно совпадать с количеством строк второй");
        }

        double[][] result = new double[firstMatrix.length][resultColumns];

        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < resultColumns; j++) {
                double sum = 0;
                for (int k = 0; k < sharedDimension; k++) {
                    sum += firstMatrix[i][k] * secondMatrix[k][j];
                }
                result[i][j] = sum;
            }
        }

        return result;
    }

    public static double[][] multiplyTransposed(double[][] firstMatrix, double[][] secondMatrix) {
        int sharedDimension = columnCount(firstMatrix, "Первая матрица");
        int resultColumns = columnCount(secondMatrix, "Вторая матрица");

        if (sharedDimension != secondMatrix.length) {
            throw new IllegalArgumentException(
                    "Количество столбцов первой матрицы должно совпадать с количеством строк второй");
        }

        double[][] transposed = new double[resultColumns][secondMatrix.length];
        for (int i = 0; i < secondMatrix.length; i++) {
            for (int j = 0; j < resultColumns; j++) {
                transposed[j][i] = secondMatrix[i][j];
            }
        }

        double[][] result = new double[firstMatrix.length][resultColumns];
        for (int i = 0; i < firstMatrix.length; i++) {
            double[] firstRow = firstMatrix[i];
            for (int j = 0; j < resultColumns; j++) {
                double[] secondRow = transposed[j];
                double sum = 0;
                for (int k = 0; k < sharedDimension; k++) {
                    sum += firstRow[k] * secondRow[k];
                }
                result[i][j] = sum;
            }
        }

        return result;
    }

    public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix) {
        int sharedDimension = columnCount(firstMatrix, "Первая матрица");
        int resultColumns = columnCount(secondMatrix, "Вторая матрица");

        if (sharedDimension != secondMatrix.length) {
            throw new IllegalArgumentException(
                    "Количество столбцов первой матрицы должно совпадать с количеством строк второй");
        }

        double[][] result = new double[firstMatrix.length][resultColumns];
        for (int i = 0; i < firstMatrix.length; i++) {
            double[] firstRow = firstMatrix[i];
            double[] resultRow = result[i];
            for (int k = 0; k < sharedDimension; k++) {
                double firstValue = firstRow[k];
                double[] secondRow = secondMatrix[k];
                for (int j = 0; j < resultColumns; j++) {
                    resultRow[j] += firstValue * secondRow[j];
                }
            }
        }

        return result;
    }

    private static int columnCount(double[][] matrix, String name) {
        if (matrix == null || matrix.length == 0) {
            throw new IllegalArgumentException(name + " не должна быть null или пустой");
        }
        if (matrix[0] == null || matrix[0].length == 0) {
            throw new IllegalArgumentException(name + " должна содержать непустые строки");
        }

        int columns = matrix[0].length;
        for (double[] row : matrix) {
            if (row == null || row.length != columns) {
                throw new IllegalArgumentException(name + " должна быть прямоугольной");
            }
        }
        return columns;
    }

    private static double[][] randomMatrix(int rows, int columns, Random random) {
        double[][] matrix = new double[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = random.nextDouble();
            }
        }
        return matrix;
    }

    private static void verifyMultiplication(MatrixMultiplier multiplier) {
        double[][] firstMatrix = {
                {1, 2, 3},
                {4, 5, 6}
        };
        double[][] secondMatrix = {
                {7, 8},
                {9, 10},
                {11, 12}
        };
        double[][] expected = {
                {58, 64},
                {139, 154}
        };

        double[][] actual = multiplier.multiply(firstMatrix, secondMatrix);
        for (int i = 0; i < expected.length; i++) {
            for (int j = 0; j < expected[i].length; j++) {
                if (Math.abs(expected[i][j] - actual[i][j]) > 1e-9) {
                    throw new AssertionError("Метод умножения матриц вернул неверный результат");
                }
            }
        }
    }

    // Прогрев запускает JIT-компиляцию до начала измерений
    // Подробнее: https://wiki.openjdk.org/display/HotSpot/MicroBenchmarks
    private static void warmUp(MatrixMultiplier... multipliers) {
        Random random = new Random(1);
        double[][] firstMatrix = randomMatrix(128, 128, random);
        double[][] secondMatrix = randomMatrix(128, 128, random);
        for (MatrixMultiplier multiplier : multipliers) {
            for (int i = 0; i < 5; i++) {
                multiplier.multiply(firstMatrix, secondMatrix);
            }
        }
    }

    private static double measureMedianMillis(
            MatrixMultiplier multiplier,
            double[][] firstMatrix,
            double[][] secondMatrix,
            int runCount) {
        long[] elapsedNanos = new long[runCount];
        double checksum = 0;

        for (int i = 0; i < runCount; i++) {
            long startNanos = System.nanoTime();
            double[][] result = multiplier.multiply(firstMatrix, secondMatrix);
            elapsedNanos[i] = System.nanoTime() - startNanos;
            checksum += result[0][0];
        }

        if (!Double.isFinite(checksum)) {
            throw new AssertionError("Метод умножения матриц вернул некорректное значение");
        }

        Arrays.sort(elapsedNanos);
        if (runCount % 2 == 1) {
            return elapsedNanos[runCount / 2] / 1_000_000.0;
        }
        return (elapsedNanos[runCount / 2 - 1] + elapsedNanos[runCount / 2]) / 2_000_000.0;
    }

    public static void main(String[] args) {
        int matrixSize = args.length == 0 ? 1_000 : Integer.parseInt(args[0]);
        int runCount = args.length < 2 ? 5 : Integer.parseInt(args[1]);
        if (matrixSize <= 0) {
            throw new IllegalArgumentException("Размер матрицы должен быть положительным");
        }
        if (runCount <= 0) {
            throw new IllegalArgumentException("Количество запусков должно быть положительным");
        }

        verifyMultiplication(MatrixMult::multiplyClassic);
        verifyMultiplication(MatrixMult::multiplyTransposed);
        verifyMultiplication(MatrixMult::multiplyOptimized);
        warmUp(
                MatrixMult::multiplyClassic,
                MatrixMult::multiplyTransposed,
                MatrixMult::multiplyOptimized);

        Random random = new Random(42);
        double[][] firstMatrix = randomMatrix(matrixSize, matrixSize, random);
        double[][] secondMatrix = randomMatrix(matrixSize, matrixSize, random);

        double classicMillis = measureMedianMillis(
                MatrixMult::multiplyClassic, firstMatrix, secondMatrix, runCount);
        double transposedMillis = measureMedianMillis(
                MatrixMult::multiplyTransposed, firstMatrix, secondMatrix, runCount);
        double optimizedMillis = measureMedianMillis(
                MatrixMult::multiplyOptimized, firstMatrix, secondMatrix, runCount);

        System.out.printf("Размер матриц: %d x %d%n", matrixSize, matrixSize);
        System.out.printf("Количество запусков: %d%n", runCount);
        System.out.printf("Классический i-j-k:  %10.3f мс | ускорение 1.00x%n", classicMillis);
        System.out.printf(
                "Транспонирование:      %10.3f мс | ускорение %.2fx%n",
                transposedMillis,
                classicMillis / transposedMillis);
        System.out.printf(
                "Оптимизированный i-k-j:%10.3f мс | ускорение %.2fx%n",
                optimizedMillis,
                classicMillis / optimizedMillis);
    }
}
