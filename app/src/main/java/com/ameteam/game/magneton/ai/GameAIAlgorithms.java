package com.ameteam.game.magneton.ai;

import java.util.ArrayList;

/**
 * Created by edson on 2/04/2016.
 */
public class GameAIAlgorithms {

    public static GamePosition random(GameState gameState){
        ArrayList<GamePosition> lstFreePositions = new ArrayList<>();
        for(int i = 0; i < gameState.getMatrix().length; i++){
            for(int j = 0; j < gameState.getMatrix()[0].length; j++){
                if(gameState.getMatrix()[i][j] == GameState.SQUARE_NOTHING) {
                    lstFreePositions.add(new GamePosition(i, j));
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

}
