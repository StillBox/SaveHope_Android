package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;

public class GameCamera extends GameObject {

    private float target_x, target_y;
    private float u, v;
    private boolean bClip;

    public GameCamera() {

        setPosition(0f, 0f);
        setSize(MainView.w, MainView.h);
        setOffset(0f, 0f);
        setClip(false);
    }

    public void onDestroy() {

    }

    public void draw(Canvas canvas, Paint paint) {

        if (MainView.bIsDebugMode) {
            paint.setTextSize(16);
            paint.setTextAlign(Paint.Align.LEFT);
            paint.setColor(Color.GREEN);
            canvas.drawText("top-left: x - " + x + ", y - " + y, 0, 16, paint);
            paint.setColor(Color.YELLOW);
            canvas.drawText("Offset: x - " + offset_x + ", y - " + offset_y, x + offset_x + 16, y + offset_y + 16, paint);
            paint.setColor(Color.YELLOW);
            canvas.drawText("height - " + h, 16, y + offset_y + h / 2 + 16, paint);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("width -" + w, x + offset_x + w / 2, 16, paint);
        }
    }

    public void update(int elapsedTime) {

        if (u != 0f) {
            x += u * elapsedTime;
            if (u * (x - target_x) >= 0f) {
                x = target_x;
                u = 0f;
            }
        }

        if (v != 0f) {
            y += v * elapsedTime;
            if (v * (y - target_y) >= 0f) {
                y = target_y;
                v = 0f;
            }
        }
    }

    public void setTargetPosition(float target_x, float target_y, int duration) {

        this.target_x = target_x;
        this.target_y = target_y;
        u = (target_x - x) / duration;
        v = (target_y - y) / duration;
    }

    public boolean isClip() {

        return bClip;
    }

    public void setClip(boolean bClip) {

        this.bClip = bClip;
    }
}
