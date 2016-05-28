package com.ameteam.game.magneton.ai;

import android.os.AsyncTask;

import com.ameteam.game.magneton.actors.Board;

/**
 * Created by edson on 19/03/2016.
 */
public class GameAI{

    protected GameRules rules;
    protected GameState gameState;
    protected GamePosition nextPlay;
    protected Board board;

    public GameAI(GameRules rules){
        this.rules = rules;

        GameRules.HeurSumsAux.HI.init(rules.getTuple());
        GameRules.HeurSumsAux.HJ.init(rules.getTuple());
        GameRules.HeurSumsAux.HIJ.init(rules.getTuple());
        GameRules.HeurSumsAux.HJI.init(rules.getTuple());
    }

    public GamePosition getNextPlayMachine(){
        nextPlay = aiAlgorithm();
        return nextPlay;
    }

    public GamePosition aiAlgorithm(){
        return null;
    }

    public void start() {
        GameAITask gameAITask = new GameAITask();
        gameAITask.setGameAI(this);
        new Thread(gameAITask).start();
    }

    public GameRules getRules() {
        return rules;
    }

    public void setRules(GameRules rules) {
        this.rules = rules;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public GamePosition getNextPlay() {
        return nextPlay;
    }

    public void setNextPlay(GamePosition nextPlay) {
        this.nextPlay = nextPlay;
    }

    public class GameAITask implements Runnable {

        private GameAI gameAI;

        protected GameAI doInBackground() {
            long lastTime = System.currentTimeMillis();
            GamePosition gamePosition = gameAI.getNextPlayMachine();
            if(gamePosition != null) {
                long now = System.currentTimeMillis();
                while (now - lastTime < 400 || GameState.TURN_PLAYER == gameAI.getBoard().getGameState()) {
                    Thread.yield();
                    now = System.currentTimeMillis();
                }
                if(GameState.TURN_MACHINE == gameAI.getBoard().getGameState() ||
                        GameState.TURN_PLAYER == gameAI.getBoard().getGameState()) {
                    lastTime = System.currentTimeMillis();
                    onProgressUpdate();
                    now = System.currentTimeMillis();
                    while (now - lastTime < 350 || GameState.TURN_PLAYER == gameAI.getBoard().getGameState()) {
                        Thread.yield();
                        now = System.currentTimeMillis();
                    }
                    if (GameState.TURN_MACHINE == gameAI.getBoard().getGameState()) {
                        onPostExecute();
                    }
                }
            }
            return gameAI;
        }

        protected void onProgressUpdate() {
            GamePosition gamePosition = gameAI.getNextPlay();
            if(gamePosition != null){
                float x = gameAI.getBoard().getCenterXByPositionX(gamePosition.getPositionX());
                float y = gameAI.getBoard().getCenterYByPositionY(gamePosition.getPositionY());
                gameAI.getBoard().initMoving(x, y);
                gameAI.getBoard().initStatePlayerMoving();
            }
        }

        protected void onPostExecute() {
            if(gameAI.getNextPlay() != null){
                gameAI.getBoard().putPiece(gameAI.getNextPlay().getPositionX(), gameAI.getNextPlay().getPositionY(), gameAI.getGameState().getTurn());
            }
        }

        @Override
        public void run() {
            doInBackground();
        }

        public void setGameAI(GameAI gameAI) {
            this.gameAI = gameAI;
        }
    }
}

