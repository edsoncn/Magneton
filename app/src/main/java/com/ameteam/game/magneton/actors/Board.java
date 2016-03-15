package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edson on 3/03/2016.
 */
public class Board extends Character {

    private int dimension;
    private List<Piece> lstPieces;

    //Effect circle on move
    private boolean isMoving;
    private float movX, movY;

    public Board(Scene scene, int dimension) {
        super(scene);
        this.dimension = dimension;

        lstPieces = new ArrayList<>();
        isMoving = false;
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

        if(isMoving){
            paint.setColor(Color.parseColor("#88616161"));
            float movW = (float) ((getWidth() / getDimension()) * 1.1);
            float movH = (float) ((getHeight() / getDimension()) * 1.1);

            RectF rect = new RectF();
            rect.set(movX - movW / 2, movY - movH /2, movX + movW / 2, movY + movH / 2);
            canvas.drawOval(rect, paint);

            int posX= (int) ((movX - this.x)/(getWidth()/getDimension()));
            int posY= (int) ((movY - this.y)/(getHeight()/getDimension()));

            Piece squarePiece = new Piece();
            squarePiece.setPositionX(posX);
            squarePiece.setPositionY(posY);

            if(!lstPieces.contains(squarePiece)) {
                float squareX = getX() + posX * getWidth() / getDimension();
                float squareY = getY() + posY * getHeight() / getDimension();

                paint.setColor(Color.parseColor("#AAFFFFFF"));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth((getWidth() / getDimension()) * 0.04f);
                canvas.drawRect(squareX, squareY, squareX + getWidth() / getDimension(), squareY + getHeight() / getDimension(), paint);
            }
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
    }

    @Override
    public void actionOnTouchUp(float x, float y) {
        if(validateInside(x, y)){

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
        isMoving = false;
    }

    @Override
    public void actionOnTouchMove(float x, float y) {
        if(validateInside(x , y)){
            initMoving(x, y);
        }else{
            isMoving = false;
        }
    }

    public void initMoving(float x, float y){
        isMoving = true;
        this.movX = x;
        this.movY = y;
    }

}
