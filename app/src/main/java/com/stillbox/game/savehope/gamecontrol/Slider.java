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

    private StaticText title;
    private int textSize;
    private float gap;

    private int min, max;
    private int spacing, count;
    private int current;

    private int pressTime;
    private boolean bIsLeftPressed, bIsRightPressed;

    private float value_x, value_y;

    public Slider(String title, float title_x, float title_y, float title_w, float title_h, float x, float y, float w, float h) {

        this.title = new StaticText(title, title_x, title_y, title_w, title_h);

        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setTextSize((int) title_h);

        min = 0;
        max = 10;
        spacing = 1;
        count = 10;
        current = 10;
        gap = w / (3 * count - 1);
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

        title.draw(canvas, paint);

        paint.setColor(Color.BLACK);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(String.valueOf(current), value_x, value_y, paint);

        int currentCount = (current - min) / spacing;
        for (int i = 0; i < count; i++) {
            RectF rectF = new RectF(x + gap * 3 * i, y, x + gap * (3 * i + 2), y + h);
            paint.setColor(i < currentCount ? Color.BLACK : Color.GRAY);
            canvas.drawRect(rectF, paint);
        }

        Path path = new Path();
        path.moveTo(x - h * 3 / 2, y + h / 2);
        path.lineTo(x - h, y);
        path.lineTo(x - h, y + h);
        path.close();
        paint.setColor(bIsLeftPressed ? Color.GRAY : Color.BLACK);
        canvas.drawPath(path, paint);
        path.reset();
        path.moveTo(x + w + h * 3 / 2, y + h / 2);
        path.lineTo(x + w + h, y);
        path.lineTo(x + w + h, y + h);
        path.close();
        paint.setColor(bIsRightPressed ? Color.GRAY : Color.BLACK);
        canvas.drawPath(path, paint);
    }

    @Override
    public void update(int elapsedTime) {

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
                int touch_count = (int) ((touch_x - x + gap) / w * count);
                int touch_value = min + touch_count * spacing;
                if (touch_value != current) {
                    setCurrent(min + touch_count * spacing);
                    GameSound.playSE(GameSound.ID_SE_SELECT);
                }
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
        value_x = x + w + h * 4;
        value_y = y + h / 2 - (metrics.bottom + metrics.top) / 2;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
        current = max;
    }

    public void setRange(int min, int max) {
        setMin(min);
        setMax(max);
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
        count = (max - min) / spacing;
        gap = w / (3 * count - 1);
    }

    public void setCount(int count) {
        this.count = count;
        spacing = (max - min) / count;
        gap = w / (3 * count - 1);
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current > max ? max : current < min ? min : current;
        if (onValueChangeListener != null) onValueChangeListener.onValueChange();
    }

    public void add() {
        setCurrent(current + spacing);
        GameSound.playSE(GameSound.ID_SE_SELECT);
    }

    public void sub() {
        setCurrent(current - spacing);
        GameSound.playSE(GameSound.ID_SE_SELECT);
    }

    public void setOnResetListener(OnResetListener onResetListener) {
        this.onResetListener = onResetListener;
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.onValueChangeListener = onValueChangeListener;
    }

    private int checkTouchPoint(float touch_x, float touch_y) {

        if (touch_y >= y - h / 2 && touch_y <= y + h * 3 / 2) {
            if (touch_x > x - 3 * h && touch_x < x - h / 2)
                return -1;
            if (touch_x > x + w + h / 2 && touch_x < x + w + 3 * h)
                return 1;
            if (touch_x > x - gap && touch_x < x + w + gap)
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
