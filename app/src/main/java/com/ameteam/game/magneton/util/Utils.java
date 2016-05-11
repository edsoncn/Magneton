package com.ameteam.game.magneton.util;

/**
 * Created by edson on 27/03/2016.
 */
public class Utils {

    public static String getHexStringByInt(int value, int digits){
        return String.format("%"+digits+"s", Integer.toHexString(value)).replaceAll(" ", "0");
    }

    public static int[][] clone(int dimension, int[][] m){
        int[][] matrix = new int[dimension][dimension];
        for(int i = 0; i < dimension; i ++){
            for(int j = 0; j < dimension; j++){
                matrix[i][j] = m[i][j];
            }
        }
        return matrix;
    }
}
