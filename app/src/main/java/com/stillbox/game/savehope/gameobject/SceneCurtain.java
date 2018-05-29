package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class SceneCurtain extends GameObject {

    private boolean bIsOn;
    private int color;
    private int begAlpha, endAlpha;
    private int duration;
    private int updateTime;
    private int updateRate;
    private int currentAlpha;
    private OnEndListener listener;

    public SceneCurtain(float x, float y, float w, float h) {

        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        color = Color.BLACK;
        begAlpha = 0;
        endAlpha = 255;
        duration = 500;
        updateTime = 0;
        updateRate = 1;
        listener = null;
    }

    @Override
    public void onDestroy() {

        listener = null;
    }

    public void draw(Canvas canvas, Paint paint) {

        if (!bIsOn) return;

        paint.setColor(color);
        paint.setAlpha(currentAlpha);
        canvas.drawRect(x, y, x + w, y + h, paint);
        paint.setAlpha(255);
    }

    public void update(int elapsedTime) {

        if (!bIsOn) return;

        updateTime += updateRate * elapsedTime;
        if (updateTime >= duration) {
            end();
        }
        currentAlpha = begAlpha + (int) ((float) (endAlpha - begAlpha) * updateTime / duration);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (bIsOn) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                updateRate = 3;
            }
        }
    }

    public boolean isOn() {
        return bIsOn;
    }

    public void start() {
        bIsOn = true;
        updateTime = 0;
    }

    public void end() {
        bIsOn = false;
        if (listener != null) {
            listener.onEnd();
            listener = null;
        }
    }

    public void setColor(int color) {

        this.color = color;
    }

    public void set(int begAlpha, int endAlpha, int duration, OnEndListener listener) {

        bIsOn = true;
        updateTime = 0;
        this.begAlpha = begAlpha;
        this.endAlpha = endAlpha;
        this.duration = duration;
        this.listener = listener;
    }

    public interface OnEndListener {

        void onEnd();
    }

    public void setOnEndListener(OnEndListener listener) {

        this.listener = listener;
    }
}
