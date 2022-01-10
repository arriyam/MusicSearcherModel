package Tools;

import java.util.ArrayList;

public class MatMult {
// Uses scale to multiply elements in the matrix. Returns a new Matrix.
    public static double[][] mult_scalar(double[][] matrix,double scale){
        double[][] newMatrix = new double[matrix.length][];
        for (int i = 0;i < newMatrix.length;i++){
            newMatrix[i]= new double[newMatrix.length];
            for (int b = 0 ; b<newMatrix[i].length;b++){
                newMatrix[i][b] = matrix[i][b] * scale;
            }
        }
        return newMatrix;
    }
// Dot products 2 matrixs. Returns a new Matrix.
    public static double[][] mult_matrix(double[][] a,double[][] b){
        int rowSize = a[0].length;
        double[][] out = new double[1][rowSize];

            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < b[0].length; j++) {
                    for (int k = 0; k < a[0].length; k++) {
                        out[i][j] += a[i][k] * b[k][j];
                    }
                }
            }
        return out;
    }

//  Finds the euclidean distance between 2 vectors. Returns distance.
    public static double euclidean_dist(double[][] a,double[][]b){
        double count = 0;
        for (int i = 0;i<a[0].length;i++){
            count += Math.pow(a[0][i]-b[0][i],2);
        }
        return Math.pow(count,0.5);
    }

    //Function for log base 2 cause Java doesn't have one.
    public static double logBasex(double val,double base){
        double output = Math.log(val)/Math.log(base);
        return output;
    }

}
