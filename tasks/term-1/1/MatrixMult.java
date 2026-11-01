import java.util.Arrays;
import java.util.Random;
import java.util.function.BinaryOperator;

public class MatrixMult {

    static final int WARMUP = 2; 
    static final int RUNS = 5;   

    /* Оптимизированная версия: порядок циклов i -> k -> j. */
    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        int rowsA = firstMatrix.length;
        int colsA = firstMatrix[0].length;
        int colsB = secondMatrix[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int k = 0; k < colsA; k++) {
                double temp = firstMatrix[i][k];
                for (int j = 0; j < colsB; j++) {
                    result[i][j] += temp * secondMatrix[k][j];
                }
            }
        }
        return result;
    }

    /* Базовая версия (i -> j -> k), оставлена для сравнения. */
    public static double[][] multiplyNaive(double[][] firstMatrix, double[][] secondMatrix) {
        int rowsA = firstMatrix.length;
        int colsA = firstMatrix[0].length;
        int colsB = secondMatrix[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                double sum = 0;
                for (int k = 0; k < colsA; k++) {
                    sum += firstMatrix[i][k] * secondMatrix[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public static void main(String[] args) {
        int size = args.length > 0 ? Integer.parseInt(args[0]) : 1000;

        Random random = new Random(42); 
        double[][] a = new double[size][size];
        double[][] b = new double[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = random.nextDouble();
                b[i][j] = random.nextDouble();
            }
        }

        System.out.println("size: " + size + "x" + size);
        double naive = measure("naive     (i-j-k)", MatrixMult::multiplyNaive, a, b);
        double opt = measure("optimized (i-k-j)", MatrixMult::multiply, a, b);
        System.out.printf("speedup: %.1fx%n", naive / opt);

        // проверка: обе версии дают одинаковый результат
        double[][] r1 = multiplyNaive(a, b);
        double[][] r2 = multiply(a, b);
        boolean same = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (Math.abs(r1[i][j] - r2[i][j]) > 1e-6) same = false;
            }
        }
        System.out.println("results equal: " + same);
    }

    
    static double measure(String name, BinaryOperator<double[][]> f, double[][] a, double[][] b) {
        for (int i = 0; i < WARMUP; i++) f.apply(a, b);
        double[] times = new double[RUNS];
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            f.apply(a, b);
            times[i] = (System.nanoTime() - start) / 1e6;
        }
        Arrays.sort(times);
        double median = times[RUNS / 2];
        System.out.printf("%s: %.0f ms (median of %d runs)%n", name, median, RUNS);
        return median;
    }
}