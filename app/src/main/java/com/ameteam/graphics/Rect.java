package com.ameteam.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by edson on 8/05/2016.
 */
public class Rect {

    public static void drawRectWithText(Canvas canvas, String text, RectF rectF, int color, Paint.Align align, float proportion){
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setTextAlign(align);
        paint.setTextSize(rectF.height() * proportion);
        android.graphics.Rect textBounds = new android.graphics.Rect();
        paint.getTextBounds(text, 0, text.length(), textBounds);

        //Drawing the text
        canvas.drawText(text, rectF.left + (align.equals(Paint.Align.CENTER) ? rectF.width() / 2 :
                        (align.equals(Paint.Align.RIGHT) ? rectF.width() : 0) ),
                rectF.top + rectF.height() / 2 - textBounds.exactCenterY(), paint);
    }

}
