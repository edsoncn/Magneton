package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.ameteam.game.magneton.MagnetonGame;
import com.ameteam.game.magneton.ai.GameAI;
import com.ameteam.game.magneton.ai.GameAIEasy;
import com.ameteam.game.magneton.ai.GameAIHard;
import com.ameteam.game.magneton.ai.GameAIMedium;
import com.ameteam.game.magneton.ai.GameRules;
import com.ameteam.game.magneton.ai.GameState;

/**
 * Created by edson on 5/03/2016.
 */
public class PlayScene extends Scene {

    private Board board;
    private GameRules gameRules;
    private GameAI gameAI;

    private int turn; //GameState: TURN_PLAYER, TURN_MACHINE, TURN_WAITING

    public PlayScene(MagnetonGame magnetonGame){
        super(magnetonGame);
        board = new Board(this, GameRules.DIMENSION_8x8);
    }

    @Override
    public void init() {
        super.init();

        board.init();
        turn = GameState.TURN_PLAYER;
        gameRules = new GameRules(magnetonGame.getDimension());
        gameAI = null;
    }

    @Override
    public void doDraw(Canvas canvas) {
        if(canvas == null) return;

        //Background
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paint);

        //Scene
        Paint paintScene = new Paint();
        paintScene.setColor(Color.parseColor("#FFBB00"));
        paintScene.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x, y, x + getWidth(), y + getHeight(), paintScene);

        //Board
        board.doDraw(canvas);
    }

    @Override
    public void update(float secondsElapsed) {
        board.update(secondsElapsed);
    }

    public void changeTurn(){
        if(turn == GameState.TURN_PLAYER){
            turn = GameState.TURN_MACHINE;
        }else{
            turn = GameState.TURN_PLAYER;
        }
        if(turn == GameState.TURN_MACHINE){
            Log.i("PlayScene", "Turn: " + turn);
            gamaAIAction();
        }
    }

    private void gamaAIAction(){
        switch (magnetonGame.getLevel()){
            case MagnetonGame.LEVEL_EASY:
                gameAI = new GameAIEasy(gameRules);
                break;
            case MagnetonGame.LEVEL_MEDIUM:
                gameAI = new GameAIMedium(gameRules);
                break;
            case MagnetonGame.LEVEL_HARD:
                gameAI = new GameAIHard(gameRules);
                break;
            default:
                gameAI = new GameAIEasy(gameRules);
                break;
        }
        GameState gameState = new GameState(gameAI.getRules());
        gameState.setMatrix(board.getMatrix());
        gameState.setTurn(turn);
        gameAI.setGameState(gameState);
        gameAI.setBoard(board);
        gameAI.start();
    }

    @Override
    public void actionOnTouch(float x, float y) {
        if(turn == GameState.TURN_PLAYER) {
            if (board.validateInside(x, y)) {
                switch (board.getState()) {
                    case Board.STATE_WAITING:
                        board.initMoving(x, y);
                        board.initStatePlayerMoving();
                        break;
                }
            }
        }
    }

    @Override
    public void actionOnTouchUp(float x, float y) {
        if(turn == GameState.TURN_PLAYER) {
            if (board.validateInside(x, y)) {
                switch (board.getState()) {
                    case Board.STATE_PLAYER_MOVING:
                        int posX = board.getPositionXByX(x);
                        int posY = board.getPositionYByY(y);

                        if (!board.validateIfContainsPiece(posX, posY)) {
                            board.putPiece(posX, posY, turn);
                        } else {
                            board.initStateWaiting();
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void actionOnTouchMove(float x, float y) {
        if(turn == GameState.TURN_PLAYER) {
            switch (board.getState()) {
                case Board.STATE_PLAYER_MOVING:
                    board.initMoving(x, y);
                    break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        float p = 0.96f; //proportion
        float r = 1.25f; //16.0f / 10.0f; //ratio

        setDimensions(width * p, width * p * r);
        setPosition((width - this.width) / 2, (height - this.height) / 2);

        board.resize();
    }

    public Board getBoard() {
        return board;
    }
}
