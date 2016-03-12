package com.ameteam.game.magneton.actors;

/**
 * Created by edson on 5/03/2016.
 */
public abstract class Character extends Actor{

    protected Scene scene;

    public Character(Scene scene) {
        super();
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public abstract void resize();

}
