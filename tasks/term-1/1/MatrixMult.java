public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
        int n = firstMatrix.length;
        int m = firstMatrix[0].length;
        int p = secondMatrix[0].length;

        if (m != secondMatrix.length) {
            System.out.println("The matrices' sizes are imcompatible... :(");
            System.out.println("Returning default matrix [[0]]...");
            double[][] result = new double[1][1];
            result[1][1] = 0;
            return result;
        }

        double[][] result = new double[n][p];

        long startTime = System.nanoTime();
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < m; k++) {
                for (int j = 0; j < p; j++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        long endTime = System.nanoTime();

        System.out.println("Time for original multiplication: " + (endTime - startTime) + " ms");

        return result;
	}

	public static double[][] multiplyLinear(double[][] firstMatrix, double[][] secondMatrix) {
        int n = firstMatrix.length;
        int m = firstMatrix[0].length;
        int l = secondMatrix.length;
        int p = secondMatrix[0].length;

        if (m != l) { // TODO: make it a method.
            System.out.println("The matrices' sizes are imcompatible... :(");
            System.out.println("Returning default matrix [[0]]...");
            double[][] result = new double[1][1];
            result[1][1] = 0;
            return result;
        }

        double[] m1 = new double[n * m];
        double[] m2 = new double[p * l];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                m1[i * m + j] = firstMatrix[i][j];
            }
        }

        for (int i = 0; i < l; i++) {
            for (int j = 0; j < p; j++) {
                m2[i * p + j] = secondMatrix[i][j];
            }
        }

        double[][] result = new double[n][p];

        long startTime = System.nanoTime();

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < m; k++) {
                for (int j = 0; j < p; j++) {
                    result[i][j] += m1[i * m + k] * m2[k * p + j];
                }
            }
        }

        long endTime = System.nanoTime();

        System.out.println("Time for optimized multiplication: " + (endTime - startTime) + " ms");

        return result;
	}
}
