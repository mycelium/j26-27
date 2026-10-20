import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class MatrixMult {
    private static volatile double lastResult;

    public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix) {
        checkDimensions(firstMatrix, secondMatrix);

        int rows = firstMatrix.length;
        int common = secondMatrix.length;
        int columns = secondMatrix[0].length;
        double[][] result = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            double[] firstRow = firstMatrix[i];
            double[] resultRow = result[i];

            for (int k = 0; k < common; k++) {
                double value = firstRow[k];
                double[] secondRow = secondMatrix[k];

                //reading along a row is faster than jumping between rows.
                for (int j = 0; j < columns; j++) {
                    resultRow[j] += value * secondRow[j];
                }
            }
        }

        return result;
    }

    // original loop order,  used for comparison.
    public static double[][] multiplyBasic(double[][] firstMatrix, double[][] secondMatrix) {
        checkDimensions(firstMatrix, secondMatrix);

        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int i = 0; i < firstMatrix.length; i++) {
            for (int j = 0; j < secondMatrix[0].length; j++) {
                for (int k = 0; k < secondMatrix.length; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return result;
    }

    private static void checkDimensions(double[][] firstMatrix, double[][] secondMatrix) {
        checkMatrix(firstMatrix, "firstMatrix");
        checkMatrix(secondMatrix, "secondMatrix");

        if (firstMatrix[0].length != secondMatrix.length) {
            throw new IllegalArgumentException("First matrix columns must match second matrix rows.");
        }
    }

    private static void checkMatrix(double[][] matrix, String name) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
            throw new IllegalArgumentException(name + " must be a non-empty matrix.");
        }

        int columns = matrix[0].length;
        for (double[] row : matrix) {
            if (row == null || row.length != columns) {
                throw new IllegalArgumentException(name + " has a null row or different row lengths.");
            }
        }
    }

    public static void main(String[] args) {
        int[] sizes = {250, 500, 1000};
        Random random = new Random(42);

        //warm up before measuring.
        double[][] warmupFirst = randomMatrix(200, random);
        double[][] warmupSecond = randomMatrix(200, random);
        for (int run = 0; run < 10; run++) {
            saveResult(multiplyBasic(warmupFirst, warmupSecond));
            saveResult(multiplyOptimized(warmupFirst, warmupSecond));
        }

        System.out.println("Median of 5 runs after warm-up (includes result allocation)");
        System.out.printf("%12s %14s %14s %10s%n", "Size", "Basic (ms)", "Optimized (ms)", "Speedup");

        for (int size : sizes) {
            double[][] first = randomMatrix(size, random);
            double[][] second = randomMatrix(size, random);
            long[] basicTimes = new long[5];
            long[] optimizedTimes = new long[5];

            for (int run = 0; run < basicTimes.length; run++) {
                // take turns running first.
                if (run % 2 == 0) {
                    basicTimes[run] = measure(first, second, false);
                    optimizedTimes[run] = measure(first, second, true);
                } else {
                    optimizedTimes[run] = measure(first, second, true);
                    basicTimes[run] = measure(first, second, false);
                }
            }

            Arrays.sort(basicTimes);
            Arrays.sort(optimizedTimes);
            double basicMillis = basicTimes[2] / 1_000_000.0;
            double optimizedMillis = optimizedTimes[2] / 1_000_000.0;

            System.out.printf(Locale.ROOT, "%12s %14.3f %14.3f %9.2fx%n",
                    size + "x" + size, basicMillis, optimizedMillis, basicMillis / optimizedMillis);
        }
    }

    private static long measure(double[][] first, double[][] second, boolean optimized) {
        long start = System.nanoTime();
        double[][] result = optimized ? multiplyOptimized(first, second) : multiplyBasic(first, second);
        long elapsed = System.nanoTime() - start;
        saveResult(result);
        return elapsed;
    }

    private static void saveResult(double[][] matrix) {
        //keep the JVM from treating the calculation as unused.
        lastResult = matrix[matrix.length - 1][matrix[0].length - 1];
    }

    private static double[][] randomMatrix(int size, Random random) {
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = random.nextDouble();
            }
        }
        return matrix;
    }
}
