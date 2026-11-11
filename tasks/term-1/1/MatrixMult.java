import java.util.Arrays;
import java.util.Random;

public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
		validate(firstMatrix, secondMatrix);

		int n = firstMatrix.length;
		int m = secondMatrix.length;
		int p = secondMatrix[0].length;

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

	public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix) {
		validate(firstMatrix, secondMatrix);

		int n = firstMatrix.length;
		int m = secondMatrix.length;
		int p = secondMatrix[0].length;

		double[][] result = new double[n][p];

		for (int i = 0; i < n; i++) {
			double[] firstRow = firstMatrix[i];
			double[] resultRow = result[i];
			for (int k = 0; k < m; k++) {
				double aik = firstRow[k];
				double[] secondRow = secondMatrix[k];
				for (int j = 0; j < p; j++) {
					resultRow[j] += aik * secondRow[j];
				}
			}
		}
		return result;
	}

	private static void validate(double[][] a, double[][] b) {
		if (a == null || b == null || a.length == 0 || b.length == 0 || a[0].length == 0 || b[0].length == 0) {
			throw new IllegalArgumentException("Матрицы не должны быть null или пустыми");
		}
		if (a[0].length != b.length) {
			throw new IllegalArgumentException("Число столбцов первой матрицы (" + a[0].length + ") должно равняться числу строк второй (" + b.length + ")");
		}
	}

	private static double[][] randomMatrix(int rows, int cols, Random random) {
		double[][] matrix = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = random.nextDouble();
			}
		}
		return matrix;
	}

	private static boolean equalMatrices(double[][] a, double[][] b, double eps) {
		if (a.length != b.length || a[0].length != b[0].length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				if (Math.abs(a[i][j] - b[i][j]) > eps) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int size = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
		Random random = new Random(42);


		double[][] warmA = randomMatrix(200, 200, random);
		double[][] warmB = randomMatrix(200, 200, random);
		for (int i = 0; i < 5; i++) {
			multiply(warmA, warmB);
			multiplyOptimized(warmA, warmB);
		}

		System.out.println("\nРазмер матриц: " + size + " x " + size);
		double[][] first = randomMatrix(size, size, random);
		double[][] second = randomMatrix(size, size, random);
        // double[][] first = randomMatrix(1200, 1500, random);
        // double[][] second = randomMatrix(1500, 800, random);

		long start = System.nanoTime();
		double[][] result = multiply(first, second);
		long naiveTime = System.nanoTime() - start;
		System.out.printf("multiply: %.3f с%n", naiveTime / 1e9);

		start = System.nanoTime();
		double[][] resultOpt = multiplyOptimized(first, second);
		long optTime = System.nanoTime() - start;
		System.out.printf("multiplyOptimized: %.3f с%n", optTime / 1e9);

		System.out.printf("Ускорение: %.2fx%n", (double) naiveTime / optTime);
		System.out.println("Результаты совпадают: " + equalMatrices(result, resultOpt, 1e-9));
	}
}