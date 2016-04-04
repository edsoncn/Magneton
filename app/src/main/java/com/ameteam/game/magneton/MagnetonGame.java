package com.ameteam.game.magneton;

import android.graphics.Canvas;

import com.ameteam.game.magneton.actors.LevelsScene;
import com.ameteam.game.magneton.actors.PlayScene;
import com.ameteam.game.magneton.actors.SelectScene;
import com.ameteam.game.magneton.ai.GameRules;

/**
 * Created by edson on 2/03/2016.
 */
public class MagnetonGame extends GameThread {

    public static final int STATE_GAME_SELECT = 1;
    public static final int STATE_GAME_LEVELS = 2;
    public static final int STATE_GAME_PLAY = 3;

    public static final int LEVEL_EASY = 1;
    public static final int LEVEL_MEDIUM = 2;
    public static final int LEVEL_HARD = 3;

    private PlayScene playScene;
    private SelectScene selectScene;
    private LevelsScene levelsScene;

    private int stateGame;
    private int dimension;
    private int level;

    public MagnetonGame(GameView gameView) {
        super(gameView);
        setDimension(GameRules.DIMENSION_8x8);
        setLevel(LEVEL_EASY);
    }

    private void setStateGame(int state){
        this.stateGame = state;
    }

    public synchronized void changeState(int state){
        switch (this.stateGame){
            case STATE_GAME_LEVELS:
                switch (state){
                    case STATE_GAME_PLAY:
                        playScene.getBoard().init();
                        playScene.getBoard().resize();
                        break;
                }
                break;
        }
        setStateGame(state);
    }

    @Override
    protected void doDraw(Canvas canvas) {
        switch (stateGame){
            case STATE_GAME_SELECT:
                selectScene.doDraw(canvas);
                break;
            case STATE_GAME_PLAY:
                playScene.doDraw(canvas);
                break;
            case STATE_GAME_LEVELS:
                levelsScene.doDraw(canvas);
                break;
        }
    }

    @Override
    public void setupBeginning() {
        playScene = new PlayScene(this);
        playScene.init();
        selectScene = new SelectScene(this);
        selectScene.init();
        levelsScene = new LevelsScene(this);
        levelsScene.init();

        setStateGame(STATE_GAME_SELECT);
    }

    @Override
    public void resize(int width, int height) {
        selectScene.resize(width, height);
        playScene.resize(width, height);
        levelsScene.resize(width, height);
    }

    @Override
    protected void updateGame(float secondsElapsed) {
        switch (stateGame){
            case STATE_GAME_SELECT:
                selectScene.update(secondsElapsed);
                break;
            case STATE_GAME_PLAY:
                playScene.update(secondsElapsed);
                break;
            case STATE_GAME_LEVELS:
                levelsScene.update(secondsElapsed);
        }
    }

    @Override
    protected void actionOnTouch(float x, float y) {
        super.actionOnTouch(x, y);
        switch (stateGame){
            case STATE_GAME_SELECT:
                selectScene.actionOnTouch(x, y);
                break;
            case STATE_GAME_PLAY:
                playScene.actionOnTouch(x, y);
                break;
            case STATE_GAME_LEVELS:
                levelsScene.actionOnTouch(x, y);
                break;
        }
    }

    @Override
    protected void actionOnTouchUp(float x, float y) {
        super.actionOnTouchUp(x, y);
        switch (stateGame){
            case STATE_GAME_SELECT:
                selectScene.actionOnTouchUp(x, y);
                break;
            case STATE_GAME_PLAY:
                playScene.actionOnTouchUp(x, y);
                break;
            case STATE_GAME_LEVELS:
                levelsScene.actionOnTouchUp(x, y);
                break;
        }
    }

    @Override
    protected void actionOnTouchMove(float x, float y) {
        super.actionOnTouchMove(x, y);
        switch (stateGame){
            case STATE_GAME_SELECT:
                selectScene.actionOnTouchMove(x, y);
                break;
            case STATE_GAME_PLAY:
                playScene.actionOnTouchMove(x, y);
                break;
            case STATE_GAME_LEVELS:
                levelsScene.actionOnTouchMove(x, y);
                break;
        }
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
