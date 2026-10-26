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

    public static double[] multiplyFlat(
            double[] firstMatrix,
            double[] secondMatrix,
            int rows,
            int sharedDimension,
            int cols) {

        if (firstMatrix.length != rows * sharedDimension
                || secondMatrix.length != sharedDimension * cols) {
            throw new IllegalArgumentException("Размеры массивов не соответствуют размерам матрицам.");
        }

        double[] result = new double[rows * cols];

        for (int i = 0; i < rows; i++) {
            int firstRowOffset = i * sharedDimension;
            int resultRowOffset = i * cols;

            for (int k = 0; k < sharedDimension; k++) {
                double value = firstMatrix[firstRowOffset + k];
                int secondRowOffset = k * cols;

                for (int j = 0; j < cols; j++) {
                    result[resultRowOffset + j] += value * secondMatrix[secondRowOffset + j];
                }
            }
        }

        return result;
    }

    public static double[][] CreateDefaultMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = i * cols + j;
            }
        }

        return matrix;
    }

    public static double[] createFlatMatrix(int rows, int cols) {
        double[] matrix = new double[rows * cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i * cols + j] = i * cols + j;
            }
        }

        return matrix;
    }

    private static double measureAverageTime2D(
            double[][] firstMatrix,
            double[][] secondMatrix,
            int runs) {

        long totalNanos = 0;

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            multiply(firstMatrix, secondMatrix);
            totalNanos += System.nanoTime() - start;
        }

        return totalNanos / (double) runs / 1000000.0;
    }

    private static double measureAverageTimeFlat(
            double[] firstMatrix,
            double[] secondMatrix,
            int size,
            int runs) {

        long totalNanos = 0;

        for (int i = 0; i < runs; i++) {
            long start = System.nanoTime();
            multiplyFlat(firstMatrix, secondMatrix, size, size, size);
            totalNanos += System.nanoTime() - start;
        }

        return totalNanos / (double) runs / 1000000.0;
    }

    public static void main(String[] args) {
        int[] sizes = { 1500, 2000, 2500 };
        int runs = 3;

        for (int size : sizes) {
            double[][] firstMatrix = CreateDefaultMatrix(size, size);
            double[][] secondMatrix = CreateDefaultMatrix(size, size);

            double averageTime2D = measureAverageTime2D(
                    firstMatrix, secondMatrix, runs);

            double[] firstFlat = createFlatMatrix(size, size);
            double[] secondFlat = createFlatMatrix(size, size);

            double averageTimeFlat = measureAverageTimeFlat(
                    firstFlat, secondFlat, size, runs);

            System.out.printf(
                    "%dx%d: среднее время 2D %.3f мс.%n",
                    size, size, averageTime2D);

            System.out.printf(
                    "%dx%d: среднее время flat %.3f мс.%n",
                    size, size, averageTimeFlat);
        }
    }
}
