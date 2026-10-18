import java.util.Scanner;

public class MatrixMult {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int mode = readMode(scanner);
		if (mode == -1) {
			return;
		}
		double[][] firstMatrix;
		double[][] secondMatrix;

		if (mode == 1) {
			firstMatrix = readMatrix(scanner, "первой", 0);
			if (firstMatrix == null) {
				return;
			}
			secondMatrix = readMatrix(scanner, "второй", firstMatrix[0].length);
		} else {
			firstMatrix = generateRandomMatrix(scanner, "первой", 0);
			if (firstMatrix == null) {
				return;
			}
			secondMatrix = generateRandomMatrix(scanner, "второй", firstMatrix[0].length);
		}
		if (secondMatrix == null) {
			return;
		}
		double[][] result = multiply(firstMatrix, secondMatrix);

		if (mode == 1) {
			System.out.println("Результат умножения матриц:");
			for (double[] row : result) {
				for (double value : row) {
					System.out.print(value + "\t");
				}
				System.out.println();
			}
		}
	}

	private static String[] readTokens(Scanner scanner, String prompt, int expectedCount) {
		while (true) {
			System.out.println(prompt);
			if (!scanner.hasNextLine()) {
				System.out.println("Ввод завершён.");
				return null;
			}
			String line = scanner.nextLine().trim();
			String[] tokens = line.isEmpty() ? new String[0] : line.split("\\s+");
			if (tokens.length == expectedCount) {
				return tokens;
			}
			System.out.println("ОШИБКА: Требуется ровно " + expectedCount
					+ " значений через пробел. Получено: " + tokens.length + ". Повторите ввод.");
		}
	}

	private static int readMode(Scanner scanner) {
		String prompt = "Выберите режим работы программы (1 - ручной ввод, "
				+ "2 - вывод времени умножения случайных матриц больших размерностей).";
		while (true) {
			String[] tokens = readTokens(scanner, prompt, 1);
			if (tokens == null) {
				return -1;
			}
			try {
				int mode = Integer.parseInt(tokens[0]);
				if (mode == 1 || mode == 2) {
					return mode;
				}
			} catch (NumberFormatException exception) { }
			System.out.println("ОШИБКА: Введите 1 или 2. Повторите ввод.");
		}
	}

	private static int[] readDimensions(Scanner scanner, String matrixName, int requiredRows) {
		String prompt = "Введите количество строк и столбцов " + matrixName
				+ " матрицы (ровно два положительных целых числа через пробел):";
		while (true) {
			String[] tokens = readTokens(scanner, prompt, 2);
			if (tokens == null) {
				return null;
			}
			try {
				int rows = Integer.parseInt(tokens[0]);
				int columns = Integer.parseInt(tokens[1]);
				if (rows <= 0 || columns <= 0) {
					System.out.println("ОШИБКА: Размеры матрицы должны быть больше нуля. Повторите ввод.");
					continue;
				}
				if (requiredRows > 0 && rows != requiredRows) {
					System.out.println("ОШИБКА: Число строк второй матрицы должно быть равно числу "
							+ "столбцов первой (" + requiredRows + "). Повторите ввод.");
					continue;
				}
				return new int[] {rows, columns};
			} catch (NumberFormatException exception) {
				System.out.println("ОШИБКА: Размеры должны быть целыми числами в диапазоне от 1 до "
						+ Integer.MAX_VALUE + ". Повторите ввод.");
			}
		}
	}

	private static double[][] readMatrix(Scanner scanner, String matrixName, int requiredRows) {
		int[] dimensions = readDimensions(scanner, matrixName, requiredRows);
		if (dimensions == null) {
			return null;
		}
		int rows = dimensions[0];
		int columns = dimensions[1];
		double[][] matrix = new double[rows][columns];

		for (int i = 0; i < rows; i++) {
			String prompt = "Введите строку " + (i + 1) + " " + matrixName + " матрицы (ровно "
					+ columns + " чисел через пробел, дробная часть через точку):";
			while (true) {
				String[] tokens = readTokens(scanner, prompt, columns);
				if (tokens == null) {
					return null;
				}
				boolean valid = true;
				try {
					for (int j = 0; j < columns; j++) {
						matrix[i][j] = Double.parseDouble(tokens[j]);
						if (!Double.isFinite(matrix[i][j])) {
							valid = false;
							break;
						}
					}
				} catch (NumberFormatException exception) {
					valid = false;
				}
				if (valid) {
					break;
				}
				System.out.println("ОШИБКА: Все элементы строки должны быть числовыми. Повторите ввод.");
			}
		}

		return matrix;
	}

	private static double[][] generateRandomMatrix(Scanner scanner, String matrixName, int requiredRows) {
		int[] dimensions = readDimensions(scanner, matrixName, requiredRows);
		if (dimensions == null) {
			return null;
		}
		int rows = dimensions[0];
		int columns = dimensions[1];
		double[][] matrix = new double[rows][columns];

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				matrix[i][j] = Math.random() * Integer.MAX_VALUE;
			}
		}

		return matrix;
	}

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		int rowsFirst = firstMatrix.length;
		int colsFirst = firstMatrix[0].length;
		int colsSecond = secondMatrix[0].length;

		double[][] result = new double[rowsFirst][colsSecond];

		long startTime = System.nanoTime();
		for (int i = 0; i < rowsFirst; i++) {
			for (int j = 0; j < colsSecond; j++) {
				for (int k = 0; k < colsFirst; k++) {
					result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}
		long endTime = System.nanoTime();
		long duration = endTime - startTime;

		System.out.println("Время обычного умножения: " + (duration / 1_000_000.0) + " мс");

		double[][] transposedResult = new double[rowsFirst][colsSecond];

		long transposedStartTime = System.nanoTime();
		double[][] transposedSecondMatrix = new double[colsSecond][colsFirst];
		for (int i = 0; i < colsFirst; i++) {
			for (int j = 0; j < colsSecond; j++) {
				transposedSecondMatrix[j][i] = secondMatrix[i][j];
			}
		}

		for (int i = 0; i < rowsFirst; i++) {
			double[] firstRow = firstMatrix[i];
			for (int j = 0; j < colsSecond; j++) {
				double[] secondRow = transposedSecondMatrix[j];
				double sum = 0;
				for (int k = 0; k < colsFirst; k++) {
					sum += firstRow[k] * secondRow[k];
				}
				transposedResult[i][j] = sum;
			}
		}
		long transposedEndTime = System.nanoTime();
		long transposedDuration = transposedEndTime - transposedStartTime;

		System.out.println("Время умножения с транспонированием: " + (transposedDuration / 1_000_000.0) + " мс");
		return transposedResult;
	}
}
