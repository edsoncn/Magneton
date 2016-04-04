package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.ameteam.game.magneton.util.Constants;
import com.ameteam.game.magneton.util.Utils;

/**
 * Created by MARISSA on 08/03/2016.
 */
public class Piece extends Character{

    public static final int STATE_WAIT = 0;
    public static final int STATE_MAGNETIC_EFECT = 1;
    public static final int COUNT_EFECT_MAX = 8;

    private int type;
    private int positionX;
    private int positionY;
    private Board board;
    private float m;

    public static int RED = 1;
    public static int BLUE = 2;

    private int stopPosX; // use for the magnetic effect
    private int stopPosY; // use for the magnetic effect

    private int state;

    public int countEfect;
    public int incEfect;

    public Piece(Scene scene, Board board) {
        super(scene);
        this.board = board;
    }

    public Piece(int positionX, int positionY, Scene scene, Board board) {
        super(scene);
        this.positionX = positionX;
        this.positionY = positionY;
        this.board = board;
    }

    public Piece(){
        super(null);
    }

    @Override
    public void init() {
        super.init();
        this.m = 0;
        this.stopPosX = -1;
        this.stopPosY = -1;
        state = STATE_WAIT;
    }

    @Override
    public void resetMoving() {
        super.resetMoving();
        this.m = 0;
        this.stopPosX = -1;
        this.stopPosY = -1;
    }

    @Override
    public void doDraw(Canvas canvas) {
        Paint paint = new Paint();
        RectF rect = new RectF();

        if(state == STATE_MAGNETIC_EFECT){
            if(0 <= countEfect && countEfect <= COUNT_EFECT_MAX){
                float factor = (float)countEfect / COUNT_EFECT_MAX;
                float extra = 0.16f;

                int transparent = (int)(Constants.RGB_EFFECT_MAX / 2
                        + Constants.RGB_EFFECT_MAX * (incEfect < 0 ? factor : 1 - factor));
                paint.setColor(Color.parseColor("#" + Utils.getHexStringByInt(transparent, 2) + "000000"));

                float we = getWidth() + getWidth() * factor * extra;
                float he = getHeight() + getHeight() * factor * extra;
                rect.set(getCenterX() - we / 2, getCenterY() - he / 2, getCenterX() + we / 2, getCenterY() + he / 2);
                canvas.drawOval(rect, paint);

                countEfect+=incEfect;
            }else{
                incEfect*=-1;
                countEfect+=incEfect;
            }
        }

        if(this.getType() == RED){
            paint.setColor(Color.parseColor("#ff5252"));
        }else if(this.getType() == BLUE){
            paint.setColor(Color.parseColor("#448aff"));
        }
        rect.set(x + getWidth() * 0.1f, y + getHeight() * 0.1f, x + getWidth() * 0.9f, y + getHeight() * 0.9f);
        canvas.drawOval(rect, paint);
    }

    public void initStateWait(){
        state = STATE_WAIT;
    }

    public void initStateMagneticEfect(){
        countEfect = 0;
        incEfect = 1;
        state = STATE_MAGNETIC_EFECT;
    }

    @Override
    public void resize() {
        setDimensions(board.getWidth() / board.getDimension(), board.getHeight() / board.getDimension());
        setPosition(this.board.getX() + getPositionX() * getWidth(), this.board.getY() + getPositionY() * getHeight());
    }

    @Override
    public void update(float secondsElapsed) {

    }

    @Override
    public void actionOnTouch(float x, float y) {

    }

    @Override
    public void actionOnTouchUp(float x, float y) {

    }

    @Override
    public void actionOnTouchMove(float x, float y) {

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

    public float getM() {
        return m;
    }

    public void setM(float m) {
        this.m = m;
    }

    public int getStopPosX() {
        return stopPosX;
    }

    public void setStopPosX(int stopPosX) {
        this.stopPosX = stopPosX;
    }

    public int getStopPosY() {
        return stopPosY;
    }

    public void setStopPosY(int stopPosY) {
        this.stopPosY = stopPosY;
    }

}
