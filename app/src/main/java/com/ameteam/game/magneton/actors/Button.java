package com.ameteam.game.magneton.actors;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import com.ameteam.game.magneton.R;

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

        //Drawing the box
        final RectF rect = new RectF();
        rect.set(x, y, x + width, y + height);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(getScene().getResources().getColor(R.color.btn_fill));
        canvas.drawRoundRect(rect, getHeight() * 0.25f, getHeight() * 0.25f, paint);

        int borderColor = getScene().getResources().getColor(R.color.btn_border);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(borderColor);
        paint.setStrokeWidth(getHeight() * 0.0625f);
        canvas.drawRoundRect(rect, getHeight() * 0.25f, getHeight() * 0.25f, paint);

        com.ameteam.graphics.Rect.drawRectWithText(canvas, text, rect, borderColor, Paint.Align.CENTER, 0.6f);
    }

    @Override
    public void update(float secondsElapsed) {
    }

    @Override
    public void actionOnTouch(float x, float y) {
    }

    @Override
    public void actionOnTouchUp(float x, float y) {

    }

    @Override
    public void actionOnTouchMove(float x, float y) {

    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
