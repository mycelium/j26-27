public class MatrixMult{
    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
    int rowsA = firstMatrix.length;
    int columnsA = firstMatrix[0].length;

    int rowsB = secondMatrix.length;
    int columnsB = secondMatrix[0].length;

    if (columnsA != rowsB){
        throw new IllegalArgumentException("Матрицы нельзя перемножить");
    }
    
    double[][] result = new double[rowsA][columnsB];
    for (int i = 0; i < rowsA; i++){
        for (int k = 0; k < columnsA; k++){
            for (int j = 0; j < columnsB; j++){
                result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
            }
        }
    }
    return result;
    }


    public static double[][] createMatrix(int rows, int columns){
        double[][] result = new double[rows][columns];
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++){
                result[i][j] = Math.random();
            }
        }

        return result;
    }

    public static void main(String[] args){
        // double[][] matrixA = {
        //     {1, 2},
        //     {3, 4}
        // };

        // double[][] matrixB = {
        //     {4, 3, 2},
        //     {2, 3, 5}
        // };

        double[][]matrixA = createMatrix(2000, 2000);
        double[][] matrixB = createMatrix(2000, 2000);

        long startTime = System.nanoTime();
        double[][] result = multiply(matrixA, matrixB);
        long endTime = System.nanoTime();
        double elapsMs = (endTime - startTime) / 1000000.0;
        System.out.println("Время работы метода " + elapsMs + " миллисекунд");
    }
}