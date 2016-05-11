package com.ameteam.game.magneton.ai;

import android.util.Log;

import com.ameteam.game.magneton.util.Constants;

import java.util.Stack;

/**
 * Created by edson on 19/03/2016.
 */
public class GameRules {

    public static final int DIMENSION_6x6 = 6;
    public static final int DIMENSION_8x8 = 8;

    public static final int GAME_WIN_PLAYER = -1;
    public static final int GAME_WIN_MACHINE = 1;
    public static final int GAME_DRAW = 0;
    public static final int GAME_WIN_NOBODY = -2;

    public static final int TUPLE_6x6 = 3;
    public static final int TUPLE_8x8 = 4;

    public static final int MAX_PIECES_6x6 = 11;
    public static final int MAX_PIECES_8x8 = 15;

    private int dimension;
    private int tuple;
    private int maxPieces;

    public GameRules(int dimension){
        this.dimension = dimension;

        if(dimension == DIMENSION_6x6){
            this.tuple = TUPLE_6x6;
            this.maxPieces = MAX_PIECES_6x6;
        }else{
            this.tuple = TUPLE_8x8;
            this.maxPieces = MAX_PIECES_8x8;
        }
    }

    /**
     * Velidate validates if the current state meets the goal (make a tuple)
     * of winning the game or not
     *
     * @return true if you win the game or false if not
     */
    public int objetiveFunction(GameState state){
        return objetiveFunction(dimension, tuple, state.getMatrix(), state.getRestPiecesPlayer(), state.getRestPiecesMachine());
    }

    public static int objetiveFunction(int dimension, int tuple, int[][] matrix, int restPiecesPlayer, int restPiecesMachine){
        int winPlayer = 0, winMachine = 0;
        for(int i = 0; i <= dimension; i++){
            int auxj = 0, auxi = 0, auxij = 0, auxji = 0;
            int sumj = 0, sumi = 0, sumij = 0, sumji = 0;
            for(int j = 0; j < dimension; j++){

                if(i < dimension) {
                    int valj = matrix[i][j], vali = matrix[j][i];

                    if (valj != auxj) sumj = valj; else sumj += valj;
                    if (vali != auxi) sumi = vali; else sumi += vali;

                    if (Math.abs(sumj) == tuple) { if (sumj == tuple * GameState.TURN_PLAYER) winPlayer++; else winMachine++; }
                    if (Math.abs(sumi) == tuple) { if (sumi == tuple * GameState.TURN_PLAYER) winPlayer++; else winMachine++; }

                    auxj = valj;
                    auxi = vali;
                }

                int indexij = i - tuple + j, indexji = dimension - indexij - 1;
                int valij = (indexij >= 0 && indexij < dimension ) ? matrix[j][indexij] : 0 ;
                int valji = (indexji >= 0 && indexji < dimension ) ? matrix[j][indexji] : 0 ;

                if(valij != auxij) sumij = valij; else sumij += valij;
                if(valji != auxji) sumji = valji; else sumji += valji;

                if(Math.abs(sumij) == tuple){ if(sumij == tuple * GameState.TURN_PLAYER) winPlayer++; else winMachine++; }
                if(Math.abs(sumji) == tuple){ if(sumji == tuple * GameState.TURN_PLAYER) winPlayer++; else winMachine++; }

                auxij = valij;
                auxji = valji;
            }
        }
        if (winPlayer == 0 && winMachine == 0){
            if(restPiecesPlayer == 0 && restPiecesMachine == 0){
                return GAME_DRAW;
            }else{
                return GAME_WIN_NOBODY;
            }
        } else if (winPlayer > winMachine){
            return GAME_WIN_PLAYER;
        } else if (winMachine > winPlayer){
            return GAME_WIN_MACHINE;
        } else {
            return GAME_DRAW;
        }
    }

    public int heuristic(GameState state){
        return heuristic(dimension, tuple, state.getMatrix());
    }

    public static int heuristic(int dimension, int tuple, int[][] matrix){
        int suma = 0;
        for(int i = 0; i <= dimension; i++){
            for(int j = 0; j < dimension; j++){

                if(i < dimension) {
                    int valj = matrix[i][j], vali = matrix[j][i];
                    HeurSumsAux.HJ.evaluate(valj);
                    HeurSumsAux.HI.evaluate(vali);
                }

                int indexij = i - tuple + j, indexji = dimension - indexij - 1;
                int valij = (indexij >= 0 && indexij < dimension ) ? matrix[j][indexij] : 0 ;
                int valji = (indexji >= 0 && indexji < dimension ) ? matrix[j][indexji] : 0 ;

                HeurSumsAux.HIJ.evaluate(valij);
                HeurSumsAux.HJI.evaluate(valji);
            }
            //evaluate one more time for if end in a piece, and reverse for better evaluation
            HeurSumsAux.HI.finish();
            HeurSumsAux.HJ.finish();
            HeurSumsAux.HIJ.finish();
            HeurSumsAux.HJI.finish();

            suma += HeurSumsAux.HI.getSum();
            suma += HeurSumsAux.HJ.getSum();
            suma += HeurSumsAux.HIJ.getSum();
            suma += HeurSumsAux.HJI.getSum();

            HeurSumsAux.HI.reset();
            HeurSumsAux.HJ.reset();
            HeurSumsAux.HIJ.reset();
            HeurSumsAux.HJI.reset();
        }
        return suma;
    }

