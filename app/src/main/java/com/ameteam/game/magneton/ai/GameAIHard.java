package com.ameteam.game.magneton.ai;

/**
 * Created by edson on 27/03/2016.
 */
public class GameAIHard extends GameAI {

    public GameAIHard(GameRules rules) {
        super(rules);
    }

    @Override
    public GamePosition aiAlgorithm() {
        return GameAIAlgorithms.random(gameState);
    }
}
