public class MatrixMult {
		private static final int SIZE = 1200;

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		
		checkMatrices(firstMatrix, secondMatrix);

        int n = firstMatrix.length;
        int m = firstMatrix[0].length;
        int l = secondMatrix[0].length;

        double[][] result = new double[n][l];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < l; j++) {
                double sum = 0;
                for (int k = 0; k < m; k++) {
                    sum += firstMatrix[i][k] * secondMatrix[k][j];
                }
                result[i][j] = sum;
            }
        }
        return result;
	}

	public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix){

        checkMatrices(firstMatrix, secondMatrix);

        double[][] transposed = transpose(secondMatrix);

        int n = firstMatrix.length;
        int m = firstMatrix[0].length;
        int l = secondMatrix[0].length;

        double[][] result = new double[n][l];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < l; j++) {
                double sum = 0;
                for (int k = 0; k < m; k++) {
                    sum += firstMatrix[i][k] * transposed[j][k];
                }
                result[i][j] = sum;
            }
        }
        return result;
    }

    public static double[][] transpose(double[][] matrix){
        int n = matrix.length;
        int m = matrix[0].length;

        double[][] result = new double[m][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    private static void checkMatrices(double[][] firstMatrix, double[][] secondMatrix){

        if (firstMatrix == null || secondMatrix == null){
            throw new IllegalArgumentException("Матрицы не должны быть null");
        }

        if (firstMatrix.length == 0 || secondMatrix.length == 0){
            throw new IllegalArgumentException("Матрицы не должны быть пустыми");
        }

        if (firstMatrix[0] == null || secondMatrix[0] == null){
            throw new IllegalArgumentException("Строки матриц не должны быть null");
        }

        int firstCols = firstMatrix[0].length;
        int secondCols = secondMatrix[0].length;

        if (firstCols == 0 || secondCols == 0){
            throw new IllegalArgumentException("Матрицы должны содержать хотя бы один столбец");
        }

        for (int i = 0; i < firstMatrix.length; i++){
            if (firstMatrix[i] == null || firstMatrix[i].length != firstCols){
                throw new IllegalArgumentException("Первая матрица должна быть прямоугольной");
            }
        }

        for (int i = 0; i < secondMatrix.length; i++){
            if (secondMatrix[i] == null || secondMatrix[i].length != secondCols) {
                throw new IllegalArgumentException("Вторая матрица должна быть прямоугольной");
            }
        }

        if (firstCols != secondMatrix.length){
            throw new IllegalArgumentException("Число столбцов первой матрицы должно совпадать с числом строк второй матрицы");
        }
    }

    public static double[][] generateMatrix(int n, int m){
        double[][] matrix = new double[n][m];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = Math.random() * 10;
            }
        }
        return matrix;
    }

    public static void main(String[] args){
        double[][] firstMatrix = generateMatrix(SIZE, SIZE);
        double[][] secondMatrix = generateMatrix(SIZE, SIZE);

        try {
            long start = System.nanoTime();
            double[][] result1 = multiply(firstMatrix, secondMatrix);
            long end = System.nanoTime();
            double time1 = (end - start) / 1_000_000.0;

            start = System.nanoTime();
            double[][] result2 = multiplyOptimized(firstMatrix, secondMatrix);
            end = System.nanoTime();
            double time2 = (end - start) / 1_000_000.0;

            System.out.println("Размер матриц: " + SIZE + " x " + SIZE);
            System.out.printf("Обычное умножение: %.3f мс%n", time1);
            System.out.printf("Умножение с транспонированием: %.3f мс%n", time2);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при умножении матриц: " + e.getMessage());
        }
    }
}
