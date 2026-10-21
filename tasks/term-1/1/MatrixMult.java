import java.util.Random;

public class MatrixMult {
    static Random random = new Random();

    static int rows1 = 5;
    static int cols1 = 8;
    static int rows2 = 8;
    static int cols2 = 4;
    static int big = 1000;//для демонстрации берем квадратные большие матрицы
    

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix)
    {
        int rows1 = firstMatrix.length;
        int rows2 = secondMatrix.length;
        if(rows1 <=0 || rows2 <=0 )
        {
            throw new IllegalArgumentException();
        }
        
        int cols1 = firstMatrix[0].length;
        int cols2 = secondMatrix[0].length;
        
        if(rows2 != cols1 || cols1 <=0 || cols2 <=0 )
        {
            throw new IllegalArgumentException();
        }
        
        
        double[][] result = new double[rows1][cols2];

        for (int i = 0; i < rows1; i++)
        {
            for(int j = 0; j < cols2; j++)
            {
                for(int k = 0; k < rows2; k++)
                {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return result;
    }


    public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix)
    {
        int rows1 = firstMatrix.length;
        int rows2 = secondMatrix.length;
        if(rows1 <=0 || rows2 <=0 )
        {
            throw new IllegalArgumentException();
        }

        int cols1 = firstMatrix[0].length;
        int cols2 = secondMatrix[0].length;
        
        if(rows2 != cols1 || cols1 <=0 || cols2 <=0 )
        {
            throw new IllegalArgumentException();
        }
        
        double[][] result = new double[rows1][cols2];

        for (int i = 0; i < rows1; i++)
        {
            for(int k = 0; k < cols1; k++)
            {
                for(int j = 0; j < cols2; j++)
                {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return result;
    }



    public static double[][] randomFillMatrix(double[][] matr)
    {
        if(matr.length <= 0)
        {
            throw new IllegalArgumentException();
        }
        if(matr[0].length <= 0)
        {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < matr.length; i++)
        {
            for(int j = 0; j < matr[0].length; j++)
            {
                matr[i][j] = random.nextDouble() * 10;
            }
        }
        return matr;
    }
    

    public static void main(String[] args) 
    {

        try
        {
            double[][] firstMatrix = new double[rows1][cols1];
            double[][] secondMatrix = new double[rows2][cols2];

            double[][] firstMatrixBig = new double[big][big];
            double[][] secondMatrixBig = new double[big][big];

            firstMatrix = randomFillMatrix(firstMatrix);
            secondMatrix = randomFillMatrix(secondMatrix);

            firstMatrixBig = randomFillMatrix(firstMatrixBig);
            secondMatrixBig = randomFillMatrix(secondMatrixBig);


            double[][] result = multiply(firstMatrix,secondMatrix);

            System.out.println("Результат для малой матрицы:");

            for (int i = 0; i < result.length; i++)
            {
                for(int j = 0; j < result[0].length; j++)
                {
                    System.out.printf(" %6.2f ",result[i][j]);
                }
                System.out.println("");
            }

            System.out.println("Результат для большой матрицы:");
            
            
            long start = System.nanoTime();
            multiply(firstMatrixBig,secondMatrixBig);
            long elapsed = System.nanoTime() - start;
            
    
            System.out.printf("Матрица большая для вывода, будет дано время перемножения: %.3f сек%n", elapsed / 1e9);
            start = System.nanoTime();
            multiplyOptimized(firstMatrixBig,secondMatrixBig);
            elapsed = System.nanoTime() - start;
            System.out.printf("Время для оптимизированного алгоритма: %.3f сек%n", elapsed / 1e9);
            
            return;
        }
        catch(IllegalArgumentException e)
        {
            System.out.println("Матрицы не подходят для умножения.");
        }
        catch(OutOfMemoryError e)
        {
            System.out.println("Матрицы слишком большие, не хватает памяти.");
        }
        
    }
}