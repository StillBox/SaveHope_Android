package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;

import java.util.ArrayList;

public class DigitalBackground extends GameObject {

    private static final int DEFAULT_PERIOD = 60;

    private float size;
    private int count_x, count_y, count;
    private int length;
    private int updateTime;
    private int updatePeriod;
    private boolean bReverse;
    private boolean bIsActive;
    private ArrayList<Tail> tails;

    public DigitalBackground(boolean bClear, boolean bReverse) {

        size = 16f * MainView.rate;
        count_x = (int) (MainView.w / size);
        count_y = (int) (MainView.h / size);
        count = count_x * count_y / 160;
        length = 7;

        updateTime = 0;
        updatePeriod = DEFAULT_PERIOD;
        this.bReverse = bReverse;
        bIsActive = false;

        tails = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int head_x = (int) (Math.random() * count_x);
            int head_y = (int) (Math.random() * count_y) + (bClear ? (bReverse ? count_y : -count_y) : 0);
            tails.add(new Tail(head_x, head_y));
        }

    }

    @Override
    public void onDestroy() {

        tails.clear();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        for (Tail tail : tails) {
            for (int i = 0; i < length; i++) {
                float left = tail.x[i] * size;
                float top = tail.y[i] * size;
                float right = left + size - 1f;
                float bottom = top + size - 1f;
                RectF spot = new RectF(left, top, right, bottom);
                int alpha = i == 0 ? 240 : 15 * (length - i);

                paint.setColor(bReverse ? Color.RED : Color.GREEN);
                paint.setAlpha(alpha);
                canvas.drawRect(spot, paint);
            }
        }
        paint.setAlpha(255);
    }

    @Override
    public void update(int elapsedTime) {

        if (!bIsActive) return;

        updateTime += elapsedTime;
        if (updateTime > updatePeriod) {
            updateTime -= updatePeriod;
            //update the position
            for (Tail tail : tails) {
                for (int i = length - 1; i > 0; i--) {
                    tail.x[i] = tail.x[i - 1];
                    tail.y[i] = tail.y[i - 1];
                }
                if (Math.random() < 0.05) {
                    if (Math.random() < 0.5) {
                        tail.x[0]--;
                    } else {
                        tail.x[0]++;
                    }
                } else {
                    if (bReverse) {
                        tail.y[0]--;
                    } else {
                        tail.y[0]++;
                    }
                }
                //renew tails when out of screen
                if (bReverse) {
                    if (tail.y[length - 1] < 0) {
                        int head_x = (int) (Math.random() * count_x);
                        int head_y = (int) (Math.random() * length) + count_y;
                        tail.renew(head_x, head_y);
                    }
                } else {
                    if (tail.y[length - 1] > count_y) {
                        int head_x = (int) (Math.random() * count_x);
                        int head_y = -(int) (Math.random() * length);
                        tail.renew(head_x, head_y);
                    }
                }
            }
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    private class Tail {
        int[] x;
        int[] y;

        Tail(int head_x, int head_y) {
            x = new int[length];
            y = new int[length];
            renew(head_x, head_y);
        }

        void renew(int head_x, int head_y) {
            x[0] = head_x;
            y[0] = head_y;
            for (int i = 1; i < length; i++) {
                x[i] = x[0];
                y[i] = y[0] + (bReverse ? i : -i);
            }
        }
    }

    public boolean isActive() {
        return bIsActive;
    }

    public void setActive(boolean bIsActive) {
        this.bIsActive = bIsActive;
    }

    public boolean isReverse() {
        return bReverse;
    }

    public void setReverse(boolean bReverse) {
        this.bReverse = bReverse;
    }
}
