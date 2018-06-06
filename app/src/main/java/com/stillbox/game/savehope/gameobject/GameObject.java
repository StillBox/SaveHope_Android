package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public abstract class GameObject {

    protected float x, y, w, h, offset_x, offset_y, scale = 1f;

    public void setX(float x) {
        this.x = x;
    }

    public float getX() {
        return x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void setW(float w) {
        this.w = w;
    }

    public void setH(float h) {
        this.h = h;
    }

    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }

    public void setOffset_x(float offset_x) {
        this.offset_x = offset_x;
    }

    public void setOffset_y(float offset_y) {
        this.offset_y = offset_y;
    }

    public void setOffset(float offset_x, float offset_y) {
        this.offset_x = offset_x;
        this.offset_y = offset_y;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public abstract void onDestroy();

    public abstract void draw(Canvas canvas, Paint paint);

    public abstract void update(int elapsedTime);

    public abstract void onTouchEvent(MotionEvent event);

    public static void release(GameObject object) {
        if (object != null) {
            object.onDestroy();
        }
    }
}
