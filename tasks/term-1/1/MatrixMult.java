public class MatrixMult {
	public static double[][] multiply(
			double[][] firstMatrix,
			double[][] secondMatrix
	){
		if (firstMatrix.length == 0 || secondMatrix.length == 0) {
			return new double[0][0];
		}

		int firstRows = firstMatrix.length;
		int firstColumns = firstMatrix[0].length;

		int secondRows = secondMatrix.length;
		int secondColumns = secondMatrix[0].length;

		// кол-во столбцов 1 не равно колву строк 2 матрицы
		if (firstColumns != secondRows) {
			return new double[0][0];
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

	public static void printMatrix(double[][] matrix) {
		for (double[] row : matrix) {
			for (double value : row) {
				System.out.print(value + "\t");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int n = 1000;

		double[][] firstMatrix = new double[n][n];
		double[][] secondMatrix = new double[n][n];

		// заполняем матрицы
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				firstMatrix[i][j] = i + 2*j;
				secondMatrix[i][j] = 2*i - j;
			}
		}

		long startTime = System.nanoTime();
		double[][] result = multiply(firstMatrix, secondMatrix);
		long endTime = System.nanoTime();

		double seconds = (endTime - startTime) / 1_000_000_000.0;
		System.out.println("Время умножения: " + seconds + " секунд");
		System.out.println("Проверочное значение result[0][0]: " + result[0][0]);
	}
}
