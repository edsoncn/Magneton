package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.Log;

import com.ameteam.game.magneton.MagnetonGame;
import com.ameteam.game.magneton.R;
import com.ameteam.game.magneton.ai.GameState;
import com.ameteam.game.magneton.util.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edson on 3/03/2016.
 */
public class Board extends Character {

    public static final int STATE_WAITING = 0;
    public static final int STATE_PLAYER_MOVING = 1;
    public static final int STATE_MAGNETIC_EFECT = 2;

    private int state;

    private int dimension;
    private List<Piece> lstPieces;
    private Piece lastPiece;

    //Effect circle on move
    private float movX, movY;
    public final float K = 0.000775f; // Gravitational constant
    public final float FF = 0.000175f; // Frictional force

    public static float ST_MAG_TIME_EFECT = 1.25f;
    private float sumSecElapsed;
    private boolean endStateMagnetic;

    public Board(Scene scene, int dimension) {
        super(scene);
        this.dimension = dimension;
    }

    @Override
    public void init() {
        super.init();

        state = STATE_WAITING;
        this.setDimension(scene.getMagnetonGame().getDimension());

        lstPieces = new ArrayList<>();
        lastPiece = null;

        sumSecElapsed = 0f;
        endStateMagnetic = false;

    }

    public void initStateWaiting(){
        for (Piece p : lstPieces) {
            p.initStateWait();
        }
        scene.getMagnetonGame().setState(MagnetonGame.STATE_READY);
        state = STATE_WAITING;
    }

    public void initStatePlayerMoving(){
        lastPiece = null;

        scene.getMagnetonGame().setState(MagnetonGame.STATE_READY);
        state = STATE_PLAYER_MOVING;
    }
    public void initStateMagneticEfect(){
        lastPiece.initStateMagneticEfect();
        sumSecElapsed = 0f;
        endStateMagnetic = false;
        scene.getMagnetonGame().setState(MagnetonGame.STATE_RUNNING);
        state = STATE_MAGNETIC_EFECT;
    }

    @Override
    public void doDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(getScene().getResources().getColor(R.color.board_square_black));
        canvas.drawRect(x, y, x + width, y + height, paint);

        Paint paintBlack = new Paint();
        paintBlack.setColor(getScene().getResources().getColor(R.color.board_square_white));
        int total = getDimension();
        float lado = width / total;
        for (int i = 0; i < total; i++) {
            for (int j = 0; j < total; j++) {
                int pos = i * total + j;
                if ((pos + (i % 2 == 0 ? 0 : 1)) % 2 == 0) {
                    canvas.drawRect(x + j * lado, y + i * lado, x + (j + 1) * lado, y + (i + 1) * lado, paintBlack);
                }
            }
        }

        synchronized (lstPieces) {
            for (Piece p : lstPieces) {
                p.doDraw(canvas);
            }
        }

