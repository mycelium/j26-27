public class Main {
    public static void main(String[] args) {
        int size = 1000;

        double[][] m1 = new double[size][size];
        double[][] m2 = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                m1[i][j] = i + j;
                m2[i][j] = i - j;
            }
        }

        double[][] result = new double[size][size];

        long startTime0 = System.nanoTime();
        result = MatrixMult.multiply(m1, m2);
        long endTime0 = System.nanoTime();
        System.out.println((endTime0 - startTime0) + " - time for original method in ns");

        long startTime1 = System.nanoTime();
        result = MatrixMult.multiplyO(m1, m2);
        long endTime1 = System.nanoTime();
        System.out.println((endTime1 - startTime1) + " - time for optimized method in ns");

        long startTime2 = System.nanoTime();
        result = MatrixMult.multiplyLinear(m1, m2);
        long endTime2 = System.nanoTime();
        System.out.println((endTime2 - startTime2) + " - time for linear method in ns");

        long startTime3 = System.nanoTime();
        result = MatrixMult.multiplyCached(m1, m2);
        long endTime3 = System.nanoTime();
        System.out.println((endTime3 - startTime3) + " - time for cached method in ns");
    }
}
