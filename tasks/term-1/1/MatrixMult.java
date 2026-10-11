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

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < m; k++) {
                for (int j = 0; j < p; j++) {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return result;
	}
}
