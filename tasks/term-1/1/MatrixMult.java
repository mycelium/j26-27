import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class MatrixMult {

    /**Default block size for tiling (tuned for typical L1/L2 cache). */
    private static final int BLOCK_SIZE = 64;

    /**
     *Multiplies two matrices. Returns a new matrix of size
     *firstMatrix.length x secondMatrix[0].length.
     *
     *Preconditions:
     *- both matrices are non-null and rectangular
     *- firstMatrix[0].length == secondMatrix.length
     */
    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix) {
        if (firstMatrix == null || secondMatrix == null) {
            throw new IllegalArgumentException("Matrices must not be null");
        }
        int rowsA = firstMatrix.length;
        if (rowsA == 0) {
            return new double[0][0];
        }
        int colsA = firstMatrix[0].length;
        int rowsB = secondMatrix.length;
        if (rowsB == 0) {
            return new double[rowsA][0];
        }
        int colsB = secondMatrix[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException(
                    "Incompatible dimensions: " + colsA + " != " + rowsB);
        }

        //Choose strategy by size
        if (rowsA * colsB < 10_000) {
            //Small matrices - simple sequential version is enough
            return multiplySimple(firstMatrix, secondMatrix);
        }
        else{
            return multiplyBlocked(firstMatrix, secondMatrix);
        }
    }

    // -------------------------------------------------------------------------
    //1. Simple triple-loop multiplication
    // -------------------------------------------------------------------------
    public static double[][] multiplySimple(double[][] A, double[][] B) {
        int m = A.length;
        int n = B[0].length;
        int p = B.length;
        double[][] C = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                for (int k = 0; k < p; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }
        return C;
    }

    // -------------------------------------------------------------------------
    //2. Optimized: transpose B first -> sequential access, then blocked loops
    // -------------------------------------------------------------------------
    public static double[][] multiplyBlocked(double[][] A, double[][] B) {
        int m = A.length;
        int n = B[0].length;
        int p = B.length;
        double[][] C = new double[m][n];

        //Transpose B for better spatial locality (B is accessed by columns)
        double[][] Bt = transpose(B);

        for (int i0 = 0; i0 < m; i0 += BLOCK_SIZE) {
            int iMax = Math.min(i0 + BLOCK_SIZE, m);
            for (int j0 = 0; j0 < n; j0 += BLOCK_SIZE) {
                int jMax = Math.min(j0 + BLOCK_SIZE, n);
                for (int k0 = 0; k0 < p; k0 += BLOCK_SIZE) {
                    int kMax = Math.min(k0 + BLOCK_SIZE, p);

                    //Micro-kernel over the block
                    for (int i = i0; i < iMax; i++) {
                        double[] Ai = A[i];
                        double[] Ci = C[i];
                        for (int j = j0; j < jMax; j++) {
                            double sum = Ci[j];
                            double[] Btj = Bt[j];
                            for (int k = k0; k < kMax; k++) {
                                sum += Ai[k] * Btj[k];
                            }
                            Ci[j] = sum;
                        }
                    }
                }
            }
        }
        return C;
    }

    // -------------------------------------------------------------------------
    //Helpers
    // -------------------------------------------------------------------------
    private static double[][] transpose(double[][] M) {
        int rows = M.length;
        int cols = M[0].length;
        double[][] T = new double[cols][rows];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                T[j][i] = M[i][j];
            }
        }
        return T;
    }

    // -------------------------------------------------------------------------
    //Benchmark & demo
    // -------------------------------------------------------------------------
    public static void main(String[] args) {
        //Warm-up JVM
        System.out.println("Warming up JVM...");
        double[][] warmA = randomMatrix(200, 200);
        double[][] warmB = randomMatrix(200, 200);
        for (int i = 0; i < 5; i++) {
            multiply(warmA, warmB);
        }

        int[] sizes = {256, 512, 1024, 1536};

        System.out.println();
        System.out.printf("%-8s | %-14s | %-14s | %-10s%n",
                "Size", "Simple (ms)", "Blocked (ms)", "Speedup");
        System.out.println("---------+----------------+----------------+----------------");

        for (int n : sizes) {
            double[][] A = randomMatrix(n, n);
            double[][] B = randomMatrix(n, n);

            // Simple
            long t0 = System.nanoTime();
            double[][] C1 = multiplySimple(A, B);
            long t1 = System.nanoTime();
            double simpleMs = (t1 - t0) / 1_000_000.0;

            // Blocked
            t0 = System.nanoTime();
            double[][] C2 = multiplyBlocked(A, B);
            t1 = System.nanoTime();
            double blockedMs = (t1 - t0) / 1_000_000.0;


            //Sanity check (only for smaller sizes to keep runtime reasonable)
            if (n <= 512) {
                checkEqual(C1, C2, 1e-9);
            }
            System.out.printf("%-8d | %-14.2f | %-14.2f | %.2fx%n", 
                    n, simpleMs, blockedMs, simpleMs / blockedMs);

        }

        System.out.println();
        System.out.println("Notes:");
        System.out.println("  - Simple   : classic i-j-k triple loop");
        System.out.println("  - Blocked  : B transposed + loop tiling (block=" + BLOCK_SIZE + ")");
    }

    private static double[][] randomMatrix(int rows, int cols) {
        double[][] M = new double[rows][cols];
        java.util.Random rnd = new java.util.Random(42);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                M[i][j] = rnd.nextDouble();
            }
        }
        return M;
    }

    private static void checkEqual(double[][] X, double[][] Y, double eps) {
        if (X.length != Y.length || X[0].length != Y[0].length) {
            throw new AssertionError("Dimension mismatch");
        }
        for (int i = 0; i < X.length; i++) {
            for (int j = 0; j < X[0].length; j++) {
                if (Math.abs(X[i][j] - Y[i][j]) > eps) {
                    throw new AssertionError(
                            String.format("Mismatch at [%d][%d]: %f vs %f",
                                    i, j, X[i][j], Y[i][j]));
                }
            }
        }
    }
}
