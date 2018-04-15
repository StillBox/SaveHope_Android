package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class MenuScene extends GameScene {

    @Override
    public void init() {

    }

    @Override
    public void draw(Canvas canvas) {

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        canvas.drawText("MenuScene", screenW / 2, screenH / 2, paint);
    }

    @Override
    public void update(long elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
