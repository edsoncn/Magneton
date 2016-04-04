package com.ameteam.game.magneton.ai;

/**
 * Created by edson on 19/03/2016.
 */
public class GamePosition {

    private int positionX;
    private int positionY;

    public GamePosition(int positionX, int positionY){
        this.positionX = positionX;
        this.positionY = positionY;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GamePosition{");
        sb.append("positionX=").append(positionX);
        sb.append(", positionY=").append(positionY);
        sb.append('}');
        return sb.toString();
    }
}
