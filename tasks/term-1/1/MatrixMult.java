public class MatrixMult {
	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
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
}