    public static String hashKeyForMatrix(int dimension, int[][] matrix){
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int suma = 0;
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                int val = matrix[i][j] + 1;
                suma = suma * 3 + val;
                count ++;
                if(count == 4){
                    sb.append("["+suma+"]"+Constants.CHARACTERS[suma]);
                    suma = 0;
                    count = 0;
                }
            }
        }
        return sb.toString();
    }

    private enum HeurSumsAux{

        HI,HJ,HIJ,HJI;

        private int aux;//last piece
        private int aux2;//last piece after difference found
        private float saux;
        private int sum;
        private static final int OVERFLOW_PIECE = -11;
        private static final float ADD = 0.25f;

        HeurSumsAux(){
            aux = OVERFLOW_PIECE;
            aux2 = OVERFLOW_PIECE;
            saux = 0;
            sum = 0;
        }

        public void evaluate(int val){
            sum += val;
            if (aux == val) {
                saux += val;
            } else {
                if(saux != 0){
                    saux += aux * ADD * ((aux2 == 0 ? 1 : 0) + (val == 0 ? 1 : 0));
                    sum += saux * Math.pow(10, (1 + Math.abs(saux))); //heuristic function
                }
                aux2 = aux;
                saux = 0;
            }
            aux = val;
        }

        public void finish(){
            evaluate(OVERFLOW_PIECE);//evaluate like other piece if is out of board
        }

        public int getSum(){
            return sum;
        }

        public void reset(){
            aux = OVERFLOW_PIECE;
            aux2 = OVERFLOW_PIECE;
            saux = 0;
            sum = 0;
        }
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getTuple() {
        return tuple;
    }

    public void setTuple(int tuple) {
        this.tuple = tuple;
    }

    public int getMaxPieces() {
        return maxPieces;     }

    public void setMaxPieces(int maxPieces) {
        this.maxPieces = maxPieces;
    }

    public static void main(String[] args){
        GameRules gameRules = new GameRules(GameRules.DIMENSION_8x8);
        GameState gameState = new GameState(gameRules);
        gameState.init();
        gameState.setRestPiecesMachine(8);
        gameState.setRestPiecesPlayer(8);
        gameState.setTurn(GameState.TURN_MACHINE);
        gameState.setMatrix(new int[][]{
                    /*       0  1  2  3  4  5  6  7  */
                    /* 0 */{0, 0, 0, 0, 0, 1, 1, 1},
                    /* 1 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 2 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 3 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 4 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 5 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 6 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 7 */{0, 0, 0, 0, 0, 0, 0, 0},
                }
        );
        //System.out.println(gameState);

        /*GameAI gameAI = new GameAIHard(gameRules);
        gameAI.setGameState(gameState);
        GamePosition gamePosition = gameAI.aiAlgorithm();
        gameState.putPiece(gamePosition.getPositionY(), gamePosition.getPositionX(), GameState.TURN_MACHINE);
        System.out.println(gameState);*/

        //System.out.println("Heu=" + gameRules.heuristic(gameState));

        gameState.setMatrix(new int[][]{
                    /*       0  1  2  3  4  5  6  7  */
                    /* 0 */{1, 0, 0, 0, 0, 0, -1, 1},
                    /* 1 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 2 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 3 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 4 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 5 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 6 */{0, 0, 0, 0, 0, 0, 0, 0},
                    /* 7 */{-1, 1, 0, 0, 0, 0, 0, 0},
                }
        );
        gameState.setRestPiecesMachine(14);
        gameState.setRestPiecesPlayer(13);

        System.out.println(gameState);

        GameAI gameAI = new GameAIHard(gameRules);
        gameAI.setGameState(gameState);
        GamePosition gamePosition = gameAI.aiAlgorithm();
        System.out.println(gameState);
        gameState.putPiece(gamePosition.getPositionY(), gamePosition.getPositionX(), GameState.TURN_MACHINE);

        System.out.println("px=" + gamePosition.getPositionX() + ", py=" + gamePosition.getPositionY());
        System.out.println(gameState);

        //System.out.println("Hash="+GameRules.hashKeyForMatrix(8, gameState.getMatrix()));

    }
}
