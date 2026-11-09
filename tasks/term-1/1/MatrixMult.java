import java.util.Random;


public class MatrixMult {

    private static void checkSizes(double[][] firstMatrix, double[][] secondMatrix) {
        if (firstMatrix == null || secondMatrix == null
                || firstMatrix.length == 0 || secondMatrix.length == 0
                || firstMatrix[0].length == 0 || secondMatrix[0].length == 0) {
            throw new IllegalArgumentException("Matrices must not be null or empty");
        }
        if (firstMatrix[0].length != secondMatrix.length) {
            throw new IllegalArgumentException("Matrices cannot be multiplied: "
                    + firstMatrix.length + "x" + firstMatrix[0].length + " and "
                    + secondMatrix.length + "x" + secondMatrix[0].length);
        }
    }

    public static double[][] multiply( double[][] firstMatrix, double[][] secondMatrix) {

        checkSizes(firstMatrix, secondMatrix);

        int rows = firstMatrix.length;
        int columns = secondMatrix[0].length;
        int commonSize = firstMatrix[0].length;

        double[][] result = new double[rows][columns];

       for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                 double sum = 0;
                for (int k = 0; k < commonSize; k++) {
                     sum += firstMatrix[i][k] * secondMatrix[k][j];
                    }
                result[i][j] = sum;
            }
        }

        return result;
    }

    public static double[][] multiplyReordered( double[][] firstMatrix, double[][] secondMatrix) {

        checkSizes(firstMatrix, secondMatrix);

        int rows = firstMatrix.length;
        int columns = secondMatrix[0].length;
        int commonSize = firstMatrix[0].length;

        double[][] result = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < commonSize; k++) {
                double a = firstMatrix[i][k];

                for (int j = 0; j < columns; j++) {
                    result[i][j] += a * secondMatrix[k][j];
                }
            }
        }

        return result;
    }


    
public static double[][] multiplyBlocked( double[][] firstMatrix, double[][] secondMatrix, int blockSize) {

    checkSizes(firstMatrix, secondMatrix);

    if (blockSize <= 0) {
        throw new IllegalArgumentException("Block size must be positive");
    }

    int rows = firstMatrix.length;
    int commonSize = firstMatrix[0].length;
    int columns = secondMatrix[0].length;

    double[][] result = new double[rows][columns];

    for (int ii = 0; ii < rows; ii += blockSize) {
        for (int kk = 0; kk < commonSize; kk += blockSize) {
            for (int jj = 0; jj < columns; jj += blockSize) {

                int iLimit = Math.min(ii + blockSize, rows);
                int kLimit = Math.min(kk + blockSize, commonSize);
                int jLimit = Math.min(jj + blockSize, columns);

                for (int i = ii; i < iLimit; i++) {
                    for (int k = kk; k < kLimit; k++) {
                        double a = firstMatrix[i][k];

                        for (int j = jj; j < jLimit; j++) {
                            result[i][j] += a * secondMatrix[k][j];
                        }
                    }
                }
            }
        }
    }

    return result;
}



    public static double[][] createMatrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Matrix size must be positive: " + rows + "x" + cols);
        }

        double[][] matrix = new double[rows][cols];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextDouble();
            }
        }

        return matrix;
    }

    public static void printMatrix(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    
    public static void main(String[] args) {


        double[][] a = {{1, 2, 3}, {4, 5, 6}};
        double[][] b = {{7, 8}, {9, 10}, {11, 12}};
        System.out.println("2x3 * 3x2 (multiply):");
        printMatrix(multiply(a, b));
        System.out.println("2x3 * 3x2 (reordered):");
        printMatrix(multiplyReordered(a, b));
        System.out.println("2x3 * 3x2 (blocked):");
        printMatrix(multiplyBlocked(a, b, 2));

        double[][] c = {{1, 2}, {3, 4}};
        double[][] d = {{5, 6}, {7, 8}};
        System.out.println("2x2 * 2x2 (multiply):");
        printMatrix(multiply(c, d));

        try {
            multiply(a, a);
            System.out.println("ОШИБКА: исключение не выброшено");
        } catch (IllegalArgumentException e) {
            System.out.println("2x3 * 2x3: " + e.getMessage());
        }

        try {
            createMatrix(0, 5);
            System.out.println("ОШИБКА: исключение не выброшено");
        } catch (IllegalArgumentException e) {
            System.out.println("createMatrix(0, 5): " + e.getMessage());
        }

        try {
            multiply(new double[0][0], a);
            System.out.println("ОШИБКА: исключение не выброшено");
        } catch (IllegalArgumentException e) {
            System.out.println("пустая матрица * 2x3: " + e.getMessage());
        }

        double[][] x = createMatrix(500, 700);
        double[][] y = createMatrix(700, 300);
        double[][] r1 = multiply(x, y);
        double[][] r2 = multiplyReordered(x, y);
        double[][] r3 = multiplyBlocked(x, y, 64);



        double[][] first = createMatrix(1000, 1000);
        double[][] second = createMatrix(1000, 1000);

        long start = System.nanoTime();
        double[][] result = multiply(first, second);
        long end = System.nanoTime();
        double milliseconds = (end - start) / 1_000_000.0;
        System.out.println("Time: " + milliseconds + " ms");

        long start1 = System.nanoTime();
        double[][] result1 = multiplyReordered(first, second);
        long end1 = System.nanoTime();
        double milliseconds1 = (end1 - start1) / 1_000_000.0;
        System.out.println("Time (reordered): " + milliseconds1 + " ms");

        long start2 = System.nanoTime();
        double[][] result2_1 = multiplyBlocked(first, second, 100);
        long end2 = System.nanoTime();
        double milliseconds2 = (end2 - start2) / 1_000_000.0;
        System.out.println("Time (blocked): " + milliseconds2 + " ms");

        long start2_2 = System.nanoTime();
        double[][] result2_2 = multiplyBlocked(first, second, 64);
        long end2_2 = System.nanoTime();
        double milliseconds2_2 = (end2_2 - start2_2) / 1_000_000.0;
        System.out.println("Time (blocked): " + milliseconds2_2 + " ms");

        long start2_3 = System.nanoTime();
        double[][] result2_3 = multiplyBlocked(first, second, 150);
        long end2_3 = System.nanoTime();
        double milliseconds2_3 = (end2_3 - start2_3) / 1_000_000.0;
        System.out.println("Time (blocked): " + milliseconds2_3 + " ms");

        long start2_4 = System.nanoTime();
        double[][] result2_4 = multiplyBlocked(first, second, 30);
        long end2_4 = System.nanoTime();
        double milliseconds2_4 = (end2_4 - start2_4) / 1_000_000.0;
        System.out.println("Time (blocked): " + milliseconds2_4  + " ms");

    }
}
