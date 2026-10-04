public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix == null || firstMatrix.length == 0 || firstMatrix[0] == null ||
				secondMatrix == null || secondMatrix.length == 0 || secondMatrix[0] == null) {
			System.out.println("Одна из матриц пуста!");
			return new double[1][1];
		}

		if (firstMatrix[0].length != secondMatrix.length) {
			System.out.println("Размеры матриц несогласованы!");
			return new double[1][1];
		}

		double[][] resultMatrix = new double[firstMatrix.length][secondMatrix[0].length];

		//таймер
		long startTime = System.nanoTime();

		for (int i=0; i < firstMatrix.length; i++) {
			for (int j=0; j < secondMatrix[0].length; j++) {
				for (int k=0; k < firstMatrix[0].length; k++) {
					resultMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}

		long endTime = System.nanoTime();
		System.out.printf("Время перемножения матриц классическим способом: %d нc\n", endTime - startTime);

		return resultMatrix;
	}
}
