package com.ameteam.game.magneton;

import android.graphics.Canvas;

import com.ameteam.game.magneton.actors.PlayScene;
import com.ameteam.game.magneton.actors.Scene;
import com.ameteam.game.magneton.actors.Stage;

/**
 * Created by edson on 2/03/2016.
 */
public class MagnetonGame extends GameThread {

    private Scene scene;

    public MagnetonGame(GameView gameView) {
        super(gameView);
    }

    @Override
    protected void doDraw(Canvas canvas) {
        if(scene != null){
            scene.doDraw(canvas);
        }
    }

    @Override
    public void setupBeginning() {
        Stage stage = new Stage(mCanvasWidth, mCanvasHeight);
        scene = new PlayScene(stage);
        scene.init();
    }

    @Override
    protected void updateGame(float secondsElapsed) {

    }
}
