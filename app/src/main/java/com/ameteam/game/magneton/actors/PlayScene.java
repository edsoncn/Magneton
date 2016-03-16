package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ameteam.game.magneton.MagnetonGame;

/**
 * Created by edson on 5/03/2016.
 */
public class PlayScene extends Scene {

    private Board board;

    public PlayScene(MagnetonGame magnetonGame){
        super(magnetonGame);
        board = new Board(this, MagnetonGame.DIMENSION_8x8);
    }

    @Override
    public void init() {
        super.init();

        board.init();
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

    @Override
    public void actionOnTouch(float x, float y) {
        board.actionOnTouch(x, y);
    }

    @Override
    public void actionOnTouchUp(float x, float y) {
        board.actionOnTouchUp(x, y);
    }

    @Override
    public void actionOnTouchMove(float x, float y) {
        board.actionOnTouchMove(x, y);
    }

    @Override
    public void resize(int width, int height) {
        float p = 0.96f; //proportion
        float r = 16.0f / 10.0f; //ratio

        setDimensions(width * p, width * p * r);
        setPosition((width - this.width) / 2, (height - this.height) / 2);

        board.resize();
    }

    public Board getBoard() {
        return board;
    }
}
