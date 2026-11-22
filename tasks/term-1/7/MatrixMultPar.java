import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MatrixMultPar {
    private static final int PREFERRED_THREADS = 19;
    private static final int[] DEFAULT_SIZES = {1000, 1500, 2000};
    private static final int MEASUREMENTS = 3;

    public static double[][] multiplyParallel(double[][] firstMatrix, double[][] secondMatrix) {
        return multiplyParallel(firstMatrix, secondMatrix, defaultThreadCount());
    }

    public static double[][] multiplyParallel(double[][] firstMatrix, double[][] secondMatrix, int threadCount) {
        int innerSize = columnCount(firstMatrix);
        int resultColumns = columnCount(secondMatrix);
        if (innerSize != secondMatrix.length) {
            throw new IllegalArgumentException("The number of columns in the first matrix must match the rows in the second.");
        }
        if (threadCount < 1) {
            throw new IllegalArgumentException("The thread count must be positive.");
        }

        double[][] transposed = new double[resultColumns][innerSize];
        for (int row = 0; row < innerSize; row++) {
            for (int column = 0; column < resultColumns; column++) {
                transposed[column][row] = secondMatrix[row][column];
            }
        }

        int resultRows = firstMatrix.length;
        double[][] result = new double[resultRows][resultColumns];
        int workers = Math.min(threadCount, resultRows);
        if (workers == 1) {
            multiplyRows(firstMatrix, transposed, result, 0, resultRows);
            return result;
        }

        List<Callable<Void>> tasks = new ArrayList<>(workers);
        for (int worker = 0; worker < workers; worker++) {
            int start = (int) ((long) resultRows * worker / workers);
            int end = (int) ((long) resultRows * (worker + 1) / workers);
            tasks.add(() -> {
                multiplyRows(firstMatrix, transposed, result, start, end);
                return null;
            });
        }

        ExecutorService executor = Executors.newFixedThreadPool(workers);
        try {
            for (Future<Void> future : executor.invokeAll(tasks)) {
                future.get();
            }
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Matrix multiplication was interrupted.", exception);
        } catch (ExecutionException exception) {
            throw new IllegalStateException("Matrix multiplication failed.", exception.getCause());
        } finally {
            executor.shutdownNow();
        }
        return result;
    }

    private static void multiplyRows(double[][] first, double[][] transposed, double[][] result, int start, int end) {
        for (int row = start; row < end; row++) {
            if (Thread.currentThread().isInterrupted()) {
                throw new CancellationException("Matrix multiplication was interrupted.");
            }
            double[] firstRow = first[row];
            double[] resultRow = result[row];
            for (int column = 0; column < transposed.length; column++) {
                double[] secondRow = transposed[column];
                double sum = 0.0;
                for (int index = 0; index < firstRow.length; index++) {
                    sum += firstRow[index] * secondRow[index];
                }
                resultRow[column] = sum;
            }
        }
    }

    private static int columnCount(double[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
            throw new IllegalArgumentException("Matrices must be nonempty and rectangular.");
        }
        int columns = matrix[0].length;
        for (double[] row : matrix) {
            if (row == null || row.length != columns) {
                throw new IllegalArgumentException("Matrices must be nonempty and rectangular.");
            }
        }
        return columns;
    }

    public static void main(String[] args) {
        try {
            int[] sizes = args.length > 0 ? new int[]{parsePositiveInt(args[0])} : DEFAULT_SIZES;
            int[] threadCounts = new int[Math.max(1, args.length - 1)];
            if (args.length < 2) {
                threadCounts[0] = defaultThreadCount();
            } else {
                for (int index = 0; index < threadCounts.length; index++) {
                    threadCounts[index] = parsePositiveInt(args[index + 1]);
                }
            }
            for (int size : sizes) {
                benchmark(size, threadCounts);
            }
        } catch (IllegalArgumentException exception) {
            System.err.println("Usage: java MatrixMultPar [matrix-size [threads ...]]");
            System.err.println("The size and thread counts must be positive integers.");
            System.exit(2);
        }
    }

    private static void benchmark(int size, int[] threadCounts) {
        System.out.printf(Locale.ROOT, "Matrices: %d x %d. Logical processors available to Java: %d.%n",
                size, size, Runtime.getRuntime().availableProcessors());
        Random random = new Random(42);
        double[][] first = randomMatrix(size, random);
        double[][] second = randomMatrix(size, random);
        double[][] expected = null;

        for (int threads : threadCounts) {
            double[][] result = multiplyParallel(first, second, threads);
            if (expected == null) {
                expected = result;
            }
        }

        long[][] times = new long[threadCounts.length][MEASUREMENTS];
        for (int run = 0; run < MEASUREMENTS; run++) {
            for (int offset = 0; offset < threadCounts.length; offset++) {
                int index = (run + offset) % threadCounts.length;
                long start = System.nanoTime();
                double[][] result = multiplyParallel(first, second, threadCounts[index]);
                times[index][run] = System.nanoTime() - start;
                if (!Arrays.deepEquals(expected, result)) {
                    throw new IllegalStateException("Results differ between runs.");
                }
            }
        }

        long bestTime = Long.MAX_VALUE;
        int bestThreads = threadCounts[0];
        for (int index = 0; index < threadCounts.length; index++) {
            Arrays.sort(times[index]);
            long[] samples = times[index];
            long median = samples[MEASUREMENTS / 2];
            int workers = Math.min(threadCounts[index], size);
            System.out.printf(Locale.ROOT, "%2d threads: median %8.2f ms (min %.2f, max %.2f)%n",
                    workers, median / 1_000_000.0, samples[0] / 1_000_000.0, samples[MEASUREMENTS - 1] / 1_000_000.0);
            if (median < bestTime) {
                bestTime = median;
                bestThreads = workers;
            }
        }
        if (threadCounts.length > 1) {
            System.out.printf(Locale.ROOT, "Fastest in this run: %d threads (%.2f ms).%n",
                    bestThreads, bestTime / 1_000_000.0);
        }
    }

    private static double[][] randomMatrix(int size, Random random) {
        double[][] matrix = new double[size][size];
        for (double[] row : matrix) {
            for (int column = 0; column < row.length; column++) {
                row[column] = random.nextDouble();
            }
        }
        return matrix;
    }

    private static int defaultThreadCount() {
        return Math.min(PREFERRED_THREADS, Runtime.getRuntime().availableProcessors());
    }

    private static int parsePositiveInt(String text) {
        int number = Integer.parseInt(text);
        if (number < 1) {
            throw new IllegalArgumentException("Expected a positive integer.");
        }
        return number;
    }
}
