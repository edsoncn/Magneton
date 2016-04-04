package com.ameteam.game.magneton.ai;

/**
 * Created by edson on 19/03/2016.
 */
public class GameState {

    public static int TURN_PLAYER = -1;
    public static int TURN_MACHINE = 1;
    public static int TURN_WAITING = 0;

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
