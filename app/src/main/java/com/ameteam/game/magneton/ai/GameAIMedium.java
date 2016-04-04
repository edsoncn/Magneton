package com.ameteam.game.magneton.ai;

/**
 * Created by edson on 27/03/2016.
 */
public class GameAIMedium extends GameAI {

    public GameAIMedium(GameRules rules) {
        super(rules);
    }

    @Override
    public GamePosition aiAlgorithm() {
        return GameAIAlgorithms.random(gameState);
    }
}
