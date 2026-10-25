public class MatrixMult {

	public static double[] reverseCol(double[][] matrix, int j){  // j - номер столбца
    int n = matrix.length;
    double[] ans = new double[n];
    for(int i = 0; i < n; ++i){
      ans[i] = matrix[i][j];
    }
    return ans;
  }

  public static double getEl(double[] line, double[] col){ // предположительно, размерность одинакова
    double res = 0;
    for(int i = 0; i < line.length; ++i){
      res += line[i] * col[i];
    }
    return res;
  }

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		int n1 = firstMatrix[0].length;
		int m2 = secondMatrix.length;
		assert(n1 == m2);

		int m1 = firstMatrix.length;
		int n2 = secondMatrix[0].length;

		double[][] ans = new double[m1][];

		for(int i = 0; i < m1; ++i){
			ans[i] = new double[n2];
		}

		for(int i = 0; i < m1; ++i){
			for(int j = 0; j < n2; ++j){
				ans[i][j] = getEl(firstMatrix[i], reverseCol(secondMatrix, j));
			}
		}

		return ans;
  }

  public static void main(String[] args){

    int m1 = 1200;
    int n1 = 2000;
    int m2 = 2000;
    int n2 = 1200;

	/* Инициализация матриц */

    double[][] fir = new double[m1][n1];
    double[][] sec = new double[m2][n2];

    for(int i = 0; i < m1; ++i){
      for(int j = 0; j < n1; ++j){
        fir[i][j] = i + j + 1;
      }
    }

    for(int i = 0; i < m2; ++i){
      for(int j = 0; j < n2; ++j){
        sec[i][j] = 2*(i + j);
      }
    }


	/* Замер времени */


    long start = System.nanoTime();
    double[][] res = multiply(fir, sec);
    long end = System.nanoTime();

    System.out.println("res[0][0] = " + res[0][0]);
    System.out.printf("Время: %.1f с%n", (end - start) / 1e9);

  }

}

