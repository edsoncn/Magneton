package com.ameteam.game.magneton.ai;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edson on 27/03/2016.
 */
public class GameAIHard extends GameAI {

    private static final float PORC_FOR_DEPTH_1 = 0.85f;
    private static final float PORC_FOR_DEPTH_2 = 0.62f;
    private static final float PORC_FOR_DEPTH_3 = 0.56f;

    public static final int MAX_DEPTH = 3;

    public GameAIHard(GameRules rules) {
        super(rules);
    }

    @Override
    public GamePosition aiAlgorithm() {

        Log.i("GameAIHard", " - minimax");
        //System.out.println("GameAIHard: - minimax");

        float depth;
        if(gameState.getRestPiecesMachine() >= 14){
            depth = 1f;
        }else if(gameState.getRestPiecesMachine() >=9){
            depth = 2.0f;
        }else{
            depth = 3.0f;
        }

        Log.i("GameAIHard", " - minimax: depth=" + depth);
        //System.out.println("GameAIHard: - minimax: depth=" + depth);

        long timeBefore = System.currentTimeMillis();
        GamePosition gamePosition = GameAIAlgorithms.minmax(gameState, depth);
        long timeAfter = System.currentTimeMillis();

        Log.i("GameAIHard", " - time: " + ((timeAfter - timeBefore) / 1000f));
        //System.out.println("GameAIHard: - time: " + ((timeAfter - timeBefore) / 1000f));

        //Log.i("GameAIHard", " - minimax: fin; x=" + gamePosition.getPositionX()+", y=" + gamePosition.getPositionY());
        return gamePosition;
    }

}
