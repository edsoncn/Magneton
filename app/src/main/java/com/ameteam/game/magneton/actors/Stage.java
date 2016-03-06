package com.ameteam.game.magneton.actors;

/**
 * Created by edson on 3/03/2016.
 */
public class Stage {

    private float width;
    private float height;

    public Stage(float width, float height){
        this.width = width;
        this.height = height;
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
}
