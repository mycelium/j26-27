public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix[0].length != secondMatrix.length){
			throw new IllegalArgumentException("Несовместимые размеры матриц для перемножения");
		}

		double[][] resultMatrix = new double[firstMatrix.length][secondMatrix[0].length];

		for (int i = 0; i < firstMatrix.length; i++) {
    		for (int j = 0; j < secondMatrix[0].length; j++) {
        		for (int k = 0; k < secondMatrix.length; k++) {
            		resultMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j]; 
				}
			}
		}

		return resultMatrix;
	}

	public static double[][] multiplyOpt(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix[0].length != secondMatrix.length){
			throw new IllegalArgumentException("Несовместимые размеры матриц для перемножения");
		}

		double[][] resultMatrix = new double[firstMatrix.length][secondMatrix[0].length];

		for (int i = 0; i < firstMatrix.length; i++) {
    		for (int k = 0; k < secondMatrix.length; k++) {
        		double val = firstMatrix[i][k]; 
        		for (int j = 0; j < secondMatrix[0].length; j++) {
            		resultMatrix[i][j] += val * secondMatrix[k][j]; 
				}
			}
		}

		return resultMatrix;
	}

	public static void printMatrix(double[][] matrix, String title) {
		System.out.println(title);
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.printf("%.0f ", matrix[i][j]);
			}
			System.out.println(); 
		}
		System.out.println(); 
	}

	public static double[][] createSeqMatrix(int rows, int cols) {
		double[][] matrix = new double[rows][cols];
		int counter = 1;

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				matrix[i][j] = counter;
				counter++;
			}
		}
		return matrix;
	}

	public static void main(String[] args) {
		int rows = 1000;
		int cols = 2000;

		double[][] matrix1 = createSeqMatrix(rows, cols);
		double[][] matrix2 = createSeqMatrix(cols, rows);

		long startTime = System.nanoTime();
		multiply(matrix1, matrix2);
		long endTime = System.nanoTime();

		long duration = (endTime - startTime) / 1_000_000;
		System.out.println("Время выполнения классической версии: " + duration + " мс");

		long startTimeOpt = System.nanoTime();
		multiplyOpt(matrix1, matrix2);
		long endTimeOpt = System.nanoTime();

		long durationOpt = (endTimeOpt - startTimeOpt) / 1_000_000;
		System.out.println("Время выполнения оптимизированной версии: " + durationOpt + " мс");
		
		
	}
}
