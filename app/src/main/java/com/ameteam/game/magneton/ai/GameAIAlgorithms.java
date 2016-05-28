package com.ameteam.game.magneton.ai;

import android.util.Log;

import com.ameteam.game.magneton.util.Constants;
import com.ameteam.game.magneton.util.Utils;

import java.util.ArrayList;

/**
 * Created by edson on 2/04/2016.
 */
public class GameAIAlgorithms {

    public static GamePosition random(GameState gameState){
        int dimension = gameState.getRules().getDimension();
        ArrayList<GamePosition> lstFreePositions = new ArrayList<>();
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                if(gameState.getMatrix()[i][j] == GameState.SQUARE_NOTHING) {
                    lstFreePositions.add(new GamePosition(j, i));
                }
            }
        }
        if(lstFreePositions.size() > 0) {
            int indexRandom = (int) (lstFreePositions.size() * Math.random());
            return lstFreePositions.get(indexRandom);
        }else{
            return null;
        }
    }

    public static GamePosition depthFirstSearch(GameState gameState){
        int dimension = gameState.getRules().getDimension();
        int objectiveFunction = gameState.getRules().objetiveFunction(gameState);
        int ramdomI = (int)(Math.random() * Constants.PRIME_NUMBER);
        int ramdomJ = (int)(Math.random() * Constants.PRIME_NUMBER);
        if(objectiveFunction == GameRules.GAME_WIN_NOBODY){
            for(int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    int newI = (i + ramdomI) % dimension;
                    int newJ = (j + ramdomJ) % dimension;
                    if(gameState.getMatrix()[newI][newJ] == GameState.SQUARE_NOTHING) {

                        GamePosition gamePosition = new GamePosition(newJ, newI);
                        GameState gameStateClone = gameState.clone();
                        gameStateClone.putPiece(newI, newJ, gameState.getTurn());

                        GamePosition lastMove = depthFirstSearch(gameStateClone);

                        if(lastMove != null){
                            return gamePosition;
                        }
                    }
                }
            }
            return null;
        }else{
            if(objectiveFunction == GameRules.GAME_WIN_MACHINE){
                return new GamePosition(-1, -1);
            }else{
                return null;
            }
        }
    }

    public static GamePosition bestBetter(GameState gameState){
        int dimension = gameState.getRules().getDimension();
        GamePosition gamePosition = null;
        int heuristic = -Constants.INFINITE;
        float randomMin = 1 / (((float)Math.pow(gameState.getRules().getDimension(), 2) / 2) - (gameState.getRules().getMaxPieces() - gameState.getRestPiecesMachine()));
        for(int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (gameState.getMatrix()[i][j] == GameState.SQUARE_NOTHING) {
                    GameState gameStateClone = gameState.clone();
                    gameStateClone.putPiece(i, j, gameState.getTurn());
                    Integer heuristicAux = gameState.getRules().heuristic(gameStateClone);
                    if(heuristicAux == null){
                        heuristic = 0;
                    }
                    if(Math.random() <= randomMin ? heuristic <= heuristicAux : heuristic < heuristicAux){
                        heuristic = heuristicAux;
                        gamePosition = new GamePosition(j, i);
                    }
                }
            }
        }
        Log.i("GameAIAlgorithms", "heuristic="+heuristic);
        return gamePosition;
    }

    public static GamePosition
    minmax(GameState gameState, float depth) {
        GameRules gameRules = gameState.getRules();
        return minmax(depth, gameRules.getDimension(), gameRules.getTuple(), gameState.getMatrix(),
                gameState.getTurn(), gameRules.getMaxPieces(), gameState.getRestPiecesPlayer(), gameState.getRestPiecesMachine());
    }

    public static GamePosition minmax(float depth, int dimension, int tuple, int[][] matrix, int turn, int maxPieces, int restPiecesPlayer, int restPiecesMachine){
        GamePosition gamePosition = null;
        int heuristicMinMax = -1 * Constants.INFINITE * turn;
        float randomMin = 1 / (((float)Math.pow(dimension, 2) / 2) - (maxPieces - (turn == GameState.TURN_PLAYER ? restPiecesPlayer : restPiecesMachine)));
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (matrix[i][j] == GameState.SQUARE_NOTHING) {
                    int[][] matrixClone = Utils.clone(dimension, matrix);
                    GameState.putPiece(dimension, matrixClone, i, j, turn);
                    int heuristicAux = minmaxRecursive(depth - 1, dimension, tuple, matrixClone, turn * -1, maxPieces,
                            restPiecesPlayer - (turn == GameState.TURN_PLAYER ? 1 : 0),
                            restPiecesMachine - (turn == GameState.TURN_MACHINE ? 1 : 0));
                    // if machine state find max heuritic else the min
                    if (turn == GameState.TURN_MACHINE ?
                            (Math.random() <= randomMin ? heuristicMinMax < heuristicAux : heuristicMinMax <= heuristicAux) :
                            (Math.random() <= randomMin ? heuristicMinMax > heuristicAux : heuristicMinMax >= heuristicAux)) {
                        heuristicMinMax = heuristicAux;
                        gamePosition = new GamePosition(j, i);
                        if(heuristicMinMax >= Constants.INFINITE){
                            return gamePosition;
                        }
                    }
                }
            }
        }
        return gamePosition;
    }

    public static int minmaxRecursive(float depth, int dimension, int tuple, int[][] matrix, int turn, int maxPieces, int restPiecesPlayer, int restPiecesMachine){
        Integer heuristic = GameRules.heuristic(dimension, tuple, matrix);
        if(heuristic != null && Math.abs(heuristic) != Constants.INFINITE){
            if(depth > 0) {
                int heuristicMinMax = -1 * Constants.INFINITE * turn;
                float randomMin = 1 / (((float)Math.pow(dimension, 2) / 2) - (maxPieces - (turn == GameState.TURN_PLAYER ? restPiecesPlayer : restPiecesMachine)));
                for (int i = 0; i < dimension; i++) {
                    for (int j = 0; j < dimension; j++) {
                        if (matrix[i][j] == GameState.SQUARE_NOTHING) {
                            int[][] matrixClone = Utils.clone(dimension, matrix);
                            GameState.putPiece(dimension, matrixClone, i, j, turn);
                            float newDepth = depth == 0.5f ? (Math.random() < 0.000075f ? 1 : 0) : depth - 1;
                            int heuristicAux = minmaxRecursive(newDepth, dimension, tuple, matrixClone, turn * -1, maxPieces,
                                    restPiecesPlayer - (turn == GameState.TURN_PLAYER ? 1 : 0),
                                    restPiecesMachine - (turn == GameState.TURN_MACHINE ? 1 : 0));
                            // if machine state find max heuritic else the min
                            if (turn == GameState.TURN_MACHINE ?
                                    (Math.random() <= randomMin ? heuristicMinMax < heuristicAux : heuristicMinMax <= heuristicAux) :
                                    (Math.random() <= randomMin ? heuristicMinMax > heuristicAux : heuristicMinMax >= heuristicAux)) {
                                heuristicMinMax = heuristicAux;
                            }
                        }
                    }
                }
                return heuristicMinMax;
            }else{
                return heuristic;
            }
        }else if(heuristic == null){
            return 0;
        }else{
            return heuristic;
        }
    }

}
