import java.util.Arrays;
import java.util.Random;

public class MatrixMultPar {

	public static double[][] multiplyParallel(double[][] firstMatrix, double[][] secondMatrix) {
		return multiplyParallel(firstMatrix, secondMatrix, Runtime.getRuntime().availableProcessors());
	}

	public static double[][] multiplyParallel(double[][] firstMatrix, double[][] secondMatrix, int threadsCount) {
		int n = firstMatrix.length;
		int m = secondMatrix.length;
		if (n == 0 || m == 0 || firstMatrix[0].length != m) {
			throw new IllegalArgumentException("Размеры матриц несовместимы");
		}
		int p = secondMatrix[0].length;
		double[][] result = new double[n][p];

		int threads = Math.max(1, Math.min(threadsCount, n));
		Thread[] workers = new Thread[threads];
		int chunk = (n + threads - 1) / threads;

		for (int t = 0; t < threads; t++) {
			final int from = t * chunk;
			final int to = Math.min(n, from + chunk);
			workers[t] = new Thread(() -> multiplyRows(firstMatrix, secondMatrix, result, from, to));
			workers[t].start();
		}
		for (Thread w : workers) {
			try {
				w.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException("Прервано ожидание потоков", e);
			}
		}
		return result;
	}


	private static void multiplyRows(double[][] a, double[][] b, double[][] c, int from, int to) {
		int m = b.length;
		int p = b[0].length;
		for (int i = from; i < to; i++) {
			double[] ai = a[i];
			double[] ci = c[i];
			for (int k = 0; k < m; k++) {
				double aik = ai[k];
				double[] bk = b[k];
				for (int j = 0; j < p; j++) {
					ci[j] += aik * bk[j];
				}
			}
		}
	}


	public static double[][] multiplySequential(double[][] a, double[][] b) {
		double[][] c = new double[a.length][b[0].length];
		multiplyRows(a, b, c, 0, a.length);
		return c;
	}


	private static double[][] randomMatrix(int rows, int cols, long seed) {
		Random rnd = new Random(seed);
		double[][] m = new double[rows][cols];
		for (double[] row : m) {
			for (int j = 0; j < cols; j++) {
				row[j] = rnd.nextDouble();
			}
		}
		return m;
	}

	private static boolean equalMatrices(double[][] x, double[][] y) {
		for (int i = 0; i < x.length; i++) {
			for (int j = 0; j < x[i].length; j++) {
				if (Math.abs(x[i][j] - y[i][j]) > 1e-9) {
					return false;
				}
			}
		}
		return true;
	}

	private static double timeMs(Runnable task, int runs) {
		double[] times = new double[runs];
		for (int r = 0; r < runs; r++) {
			long start = System.nanoTime();
			task.run();
			times[r] = (System.nanoTime() - start) / 1e6;
		}
		Arrays.sort(times);
		return times[runs / 2];
	}

	public static void main(String[] args) {
		int cores = Runtime.getRuntime().availableProcessors();
		System.out.println("Доступно логических процессоров: " + cores);

		int[] sizes = {500, 1000, 1500};
		int[] threadCounts = {1, 2, 4, 8, 12, 16, 32};

		for (int size : sizes) {
			double[][] a = randomMatrix(size, size, 1);
			double[][] b = randomMatrix(size, size, 2);

			// прогрев JIT
			multiplySequential(a, b);
			multiplyParallel(a, b, cores);

			double[][] expected = multiplySequential(a, b);
			double seqMs = timeMs(() -> multiplySequential(a, b), 5);

			System.out.printf("%nРазмер матрицы: %dx%d %n", size, size);
			System.out.printf("Однопоточная: %.1f мс%n", seqMs);
			System.out.printf("%-8s %-12s %-10s %s%n", "Потоков", "Время, мс", "Ускорение", "Корректно");

			for (int threads : threadCounts) {
				double[][] res = multiplyParallel(a, b, threads);
				boolean ok = equalMatrices(expected, res);
				double ms = timeMs(() -> multiplyParallel(a, b, threads), 5);
				System.out.printf("%-8d %-12.1f %-10.2f %s%n", threads, ms, seqMs / ms, ok);
			}
		}
	}
}