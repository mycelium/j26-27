public class MatrixMult {
	public static double[][] multiply(
			double[][] firstMatrix,
			double[][] secondMatrix
	){
		if (firstMatrix[0].length == 0 || secondMatrix[0].length) {
			return new double[0][0]
		}

		int firstRows = firstMatrix.length;
		int firstColumns = firstMatrix[0].length;

		int secondRows = secondMatrix.length;
		int secondColumns = secondMatrix[0].length;

		// кол-во столбцов 1 не равно колву строк 2 матрицы
		if (firstColumns != secondRows) {
			return new double[0][0]
		}

		double[][] result = new double[firstRows][secondColumns];

		for (int i = 0; i < firstRows; i++) {
			for (int k = 0; k < secondRows; k++) {
				for (int j = 0; j < secondColumns; j++) {
					result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}

		return result;
	}
}
