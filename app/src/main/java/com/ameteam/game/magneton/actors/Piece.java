package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by MARISSA on 08/03/2016.
 */
public class Piece extends Character{

    private int type;
    private int positionX;
    private int positionY;
    private Board board;

    public static int RED = 1;
    public static int BLUE = 2;

    public Piece(Scene scene, Board board) {
        super(scene);
        this.board = board;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void doDraw(Canvas canvas) {
        Paint paint = new Paint();
        RectF rect = new RectF();
        if(this.getType() == RED){
            paint.setColor(Color.parseColor("#ff5252"));
        }else if(this.getType() == BLUE){
            paint.setColor(Color.parseColor("#448aff"));
        }
        rect.set((float) (x + getWidth() * 0.1), (float) (y + getHeight() * 0.1), x + getWidth(), y + getHeight());
        canvas.drawOval(rect, paint);
    }

    @Override
    public void resize() {
        setDimensions((float) (board.getWidth() / board.getDimension() * 0.9), (float) (board.getHeight() / board.getDimension() * 0.9));
        setPosition(this.board.getX()+getPositionX()*board.getWidth()/board.getDimension(),this.board.getY()+getPositionY()*board.getHeight()/board.getDimension());
    }

    @Override
    public void update(float secondsElapsed) {

    }

    @Override
    public void actionOnTouch(float x, float y) {

    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Piece){
            Piece p = (Piece)o;
            return this.getPositionX() == p.getPositionX()
                    && this.getPositionY() == p.getPositionY();
        }
        return false;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

}
