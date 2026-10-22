import java.util.Random;

public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix[0].length == secondMatrix.length){
			double[][] result = new double[firstMatrix.length][secondMatrix[0].length];
			for (int i = 0; i < firstMatrix.length; i++) {
				for (int j = 0; j < secondMatrix[0].length; j++) {
					for (int k = 0; k < secondMatrix[0].length; k++) {
						result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
					}
				}
			}
			return result;
		} else {
			throw new IllegalArgumentException("Размеры матриц не подходят для перемножения");
		}
	}


	public static double[][] multiply_optimized(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix[0].length == secondMatrix.length){
			double[][] result = new double[firstMatrix.length][secondMatrix[0].length];
			for (int i = 0; i < firstMatrix.length; i++) {
				for (int k = 0; k < secondMatrix.length; k++) {
					for (int j = 0; j < secondMatrix[0].length; j++) {
						result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
					}
				}
			}
			return result;
		} else {
			throw new IllegalArgumentException("Размеры матриц не подходят для перемножения");
		}
	}

	public static void MatrixPrint(double[][] Matrix){
		for (int i = 0; i < Matrix.length; i++) {
			for (int j = 0; j < Matrix[i].length; j++) {
				System.out.print(Matrix[i][j] + "  ");
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		int size = 1000;
		double[][] A = new double[size][size];
		double[][] B = new double[size][size];

		Random random = new Random();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				A[i][j] = random.nextInt(101);
				B[i][j] = random.nextInt(101);
			}
		}

		long start = System.nanoTime();
		double[][] result = MatrixMult.multiply(A, B);
		long end = System.nanoTime();
		double time_d = (end - start) / 1_000_000.0;

		long start2 = System.nanoTime();
		double[][] result2 = MatrixMult.multiply_optimized(A, B);
		long end2 = System.nanoTime();
		double time_o = (end2 - start2) / 1_000_000.0;

		// MatrixMult.MatrixPrint(result);
		System.out.println("Время выполнения (не оптимизировано): " + time_d + "мс");
		System.out.println("Время выполнения (оптимизировано): " + time_o + "мс");
	}
}
