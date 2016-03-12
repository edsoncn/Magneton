package com.ameteam.game.magneton;

import android.graphics.Canvas;

import com.ameteam.game.magneton.actors.LevelsScene;
import com.ameteam.game.magneton.actors.PlayScene;
import com.ameteam.game.magneton.actors.SelectScene;

/**
 * Created by edson on 2/03/2016.
 */
public class MagnetonGame extends GameThread {

    public static final int STATE_LEVELS = 6;

    private PlayScene playScene;
    private SelectScene selectScene;
    private LevelsScene levelsScene;

    private int stateGame;
    private int stateToChange;
    private int dimension;

    public MagnetonGame(GameView gameView) {
        super(gameView);
    }

    private void setStateGame(int state){
        this.stateGame = state;
        this.stateToChange = state;
    }

    public synchronized void changeState(int state){
        this.stateToChange = state;
    }

    @Override
    protected void doDraw(Canvas canvas) {
        switch (stateGame){
            case STATE_READY:
                selectScene.doDraw(canvas);
                break;
            case STATE_RUNNING:
                playScene.doDraw(canvas);
                break;
            case STATE_LEVELS:
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

        setStateGame(STATE_READY);
    }

    @Override
    public void resize(int width, int height) {
        selectScene.resize(width, height);
        playScene.resize(width, height);
        levelsScene.resize(width, height);
    }

    @Override
    protected void updateGame(float secondsElapsed) {
        if(stateToChange != stateGame){

            //Do things for the new state

            setStateGame(stateToChange);
        }

        switch (stateGame){
            case STATE_READY:
                selectScene.update(secondsElapsed);
                break;
            case STATE_RUNNING:
                playScene.update(secondsElapsed);
                break;
            case STATE_LEVELS:
                levelsScene.update(secondsElapsed);
        }
    }

    @Override
    protected void actionOnTouch(float x, float y) {
        super.actionOnTouch(x, y);
        switch (stateGame){
            case STATE_READY:
                selectScene.actionOnTouch(x, y);
                break;
            case STATE_RUNNING:
                playScene.actionOnTouch(x, y);
                break;
            case STATE_LEVELS:
                levelsScene.actionOnTouch(x, y);
                break;
        }
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }
}
