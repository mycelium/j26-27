public class MatrixMult {
	
	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		if (firstMatrix[0].length != secondMatrix.length) {
			throw new IllegalArgumentException("Incorrect size of matrixes");
		}
		long startTime = System.nanoTime();
		int len_row = secondMatrix[0].length;
		int len_col = firstMatrix.length;
		int count_of_addends = secondMatrix.length;
		double[][] res_matrix = new double[len_col][len_row];
		for (int i = 0; i < len_col; i++) {
			for (int add_ind = 0; add_ind < count_of_addends; add_ind++) {
				double a = firstMatrix[i][add_ind];
				for (int k = 0; k < len_row; k++) {
					res_matrix[i][k] += a * secondMatrix[add_ind][k];
				}
			}
		}
		long endTime = System.nanoTime();
		System.out.println(endTime - startTime);
		return res_matrix;
	}
}
