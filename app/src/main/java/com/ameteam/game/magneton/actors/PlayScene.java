package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by edson on 5/03/2016.
 */
public class PlayScene extends Scene {

    private Board board;

    public PlayScene(Stage stage){
        super(stage);
        board = new Board(this);
    }

    @Override
    public void init() {
        super.init();

        float p = 0.96f; //proporcion
        float r = 16.0f / 10.0f; //ratio

        setDimensions(getStage().getWidth() * p, getStage().getWidth() * p * r);
        setPosition((getStage().getWidth() - width) / 2, (getStage().getHeight() - height) / 2);

        board.init();
    }

    @Override
    public void doDraw(Canvas canvas) {
        if(canvas == null) return;

        //Background
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#000000"));
        canvas.drawRect(0, 0, stage.getWidth(), stage.getHeight(), paint);

        //Scene
        Paint paintScene = new Paint();
        paintScene.setColor(Color.parseColor("#FFBB00"));
        paintScene.setStyle(Paint.Style.STROKE);
        canvas.drawRect(x, y, x + getWidth(), y + getHeight(), paintScene);

        //Board
        board.doDraw(canvas);
    }


}
