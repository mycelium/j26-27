public class MatrixMult {

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {

        if (firstMatrix[0].length != secondMatrix.length) {
            throw new IllegalArgumentException("Невозможно перемноить матрицы данной размерности.");
        }

        double[][] result = new double[firstMatrix.length][secondMatrix[0].length];

        for (int i = 0; i < firstMatrix.length; i++) {
            for (int k = 0; k < secondMatrix.length; k++) {
                double value = firstMatrix[i][k];
                for (int j = 0; j < secondMatrix[0].length; j++) {
                    result[i][j] += value * secondMatrix[k][j];
                }
            }
        }

        return result;
    }

    public static double[][] CreateDefaultMatrix(int rows, int cols) {
        double matrix[][] = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = i * cols + j;
            }
        }

        return matrix;
    }

    public static void main(String[] args) {
        int[] sizes = { 300, 500, 700 };

        for (int n : sizes) {
            double[][] firstMatrix = CreateDefaultMatrix(n, n);
            double[][] secondMatrix = CreateDefaultMatrix(n, n);

            long totalNanos = 0;
            double result[][] = null;

            for (int i = 0; i < 3; i++)
            {
                long start = System.nanoTime();
                result = multiply(firstMatrix, secondMatrix);
                totalNanos += System.nanoTime() - start;
            }

            double averageTime = totalNanos / 3.0 / 1000000.0;

            System.out.printf("%dx%d: среднее время %.3f мс.%n",
                n, n, averageTime);
        }
    }
}
