public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
        int n = firstMatrix.length;
        int m = secondMatrix[0].length;
        int k = secondMatrix.length;
        
        if(firstMatrix[0].length != k){
            throw new IllegalArgumentException("размеры матриц несовместимы");
        }
        
        double[][] result = new double[n][m];
        for(int i = 0; i<n; i++){
            for(int j = 0; j<m;j++){
                for(int t = 0; t<k;t++){
                    result[i][j] += firstMatrix[i][t]*secondMatrix[t][j];
                }
            }
        }
        return result;
	}
    public static double[][] multiplyOptimized(double[][] firstMatrix, double[][] secondMatrix){
        int n = firstMatrix.length;
        int m = secondMatrix[0].length;
        int k = secondMatrix.length;
        
        double[][] result = new double[n][m];
        for(int i = 0; i<n;i++){
            for(int t =0; t<k;t++){
                double firstMatrixElemment = firstMatrix[i][t];
                for(int j = 0; j<m;j++){
                    result[i][j] += firstMatrixElemment * secondMatrix[t][j];
                    
                }
            }
        }
        return result;
        
    }
    
    public static void main(String[] args){
        int n = 500;
        
        double[][] firstMatrix = new double[n][n];
        double[][] secondMatrix = new double[n][n];
        
        for(int i =0; i<n;i++){
            for(int j =0; j<n; j++){
                firstMatrix[i][j] = (i+j)/100.0;
                secondMatrix[i][j] = (i-j)/100.0;
            }
        }
        
        System.out.println("Размер матриц: " + n + "x" + n);
        
        int runs = 100;
        long basicSum = 0;
        long optimizedSum = 0;
        long basicMin = Long.MAX_VALUE;
        long optimizedMin = Long.MAX_VALUE;
        
        System.out.println("Прогонов на каждый метод: " + runs);
        for(int i = 0; i<runs; i++){
            long start = System.nanoTime();
            double[][] basic = multiply(firstMatrix,secondMatrix);
            long basicTime = System.nanoTime()-start;
        
            start = System.nanoTime();
            double[][] optimized = multiplyOptimized(firstMatrix, secondMatrix);
            long optimizedTime = System.nanoTime()-start;
            
            basicSum += basicTime;
            optimizedSum += optimizedTime;
            
            if (basicTime < basicMin) basicMin = basicTime;
            if(optimizedTime<optimizedMin) optimizedMin = optimizedTime;
        }
        
        double basicAvg = (double) basicSum / runs;
        double optimizedAvg = (double) optimizedSum / runs;
        
        System.out.printf("Обычный:          min %8.1f ms | avg %8.1f ms%n",
                basicMin / 1_000_000.0, basicAvg / 1_000_000.0);
        System.out.printf("Оптимизированный: min %8.1f ms | avg %8.1f ms%n",
                optimizedMin / 1_000_000.0, optimizedAvg / 1_000_000.0);
        System.out.printf("Ускорение (по min): %.2fx%n", (double) basicMin / optimizedMin);
        System.out.printf("Ускорение (по avg): %.2fx%n", (double) basicAvg / optimizedAvg);
        
        
    }
    
    
}
