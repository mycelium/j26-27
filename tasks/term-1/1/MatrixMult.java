import java.util.Random;

public class MatrixMult {

	public static double[][] multiply(double[][] firstMatrix, double[][] secondMatrix){
		int lengthFirstMatrixSTR = firstMatrix.length;
        int lengthFirstMatrixRow = firstMatrix[0].length;
        int lengthSecondMatrixRow = secondMatrix[0].length;

		double[][] resultMatrix = new double [lengthFirstMatrixSTR][lengthSecondMatrixRow];

		
		for (int i = 0; i < lengthFirstMatrixSTR  ; i++){
			for (int j = 0; j < lengthSecondMatrixRow; j++){
				for (int k = 0; k < lengthFirstMatrixRow ; k++){
					resultMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
		}
		
		return resultMatrix;

}



	public static double[][] multiply_with_optimization(double[][] firstMatrix, double[][] secondMatrix){

		int lengthFirstMatrixSTR = firstMatrix.length;
        int lengthFirstMatrixRow = firstMatrix[0].length;
        int lengthSecondMatrixRow = secondMatrix[0].length;
		


		double[][] resultMatrix = new double [lengthFirstMatrixSTR][lengthSecondMatrixRow];


		

		for (int i = 0; i < lengthFirstMatrixSTR; i++){
			for (int k = 0; k < lengthFirstMatrixRow ; k++){
				for (int j = 0; j < lengthSecondMatrixRow ; j++){
					resultMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
				}
			}
			
		}
		
		return resultMatrix;



	}


	public static void main(String[] args){

		int ColFirstMatrix = 1200;
		int RowFirstColSecondMatrix = 450;
		int RowSecondMatrix = 800;

		Random newRandomaser = new Random();

		double matrixA[][] = new double[ColFirstMatrix][RowFirstColSecondMatrix];
		double matrixB[][] = new double[RowFirstColSecondMatrix][RowSecondMatrix];


		for (int i = 0; i < ColFirstMatrix ; i++){
			for (int j = 0; j < RowFirstColSecondMatrix; j++){
				matrixA[i][j] = newRandomaser.nextDouble();
		}
	}



		for (int i = 0; i < RowFirstColSecondMatrix; i++){
			for (int j = 0; j < RowSecondMatrix; j++){
				matrixB[i][j] = newRandomaser.nextDouble();
		}
	}
long start = System.nanoTime();
multiply(matrixA, matrixB);
long end = System.nanoTime();
System.out.println("Обычное: " + (end - start));

start = System.nanoTime();
multiply_with_optimization(matrixA, matrixB);
end = System.nanoTime();
System.out.println("Оптимизированное: " + (end - start));
}



}

