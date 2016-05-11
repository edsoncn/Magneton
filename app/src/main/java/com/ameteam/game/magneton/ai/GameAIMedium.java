package com.ameteam.game.magneton.ai;

import android.util.Log;

/**
 * Created by edson on 27/03/2016.
 */
public class GameAIMedium extends GameAI {

    public GameAIMedium(GameRules rules) {
        super(rules);
    }

    @Override
    public GamePosition aiAlgorithm() {
        Log.i("GameAIEasy", " - bestBetter");
        return GameAIAlgorithms.bestBetter(gameState);
    }
}
