import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
		checkMatrices(firstMatrix, secondMatrix);

		int n = firstMatrix.length;
		int k = firstMatrix[0].length;
		int m = secondMatrix[0].length;

		double[][] result = new double[n][m];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				double sum = 0.0;
				for (int l = 0; l < k; l++) {
					sum += firstMatrix[i][l] * secondMatrix[l][j];
				}
				result[i][j] = sum;
			}
		}
		return result;
	}

	public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix)
	{
		checkMatrices(firstMatrix, secondMatrix);

		int n = firstMatrix.length;
		int k = firstMatrix[0].length;
		int m = secondMatrix[0].length;

		double[][] result = new double[n][m];
		for (int i = 0; i < n; i++) {
			double[] rowA = firstMatrix[i];
			double[] rowR = result[i];
			for (int l = 0; l < k; l++) {
				double v = rowA[l];
				if (v == 0.0) continue;
				double[] rowB = secondMatrix[l];
				for (int j = 0; j < m; j++) {
					rowR[j] += v * rowB[j];
				}
			}
		}
		return result;
	}

	private static void checkMatrices(double[][] firstMatrix, double[][] secondMatrix) {
		if (firstMatrix == null) {
			throw new IllegalArgumentException("Первая матрица не должна быть null");
		}
		if (secondMatrix == null) {
			throw new IllegalArgumentException("Вторая матрица не должна быть null");
		}
		if (firstMatrix.length == 0) {
			throw new IllegalArgumentException("Первая матрица не должна быть пустой");
		}
		if (secondMatrix.length == 0) {
			throw new IllegalArgumentException("Вторая матрица не должна быть пустой");
		}

		int cols1 = validateRectangular(firstMatrix, "Первая");
		int cols2 = validateRectangular(secondMatrix, "Вторая");

		if (cols1 != secondMatrix.length) {
			throw new IllegalArgumentException(String.format(
					"Несовместимые размеры матриц для умножения: " +
							"первая %dx%d, вторая %dx%d (нужно cols(A) == rows(B))",
					firstMatrix.length, cols1,
					secondMatrix.length, cols2));
		}
	}

	private static int validateRectangular(double[][] matrix, String name) {
		int cols = -1;
		for (int i = 0; i < matrix.length; i++) {
			double[] row = matrix[i];
			if (row == null) {
				throw new IllegalArgumentException(
						name + " матрица: строка " + i + " равна null");
			}
			if (cols < 0) {
				cols = row.length;
			} else if (row.length != cols) {
				throw new IllegalArgumentException(String.format(
						"%s матрица не прямоугольная: строка 0 имеет длину %d, " +
								"строка %d — %d", name, cols, i, row.length));
			}
		}
		return cols;
	}

	private static double[][] generate(int rows, int cols, long seed) {
		Random rnd = new Random(seed);
		double[][] mat = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				mat[i][j] = rnd.nextDouble() * 2 - 1;
			}
		}
		return mat;
	}

	@FunctionalInterface
	interface MatrixOp { double[][] apply(double[][] a, double[][] b); }

	private static double measureMs(int repeats, MatrixOp op, double[][] a, double[][] b) {
		for (int w = 0; w < 2; w++) op.apply(a, b);

		long best = Long.MAX_VALUE;
		for (int i = 0; i < repeats; i++) {
			long t0 = System.nanoTime();
			op.apply(a, b);
			long dt = System.nanoTime() - t0;
			if (dt < best) best = dt;
		}
		return best / 1_000_000.0;
	}

	private static boolean verify(double[][] expected, double[][] actual, double eps) {
		if (expected.length != actual.length) return false;
		for (int i = 0; i < expected.length; i++) {
			if (expected[i].length != actual[i].length) return false;
			for (int j = 0; j < expected[i].length; j++) {
				if (Math.abs(expected[i][j] - actual[i][j]) > eps) {
					System.err.printf("Mismatch [%d][%d]: expected=%.6f, actual=%.6f%n",
							i, j, expected[i][j], actual[i][j]);
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int[] sizes = {100, 200, 300, 400, 600, 800, 1200};

		System.out.printf("%-8s %-14s %-14s %-10s %-8s%n",
				"Size", "Naive (ms)", "Optimized (ms)", "Speedup", "Verify");
		System.out.println("-".repeat(60));

		for (int size : sizes) {
			double[][] a = generate(size, size, 42);
			double[][] b = generate(size, size, 43);

			double[][] expected = multiply(a, b);
			multiplyOptimized(a, b);

			int repeats = size <= 200 ? 5 : 3;
			double naiveMs = measureMs(repeats, MatrixMult::multiply, a, b);
			double optMs   = measureMs(repeats, MatrixMult::multiplyOptimized, a, b);
			boolean ok     = verify(expected, multiplyOptimized(a, b), 1e-9);

			System.out.printf("%-8d %-14.2f %-14.2f %-10.2f %-8s%n",
					size, naiveMs, optMs, naiveMs / optMs, ok ? "OK" : "FAIL");
		}
	}
}
