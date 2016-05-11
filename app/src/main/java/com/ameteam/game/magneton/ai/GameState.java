package com.ameteam.game.magneton.ai;

import com.ameteam.game.magneton.util.Constants;
import com.ameteam.game.magneton.util.Utils;

/**
 * Created by edson on 19/03/2016.
 */
public class GameState {

    public static int TURN_PLAYER = -1;
    public static int TURN_MACHINE = 1;
    public static int TURN_WAITING = 0;

    public static final int WIN_PLAYER = 2;
    public static final int WIN_MACHINE = 3;
    public static final int DRAW = 4;

    public static int SQUARE_PLAYER = TURN_PLAYER;
    public static int SQUARE_NOTHING = TURN_WAITING;
    public static int SQUARE_MACHINE = TURN_MACHINE;

    private int matrix[][]; //state matrix of square: -1 = player piece, 0 = without piece, 1 = machine piece
    private int turn; //turn: -1 = player, 1 = machine
    private int restPiecesPlayer;
    private int restPiecesMachine;

    private GameRules rules;

    public GameState(GameRules rules){
        this.rules = rules;
    }

    public void init(){
        int dimension = rules.getDimension();
        matrix = new int[dimension][dimension];
        for(int i = 0; i < dimension; i ++){
            for(int j = 0; j < dimension; j++){
                matrix[i][j] = SQUARE_NOTHING;
            }
        }
        turn = TURN_PLAYER;
        restPiecesPlayer = rules.getMaxPieces();
        restPiecesMachine = rules.getMaxPieces();
    }

    public void putPiece(int pi, int pj, int turn){
        int dimension = rules.getDimension();
        putPiece(dimension, matrix, pi, pj, turn);
        setTurn(turn * -1);
        if(turn == GameState.TURN_PLAYER) {
            setRestPiecesPlayer(getRestPiecesPlayer() - 1);
        }else{
            setRestPiecesMachine(getRestPiecesMachine() - 1);
        }
    }

    public static void putPiece(int dimension, int[][] matrix, int pi, int pj, int turn){
        matrix[pi][pj] = turn;
        for(int k = 0; k < Constants.DIRS_X.length; k++) {
            int di = Constants.DIRS_Y[k];
            int dj = Constants.DIRS_X[k];
            int ni = pi + di;
            int nj = pj + dj;
            boolean found = false;
            while(0 <= ni && ni < dimension && 0 <= nj && nj < dimension){
                if(matrix[ni][nj] != GameState.SQUARE_NOTHING){
                    found = true;
                    break;
                }
                ni += di;
                nj += dj;
            }
            if(found){
                if(turn != matrix[ni][nj]){
                    matrix[ni][nj] = SQUARE_NOTHING;
                    matrix[pi + di][pj + dj] = turn * -1;
                }else{
                    matrix[ni][nj] = SQUARE_NOTHING;
                    ni += di;
                    nj += dj;
                    while(0 <= ni && ni < dimension && 0 <= nj && nj < dimension){
                        if(matrix[ni][nj] != GameState.SQUARE_NOTHING){
                            break;
                        }
                        ni += di;
                        nj += dj;
                    }
                    matrix[ni - di][nj - dj] = turn;
                }
            }
        }
    }

    public GameState clone(){
        int dimension = rules.getDimension();
        GameState gameState = new GameState(getRules());
        gameState.setMatrix(Utils.clone(dimension, getMatrix()));
        gameState.setTurn(getTurn());
        gameState.setRestPiecesPlayer(getRestPiecesPlayer());
        gameState.setRestPiecesMachine(getRestPiecesMachine());
        return gameState;
    }

    @Override
    public String toString(){
        String text = "";
        text+="STATE:\r\n";
        text+=" - Turn=" + turn + ", restPiecesPlayer=" + restPiecesPlayer + ", restPiecesMachine=" + restPiecesMachine + "\r\n";
        text+="    ";
        for(int j = 0; j < rules.getDimension(); j++) {
            text+="_" + j + " ";
        }
        text+="\r\n";
        for(int i = 0; i < rules.getDimension(); i ++){
            text+=i + ": |";
            for(int j = 0; j < rules.getDimension(); j++){
                String piece = matrix[i][j] == 0 ? "__" : (matrix[i][j] < 0 ? "_O" : "_X");
                if(j == 0) {
                    text+=piece;
                }else{
                    text+="|" + piece;
                }
            }
            text+="|\r\n";
        }
        return text;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getRestPiecesPlayer() {
        return restPiecesPlayer;
    }

    public void setRestPiecesPlayer(int restPiecesPlayer) {
        this.restPiecesPlayer = restPiecesPlayer;
    }

    public int getRestPiecesMachine() {
        return restPiecesMachine;
    }

    public void setRestPiecesMachine(int restPiecesMachine) {
        this.restPiecesMachine = restPiecesMachine;
    }

    public GameRules getRules() {
        return rules;
    }

    public void setRules(GameRules rules) {
        this.rules = rules;
    }
}
