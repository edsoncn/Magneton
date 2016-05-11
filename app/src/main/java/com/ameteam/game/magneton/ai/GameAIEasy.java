package com.ameteam.game.magneton.ai;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by edson on 27/03/2016.
 */
public class GameAIEasy extends GameAI {

    private static final float PORC_FOR_DEPTH = 0.75f;

    public GameAIEasy(GameRules rules) {
        super(rules);
    }

    @Override
    public GamePosition aiAlgorithm() {
        if(gameState.getRestPiecesMachine() > rules.getMaxPieces() * PORC_FOR_DEPTH) {
            Log.i("GameAIEasy", " - ramdom");
            return GameAIAlgorithms.random(gameState);
        }else{
            Log.i("GameAIEasy", " - depthFirstSearch");
            GamePosition newPosition = GameAIAlgorithms.depthFirstSearch(gameState);
            if(newPosition != null) {
                return newPosition;
            }else{
                Log.i("GameAIEasy", " - ramdom 2");
                return GameAIAlgorithms.random(gameState);
            }
        }
    }
}
