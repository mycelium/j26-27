import java.util.Random;
import java.util.Scanner;

public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){

		int rowsFirst = firstMatrix.length;
		int rowsSecond = secondMatrix.length;
		int colsFirst = firstMatrix[0].length;
		int colsSecond = secondMatrix[0].length;

		if (rowsSecond != colsFirst || rowsSecond != colsSecond) {
			System.out.println("Ошибка: массивы не удовлетворяют условию для перемножения.");
			return null;
		}

		double[][] result = new double[rowsFirst][colsSecond];

		double[][] trans_secondMatrix = new double[colsSecond][rowsSecond];
		for (int i = 0; i < rowsSecond; i++) {
			for (int j = 0; j < colsSecond; j++) {
				trans_secondMatrix[j][i] = secondMatrix[i][j];
			}
		} 

		for (int i = 0; i < rowsFirst; i++) {
			for (int j = 0; j < colsSecond; j++) {
				for (int k = 0; k < rowsSecond; k++) {
					result[i][j] += firstMatrix[i][k] * trans_secondMatrix[j][k];
				}
			}
		}
		return result;
	}

	static Random random = new Random();

    public static double[][] generateMatrix(int rows, int cols) {
        double[][] m = new double[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                m[i][j] = random.nextInt(100) + 1;
            }
        }
        return m;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите количество строк в первой матрице: ");
        int rowsA = scanner.nextInt();
        System.out.print("Введите количество столбцов в первой матрице: ");
        int colsA = scanner.nextInt();
        System.out.print("Введите количество строк во второй матрице: ");
        int rowsB = scanner.nextInt();
        System.out.print("Введите количество столбцов во второй матрице: ");
        int colsB = scanner.nextInt();

        double[][] a = generateMatrix(rowsA, colsA);
        double[][] b = generateMatrix(rowsB, colsB);

        long start = System.nanoTime();
        double[][] c = multiply(a, b);
        long end = System.nanoTime();

        System.out.println("Время выполнения: " + (end - start) / 1_000_000.0 + " ms");
    }

}

