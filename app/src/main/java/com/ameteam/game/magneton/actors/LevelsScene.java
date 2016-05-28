package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.ameteam.game.magneton.MagnetonGame;
import com.ameteam.game.magneton.R;

/**
 * Created by edson on 5/03/2016.
 */
public class LevelsScene extends Scene {

    private Button button2Players;
    private Button buttonEasy;
    private Button buttonMedium;
    private Button buttonHard;

    public LevelsScene(MagnetonGame magnetonGame){
        super(magnetonGame);

        button2Players = new Button(this, getResources().getString(R.string.lvl_scn_2_players));
        button2Players.init();

        buttonEasy = new Button(this, getResources().getString(R.string.lvl_scn_easy));
        buttonEasy.init();

        buttonMedium = new Button(this, getResources().getString(R.string.lvl_scn_medium));
        buttonMedium.init();

        buttonHard = new Button(this, getResources().getString(R.string.lvl_scn_hard));
        buttonHard.init();
    }

    @Override
    public void doDraw(Canvas canvas) {
        if(canvas == null) return;

        //Scene
        Paint paintScene = new Paint();
        paintScene.setColor(getResources().getColor(R.color.lvl_scn_background));
        paintScene.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + getWidth(), y + getHeight(), paintScene);

        button2Players.doDraw(canvas);
        buttonEasy.doDraw(canvas);
        buttonMedium.doDraw(canvas);
        buttonHard.doDraw(canvas);
    }

    @Override
    public void update(float secondsElapsed) {

    }

    @Override
    public void actionOnTouch(float x, float y) {
        Log.i("LevelsScene", "Touch: x=" + x + ", y=" + y);

        if(button2Players.validateInside(x, y)){
            getMagnetonGame().setLevel(MagnetonGame.LEVEL_2_PLAYERS);
            getMagnetonGame().changeState(MagnetonGame.STATE_GAME_PLAY);
        }else if(buttonEasy.validateInside(x, y)){
            getMagnetonGame().setLevel(MagnetonGame.LEVEL_EASY);
            getMagnetonGame().changeState(MagnetonGame.STATE_GAME_PLAY);
        }else if(buttonMedium.validateInside(x, y)){
            getMagnetonGame().setLevel(MagnetonGame.LEVEL_MEDIUM);
            getMagnetonGame().changeState(MagnetonGame.STATE_GAME_PLAY);
        }else if(buttonHard.validateInside(x, y)){
            getMagnetonGame().setLevel(MagnetonGame.LEVEL_HARD);
            getMagnetonGame().changeState(MagnetonGame.STATE_GAME_PLAY);
        }
    }

    @Override
    public void actionOnTouchUp(float x, float y) {

    }

    @Override
    public void actionOnTouchMove(float x, float y) {

    }

    @Override
    public void resize(int width, int height) {
        setDimensions(width, height);
        setPosition(0, 0);

        float pW = 0.72f;
        float pH = 0.25f;

        button2Players.setWidth(getWidth() * pW);
        button2Players.setHeight(button2Players.getWidth() * pH);
        button2Players.setPosition(getX() + (getWidth() - button2Players.getWidth()) / 2, getY() + getHeight() / 2 - button2Players.getHeight() * 2.75f);
        button2Players.resize();

        buttonEasy.setWidth(getWidth() * pW);
        buttonEasy.setHeight(buttonEasy.getWidth() * pH);
        buttonEasy.setPosition(getX() + (getWidth() - buttonEasy.getWidth()) / 2, getY() + getHeight() / 2 - buttonEasy.getHeight() * 1.25f);
        buttonEasy.resize();

        buttonMedium.setWidth(getWidth() * pW);
        buttonMedium.setHeight(buttonMedium.getWidth() * pH);
        buttonMedium.setPosition(getX() + (getWidth() - buttonMedium.getWidth()) / 2, getY() + getHeight() / 2 + buttonMedium.getHeight() * 0.25f);
        buttonMedium.resize();

        buttonHard.setWidth(getWidth() * pW);
        buttonHard.setHeight(buttonHard.getWidth() * pH);
        buttonHard.setPosition(getX() + (getWidth() - buttonHard.getWidth()) / 2, getY() + getHeight() / 2 + buttonHard.getHeight() * 1.75f);
        buttonHard.resize();
    }

    @Override
    public boolean onBackPressed() {
        getMagnetonGame().setStateGame(MagnetonGame.STATE_GAME_SELECT);
        return false;
    }

}
