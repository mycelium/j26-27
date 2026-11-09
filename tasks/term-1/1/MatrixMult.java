public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix == null || firstMatrix.length == 0 || firstMatrix[0] == null ||
				secondMatrix == null || secondMatrix.length == 0 || secondMatrix[0] == null) {
			throw new IllegalArgumentException("Матрицы не должны быть пустыми!");
		}

		if (firstMatrix[0].length != secondMatrix.length) {
			throw new IllegalArgumentException("Матрицы не согласованы по размеру!");
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

	public static void printMatrix(double[][] matrix) {
		for (double[] row : matrix) {
			for (double val : row) {
				System.out.printf("%f ", val);
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		//2x3 * 3x2
		System.out.println("Успешное умножение");
		double[][] m1 = {
				{1.0, 2.0, 3.0},
				{4.0, 5.0, 6.0}
		};
		double[][] m2 = {
				{7.0, 8.0},
				{9.0, 1.0},
				{2.0, 3.0}
		};

		double[][] result1 = MatrixMult.multiply(m1, m2);
		printMatrix(result1);
		System.out.println();

		//Несогласованные размеры (2x3 * 2x2)
		System.out.println("Несогласованные размеры");
		double[][] m3 = {
				{1.0, 2.0},
				{3.0, 4.0}
		};

		//double[][] result2 = MatrixMult.multiply(m1, m3);
		System.out.println();

		//Передача null
		System.out.println("Передача null");
		//double[][] result = MatrixMult.multiply(m1, null);
		System.out.println();


		//Матрицы большой размерности
		int bigN = 2000;
		double[][] big1 = new double[bigN][bigN];
		double[][] big2 = new double[bigN][bigN];

		for (int i = 0; i < bigN; i++) {
			for (int j = 0; j < bigN; j++) {
				big1[i][j] = i * i;
				big2[i][j] = i * i;
			}
		}

		double[][] bigResult = MatrixMult.multiply(big1, big2);
		System.out.printf("Матрицы размром %d на %d были перемножены\n", bigN, bigN);
		System.out.printf("Эл-т[1000][1000] результата перемножения: %f\n", bigResult[1001][1001]);

	}
}
