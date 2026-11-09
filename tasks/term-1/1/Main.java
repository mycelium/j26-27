public class Main {
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