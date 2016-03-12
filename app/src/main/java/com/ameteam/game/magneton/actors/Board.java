package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edson on 3/03/2016.
 */
public class Board extends Character {

    private int dimension;
    private List<Piece> lstPieces;

    public Board(Scene scene, int dimension) {
        super(scene);
        this.dimension = dimension;

        lstPieces = new ArrayList<>();

        Piece piece = new Piece(scene,this);
        piece.setType(1);
        piece.setPositionX(1);
        piece.setPositionY(3);
        lstPieces.add(piece);

        /*Piece piece2 = new Piece(scene,this);
        piece2.setType(2);
        piece2.setPositionX(3);
        piece2.setPositionY(4);
        lstPieces.add(piece2);*/
    }

    @Override
    public void init() {
        super.init();

        this.setDimension(scene.getMagnetonGame().getDimension());
        
        for(Piece p: lstPieces){
            p.init();
        }
    }

    @Override
    public void doDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#616161"));
        canvas.drawRect(x, y, x + width, y + height, paint);

        Paint paintBlack = new Paint();
        paintBlack.setColor(Color.parseColor("#9e9e9e"));
        int total = getDimension();
        float lado = width / total;
        for(int i = 0; i < total; i++){
            for(int j = 0; j < total; j++){
                int pos = i * total + j;
                if((pos + (i % 2 == 0 ? 0 : 1)) % 2 == 0){
                    canvas.drawRect(x + j * lado, y + i * lado, x + (j + 1) * lado, y + (i + 1) * lado, paintBlack);
                }
            }
        }
        for(Piece p: lstPieces){
            p.doDraw(canvas);
        }
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public void resize() {
        setDimensions(scene.getWidth(), scene.getWidth());
        setPosition(scene.getX(), scene.getY() + (scene.getHeight() - height) / 2);

        for(Piece p: lstPieces){
            p.resize();
        }
    }

    @Override
    public void update(float secondsElapsed) {
    }

    @Override
    public void actionOnTouch(float x, float y) {
        if(y>this.y && y<this.y+getHeight()){
            int posX= (int) ((x-this.x)/(getWidth()/getDimension()));
            int posY= (int) ((y-this.y)/(getHeight()/getDimension()));

            Piece piece = new Piece(scene,this);
            piece.setPositionX(posX);
            piece.setPositionY(posY);
            if(!lstPieces.contains(piece)) {
                piece.setType(lstPieces.size() % 2 == 0 ? 2 : 1);
                piece.init();
                piece.resize();
                lstPieces.add(piece);
            }
        }
    }

}
