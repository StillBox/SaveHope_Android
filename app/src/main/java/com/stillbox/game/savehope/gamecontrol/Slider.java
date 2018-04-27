package com.stillbox.game.savehope.gamecontrol;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.stillbox.game.savehope.gamesound.GameSound;

public class Slider extends GameControl {

    private static final int RESPONSE_TIME = 600;
    private static final int RESPONSE_PERIOD = 100;

    private OnResetListener onResetListener;
    private OnValueChangeListener onValueChangeListener;

    private String text;
    private int textSize;
    private float slider_x, slider_y, slider_w, slider_h;
    private float gap;

    private int min, max;
    private int spacing, count;
    private int current;

    private int pressTime;
    private boolean bIsLeftPressed, bIsRightPressed;

    private float text_x, text_y;
    private float value_x, value_y;

    public Slider(String text, float x, float y, float w, float h, float slider_x, float slider_y, float slider_w, float slider_h) {

        this.text = text;
        setX(x);
        setY(y);
        setW(w);
        setH(h);
        this.slider_x = slider_x;
        this.slider_y = slider_y;
        this.slider_w = slider_w;
        this.slider_h = slider_h;

        setTextSize((int) h);
        min = 0;
        max = 10;
        spacing = 1;
        count = 10;
        current = 10;
        gap = slider_w / (3 * count - 1);
    }

    @Override
    public void reset() {
        if (onResetListener != null) onResetListener.onReset();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text, text_x, text_y, paint);

        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(current), value_x, value_y, paint);

        int currentCount = (current - min) / spacing;
        for (int i = 0; i < count; i++) {
            RectF rectF = new RectF(slider_x + gap * 3 * i, slider_y, slider_x + gap * (3 * i + 2), slider_y + slider_h);
            paint.setColor(i < currentCount ? Color.BLACK : Color.GRAY);
            canvas.drawRect(rectF, paint);
        }

        Path path = new Path();
        path.moveTo(slider_x - slider_h * 3 / 2, slider_y + slider_h / 2);
        path.lineTo(slider_x - slider_h, slider_y);
        path.lineTo(slider_x - slider_h, slider_y + slider_h);
        path.close();
        paint.setColor(bIsLeftPressed ? Color.GRAY : Color.BLACK);
        canvas.drawPath(path, paint);
        path.reset();
        path.moveTo(slider_x + slider_w + slider_h * 3 / 2, slider_y + slider_h / 2);
        path.lineTo(slider_x + slider_w + slider_h, slider_y);
        path.lineTo(slider_x + slider_w + slider_h, slider_y + slider_h);
        path.close();
        paint.setColor(bIsRightPressed ? Color.GRAY : Color.BLACK);
        canvas.drawPath(path, paint);
    }

    @Override
    public void update(long elapsedTime) {

        if (bIsLeftPressed) {
            pressTime += elapsedTime;
            if (pressTime >= RESPONSE_TIME) {
                sub();
                pressTime -= RESPONSE_PERIOD;
            }
        } else if (bIsRightPressed) {
            pressTime += elapsedTime;
            if (pressTime >= RESPONSE_TIME) {
                add();
                pressTime -= RESPONSE_PERIOD;
            }
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (checkTouchPoint(touch_x, touch_y) == 0) {
                bIsLeftPressed = false;
                bIsRightPressed = false;
                pressTime = 0;
            } else if (checkTouchPoint(touch_x, touch_y) == -1) {
                if (!bIsLeftPressed) {
                    bIsLeftPressed = true;
                    sub();
                }
            } else if (checkTouchPoint(touch_x, touch_y) == 1) {
                if (!bIsRightPressed) {
                    bIsRightPressed = true;
                    add();
                }
            } else if (checkTouchPoint(touch_x, touch_y) == 2) {
                int touch_count = (int) ((touch_x - slider_x + gap) / slider_w * count);
                setCurrent(touch_count * spacing);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            bIsLeftPressed = false;
            bIsRightPressed = false;
            pressTime = 0;
        }
    }

    public void setTextSize(int textSize) {

        this.textSize = textSize;
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        text_x = x + w / 2;
        text_y = y + h / 2 - (metrics.bottom + metrics.top) / 2;
        value_x = slider_x + slider_w + slider_h * 4;
        value_y = slider_y + slider_h / 2 - (metrics.bottom + metrics.top) / 2;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public void setRange(int min, int max) {
        setMin(min);
        setMax(max);
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        count = (max - min) / spacing;
    }

    public void setCount(int count) {
        this.count = count;
        spacing = (max - min) / count;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (this.current != current) {
            this.current = current > max ? max : current < min ? min : current;
            if (onValueChangeListener != null) onValueChangeListener.onValueChange();
            GameSound.playSE(GameSound.ID_SE_SELECT);
        }
    }

    public void add() {
        setCurrent(current + spacing);
    }

    public void sub() {
        setCurrent(current - spacing);
    }

    public void setOnResetListener(OnResetListener onResetListener) {
        this.onResetListener = onResetListener;
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    private int checkTouchPoint(float touch_x, float touch_y) {

        if (touch_y >= slider_y - slider_h / 2 && touch_y <= slider_y + slider_h * 3 / 2) {
            if (touch_x > slider_x - 3 * slider_h && touch_x < slider_x - slider_h / 2)
                return -1;
            if (touch_x > slider_x + slider_w + slider_h / 2 && touch_x < slider_x + slider_w + 3 * slider_h)
                return 1;
            if (touch_x > slider_x - gap && touch_x < slider_x + slider_w + gap)
                return 2;
        }
        return 0;
    }

    public interface OnResetListener {

        void onReset();
    }

    public interface OnValueChangeListener {

        void onValueChange();
    }
}
