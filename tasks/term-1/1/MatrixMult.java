import java.util.Arrays;
import java.util.Random;

public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		int firstRows = firstMatrix.length;
		int commonSize = firstMatrix[0].length;
		int secondCols = secondMatrix[0].length;

		if (commonSize != secondMatrix.length){
			throw new IllegalArgumentException("Число столбцов первой матрицы не равно числу строк второй!");
		}

		double[][] result = new double[firstRows][secondCols];

		for (int i = 0; i < firstRows; i++){
			for (int j = 0; j < secondCols; j++){
				for (int k = 0; k < commonSize; k++){
					result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}
		return result;
	}

	public static double[][] multiplyReordered(double[][] firstMatrix, double[][] secondMatrix){
		int firstRows = firstMatrix.length;
		int commonSize = firstMatrix[0].length;
		int secondCols = secondMatrix[0].length;

		if (commonSize != secondMatrix.length){
			throw new IllegalArgumentException("Число столбцов первой матрицы не равно числу строк второй!");
		}

		double[][] result = new double[firstRows][secondCols];

		for (int i = 0; i < firstRows; i++){
			for (int k = 0; k < commonSize; k++){
				for (int j = 0; j < secondCols; j++){
					result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}
		return result;
	}

	public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix){
		int firstRows = firstMatrix.length;
		int commonSize = firstMatrix[0].length;
		int secondCols = secondMatrix[0].length;

		if (commonSize != secondMatrix.length){
			throw new IllegalArgumentException("Число столбцов первой матрицы не равно числу строк второй!");
		}

		double[][] result = new double[firstRows][secondCols];

		for (int i = 0; i < firstRows; i++){
			double[] resultRow = result[i];
			for (int k = 0; k < commonSize; k++){
				double firstElement = firstMatrix[i][k];
				double[] secondRow = secondMatrix[k];
				for (int j = 0; j < secondCols; j++){
					resultRow[j] += firstElement * secondRow[j];
				}
			}
		}
		return result;
	}

	public static double[][] randomMatrix(int rows, int cols, Random random){
		double[][] matrix = new double[rows][cols];

		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				matrix[i][j] = random.nextDouble();
			}
		}
		return matrix;
	}

	public static void printMatrix(String title, double[][] matrix){
		System.out.println(title + ":");
		for (double[] row : matrix){
			System.out.println(Arrays.toString(row));
		}
	}

	public static double measureMs(int version, double[][] firstMatrix, double[][] secondMatrix){
		long start = System.nanoTime();

		switch (version){
			case 0: multiply(firstMatrix, secondMatrix); break;
			case 1: multiplyReordered(firstMatrix, secondMatrix); break;
			case 2: multiplyOptimized(firstMatrix, secondMatrix); break;
		}

		long end = System.nanoTime();
		return (end - start) / 1_000_000.0;
	}
	public static void main(String[] args){
		double[][] smallFirst = {{1, 2, 3}, {4, 5, 6}};
		double[][] smallSecond = {{1, 2}, {3, 4}, {5, 6}};

		printMatrix("multiply", multiply(smallFirst, smallSecond));
		printMatrix("multiplyReordered", multiplyReordered(smallFirst, smallSecond));
		printMatrix("multiplyOptimized", multiplyOptimized(smallFirst, smallSecond));

		Random random = new Random();
		int[] sizes = {200, 500, 1000};
		String[] names = {"multiply", "multiplyReordered", "multiplyOptimized"};

		for (int size : sizes){
			double[][] firstMatrix = randomMatrix(size, size, random);
			double[][] secondMatrix = randomMatrix(size, size, random);

			double timeNaiveMs = 0;
			for (int version = 0; version < names.length; version++){
				double timeMs = measureMs(version, firstMatrix, secondMatrix);
				if (version == 0){
					timeNaiveMs = timeMs;   
				}
				System.out.printf("Результат %s при n = %d: %.2f ms (x%.1f)%n", names[version], size, timeMs, timeNaiveMs / timeMs);
			}
		}
	}

}
