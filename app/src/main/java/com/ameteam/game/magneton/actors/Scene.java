package com.ameteam.game.magneton.actors;

import android.content.res.Resources;

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

    public Resources getResources(){
        return magnetonGame.getmGameView().getResources();
    }

    public abstract void resize(int width, int height);

    public abstract boolean onBackPressed();

    public MagnetonGame getMagnetonGame() {
        return magnetonGame;
    }

    public void setMagnetonGame(MagnetonGame magnetonGame) {
        this.magnetonGame = magnetonGame;
    }
}
