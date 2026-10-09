public class MatrixMult {

    private static final double EPSILON = 1e-12; // нет четкого обоснования, просто взял малое
    private static final String ERROR_MSG =
            "The number of columns in the first matrix must match "
                    + "the number of rows in the second matrix.";

    private static void validateMultiplicationDimensions(
            double[][] firstMatrix,
            double[][] secondMatrix
    ) {
        int colsA = firstMatrix[0].length;
        int rowsB = secondMatrix.length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException(ERROR_MSG);
        }
    }

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        validateMultiplicationDimensions(firstMatrix, secondMatrix);

        int rowsA = firstMatrix.length;
        int colsA = firstMatrix[0].length;
        int colsB = secondMatrix[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int k = 0; k < colsA; k++) {
                double value = firstMatrix[i][k];
                // i-k-j перебор
                for (int j = 0; j < colsB; j++) {
                    result[i][j] += value * secondMatrix[k][j];
                }
            }
        }

        return result;
    }

    public static double[][] multiplyNaive(double[][] firstMatrix, double[][] secondMatrix) {
        validateMultiplicationDimensions(firstMatrix, secondMatrix);

        int rowsA = firstMatrix.length;
        int colsA = firstMatrix[0].length;
        int colsB = secondMatrix[0].length;
        double[][] result = new double[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return result;
    }

    private static boolean matricesAreEqual(double[][] firstMatrix, double[][] secondMatrix) {
        if (firstMatrix.length != secondMatrix.length) {
            return false;
        }

        for (int i = 0; i < firstMatrix.length; i++) {
            if (firstMatrix[i].length != secondMatrix[i].length) {
                return false;
            }

            for (int j = 0; j < firstMatrix[i].length; j++) {
                if (Math.abs(firstMatrix[i][j] - secondMatrix[i][j]) > EPSILON) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void printMatrix(String title, double[][] matrix) {
        System.out.println(title);

        for (double[] row : matrix) {
            for (double value : row) {
                System.out.printf("%8.1f", value);
            }
            System.out.println();
        }

        System.out.println();
    }

    private static void printExample() {
        double[][] firstMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[][] secondMatrix = {
                {1, 0, 0},
                {0, 2, 0},
                {0, 0, 3}
        };

        double[][] result = multiply(firstMatrix, secondMatrix);
        double[][] naiveResult = multiplyNaive(firstMatrix, secondMatrix);

        printMatrix("First matrix:", firstMatrix);
        printMatrix("Second matrix:", secondMatrix);
        printMatrix("Multiplication result:", result);
        System.out.println(
                "Results from both algorithms are equal: "
                        + matricesAreEqual(result, naiveResult)
        );
    }

    private static void benchmarkAlgorithms() {
        int size = 1_000;      // размерность матрицы
        int repetitions = 10;  // число прогонов бенча
        double[][] largeFirstMatrix = new double[size][size];
        double[][] largeSecondMatrix = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                largeFirstMatrix[i][j] = (i + j) % 10;
                largeSecondMatrix[i][j] = (i * 2 + j) % 10;
            }
        }

        double[][] largeNaiveResult = null;
        long startTime = System.nanoTime();
        for (int run = 0; run < repetitions; run++) {
            largeNaiveResult = multiplyNaive(largeFirstMatrix, largeSecondMatrix);
        }
        long naiveElapsedTime = System.nanoTime() - startTime;

        double[][] largeOptimizedResult = null;
        startTime = System.nanoTime();
        for (int run = 0; run < repetitions; run++) {
            largeOptimizedResult = multiply(largeFirstMatrix, largeSecondMatrix);
        }
        long optimizedElapsedTime = System.nanoTime() - startTime;

        double naiveAverageMillis = naiveElapsedTime / 1_000_000.0 / repetitions;
        double optimizedAverageMillis = optimizedElapsedTime / 1_000_000.0 / repetitions;

        System.out.printf(
                "Naive algorithm: %.2f ms average runtime%n",
                naiveAverageMillis
        );
        System.out.printf(
                "i-k-j algorithm: %.2f ms average runtime%n",
                optimizedAverageMillis
        );
        System.out.println(
                "Results for large matrices are equal: "
                        + matricesAreEqual(largeNaiveResult, largeOptimizedResult)
        );

        double speedupPercent =
                (naiveAverageMillis - optimizedAverageMillis) / naiveAverageMillis * 100.0;
        System.out.printf(
                "Runtime reduction: %.2f%%%n",
                speedupPercent
        );
    }

    public static void main(String[] args) {
        printExample();
        benchmarkAlgorithms();
    }
}
