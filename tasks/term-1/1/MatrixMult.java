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

    public static void main(String[] args) {
        double[][] firstMatrix = {
                { 1, 2 },
                { 3, 4 }
        };

        double[][] secondMatrix = {
            {5, 6},
            {7, 8}
        };

        double[][] multiplyResult = multiply(firstMatrix, secondMatrix);
        System.out.println(java.util.Arrays.deepToString(multiplyResult));
    }
}
