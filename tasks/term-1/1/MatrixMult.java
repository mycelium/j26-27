import java.util.Scanner;
import java.util.Random;
import java.util.InputMismatchException;

public class MatrixMult {
    static Scanner scanner = new Scanner(System.in);
    static Random random = new Random();

    public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix)
    {
        int rows1 = firstMatrix.length;
        int cols1 = firstMatrix[0].length;
        int rows2 = secondMatrix.length;
        int cols2 = secondMatrix[0].length;

        if(rows2 != cols1)//все остальные "плохие случаи" исключаются в функции ввода.
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
        int cols1 = firstMatrix[0].length;
        int rows2 = secondMatrix.length;
        int cols2 = secondMatrix[0].length;

        if(rows2 != cols1)//все остальные "плохие случаи" исключаются в функции ввода.
        {
            throw new IllegalArgumentException();
        }
        
        double[][] result = new double[rows1][cols2];

        for (int i = 0; i < rows1; i++)
        {
            for(int k = 0; k < rows2; k++)
            {
                for(int j = 0; j < cols2; j++)
                {
                    result[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }

        return result;
    }

    public static int intInput(String text)
    {
        while(true){
            try
            {
                System.out.println(text);
                int res = scanner.nextInt();
                if(res <= 0) {
                    System.out.println("Введите число больше 0");
                    continue;
                }
                return res;
            }
            catch(InputMismatchException e)
            {
                System.out.println("Неверный тип: введите целое число до 2 147 483 647");
                scanner.nextLine();
            }
        }
    }

    public static double[][] randomFillMatrix(double[][] matr)
    {
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
        while (true)//для повторного ввода при ошибке
        {
            int rows1 = intInput("Введите количество строк в матрице 1:");
            int cols1 = intInput("Введите количество столбцов в матрице 1:");
            int rows2 = intInput("Введите количество строк в матрице 2:");
            int cols2 = intInput("Введите количество столбцов в матрице 2:");

            try
            {
                double[][] firstMatrix = new double[rows1][cols1];
                double[][] secondMatrix = new double[rows2][cols2];

                firstMatrix = randomFillMatrix(firstMatrix);
                secondMatrix = randomFillMatrix(secondMatrix);

                double[][] result = new double[0][0];
            
                long start = System.nanoTime();
                result = multiply(firstMatrix,secondMatrix);
                long elapsed = System.nanoTime() - start;
                
                
                if(rows1 * cols1 >= 1000000 || rows2 * cols2 >= 1000000 || rows1 * cols2 >= 100000) 
                {
                    System.out.printf("Матрица большая для вывода, будет дано время перемножения: %.3f сек%n", elapsed / 1e9);
                    start = System.nanoTime();
                    result = multiplyOptimized(firstMatrix,secondMatrix);
                    elapsed = System.nanoTime() - start;
                    System.out.printf("Время для оптимизированного алгоритма: %.3f сек%n", elapsed / 1e9);
                }

                
                else{
                    for (int i = 0; i < result.length; i++)
                    {
                        for(int j = 0; j < result[0].length; j++)
                        {
                            System.out.printf(" %6.2f ",result[i][j]);
                        }
                        System.out.println("");
                    }
                }
                return;
            }
            catch(IllegalArgumentException e)
            {
                System.out.println("Матрицы не подходят для умножения.");
                System.out.println("Введите снова:");
            }
            catch(OutOfMemoryError e)
            {
                System.out.println("Матрицы слишком большие, не хватает памяти.");
                System.out.println("Введите снова:");
            }
        }
    }
}