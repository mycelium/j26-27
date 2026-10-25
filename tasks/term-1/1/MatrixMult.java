public class MatrixMult {

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        int rowsA = firstMatrix.length;
        int colsA = firstMatrix[0].length;
        int rowsB = secondMatrix.length;
        int colsB = secondMatrix[0].length;
        if (colsA != rowsB) {
            throw new IllegalArgumentException("Number of columns in the first matrix must be equal to the number of rows in the second matrix.");
        }
        double[][] result = new double[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                double sum = 0.0;
                for (int k = 0; k < colsA; k++) {
                    sum += firstMatrix[i][k] * secondMatrix[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public static double[][] multiplyIKJ(double[][] firstMatrix, double[][] secondMatrix) {
        int rowsA = firstMatrix.length;
        int colsA = firstMatrix[0].length;
        int rowsB = secondMatrix.length;
        int colsB = secondMatrix[0].length;
        if (colsA != rowsB) {
            throw new IllegalArgumentException("Number of columns in the first matrix must be equal to the number of rows in the second matrix.");
        }
        double[][] result = new double[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int k = 0; k < colsA; k++) {
                for (int j = 0; j < colsB; j++)
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
            }
        }
        return result;
    }
    public static void main(String[] args) {
        int n = 1000;
		double[][] matrixA = new double[n][n];
		double[][] matrixB = new double[n][n];
		double[][] matrixR1 = new double[n][n];
		double[][] matrixR2 = new double[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrixA[i][j] = (i + j) % 10;
				matrixB[i][j] = 10 - matrixA[i][j];
			}
		}
        for (int w = 0; w < 3; w++) { // warming up
            multiply(matrixA, matrixB);
			multiplyIKJ(matrixA, matrixB);
        }
		long total1 = 0;
		long total2 = 0;
		for (int i = 0; i < 3; i++) {
			long start = System.nanoTime();
			matrixR1 = multiply(matrixA, matrixB);
			long end = System.nanoTime();
			total1 += end - start;
		}
		for (int i = 0; i < 3; i++) {
			long start = System.nanoTime();
			matrixR2 = multiplyIKJ(matrixA, matrixB);
			long end = System.nanoTime();
			total2 += end - start;
		}
        System.out.println("Time for original multiply: \t\t" + (total1));
        System.out.println("Time for I-K-J multiply: \t\t" + (total2));
		System.out.println("Average time for original multiply: \t" + (total1 / 3));
		System.out.println("Average time for I-K-J multiply: \t" + (total2 / 3));
    }
}