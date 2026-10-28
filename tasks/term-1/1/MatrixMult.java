public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
		int rowsA = firstMatrix.length;
		int colsA = firstMatrix[0].length;
		int rowsB = secondMatrix.length;
		int colsB = secondMatrix[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException("Несовместимые размеры матриц");
        }

		double[][] result = new double[rowsA][colsB];

		for (int i = 0; i < rowsA; i++) {
			for (int j = 0; j < colsB; j++) {
				for (int v = 0; v < colsA; v++) {
					result[i][j] += firstMatrix[i][v] * secondMatrix[v][j];
				}
			}
		}

		return result;
	}

    public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix) {
        int rowsA = firstMatrix.length;
        int colsA = firstMatrix[0].length;
        int rowsB = secondMatrix.length;
        int colsB = secondMatrix[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException("Несовместимые размеры матриц");
        }

        double[][] result = new double[rowsA][colsB];

        double[][] transposedB = new double[colsB][rowsB];
        for (int i = 0; i < rowsB; i++) {
            for (int j = 0; j < colsB; j++) {
                transposedB[j][i] = secondMatrix[i][j];
            }
        }

        for (int i = 0; i < rowsA; i++) {
            for (int k = 0; k < colsA; k++) {
                double aVal = firstMatrix[i][k];
                double[] rowA = result[i];
                double[] rowB = transposedB[k];
                
                for (int j = 0; j < colsB; j++) {
                    rowA[j] += aVal * rowB[j];
                }
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int size = 1000;
        
        System.out.println("Генерация матриц " + size + "x" + size);
        double[][] A = generateMatrix(size, size);
        double[][] B = generateMatrix(size, size);

        System.out.println("Начало умножения (без оптмимзации)");
        long startTime1 = System.nanoTime();
        
		double[][] C1 = multiply(A, B);
        
        long endTime1 = System.nanoTime();
        long durationMs1 = (endTime1 - startTime1) / 1_000_000;

        System.out.println("Время выполнения: " + durationMs1 + " мс");

		

        System.out.println("Начало умножения (с оптимищацией)");
        long startTime2 = System.nanoTime();
        

		double[][] C2 = multiplyOptimized(A, B);
        
        long endTime2 = System.nanoTime();
        long durationMs2 = (endTime2 - startTime2) / 1_000_000;

        System.out.println("Время выполнения: " + durationMs2 + " мс");


    }

    private static double[][] generateMatrix(int rows, int cols) {
        double[][] matrix = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = Math.random();
            }
        }
        return matrix;
    }

}

