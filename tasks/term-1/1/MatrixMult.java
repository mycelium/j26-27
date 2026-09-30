public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) throws ArithmeticException {
		if (!areCompatible(firstMatrix, secondMatrix)) {
			throw new ArithmeticException(
				"Matrices " + getStrDimention(firstMatrix) + " and " + getStrDimention(secondMatrix) + " are not compatible"
			);
		}

		int resRows = rows(firstMatrix);
		int resCols = cols(secondMatrix);
		double[][] resMatrix = new double[resRows][resCols];

		int m = rows(secondMatrix); // столбцов в первой, строк во второй

		for (int i = 0; i < resRows; ++i) {
			for (int j = 0; j < resCols; ++j) {
				for (int r = 0; r < m; ++r) {
					resMatrix[i][j] += firstMatrix[i][r] * secondMatrix[r][j];
				}
			}
		}

		return resMatrix;
	}

	private static int rows(double[][] matrix) {
		return matrix.length;
	}

	private static int cols(double[][] matrix) {
		return rows(matrix) > 0 ? matrix[0].length : 0;
	}

	private static boolean isRectangle(double[][] arr2D) {
		if (rows(arr2D) <= 1) {
			return true;
		}
		
		int lenOf0Row = arr2D[0].length;
		for (var row : arr2D) {
			if (row.length != lenOf0Row) {
				return false;
			}
		}
		
		return true;
	}

	private static boolean areCompatible(double[][] firstArr2D, double[][] secondArr2D) {
		if (!isRectangle(firstArr2D) || !isRectangle(secondArr2D)) {
			return false;
		}

		return cols(firstArr2D) == rows(secondArr2D);
	}

	private static String getStrDimention(double[][] matrix) {
		return "(" + rows(matrix) + "X" + cols(matrix) + ")";
	}
}

