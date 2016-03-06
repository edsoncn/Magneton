package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;

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
    protected float v;
    protected float a;

    public Actor(){
    }

    public void init(){
        setDimensions(0, 0);
        setPosition(0, 0);
        setV(0);
        setA(0);
    }

    public abstract void doDraw(Canvas canvas);

    public void setDimensions(float width, float height){
        this.width = width;
        this.height = height;
    }

    public void setPosition(float x, float y){
        this.x = x;
        this.y = y;
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

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
    }

    public float getA() {
        return a;
    }

    public void setA(float a) {
        this.a = a;
    }

}
