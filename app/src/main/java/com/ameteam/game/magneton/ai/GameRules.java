package com.ameteam.game.magneton.ai;

/**
 * Created by edson on 19/03/2016.
 */
public class GameRules {

    public static final int DIMENSION_6x6 = 6;
    public static final int DIMENSION_8x8 = 8;

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
    public int objetiveFunction(){
        return 0;
    }

    public int heuristic(){
        return 0;
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
        return maxPieces;
    }

    public void setMaxPieces(int maxPieces) {
        this.maxPieces = maxPieces;
    }
}
