package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.ameteam.game.magneton.MagnetonGame;

/**
 * Created by edson on 5/03/2016.
 */
public class LevelsScene extends Scene {

    private Button buttonEasy;
    private Button buttonMedium;
    private Button buttonHard;

    public LevelsScene(MagnetonGame magnetonGame){
        super(magnetonGame);

        buttonEasy = new Button(this, "Fácil");
        buttonEasy.init();

        buttonMedium = new Button(this, "Intermedio");
        buttonMedium.init();

        buttonHard = new Button(this, "Difícil");
        buttonHard.init();
    }

    @Override
    public void doDraw(Canvas canvas) {
        if(canvas == null) return;

        //Scene
        Paint paintScene = new Paint();
        paintScene.setColor(Color.parseColor("#616161"));
        paintScene.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + getWidth(), y + getHeight(), paintScene);

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

        if(buttonEasy.validateInside(x, y)){
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

        buttonEasy.setWidth(getWidth() * 0.75f);
        buttonEasy.setHeight(buttonEasy.getWidth() * 0.32f);
        buttonEasy.setPosition(getX() + (getWidth() - buttonEasy.getWidth()) / 2, getY() + getHeight() / 2 - buttonEasy.getHeight() * 1.75f);
        buttonEasy.resize();

        buttonMedium.setWidth(getWidth() * 0.75f);
        buttonMedium.setHeight(buttonMedium.getWidth() * 0.32f);
        buttonMedium.setPosition(getX() + (getWidth() - buttonMedium.getWidth()) / 2, getY() + getHeight() / 2 - buttonMedium.getHeight() * 0.5f);
        buttonMedium.resize();

        buttonHard.setWidth(getWidth() * 0.75f);
        buttonHard.setHeight(buttonHard.getWidth() * 0.32f);
        buttonHard.setPosition(getX() + (getWidth() - buttonHard.getWidth()) / 2, getY() + getHeight() / 2 + buttonHard.getHeight() * 0.75f);
        buttonHard.resize();
    }

}
