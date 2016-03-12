package com.ameteam.game.magneton.actors;

import com.ameteam.game.magneton.MagnetonGame;

/**
 * Created by edson on 2/03/2016.
 */
public abstract class Scene extends Actor{

    protected MagnetonGame magnetonGame;

    public Scene(MagnetonGame magnetonGame){
        super();
        this.magnetonGame = magnetonGame;
    }

    public abstract void resize(int width, int height);

    public MagnetonGame getMagnetonGame() {
        return magnetonGame;
    }

    public void setMagnetonGame(MagnetonGame magnetonGame) {
        this.magnetonGame = magnetonGame;
    }
}
