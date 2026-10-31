public class MatrixMult {

	private static void validate(double[][] matrix, String name) {
		if (matrix == null) {
			throw new IllegalArgumentException(name + " равна null");
		}
		if (matrix.length == 0 || matrix[0] == null || matrix[0].length == 0) {
			throw new IllegalArgumentException(name + " пустая");
		}
		int cols = matrix[0].length;
		for (int i = 1; i < matrix.length; i++) {
			if (matrix[i] == null || matrix[i].length != cols) {
				throw new IllegalArgumentException(name + " не прямоугольная: строка " + i + " отличается по длине");
			}
		}
	}

	private static void checkMultipliable(double[][] firstMatrix, double[][] secondMatrix) {
		validate(firstMatrix, "Первая матрица");
		validate(secondMatrix, "Вторая матрица");
		if (firstMatrix[0].length != secondMatrix.length) {
			throw new IllegalArgumentException(
					"Ошибка! Число столбцов первой матрицы не равно числу строк второй матрицы!");
		}
	}

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
		checkMultipliable(firstMatrix, secondMatrix);

		int firstRows = firstMatrix.length;
		int innerSize = firstMatrix[0].length;
		int secondCols = secondMatrix[0].length;
		double[][] result = new double[firstRows][secondCols];

		for (int i = 0; i < firstRows; i++) {
			for (int j = 0; j < secondCols; j++) {
				for (int k = 0; k < innerSize; k++) {
					result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}
		return result;
	}

	public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix) {
		checkMultipliable(firstMatrix, secondMatrix);

		int firstRows = firstMatrix.length;
		int innerSize = firstMatrix[0].length;
		int secondCols = secondMatrix[0].length;
		double[][] result = new double[firstRows][secondCols];

		for (int i = 0; i < firstRows; i++) {
			for (int k = 0; k < innerSize; k++) {
				double firstValue = firstMatrix[i][k];
				for (int j = 0; j < secondCols; j++) {
					result[i][j] += firstValue * secondMatrix[k][j];
				}
			}
		}
		return result;
	}

	public static boolean matricesEqual(double[][] first, double[][] second, double epsilon) {
		validate(first, "Первая матрица");
		validate(second, "Вторая матрица");
		if (first.length != second.length || first[0].length != second[0].length) {
			return false;
		}
		for (int i = 0; i < first.length; i++) {
			for (int j = 0; j < first[0].length; j++) {
				if (Math.abs(first[i][j] - second[i][j]) > epsilon) {
					return false;
				}
			}
		}
		return true;
	}

	public static double[][] randomMatrix(int rows, int cols) {
		if (rows <= 0 || cols <= 0) {
			throw new IllegalArgumentException("Размеры матрицы должны быть положительными");
		}
		double[][] matrix = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = Math.random();
			}
		}
		return matrix;
	}

	public static void printMatrix(double[][] matrix) {
		validate(matrix, "Матрица");
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		// пример на небольших матрицах
		double[][] smallFirst = { { 2, 3 }, { 1, 3 } };
		double[][] smallSecond = { { 2, 3 }, { 1, 2 } };
		System.out.println("Первая матрица:");
		printMatrix(smallFirst);
		System.out.println("Вторая матрица:");
		printMatrix(smallSecond);
		System.out.println("Результат:");
		printMatrix(multiply(smallFirst, smallSecond));

		// замер времени на больших матрицах
		int firstRows = 800;
		int firstCols = 700;
		int secondRows = 700;
		int secondCols = 800;
		double[][] firstMatrix = randomMatrix(firstRows, firstCols);
		double[][] secondMatrix = randomMatrix(secondRows, secondCols);

		// прогрев
		multiply(firstMatrix, secondMatrix);
		multiplyOptimized(firstMatrix, secondMatrix);

		int measureRuns = 5;

		double naiveTotal = 0;
		double optimizedTotal = 0;
		double[][] naive = null;
		double[][] optimized = null;

		System.out.printf("Матрицы %dx%d на %dx%d, замеров: %d%n", firstRows, firstCols, secondRows, secondCols,
				measureRuns);

		for (int run = 1; run <= measureRuns; run++) {
			long start = System.nanoTime();
			naive = multiply(firstMatrix, secondMatrix);
			double naiveMillis = (System.nanoTime() - start) / 1_000_000.0;
			naiveTotal += naiveMillis;

			start = System.nanoTime();
			optimized = multiplyOptimized(firstMatrix, secondMatrix);
			double optimizedMillis = (System.nanoTime() - start) / 1_000_000.0;
			optimizedTotal += optimizedMillis;

			System.out.printf("замер %d: multiply %.3f мс, multiplyOptimized %.3f мс%n", run, naiveMillis,
					optimizedMillis);
		}
		System.out.printf("среднее: multiply %.3f мс, multiplyOptimized %.3f мс%n", naiveTotal / measureRuns,
				optimizedTotal / measureRuns);
		System.out.println("\nПроверка результатов: " +
				(matricesEqual(naive, optimized, 1e-9)
						? "матрицы совпадают."
						: "матрицы отличаются."));
	}
}