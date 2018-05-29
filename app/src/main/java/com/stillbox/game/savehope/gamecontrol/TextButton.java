package com.stillbox.game.savehope.gamecontrol;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.gamesound.GameSound;

public class TextButton extends GameControl {

    public static final int BUTTON_NORMAL = 1;
    public static final int BUTTON_TOUCHED = 2;
    public static final int BUTTON_PRESSED = 3;

    private int buttonState;

    private int textSize;
    private int textColor;
    private String text;
    private OnPressedListener listener = null;

    private float text_x, text_y;

    public TextButton(String text, float x, float y, float w, float h) {

        this.text = text;
        setX(x);
        setY(y);
        setW(w);
        setH(h);
        setState(BUTTON_NORMAL);
        setTextSize((int) h);
    }

    @Override
    public void reset() {

        setState(BUTTON_NORMAL);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);

        paint.setColor(Color.GRAY);
        canvas.drawText(text, text_x + textSize / 10, text_y + textSize / 10, paint);
        paint.setColor(Color.WHITE);
        switch (buttonState) {
            case BUTTON_NORMAL:
            case BUTTON_PRESSED:
                canvas.drawText(text, text_x, text_y, paint);
                break;
            case BUTTON_TOUCHED:
                canvas.drawText(text, text_x + textSize / 15, text_y + textSize / 15, paint);
                break;
        }
    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (checkTouchPoint(touch_x, touch_y)) {
                if (buttonState != BUTTON_TOUCHED) {
                    setState(BUTTON_TOUCHED);
                    GameSound.playSE(GameSound.ID_SE_CURSOR);
                }
            }
            else
                setState(BUTTON_NORMAL);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (checkTouchPoint(touch_x, touch_y)) {
                setState(BUTTON_PRESSED);
                GameSound.playSE(GameSound.ID_SE_DECIDE);
                if (listener != null) listener.onPressed();
            }
        }
    }

    public void setTextSize(int textSize) {

        this.textSize = textSize;
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        text_x = x + w / 2;
        text_y = y + h / 2 - (metrics.bottom + metrics.top) / 2;
    }

    public boolean isTouched() {

        return buttonState == BUTTON_TOUCHED;
    }

    public boolean isPressed() {

        return buttonState == BUTTON_PRESSED;
    }

    public void setState(int state) {

        buttonState = state;
    }

    public void setOnPressedListener(OnPressedListener listener) {

        this.listener = listener;
    }

    private boolean checkTouchPoint(float touch_x, float touch_y) {

        return touch_x >= x && touch_x <= x + w && touch_y >= y && touch_y <= y + h;
    }
    public interface OnPressedListener {

        void onPressed();
    }
}
