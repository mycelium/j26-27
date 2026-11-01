public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		int n = firstMatrix.length;
		int m = firstMatrix[0].length;
		int p = secondMatrix[0].length;

		if (m != secondMatrix.length) {
			throw new IllegalArgumentException("Matrix dimensions do not match");
		}

		double[][] result = new double[n][p];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < p; j++) {
				double sum = 0.0;
				for (int k = 0; k < m; k++) {
					sum += firstMatrix[i][k] * secondMatrix[k][j];
				}
				result[i][j] = sum;
			}
		}
		return result;
	}

	public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix){
		int n = firstMatrix.length;
		int m = firstMatrix[0].length;
		int p = secondMatrix[0].length;

		if (m != secondMatrix.length) {
			throw new IllegalArgumentException("Matrix dimensions do not match");
		}

		double[][] result = new double[n][p];
		for (int i = 0; i < n; i++) {
			for (int k = 0; k < m; k++) {
				double a = firstMatrix[i][k];
				for (int j = 0; j < p; j++) {
					result[i][j] += a * secondMatrix[k][j];
				}
			}
		}
		return result;
	}

	public static void main(String[] args) {
		int size = 500;
		double[][] a = randomMatrix(size, size);
		double[][] b = randomMatrix(size, size);

		multiply(a, b);
		multiplyOptimized(a, b);

		long start = System.nanoTime();
		multiply(a, b);
		long naive = System.nanoTime() - start;

		start = System.nanoTime();
		multiplyOptimized(a, b);
		long optimized = System.nanoTime() - start;

		System.out.printf("Naive:     %.3f s%n", naive / 1e9);
		System.out.printf("Optimized: %.3f s%n", optimized / 1e9);
		System.out.printf("Speedup:   %.2fx%n", (double) naive / optimized);
	}

	private static double[][] randomMatrix(int rows, int cols) {
		java.util.Random r = new java.util.Random(42);
		double[][] m = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				m[i][j] = r.nextDouble();
			}
		}
		return m;
	}
}