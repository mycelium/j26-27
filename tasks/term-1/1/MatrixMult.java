import java.util.Random;

public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
        if (firstMatrix == null || secondMatrix == null || firstMatrix.length == 0 || secondMatrix.length == 0) return null;
        int M = firstMatrix.length; // кол-во строк firstMatrix
        int K = firstMatrix[0].length; // кол-во столбцов firstMatrix (и строк secondMatrix)
        int N = secondMatrix[0].length; // кол-во стоблцов secondMatrix

        if (K != secondMatrix.length){
            throw new IllegalArgumentException("Столбцы firstMatrix (" + K + ") должны быть равны строкам secondMatrix (" + secondMatrix.length + ")");
        }
        double[][] thirdMatrix = new double[M][N];
        final int BLOCK_SIZE = 64; // оптимальный размер блока для процессорного кэша double-элементов

        for (int si = 0; si < M; si += BLOCK_SIZE){ // внешние циклы делят матрицы на блоки
            int iMax = Math.min(si + BLOCK_SIZE, M);
            for (int sk = 0; sk < K; sk += BLOCK_SIZE){
                int kMax = Math.min(sk + BLOCK_SIZE, K);
                for (int sj = 0; sj < N; sj += BLOCK_SIZE){
                    int jMax = Math.min(sj + BLOCK_SIZE, N);

                    for (int i = si; i < iMax; i++){
                        for (int k = sk; k < kMax; k++){
                            for (int j = sj; j < jMax; j++){
                                thirdMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                            }
                        }
                    }

                }
            }
        }
        return thirdMatrix;
        //return new double[1][1];
	}

    public static void main(String[] args){
        for (int n : new int[]{100, 300, 500, 1000}){
            double[][] fst = randomMatrix(n);
            double[][] snd = randomMatrix(n);
            long start = System.nanoTime(); // начало замера
            double[][] trd = multiply(fst, snd);
            long elapsed = System.nanoTime() - start; // конец
            System.out.printf("n=%-5d время = %8.3f сек", n, elapsed / 1e9);
        }
    }

    static double[][] randomMatrix(int n){
        double[][] m = new double[n][n];
        Random r = new Random(1);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                m[i][j] = r.nextDouble();
        return m;
    }
}
