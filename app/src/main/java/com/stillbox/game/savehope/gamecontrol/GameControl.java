package com.stillbox.game.savehope.gamecontrol;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public abstract class GameControl {

    float x, y, w, h;

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void setH(float h) {
        this.h = h;
    }

    public abstract void reset();

    public abstract void onDestroy();

    public abstract void draw(Canvas canvas, Paint paint);

    public abstract void update(long elapsedTime);

    public abstract void onTouchEvent(MotionEvent event);
}
