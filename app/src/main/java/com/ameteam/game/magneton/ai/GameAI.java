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
    }

    public GamePosition getNextPlayMachine(){
        nextPlay = aiAlgorithm();
        return nextPlay;
    }

    public GamePosition aiAlgorithm(){
        return null;
    }

    public void start() {
        new GameAITask().execute(this);
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

    public class GameAITask extends AsyncTask<GameAI, GameAI, GameAI> {

        @Override
        protected GameAI doInBackground(GameAI... params) {
            GameAI gameAI = params[0];
            long lastTime = System.currentTimeMillis();
            GamePosition gamePosition = gameAI.getNextPlayMachine();
            if(gamePosition != null) {
                long now = System.currentTimeMillis();
                while (now - lastTime < 400) {
                    Thread.yield();
                    now = System.currentTimeMillis();
                }
                lastTime = System.currentTimeMillis();
                publishProgress(gameAI);
                now = System.currentTimeMillis();
                while (now - lastTime < 350) {
                    Thread.yield();
                    now = System.currentTimeMillis();
                }
            }
            return gameAI;
        }

        @Override
        protected void onProgressUpdate(GameAI... progress) {
            GameAI gameAI = progress[0];
            GamePosition gamePosition = gameAI.getNextPlay();
            if(gamePosition != null){
                float x = gameAI.getBoard().getCenterXByPositionX(gamePosition.getPositionX());
                float y = gameAI.getBoard().getCenterYByPositionY(gamePosition.getPositionY());
                gameAI.getBoard().initMoving(x, y);
                gameAI.getBoard().initStatePlayerMoving();
            }
        }

        @Override
        protected void onPostExecute(GameAI gameAI) {
            if(gameAI.getNextPlay() != null){
                gameAI.getBoard().putPiece(gameAI.getNextPlay().getPositionX(), gameAI.getNextPlay().getPositionY(), gameAI.getGameState().getTurn());
            }
        }
    }
}

