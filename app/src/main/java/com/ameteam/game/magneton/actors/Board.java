package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by edson on 3/03/2016.
 */
public class Board extends Character {

    public Board(Scene scene) {
        super(scene);
    }

    @Override
    public void init() {
        super.init();
        setDimensions(scene.getWidth(), scene.getWidth());
        setPosition(scene.getX(), scene.getY() + (scene.getHeight() - height) / 2);
    }

    @Override
    public void doDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#616161"));
        canvas.drawRect(x, y, x + width, y + height, paint);

        Paint paintBlack = new Paint();
        paintBlack.setColor(Color.parseColor("#9e9e9e"));
        int total = 8;
        float lado = width / total;
        for(int i = 0; i < total; i++){
            for(int j = 0; j < total; j++){
                int pos = i * total + j;
                if((pos + (i % 2 == 0 ? 0 : 1)) % 2 == 0){
                    canvas.drawRect(x + j * lado, y + i * lado, x + (j + 1) * lado, y + (i + 1) * lado, paintBlack);
                }
            }
        }
    }
}
