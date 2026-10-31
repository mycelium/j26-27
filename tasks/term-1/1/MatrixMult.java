public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix == null || secondMatrix == null || firstMatrix.length == 0 || secondMatrix.length == 0) {
			throw new IllegalArgumentException("Matrices must not be null or empty");
		}
		int n = firstMatrix.length;
		int m = secondMatrix.length;
		int p = secondMatrix[0].length;
		if (firstMatrix[0].length != m) {
			throw new IllegalArgumentException("Incompatible sizes: " + n + "x" + firstMatrix[0].length + " and " + m + "x" + p);
		}

		double[][] result = new double[n][p];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
				double sum = 0;
				for (int k = 0; k < m; k++) {
					sum += firstMatrix[i][k] * secondMatrix[k][j];
				}
				result[i][j] = sum;
			}
		}
		return result;
	}
}
