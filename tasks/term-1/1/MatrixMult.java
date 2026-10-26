import java.util.Random;
import java.util.stream.IntStream;

public class MatrixMult {
    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
		int n = firstMatrix.length, m = secondMatrix.length, p = secondMatrix[0].length;
		if (firstMatrix[0].length != m) {
			throw new IllegalArgumentException("Число столбцов первой матрицы должно равняться числу строк второй");
		}
		double[][] result = new double[n][p];
		IntStream.range(0,n).parallel().forEach(i -> {
			for (int k = 0; k < m; k++) {
				double a = firstMatrix[i][k];
				double[] rowB = secondMatrix[k];
				double[] rowC = result[i];
				for (int j = 0; j < p; j++) {
					rowC[j] += a * rowB[j];
				}
 			}
		});
		return result;
    }

    public static double[][] multiplyNaive(double[][] a, double[][] b) {
		int n = a.length, m = b.length, p = b[0].length;
		double[][] c = new double[n][p];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < p; j++) 
				for (int k = 0; k < m; k++) 
					c[i][j] += a[i][k] * b[k][j]; 
		return c;
	}

    private static double[][] random(int n) {
        Random rnd = new Random();
        double[][] m = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                m[i][j] = rnd.nextDouble();
        return m;
    }

    public static void main(String[] args) {
        int n = 1000;
        double[][] a = random(n), b = random(n);

        long start = System.nanoTime();
        multiplyNaive(a, b);
        System.out.printf("Наивный:          %d ms%n", (System.nanoTime() - start) / 1_000_000);

        start = System.nanoTime();
        multiply(a, b);
        System.out.printf("Оптимизированный: %d ms%n", (System.nanoTime() - start) / 1_000_000);
    }
}