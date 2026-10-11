public class Main {
    public static void main(String[] args) {
        int size = 100;

        double[][] m1 = new double[size][size];
        double[][] m2 = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                m1[i][j] = i + j;
                m2[i][j] = i - j;
            }
        }

        double[][] result = new double[size][size];

        long startTime = System.nanoTime();
        result = MatrixMult(m1, m2);
        long endTime = System.nanoTime();

        System.out.println("Time for original method: " + (end - start) + " ms");
    }
}
