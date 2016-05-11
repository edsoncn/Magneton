package com.ameteam.game.magneton.ai;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by edson on 27/03/2016.
 */
public class GameAIHard extends GameAI {

    private static final float PORC_FOR_DEPTH_1 = 0.85f;
    private static final float PORC_FOR_DEPTH_2 = 0.56f;

    private ObjHeuMaps objHeuMaps;
    public static final int MAX_DEPTH = 3;

    public GameAIHard(GameRules rules) {
        super(rules);
        objHeuMaps = new ObjHeuMaps(MAX_DEPTH);
    }

    @Override
    public GamePosition aiAlgorithm() {

        Log.i("GameAIHard", " - minimax");
        //System.out.println("GameAIHard: - minimax");

        int depth;
        if(gameState.getRestPiecesMachine() > rules.getMaxPieces() * PORC_FOR_DEPTH_1){
            depth = 1;
        }else if(gameState.getRestPiecesMachine() > rules.getMaxPieces() * PORC_FOR_DEPTH_2){
            depth = 2;
        }else{
            depth = 3;
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

    public class ObjHeuMaps{

        public Map<String, int[][]>[] maps;

        public ObjHeuMaps(int depth){
            maps = new Map[depth];
            for(int i = 0; i < depth; i++){
                maps[i] = new HashMap<>();
            }
        }

    }
}
