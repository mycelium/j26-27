public class MatrixMult3 {

  public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
    int n1 = firstMatrix[0].length;
    int m2 = secondMatrix.length;
    assert(n1 == m2);

    int m1 = firstMatrix.length;
    int n2 = secondMatrix[0].length;

    double[][] ans = new double[m1][n2];

    for (int i = 0; i < m1; ++i) {
        double[] ai = firstMatrix[i];
        double[] ci = ans[i];
        for (int k = 0; k < n1; ++k) {
            double aik = ai[k];
            double[] bk = secondMatrix[k];
            for (int j = 0; j < n2; ++j) {
                ci[j] += aik * bk[j];
            }
        }
    }

    return ans;

  }


  public static void main(String[] args){

    int m1 = 2000;
    int n1 = 4000;
    int m2 = 4000;
    int n2 = 2000;

    double[][] fir = new double[m1][n1];
    double[][] sec = new double[m2][n2];

    /* Инициализация матриц */

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

