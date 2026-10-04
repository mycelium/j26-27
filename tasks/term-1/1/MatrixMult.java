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

		// таймер
		long startTime = System.nanoTime();

		int firstMatrix_i = firstMatrix.length;
		int firstMatrix_j = firstMatrix[0].length;
		int secondMatrix_j = secondMatrix[0].length;

		double[] resultMatrixTMP = new double[firstMatrix_i * secondMatrix_j];

		for (int i=0; i < firstMatrix_i; i++) {
			int shiftIndex = i * secondMatrix_j;
			// переставил j и k местами, чтобы регистр двигался непрерывно вдоль строк первой матрицы
			// (по идее то же самое, что и выпрямить матрицу в одномерную)
			// (но при такой реализации строки результата вычисляются не в один проход, а за несколько итераций)
			for (int k=0; k < firstMatrix_j; k++) {
				// один раз достаем элемент строки из первой матрицы и строку из второй
				double rowElement = firstMatrix[i][k];
				double[] rowSecondMatrix = secondMatrix[k];
				for (int j=0; j < secondMatrix_j; j++) {
					resultMatrixTMP[shiftIndex + j] += rowElement * rowSecondMatrix[j];
				}
			}
		}

		double[][] resultMatrix = new double[firstMatrix_i][secondMatrix_j];
		for (int i = 0; i < firstMatrix_i; i++) {
			// копируем куски одномерного массива в итоговые строки
			resultMatrix[i] = new double[secondMatrix_j];
			System.arraycopy(resultMatrixTMP, i * secondMatrix_j, resultMatrix[i], 0, secondMatrix_j);
		}

		long endTime = System.nanoTime();
		System.out.printf("Время перемножения матриц: %d нс%n", endTime - startTime);

		return resultMatrix;
	}
}
