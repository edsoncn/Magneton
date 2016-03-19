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

    public static final int STATE_TURN_PLAYER = 0;
    public static final int STATE_TURN_MACHINE = 1;
    public static final int STATE_PLAYER_MOVING = 2;
    public static final int STATE_MAGNETIC_EFECT = 3;

    private int state;

    private int dimension;
    private List<Piece> lstPieces;
    private Piece lastPiece;

    //Effect circle on move
    private boolean isMoving;

    private float movX, movY;
    public final float K = 0.0004f; // Gravitational constant
    public final float FF = 0.0001f; // Frictional force

    public Board(Scene scene, int dimension) {
        super(scene);
        this.dimension = dimension;
    }

    @Override
    public void init() {
        super.init();

        state = STATE_TURN_PLAYER;
        this.setDimension(scene.getMagnetonGame().getDimension());

        lstPieces = new ArrayList<>();
        isMoving = false;
        lastPiece = null;

    }

    public void initStateTurnPlayer(){
        isMoving = false;
        state = STATE_TURN_PLAYER;
    }

    public void initStateTurnMachine(){
        state = STATE_TURN_MACHINE;
    }
    public void initStatePlayerMoving(){
        isMoving = true;
        lastPiece = null;
        state = STATE_PLAYER_MOVING;
    }
    public void initStateMagneticEfect(){
        state = STATE_MAGNETIC_EFECT;
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

        switch (state){
            case STATE_PLAYER_MOVING:
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
                break;
        }
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
        switch (state){
            case STATE_MAGNETIC_EFECT:
                updateStateMagneticEfect();
                break;
        }

    }

    private void updateStateMagneticEfect(){
        if (getLstPieces().size() > 1) {
            boolean isMovingPieces = false;
            for (Piece p : lstPieces) {
                if (!p.equals(lastPiece) && p.getM() > 0) {
                    float dx = p.getCenterX() - lastPiece.getCenterX();
                    float dy = p.getCenterY() - lastPiece.getCenterY();
                    float dx2 = (float) (Math.pow(dx, 2));
                    float dy2 = (float) (Math.pow(dy, 2));
                    float d2 = dx2 + dy2;
                    float d = (float) Math.sqrt(d2);
                    float a = ((K * (float)Math.pow(getWidth(), 3)) * p.getM()) / d2 - (FF * getWidth());
                    p.setAx(a * (dx / d));
                    p.setAy(a * (dy / d));
                    p.setVx(p.getVx() + p.getAx());
                    p.setVy(p.getVy() + p.getAy());
                    if(p.getVx() * dx > 0 || p.getVy() * dy > 0) {
                        p.setCenterX(p.getCenterX() + p.getVx());
                        p.setCenterY(p.getCenterY() + p.getVy());
                        isMovingPieces = true;
                    }else{
                        Log.i("Board", "dx="+dx+", dy="+dy+", vx="+p.getVx()+", vy="+p.getVy());
                        p.setM(0);
                    }
                }
            }
            if(!isMovingPieces){
                for (Piece p : lstPieces) {
                    p.resetMoving();
                    p.setM(1);
                }
                initStateTurnPlayer();
            }
        }else{
            initStateTurnPlayer();
        }
    }

    @Override
    public void actionOnTouch(float x, float y) {
        if(validateInside(x, y)) {
            switch (state){
                case STATE_TURN_PLAYER:
                    initMoving(x, y);
                    initStatePlayerMoving();
                    break;
            }
        }
    }

    @Override
    public void actionOnTouchUp(float x, float y) {
        if(validateInside(x, y)){
            switch (state) {
                case STATE_PLAYER_MOVING:
                    int posX = (int) ((x - this.x) / (getWidth() / getDimension()));
                    int posY = (int) ((y - this.y) / (getHeight() / getDimension()));

                    Piece piece = new Piece(scene, this);
                    piece.setPositionX(posX);
                    piece.setPositionY(posY);
                    if (!lstPieces.contains(piece)) {
                        piece.setType(lstPieces.size() % 2 == 0 ? 2 : 1);
                        piece.init();
                        piece.resize();
                        lstPieces.add(piece);
                        lastPiece = piece;
                    }
                    initStateMagneticEfect();
                    break;
            }
        }else{
            switch (state) {
                case STATE_PLAYER_MOVING:
                    initStateTurnPlayer();
                    break;
            }
        }
    }

    @Override
    public void actionOnTouchMove(float x, float y) {
        if(validateInside(x , y)){
            switch (state) {
                case STATE_PLAYER_MOVING:
                    initMoving(x, y);
                    break;
            }
        }
    }

    public void initMoving(float x, float y){
        this.movX = x;
        this.movY = y;
    }


    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public Piece getLastPiece() {
        return lastPiece;
    }

    public void setLastPiece(Piece lastPiece) {
        this.lastPiece = lastPiece;
    }

    public List<Piece> getLstPieces() {
        return lstPieces;
    }

    public void setLstPieces(List<Piece> lstPieces) {
        this.lstPieces = lstPieces;
    }

}
