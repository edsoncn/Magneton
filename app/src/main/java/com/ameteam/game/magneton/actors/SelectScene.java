package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import com.ameteam.game.magneton.MagnetonGame;

/**
 * Created by edson on 5/03/2016.
 */
public class SelectScene extends Scene {

    private Button button6x6;
    private Button button8x8;

    public SelectScene(MagnetonGame magnetonGame){
        super(magnetonGame);

        button6x6 = new Button(this, "6 x 6");
        button6x6.init();

        button8x8 = new Button(this, "8 x 8");
        button8x8.init();
    }

    @Override
    public void doDraw(Canvas canvas) {
        if(canvas == null) return;

        //Scene
        Paint paintScene = new Paint();
        paintScene.setColor(Color.parseColor("#616161"));
        paintScene.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + getWidth(), y + getHeight(), paintScene);

        button6x6.doDraw(canvas);
        button8x8.doDraw(canvas);
    }

    @Override
    public void update(float secondsElapsed) {

    }

    @Override
    public void actionOnTouch(float x, float y) {
        Log.i("SelectScene", "Touch: x=" + x + ", y=" + y);
        if(button6x6.validateTouch(x, y)){
            getMagnetonGame().setDimension(6);
            getMagnetonGame().changeState(MagnetonGame.STATE_RUNNING);
        }else if(button8x8.validateTouch(x, y)){
            getMagnetonGame().setDimension(8);
            getMagnetonGame().changeState(MagnetonGame.STATE_LEVELS);
        }
    }

    @Override
    public void resize(int width, int height) {
        setDimensions(width, height);
        setPosition(0, 0);

        button6x6.setWidth(getWidth() * 0.75f);
        button6x6.setHeight(button6x6.getWidth() * 0.32f);
        button6x6.setPosition(getX() + (getWidth() - button6x6.getWidth()) / 2, getY() + getHeight() / 2 - button6x6.getHeight() * 1.25f);
        button6x6.resize();

        button8x8.setWidth(getWidth() * 0.75f);
        button8x8.setHeight(button8x8.getWidth() * 0.32f);
        button8x8.setPosition(getX() + (getWidth() - button8x8.getWidth()) / 2, getY() + getHeight() / 2 + button8x8.getHeight() * 0.25f);
        button8x8.resize();
    }

}
