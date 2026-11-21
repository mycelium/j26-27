import java.util.Arrays;

public class MatrixMultPar {
	private static final int THREADS = Runtime.getRuntime().availableProcessors();

    // реализация из первой лабы
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

	// реализация умножения через распараллеливание
	public static double[][] multiplyParallel(double[][] firstMatrix, double[][] secondMatrix) {
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
		Thread[] workers = new Thread[Math.min(THREADS, firstRows)];

		// у каждого потока свой кусок строк
		for (int t = 0; t < workers.length; t++) {
			int start = (int) ((long) t * firstRows / workers.length);
			int end = (int) ((long) (t + 1) * firstRows / workers.length);
			workers[t] = new Thread(() -> {
				for (int i = start; i < end; i++) {
					for (int k = 0; k < secondRows; k++) {
						for (int j = 0; j < secondColumns; j++) {
							result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
						}
					}
				}
			});
			workers[t].start();
		}

		// ждем, пока все потоки закончат
		try {
			for (Thread worker : workers) {
				worker.join();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("Умножение прервано", e);
		}
		return result;
	}

	public static void main(String[] args) {
		if (args.length > 1) {
			throw new IllegalArgumentException("Укажи только размер матрицы");
		}
		int n = args.length == 0 ? 1600 : Integer.parseInt(args[0]);
		if (n <= 0) {
			throw new IllegalArgumentException("Размер матрицы должен быть больше нуля");
		}
		double[][] firstMatrix = new double[n][n];
		double[][] secondMatrix = new double[n][n];

		// заполняем матрицы
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				firstMatrix[i][j] = i + 2*j;
				secondMatrix[i][j] = 2*i - j;
			}
		}

		// прогреваем оба варианта
		multiply(firstMatrix, secondMatrix);
		multiplyParallel(firstMatrix, secondMatrix);

		long start = System.nanoTime();
		double[][] expected = multiply(firstMatrix, secondMatrix);
		double sequential = (System.nanoTime() - start) / 1_000_000.0;

		start = System.nanoTime();
		double[][] result = multiplyParallel(firstMatrix, secondMatrix);
		double parallel = (System.nanoTime() - start) / 1_000_000.0;

		// сверяем результат после замера
		if (!Arrays.deepEquals(expected, result)) {
			throw new IllegalStateException("Результат отличается от первой лабы");
		}

		System.out.printf("Размер: %d x %d%n", n, n);
		System.out.printf("Лаба 1: %.1f мс%n", sequential);
		System.out.printf("Потоков: %d, время: %.1f мс (%.2fx)%n", Math.min(THREADS, n), parallel, sequential / parallel);
	}
}
