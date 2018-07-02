package com.stillbox.game.savehope.gamecontrol;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class StaticText extends GameControl {

    private String text;
    private int textSize;
    private int textColor;
    private Paint.Align textAlign;

    private float text_x, text_y;

    public StaticText(String text, float x, float y, float w, float h) {

        this.text = text;
        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setTextSize((int) h);
        setTextColor(Color.BLACK);
        setTextAlign(Paint.Align.CENTER);
    }

    @Override
    public void reset() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (text == null) return;

        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(textAlign);
        canvas.drawText(text, text_x, text_y, paint);
    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTextSize(int textSize) {

        this.textSize = textSize;
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        text_x = x;
        text_y = y + h / 2 - (metrics.bottom + metrics.top) / 2;
    }

    public void setTextColor(int textColor) {

        this.textColor = textColor;
    }

    public void setTextAlign(Paint.Align textAlign) {

        this.textAlign = textAlign;
        switch (textAlign) {
            case LEFT:
                text_x = x;
                break;
            case CENTER:
                text_x = x + w / 2;
                break;
            case RIGHT:
                text_x = x + w;
                break;
        }
    }
}
