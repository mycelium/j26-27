public class MatrixMult {

	private static final int BLOCK = 64;

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		checkSizes(firstMatrix, secondMatrix);
		int n = firstMatrix.length;
		int m = secondMatrix.length;
		int p = secondMatrix[0].length;

		// i-k-j order: inner loop walks rows of secondMatrix and result sequentially (cache friendly);
		// k and j are split into blocks so the used part of secondMatrix stays in CPU cache
		double[][] result = new double[n][p];
		for (int kk = 0; kk < m; kk += BLOCK) {
			int kEnd = Math.min(kk + BLOCK, m);
			for (int jj = 0; jj < p; jj += BLOCK) {
				int jEnd = Math.min(jj + BLOCK, p);
				for (int i = 0; i < n; i++) {
					double[] aRow = firstMatrix[i];
					double[] resultRow = result[i];
					for (int k = kk; k < kEnd; k++) {
						double aik = aRow[k];
						if (aik == 0) {
							continue;
						}
						double[] bRow = secondMatrix[k];
						for (int j = jj; j < jEnd; j++) {
							resultRow[j] += aik * bRow[j];
						}
					}
				}
			}
		}
		return result;
	}

	static void checkSizes(double[][] firstMatrix, double[][] secondMatrix) {
		if (firstMatrix == null || secondMatrix == null || firstMatrix.length == 0 || secondMatrix.length == 0) {
			throw new IllegalArgumentException("Matrices must not be null or empty");
		}
		if (firstMatrix[0].length != secondMatrix.length) {
			throw new IllegalArgumentException("Incompatible sizes: " + firstMatrix.length + "x" + firstMatrix[0].length
					+ " and " + secondMatrix.length + "x" + secondMatrix[0].length);
		}
	}

	// Straightforward i-j-k version, kept for comparison in the benchmark
	static double[][] multiplyNaive(double[][] firstMatrix, double[][] secondMatrix) {
		checkSizes(firstMatrix, secondMatrix);
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

	static double[][] randomMatrix(int rows, int cols) {
		java.util.Random random = new java.util.Random(42);
		double[][] matrix = new double[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = random.nextDouble();
			}
		}
		return matrix;
	}

	public static void main(String[] args) {
		// warm up JIT
		double[][] w = randomMatrix(200, 200);
		for (int i = 0; i < 5; i++) {
			multiply(w, w);
			multiplyNaive(w, w);
		}

		int[] sizes = {500, 1000, 2000};
		for (int size : sizes) {
			double[][] a = randomMatrix(size, size);
			double[][] b = randomMatrix(size, size);

			long start = System.nanoTime();
			double[][] naive = multiplyNaive(a, b);
			long naiveMs = (System.nanoTime() - start) / 1_000_000;

			start = System.nanoTime();
			double[][] fast = multiply(a, b);
			long fastMs = (System.nanoTime() - start) / 1_000_000;

			System.out.printf("%dx%d: naive %d ms, optimized %d ms, equal: %b%n",
					size, size, naiveMs, fastMs, almostEqual(naive, fast));
		}
	}

	static boolean almostEqual(double[][] x, double[][] y) {
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				if (Math.abs(x[i][j] - y[i][j]) > 1e-9) {
					return false;
				}
			}
		}
		return true;
	}
}
