public class MatrixMult {

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        if (firstMatrix.length == 0) {
            return new double[0][0];
        }

        int n = firstMatrix.length;
        int m = firstMatrix[0].length;

        if (secondMatrix.length != m) {
            throw new IllegalArgumentException("Incompatible matrix dimensions");
        }

        if (m == 0) {
            return new double[n][0];
        }

        int p = secondMatrix[0].length;
        double[][] result = new double[n][p];

        for (int i = 0; i < n; i++) {
            double[] aRow = firstMatrix[i];
            double[] cRow = result[i];

            for (int k = 0; k < m; k++) {
                double aik = aRow[k];
                double[] bRow = secondMatrix[k];

                for (int j = 0; j < p; j++) {
                    cRow[j] += aik * bRow[j];
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int size = 512;

        double[][] a = createMatrix(size, size, 1.0);
        double[][] b = createMatrix(size, size, 2.0);

        long start = System.nanoTime();
        double[][] c = multiply(a, b);
        long elapsedNs = System.nanoTime() - start;

        System.out.printf(
                "Matrix multiplication %dx%d took %.3f ms%n",
                size, size, elapsedNs / 1_000_000.0
        );

        System.out.printf(
                "Check c[0][0]=%.4f, c[%d][%d]=%.4f%n",
                c[0][0], size - 1, size - 1, c[size - 1][size - 1]
        );

    }

    private static double[][] createMatrix(int rows, int cols, double base) {
        double[][] matrix = new double[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = base + ((i + j) % 10) * 0.01;
            }
        }

        return matrix;
    }
}