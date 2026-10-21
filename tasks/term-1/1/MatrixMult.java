import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class MatrixMult {

	// Оптимизированная версия: порядок циклов i-k-j + параллельный обход строк
	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
		int cols = secondMatrix[0].length;
		double[][] result = new double[firstMatrix.length][cols];
		IntStream.range(0, firstMatrix.length).parallel().forEach(i -> {
			for (int k = 0; k < secondMatrix.length; k++) {
				double a = firstMatrix[i][k];
				for (int j = 0; j < cols; j++) {
					result[i][j] += a * secondMatrix[k][j];
				}
			}
		});
		return result;
	}

	// Медленная эталонная версия (i-j-k)
	public static double[][] multiplyNaive(double[][] firstMatrix, double[][] secondMatrix) {
		double[][] result = new double[firstMatrix.length][secondMatrix[0].length];
		for (int i = 0; i < firstMatrix.length; i++) {
			for (int j = 0; j < secondMatrix[0].length; j++) {
				for (int k = 0; k < firstMatrix[0].length; k++) {
					result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}
		return result;
	}

	public static double[][] randomMatrix(int rows, int cols) {
		Random random = new Random();
		double[][] matrix = new double[rows][cols];
		for (double[] row : matrix) {
			for (int j = 0; j < cols; j++) {
				row[j] = random.nextDouble();
			}
		}
		return matrix;
	}

	public static void main(String[] args) {
		int size = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
		double[][] a = randomMatrix(size, size);
		double[][] b = randomMatrix(size, size);

		for (int run = 1; run <= 3; run++) {
			long t = System.nanoTime();
			double[][] expected = multiplyNaive(a, b);
			long naive = (System.nanoTime() - t) / 1_000_000;

			t = System.nanoTime();
			double[][] actual = multiply(a, b);
			long optimized = (System.nanoTime() - t) / 1_000_000;

			System.out.printf("Прогон %d: naive = %d мс, optimized = %d мс, совпадают: %b%n",
					run, naive, optimized, Arrays.deepEquals(expected, actual));
		}
	}
}