public class MatrixMult {
	public static double[][] multiply(
			double[][] firstMatrix,
			double[][] secondMatrix
	){
		int n = firstMatrix.length;
		int m = secondMatrix[0].length;
		int l = secondMatrix.length;

		double[][] result = new double[n][m];

		for (int i = 0; i < n; i++) {
			for (int k = 0; k < l; k++) {
				for (int j = 0; j < m; j++) {
					result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}

		return result;
	}
}
