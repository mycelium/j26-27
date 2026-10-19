import java.util.Arrays;

public class MatrixMult {

    // Fast version: the loops go in the order i, k, j
    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        int n = firstMatrix.length;
        int m = secondMatrix.length;
        int p = secondMatrix[0].length;

        if (firstMatrix[0].length != m) {
            throw new IllegalArgumentException("Wrong matrix sizes");
        }

        double[][] result = new double[n][p];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < m; k++) {
                double a = firstMatrix[i][k];
                for (int j = 0; j < p; j++) {
                    result[i][j] += a * secondMatrix[k][j];
                }
            }
        }
        return result;
    }

    // Slow version: the loops go in the order i, j, k (only for comparing)
    public static double[][] multiplySlow(double[][] firstMatrix, double[][] secondMatrix) {
        int n = firstMatrix.length;
        int m = secondMatrix.length;
        int p = secondMatrix[0].length;

        double[][] result = new double[n][p];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < p; j++) {
                for (int k = 0; k < m; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {
        // small test, the answer must be [[58, 64], [139, 154]]
        double[][] a = {{1, 2, 3}, {4, 5, 6}};
        double[][] b = {{7, 8}, {9, 10}, {11, 12}};
        System.out.println("fast: " + Arrays.deepToString(multiply(a, b)));
        System.out.println("slow: " + Arrays.deepToString(multiplySlow(a, b)));

        // big matrices
        int[] sizes = {500, 1000};
        for (int size : sizes) {
            double[][] first = new double[size][size];
            double[][] second = new double[size][size];
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    first[i][j] = Math.random();
                    second[i][j] = Math.random();
                }
            }

            long start = System.currentTimeMillis();
            multiply(first, second);
            long fastTime = System.currentTimeMillis() - start;

            start = System.currentTimeMillis();
            multiplySlow(first, second);
            long slowTime = System.currentTimeMillis() - start;

            System.out.println("Size " + size + "x" + size);
            System.out.println("fast: " + fastTime + " ms");
            System.out.println("slow: " + slowTime + " ms");
        }
    }
}
