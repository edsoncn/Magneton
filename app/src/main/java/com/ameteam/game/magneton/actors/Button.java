package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.ameteam.game.magneton.MagnetonGame;

/**
 * Created by edson on 9/03/2016.
 */
public class Button extends Character{

    private String text;

    public Button(Scene scene, String text) {
        super(scene);
        this.text = text;
    }

    @Override
    public void resize() {
    }

    @Override
    public void doDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#212121"));
        paint.setStrokeWidth(getHeight() * 0.0625f);

        //Drawing the box
        final RectF rect = new RectF();
        rect.set(x, y, x + width, y + height);
        canvas.drawRoundRect(rect, getHeight() * 0.25f, getHeight() * 0.25f, paint);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(getHeight() * 0.6f);
        Rect textBounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);
        paint.setStyle(Paint.Style.FILL);

        //Drawing the text
        canvas.drawText(text, getX() + getWidth()/2, getY() + getHeight()/2 - textBounds.exactCenterY(), paint);
    }

    @Override
    public void update(float secondsElapsed) {
    }

    @Override
    public void actionOnTouch(float x, float y) {
    }

    public boolean validateTouch(float x, float y){
        return getX() < x && x <= getX() + getWidth()
                && getY() < y && y <= getY() + getHeight();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
