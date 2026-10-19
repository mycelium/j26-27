public class Main {
    public static void main(String[] args) {
        int size = 500;
        double[][] a = new double[size][size];
        double[][] b = new double[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                a[i][j] = Math.random();
                b[i][j] = Math.random();
            }
        }

        long startTime = System.currentTimeMillis();
        MatrixMult.multiply(a, b);
        long endTime = System.currentTimeMillis();

        System.out.println("time: " + (endTime - startTime) + " ms");
    }
}