import java.util.concurrent.ThreadLocalRandom;

public class MatrixMult {

	private static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
		if (firstMatrix == null || secondMatrix == null || firstMatrix.length == 0 || secondMatrix.length == 0) {
			throw new IllegalArgumentException("Matrix is empty");
		}

		if (firstMatrix[0].length != secondMatrix.length) {
			throw new IllegalArgumentException("The matrices cannot be multiplied. The number of columns in the first matrix must match the number of rows in the second matrix");
		}

		double[][] resultMatrix = new double[firstMatrix.length][secondMatrix[0].length];

		for (int i = 0; i < firstMatrix.length; i++) {
			for (int j = 0; j < secondMatrix[0].length; j++) {
				for (int k = 0; k < firstMatrix[0].length; k++) {
					resultMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}

		return resultMatrix;
	}

	private static double[][] multiplyCyclePermutation(double[][] firstMatrix, double[][] secondMatrix) {
		if (firstMatrix == null || secondMatrix == null || firstMatrix.length == 0 || secondMatrix.length == 0) {
			throw new IllegalArgumentException("Matrix is empty");
		}

		if (firstMatrix[0].length != secondMatrix.length) {
			throw new IllegalArgumentException("The matrices cannot be multiplied. The number of columns in the first matrix must match the number of rows in the second matrix");
		}

		double[][] resultMatrix = new double[firstMatrix.length][secondMatrix[0].length];

		for (int i = 0; i < firstMatrix.length; i++) {
			for (int k = 0; k < firstMatrix[0].length; k++) {
				double tempVal = firstMatrix[i][k];
				for (int j = 0; j < secondMatrix[0].length; j++) {
					resultMatrix[i][j] += tempVal * secondMatrix[k][j];
				}
			}
		}

		return resultMatrix;
	}

	private static void printMatrix(double[][] matrix) {
		if (matrix == null || matrix.length == 0) {
			throw new IllegalArgumentException("Matrix is empty");
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.printf("%.2f\t ", matrix[i][j]);
			}
			System.out.println();
		}
	}

	private static void fillMatrixWithRandomValues(double[][] matrix, int minValue, int maxValue) {
		if (matrix == null || matrix.length == 0) {
			throw new IllegalArgumentException("Matrix is empty");
		}

		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				matrix[i][j] = ThreadLocalRandom.current().nextDouble(minValue, maxValue);
			}
		}
	}

	public static void main(String[] args) {
		try {
			int minValueForFillingMatrixWithRandomValues = 1;
			int maxValueForFillingMatrixWithRandomValues = 10;

			int rowFirstMatrix = 1_000;
			int columnFirstMatrix = 1_000;
			double[][] firstMatrix = new double[rowFirstMatrix][columnFirstMatrix];
			fillMatrixWithRandomValues(firstMatrix, minValueForFillingMatrixWithRandomValues, maxValueForFillingMatrixWithRandomValues);

			int rowSecondMatrix = 1_000;
			int columnSecondMatrix = 1_000;
			double[][] secondMatrix = new double[rowSecondMatrix][columnSecondMatrix];
			fillMatrixWithRandomValues(secondMatrix, minValueForFillingMatrixWithRandomValues, maxValueForFillingMatrixWithRandomValues);

			long startTime = System.currentTimeMillis();

			double[][] resultMatrix = multiply(firstMatrix, secondMatrix);

			long endTime = System.currentTimeMillis();
			long durationMultiply = endTime - startTime;

//			System.out.println("firstMatrix:");
//			printMatrix(firstMatrix);
//
//			System.out.println("secondMatrix:");
//			printMatrix(secondMatrix);
//
//			System.out.println("resultMatrix:");
//			printMatrix(resultMatrix);

			System.out.println("Execution time of multiply: " + durationMultiply + "ms");

			startTime = System.currentTimeMillis();

			resultMatrix = multiplyCyclePermutation(firstMatrix, secondMatrix);

			endTime = System.currentTimeMillis();
			durationMultiply = endTime - startTime;

//			System.out.println("firstMatrix:");
//			printMatrix(firstMatrix);
//
//			System.out.println("secondMatrix:");
//			printMatrix(secondMatrix);
//
//			System.out.println("resultMatrix:");
//			printMatrix(resultMatrix);

			System.out.println("Execution time of multiplyCyclePermutation: " + durationMultiply + "ms");
		} catch (IllegalArgumentException e) {
			System.out.println("Error when working with matrices: " + e.getMessage());
		} catch (java.lang.Exception e) {
			System.err.println("Unforeseen error: " + e.getMessage());
		}
	}
}
