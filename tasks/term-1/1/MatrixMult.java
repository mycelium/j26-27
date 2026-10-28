public class MatrixMult {
	
	public static void main(String[] args) {


        double[][] first_matrix = {
                {1, 2},
                {3, 4}
        };

        double[][] second_matrix = {
                {5, 6},
                {7, 8}
        };

        System.out.println("Matrix A:");
        printMatrix(first_matrix);
        System.out.println("Matrix B:");
        printMatrix(second_matrix);
        System.out.println("Result:");
        printMatrix(MatrixMult.multiply(first_matrix, second_matrix));

    }
	
	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		
		
		int length_a = firstMatrix.length;
		int length_b = secondMatrix.length;
		int width_a = firstMatrix[0].length;
		int width_b = secondMatrix[0].length;
		
		if (width_a != length_b){
			System.out.println("Error: wrong sizes of matrix"); 
			throw new IllegalArgumentException();
		}
		
		
		long start = System.currentTimeMillis();
		
		double[][] result = new double[length_a][width_b];

		for (int i = 0; i < length_a; i++) {
		    for (int k = 0; k < width_a; k++) {
		        for (int j = 0; j < width_b; j++) {
		            result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
		        }
		    }
		}
		
		long finish = System.currentTimeMillis();
		
		System.out.println("Time required - " + (finish - start) + "ms");
		
		return result;
	}

    public static void printMatrix(double[][] matrix) {

        System.out.println("----------------");

        for (double[] row : matrix) {

            for (double value : row) {
                System.out.print(value + " ");
            }

            System.out.println();
        }
    }
	
}
