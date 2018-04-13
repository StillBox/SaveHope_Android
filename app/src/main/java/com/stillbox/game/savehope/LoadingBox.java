package com.stillbox.game.savehope;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class LoadingBox extends GameObject {

    private int updateTime = 0;
    private int maxProgress = 100;
    private int currentProgress = 0;
    private int textSize;

    public LoadingBox() {

        x = 0;
        y = 0;
        w = MainView.mainView.getWidth();
        h = MainView.mainView.getHeight();
        float ratioW = w / MainView.DEST_WIDTH;
        float ratioH = h / MainView.DEST_HEIGHT;
        float ratio = Math.min(ratioW, ratioH);
        textSize = Math.round(20f * ratio);
    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        int maxWidth = (int) paint.measureText("Loading...");
        int count = updateTime / 1000;
        String text = "Loading.";
        while (count > 0) {
            text += ".";
            count--;
        }

        paint.setColor(Color.BLACK);
        canvas.drawRect(x, y, x + w, y + h, paint);

        paint.setColor(Color.WHITE);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x + w - maxWidth, y + h - textSize / 2, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(currentProgress + " / " + maxProgress, x + w / 2, y + h / 2 + textSize / 2, paint);
    }

    @Override
    public void update(long elapsedTime) {

        updateTime += elapsedTime;
        if (updateTime >= 3000) {
            updateTime -= 3000;
        }
    }
}
