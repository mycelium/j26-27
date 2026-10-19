public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		checkDimensions(firstMatrix, secondMatrix);

		int rowsFirst = firstMatrix.length;
		int colsFirst = firstMatrix[0].length;
		int colsSecond = secondMatrix[0].length;
		double[][] result = new double[rowsFirst][colsSecond];

		for (int i = 0; i < rowsFirst; i++) {
            for (int j = 0; j < colsFirst; j++) {
                for (int k = 0; k < colsSecond; k++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return result;
	}

	    public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix) {
        checkDimensions(firstMatrix, secondMatrix);

        int rowsFirst = firstMatrix.length;
        int colsFirst = firstMatrix[0].length;
        int colsSecond = secondMatrix[0].length;

        double[][] transposedSecond = new double[colsSecond][colsFirst];
        for (int i = 0; i < colsSecond; i++) {
            for (int j = 0; j < colsFirst; j++) {
                transposedSecond[i][j] = secondMatrix[j][i];
            }
        }

        double[][] result = new double[rowsFirst][colsSecond];

        for (int i = 0; i < rowsFirst; i++) {
            for (int j = 0; j < colsSecond; j++) {
                for (int k = 0; k < colsFirst; k++) {
                    result[i][j] += firstMatrix[i][k] * transposedSecond[j][k];
                }
            }
        }
        return result;
    }

	private static void checkDimensions(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix == null || secondMatrix == null || firstMatrix.length == 0 || secondMatrix.length == 0) {
			throw new IllegalArgumentException("Матрицы не могут быть пустыми (null) или иметь нулевую размерность.");
		}
		if (firstMatrix[0].length != secondMatrix.length){
			throw new IllegalArgumentException(String.format(
                "Невозможно перемножить матрицы: количество столбцов первой (%d) не равно количеству строк второй (%d).", 
                firstMatrix[0].length, firstMatrix.length));
		}
	}

	public static double[][] generateMatrix(int sizeC, int sizeW) {
		double[][] matrix = new double[sizeW][sizeC];
		for (int i = 0; i < sizeW; i++){
			for (int j = 0; j < sizeC; j++){
				matrix[i][j] = Math.random() * 10;
			}
		}
		return matrix;
	}

    private static void measureTime(double[][] firstMatrix, double[][] secondMatrix) {
        long start1 = System.nanoTime();
        double[][] resultNaive = multiply(firstMatrix, secondMatrix);
        long end1 = System.nanoTime();
        double timeNaive = (end1 - start1) / 1_000_000.0;

        long start2 = System.nanoTime();
        double[][] resultOptimized = multiplyOptimized(firstMatrix, secondMatrix);
        long end2 = System.nanoTime();
        double timeOptimized = (end2 - start2) / 1_000_000.0;

        System.out.printf("Время наивного метода:         %.4f мс%n", timeNaive);
        System.out.printf("Время оптимизированного:       %.4f мс%n", timeOptimized);
        
        double difference = timeNaive - timeOptimized;
        double ratio = timeNaive / timeOptimized;

        if (difference > 0.001) {
            System.out.printf("Оптимизированный метод быстрее в %.2f раз(а)%n", ratio);
        } else if (Math.abs(difference) <= 0.001) {
            System.out.println("Одинаковое время выполнения");
        } else {
            System.out.printf("Оптимизированный метод медленнее в %.2f раз(а)%n", 1 / ratio);
        }
		
		boolean isCorrect = true;
        for (int i = 0; i < resultNaive.length && isCorrect; i++) {
            for (int j = 0; j < resultNaive[0].length; j++) {
                if (Math.abs(resultNaive[i][j] - resultOptimized[i][j]) > 1e-9) {
                    isCorrect = false;
                    break;
                }
            }
        }
        System.out.println("Проверка корректности умножения: результаты умножения матриц " + (isCorrect ? "совпадают" : "не совпадают!"));
    }

	public static void printMatrix(double[][] matrix){
		for (double[] row : matrix) {
            for (double val : row) {
                System.out.printf("%8.2f ", val);
            }
            System.out.println();
        }
	}

	public static void main(String[] args) {
        System.out.println("Маленькие матрицы (3x3):");
        double[][] smallA = generateMatrix(3, 3); 
        double[][] smallB = generateMatrix(3, 3);

        System.out.println("Матрица A:");
        printMatrix(smallA);
        System.out.println("\nМатрица B:");
        printMatrix(smallB);

        System.out.println("\nРезультат умножения:");
        double[][] smallResult = multiply(smallA, smallB);
        printMatrix(smallResult);

		measureTime(smallA, smallB);

        System.out.println("\nБольшие матрицы (800x800)");
        double[][] bigA = generateMatrix(800, 800);
        double[][] bigB = generateMatrix(800, 800);
        
        measureTime(bigA, bigB);
    }

}
