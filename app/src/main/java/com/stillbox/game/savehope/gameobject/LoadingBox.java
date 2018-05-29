package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class LoadingBox extends GameObject {

    private boolean bIsOn;
    private int updateTime;
    private int maxProgress = 100;
    private int currentProgress = 0;
    private int textSize;

    public LoadingBox(float x, float y, float w, float h, float textSize) {

        bIsOn = false;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.textSize = Math.round(textSize);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (!bIsOn) return;

        int maxWidth = (int) paint.measureText("Loading...");
        int count = updateTime / 1000;
        StringBuilder text = new StringBuilder("Loading.");
        while (count > 0) {
            text.append(".");
            count--;
        }

        paint.setColor(Color.BLACK);
        canvas.drawRect(x, y, x + w, y + h, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text.toString(), x + w - maxWidth, y + h - textSize / 2, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(currentProgress + " / " + maxProgress, x + w / 2, y + h / 2 + textSize / 2, paint);
    }

    @Override
    public void update(int elapsedTime) {

        if (!bIsOn) return;

        updateTime += elapsedTime;
        if (updateTime >= 3000) {
            updateTime -= 3000;
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

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
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public int getCurrentProgress() {
        return currentProgress;
    }

    public void setCurrentProgress(int currentProgress) {
        this.currentProgress = currentProgress;
    }

    public void increaseProgress(int increment) {
        currentProgress += increment;
        if (currentProgress > maxProgress) {
            currentProgress = maxProgress;
        }
    }
}
