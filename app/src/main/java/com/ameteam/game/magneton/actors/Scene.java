package com.ameteam.game.magneton.actors;

/**
 * Created by edson on 2/03/2016.
 */
public abstract class Scene extends Actor{

    protected Stage stage;

    public Scene(Stage stage){
        super();
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
