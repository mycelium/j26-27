import java.util.Random;

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

	public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix) throws ArithmeticException {
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
			double[] aRow = firstMatrix[i];
			double[] resRow = resMatrix[i];
			for (int r = 0; r < resCols; ++r) {
				double air = aRow[r];
				double[] bRow = secondMatrix[r];
				for (int j = 0; j < m; ++j) {
					resRow[j] += air * bRow[j];
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

	private static double[][] randomMatrix(int size, Random random) {
        double[][] matrix = new double[size][size];
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                matrix[i][j] = random.nextDouble();
            }
        }
        return matrix;
    }

	private static double measureMultiplyMillis(double[][] firstMatrix, double[][] secondMatrix, boolean optimized) {
        long start = System.nanoTime();
        var _ = optimized ? multiplyOptimized(firstMatrix, secondMatrix) : multiply(firstMatrix, secondMatrix);
        long elapsed = System.nanoTime() - start;
        return elapsed / 1_000_000.0;
    }

	private static void warmup(double[][] firstMatrix, double[][]secondMatrix) {
		int n_warmups = 20;
		for (int i = 0; i < n_warmups; ++i) {
			measureMultiplyMillis(firstMatrix, secondMatrix, false);
			measureMultiplyMillis(firstMatrix, secondMatrix, true);
		}
	}

	public static void main(String[] args) {
		Random random = new Random(67);

		for (int size : new int[]{250, 500, 750})
		{
			double[][] A = randomMatrix(size, random);
			double[][] B = randomMatrix(size, random);

			warmup(A, B);

			double elapsedBasic = measureMultiplyMillis(A, B, false);
			double elapsedOptimized = measureMultiplyMillis(A, B, true);
			double speedup = elapsedBasic / elapsedOptimized;

			System.out.printf("%-22s %15s%n", "Size:", getStrDimention(A));
			System.out.printf("%-22s %12.3f ms%n", "Basic realization:", elapsedBasic);
			System.out.printf("%-22s %12.3f ms%n", "Optimized realization:", elapsedOptimized);
			System.out.printf("%-22s %12.2f%n%n", "Speedup:", speedup);
		}
	}
}

