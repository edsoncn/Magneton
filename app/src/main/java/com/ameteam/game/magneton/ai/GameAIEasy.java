package com.ameteam.game.magneton.ai;

import java.util.ArrayList;

/**
 * Created by edson on 27/03/2016.
 */
public class GameAIEasy extends GameAI {

    public GameAIEasy(GameRules rules) {
        super(rules);
    }

    @Override
    public GamePosition aiAlgorithm() {
        return GameAIAlgorithms.random(gameState);
    }
}
