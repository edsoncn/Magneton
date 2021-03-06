package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.RectF;

/**
 * Created by edson on 2/03/2016.
 */
public abstract class Actor {

    /* dimension */
    protected float width;
    protected float height;

    /* position */
    protected float x;
    protected float y;

    /* velocity and acceleration */
    protected float vx,vy;
    protected float ax,ay;

    public Actor(){
    }

    public void init(){
        setDimensions(0, 0);
        setPosition(0, 0);
        resetMoving();
    }

    public void resetMoving(){
        setVx(0);
        setVy(0);
        setAx(0);
        setAy(0);
    }

    public abstract void doDraw(Canvas canvas);

    public abstract void update(float secondsElapsed);

    public abstract void actionOnTouch(float x, float y);

    public abstract void actionOnTouchUp(float x, float y);

    public abstract void actionOnTouchMove(float x, float y);

    public void setDimensions(float width, float height){
        this.width = width;
        this.height = height;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
    }

    public boolean validateInside(float x ,float y){
        return y>this.y && y<this.y+getHeight()
                && x>this.x && x<this.x+getWidth();
    }

    public RectF getRectF(){
        RectF rect = new RectF();
        rect.set(x, y, x + width, y + height);
        return rect;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getVx() {
        return vx;
    }

    public void setVx(float vx) {
        this.vx = vx;
    }

    public float getVy() {
        return vy;
    }

    public void setVy(float vy) {
        this.vy = vy;
    }

    public float getAx() {
        return ax;
    }

    public void setAx(float ax) {
        this.ax = ax;
    }

    public float getAy() {
        return ay;
    }

    public void setAy(float ay) {
        this.ay = ay;
    }

    public float getCenterX(){
        return getX() + getWidth() / 2;
    }

    public float getCenterY(){
        return getY() + getHeight() / 2;
    }

    public void setCenterX(float centerX){
        setX(centerX - getWidth() / 2);
    }

    public void setCenterY(float centerY){
        setY(centerY - getHeight() /2);
    }

    public void setCenter(float centerX, float centerY){
        setCenterX(centerX);
        setCenterY(centerY);
    }

}