        switch (state) {
            case STATE_PLAYER_MOVING:
                int posX = getPositionXByX(movX);
                int posY = getPositionYByY(movY);

                if(0 <= posX && posX < dimension && 0 <= posY && posY < dimension) {
                    Piece squarePiece = new Piece();
                    squarePiece.setPositionX(posX);
                    squarePiece.setPositionY(posY);

                    if (!lstPieces.contains(squarePiece)) {
                        float squareX = getX() + posX * getWidth() / getDimension();
                        float squareY = getY() + posY * getHeight() / getDimension();

                        paint.setColor(getScene().getResources().getColor(R.color.board_square_border));
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth((getWidth() / getDimension()) * 0.075f);
                        paint.setStrokeCap(Paint.Cap.ROUND);
                        paint.setStrokeJoin(Paint.Join.ROUND);
                        canvas.drawRect(squareX, squareY, squareX + getWidth() / getDimension(), squareY + getHeight() / getDimension(), paint);
                    }

                    paint.setStyle(Paint.Style.FILL);
                    paint.setColor(getScene().getResources().getColor(R.color.board_square_shadow));
                    float movW = (getWidth() / getDimension()) * (1 + .414214f * 0.75f);
                    float movH = (getHeight() / getDimension()) * (1 + .414214f * 0.75f);

                    RectF rect = new RectF();
                    rect.set(movX - movW / 2, movY - movH / 2, movX + movW / 2, movY + movH / 2);
                    canvas.drawOval(rect, paint);
                    break;
                }
        }
    }
    @Override
    public void resize () {
        setDimensions(scene.getWidth(), scene.getWidth());
        setPosition(scene.getX(), scene.getY() + (scene.getHeight() - height) / 2);

        for(Piece p: lstPieces){
            p.resize();
        }
    }

    @Override
    public void update(float secondsElapsed) {
        switch (state) {
            case STATE_MAGNETIC_EFECT:
                updateStateMagneticEfect(secondsElapsed);
                break;
        }
    }

    private void updateStateMagneticEfect(float secondsElapsed){
        if (getLstPieces().size() > 1 && !endStateMagnetic) {
            boolean isMovingPieces = false;
            for (Piece p : lstPieces) {
                if (!p.equals(lastPiece) && p.getM() != 0) {
                    float dx = p.getCenterX() - lastPiece.getCenterX();
                    float dy = p.getCenterY() - lastPiece.getCenterY();
                    float dx2 = (float) (Math.pow(dx, 2));
                    float dy2 = (float) (Math.pow(dy, 2));
                    float d2 = dx2 + dy2;
                    float d = (float) Math.sqrt(d2);
                    float a = ((K * (float)Math.pow(getWidth(), 3)) * p.getM()) / d2 - (FF * getWidth() * p.getM());

                    p.setAx(a * (dx / d));
                    p.setAy(a * (dy / d));
                    p.setVx(p.getVx() + p.getAx());
                    p.setVy(p.getVy() + p.getAy());

                    float stopX = getCenterXByPositionX(p.getStopPosX());
                    float stopY = getCenterYByPositionY(p.getStopPosY());

                    float iniX = getCenterXByPositionX(p.getPositionX());
                    float iniY = getCenterYByPositionY(p.getPositionY());

                    float dStopIni2 = (float)Math.pow(stopX - iniX, 2) + (float)Math.pow(stopY - iniY, 2);
                    float dPieceIni2 = (float)Math.pow((p.getCenterX() + p.getVx()) - iniX, 2) + (float)Math.pow((p.getCenterY() + p.getVy()) - iniY, 2);

                    boolean uncrossStop = dPieceIni2 < dStopIni2;

                    if(uncrossStop){
                        p.setCenterX(p.getCenterX() + p.getVx());
                        p.setCenterY(p.getCenterY() + p.getVy());
                        isMovingPieces = true;
                    }else{
                        /* Log.i("Board", "dx="+dx+", dy="+dy+", vx="+p.getVx()+", vy="+p.getVy()); */
                        p.setM(0);
                        p.setPositionX(p.getStopPosX());
                        p.setPositionY(p.getStopPosY());
                        p.resize();
                    }
                }
            }
            if(!isMovingPieces){
                endStateMagnetic = true;
            }
        }else{
            if(sumSecElapsed >= ST_MAG_TIME_EFECT) {
                lastPiece.initStateWait();
                ((PlayScene) scene).changeTurn();
                initStateWaiting();
            }
        }
        sumSecElapsed += secondsElapsed;
    }

    public void putPiece(int posX, int posY, int turn){
        Piece piece = createPiece(posX, posY, turn);
        lastPiece = piece;
        initPiecesForMagneticEffect();
        synchronized (lstPieces) {
            lstPieces.add(piece);
        }
        initStateMagneticEfect();
    }

    @Override
    public void actionOnTouch(float x, float y) {}

    @Override
    public void actionOnTouchUp(float x, float y) {}

    @Override
    public void actionOnTouchMove(float x, float y) {}

    public void initMoving(float x, float y){
        this.movX = x;
        this.movY = y;
    }

    public Piece createPiece(int positionX, int positionY, int turn){
        Piece piece = new Piece(positionX, positionY, scene, this);
        piece.init();
        piece.resize();
        if(turn == GameState.TURN_PLAYER){
            piece.setType(Piece.RED);
        }else{
            piece.setType(Piece.BLUE);
        }
        return piece;
    }

    public void initPiecesForMagneticEffect(){
        for (Piece p : lstPieces) {
            p.resetMoving();
        }
        for(int i = 0; i < Constants.DIRS_X.length; i++){
            int dx = Constants.DIRS_X[i];
            int dy = Constants.DIRS_Y[i];
            //Log.i("Board", "DIR dx=" + dx + ", dy=" + dy);
            Piece foundPiece = null;
            int posX = lastPiece.getPositionX() + dx;
            int posY = lastPiece.getPositionY() + dy;
            //Log.i("Board", "sx=" + posX + ", sy=" + posY + " - lx=" + lastPiece.getPositionX() + ", ly=" + lastPiece.getPositionY());
            while(0 <= posX && posX < dimension && 0 <= posY && posY < dimension){
                foundPiece = validateIfContainsPieceReturn(posX, posY);
                if(foundPiece != null){
                    //Log.i("Board", "FOUND");
                    if(foundPiece.getType() != lastPiece.getType()){
                        foundPiece.setM(-1);
                    }else{
                        foundPiece.setM(1);
                    }
                    break;
                }
                posX += dx;
                posY += dy;
                //Log.i("Board", "sx=" + posX + ", sy=" + posY + " - lx=" + lastPiece.getPositionX() + ", ly=" + lastPiece.getPositionY());
            }
            if(foundPiece != null){
                if(foundPiece.getM() < 0){
                    foundPiece.setStopPosX(lastPiece.getPositionX() + dx);
                    foundPiece.setStopPosY(lastPiece.getPositionY() + dy);
                }else{
                    posX += dx;
                    posY += dy;
                    while(0 <= posX && posX < dimension && 0 <= posY && posY < dimension){
                        if(validateIfContainsPiece(posX, posY)){
                            break;
                        }
                        posX += dx;
                        posY += dy;
                        //Log.i("Board", "sx=" + posX + ", sy=" + posY + " - lx=" + lastPiece.getPositionX() + ", ly=" + lastPiece.getPositionY());
                    }
                    foundPiece.setStopPosX(posX - dx);
                    foundPiece.setStopPosY(posY - dy);
                    //Log.i("Board", "Sx=" + foundPiece.getStopPosX() + ", Sy=" + foundPiece.getStopPosY());
                }
            }
        }
    }

    public boolean validateIfContainsPiece(int x, int y){
        for(Piece p : lstPieces){
            if(p.getPositionX() == x && p.getPositionY() == y){
                return true;
            }
        }
        return false;
    }
    public Piece validateIfContainsPieceReturn(int x, int y){
        for(Piece p : lstPieces){
            if(p.getPositionX() == x && p.getPositionY() == y){
                return p;
            }
        }
        return null;
    }

    public int[][] getMatrix(){
        int matrix[][] = new int[dimension][dimension];
        for(int i = 0; i < dimension; i++){
            for(int j = 0; j < dimension; j++){
                matrix[i][j] = GameState.SQUARE_NOTHING;
            }
        }
        for(Piece p : lstPieces){
            matrix[p.getPositionY()][p.getPositionX()] = p.getType() == Piece.RED ? GameState.SQUARE_PLAYER : GameState.SQUARE_MACHINE;
        }
        return matrix;
    }

    public int getCountPiecesPlayer(){
        int count = 0;
        for(Piece p : lstPieces){
            if(p.getType() == Piece.RED){
                count++;
            }
        }
        return count;
    }

    public int getCountPiecesMachine(){
        int count = 0;
        for(Piece p : lstPieces){
            if(p.getType() == Piece.BLUE){
                count++;
            }
        }
        return count;
    }

    public int getGameState(){
        return ((PlayScene)getScene()).getState();
    }

    public int getPositionXByX(float x){
        return (int) ((x - this.x) / (getWidth() / getDimension()));
    }

    public int getPositionYByY(float y){
        return (int) ((y - this.y) / (getHeight() / getDimension()));
    }

    public float getCenterXByPositionX(int posX){
        return x + ((posX + 0.5f) * width) / dimension;
    }

    public float getCenterYByPositionY(int posY){
        return y + ((posY + 0.5f) * height) / dimension;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
