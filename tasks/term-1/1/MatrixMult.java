import java.util.Random;

public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
        validateMatrices(firstMatrix, secondMatrix);
        int rows = firstMatrix.length;
        int strlen = firstMatrix[0].length;
        int cols = secondMatrix[0].length;
        double[][] result = new double[rows][cols];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){
                for (int k = 0; k<strlen;k++){
                    result[i][j] += firstMatrix[i][k]*secondMatrix[k][j];
                }
            }
        }
		return result;
	}


private static void validateMatrices(double[][] firstMatrix, double[][] secondMatrix) {
    if (firstMatrix.length == 0 || secondMatrix.length == 0) {
        throw new IllegalArgumentException("Matrix cannot be empty");
    }
    int firstCols = firstMatrix[0].length;
    int secondCols = secondMatrix[0].length;
    if (firstCols == 0 || secondCols == 0) {
            throw new IllegalArgumentException("Matrix must contain columns");
    }
    for (int i = 0; i < firstMatrix.length; i++) {
        if (firstMatrix[i] == null || firstMatrix[i].length != firstCols) {
            throw new IllegalArgumentException("Invalid first matrix row: " + i);
        }
    }
    for (int i = 0; i < secondMatrix.length; i++) {
        if (secondMatrix[i] == null || secondMatrix[i].length != secondCols) {
            throw new IllegalArgumentException("Invalid second matrix row: " + i);
        }
    }
    if (firstCols != secondMatrix.length) {
        throw new IllegalArgumentException("Incompatible matrix dimensions");
    }

}



public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix) {
    validateMatrices(firstMatrix, secondMatrix);
    int rows = firstMatrix.length;
    int cols = secondMatrix[0].length;
    int strlen = firstMatrix[0].length;
    double[][] result = new double[rows][cols];
    for (int i = 0; i < rows; i++) {
        for (int k = 0; k < strlen; k++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] +=
                    firstMatrix[i][k] * secondMatrix[k][j];
            }
        }
    }

    return result;
}


private static void printMatrix(double[][] matrix) {
    for (int i = 0; i < matrix.length; i++) {
        for (int j = 0; j < matrix[i].length; j++) {
            System.out.printf("%.2f ", matrix[i][j]);
        }
        System.out.println();
    }
}
public static void main(String[] args) {
    //проверка на корректность математических вычислений обоих методов
    double[][] testA = {
        {1, 2, 3, 4},
        {5, 6, 7, 8}
    };

    double[][] testB = {
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 9},
        {10, 11, 12}
    };

    System.out.println("Test 1: correct matrix dimensions");
    System.out.println("Standard multiplication:");
    printMatrix(multiply(testA, testB));
    System.out.println("Optimized multiplication:");
    printMatrix(multiplyOptimized(testA, testB));
//проверка на умножение матриц с несовместимыми размерами
    double[][] wrongA = new double[2][3];
    double[][] wrongB = new double[4][2];

    System.out.println("Test 2: incompatible matrix dimensions");

    try {
        multiply(wrongA, wrongB);
    } catch (IllegalArgumentException e) {
        System.out.println("Exception caught: " + e.getMessage());
    }

    int rows = 300;
    int strlen = 500;
    int cols = 400;

    double[][] firstMatrix = new double[rows][strlen];
    double[][] secondMatrix = new double[strlen][cols];

    Random random = new Random(37);
    for (int i = 0; i < rows; i++) {
        for (int j = 0; j < strlen; j++) {
            firstMatrix[i][j] = random.nextDouble();
        }
    }
    for (int i = 0; i < strlen; i++) {
        for (int j = 0; j < cols; j++) {
            secondMatrix[i][j] = random.nextDouble();
        }
    }
    long start = System.nanoTime();
    double[][] result1 = multiply(firstMatrix, secondMatrix);
    long end = System.nanoTime();
    double time1 = (end - start) / 1_000_000.0;
    start = System.nanoTime();
    double[][] result2 = multiplyOptimized(firstMatrix, secondMatrix);
    end = System.nanoTime();
    double time2 = (end - start) / 1_000_000.0;
    
    System.out.println("Matrix size: " + rows + "x" + strlen + " * " + strlen + "x" + cols);

    System.out.printf(
        "Standard multiplication: %.3f ms%n", time1
    );
    System.out.printf(
        "Optimized multiplication: %.3f ms%n", time2
    );
    System.out.printf(
        "Speedup: %.2f times%n", time1 / time2
    );
}
}


