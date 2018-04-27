package com.stillbox.game.savehope.gamecontrol;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.gamesound.GameSound;

public class Button extends GameControl {

    public static final int BUTTON_NORMAL = 1;
    public static final int BUTTON_TOUCHED = 2;
    public static final int BUTTON_PRESSED = 3;
    private int buttonState;

    private int textSize;
    private int textColor;
    private int buttonColor;
    private String strNormal;
    private String strTouched = null;
    private String strPressed = null;
    private OnPressedListener listener = null;

    private float text_x, text_y;

    public Button(String strNormal, float x, float y, float w, float h) {

        this.strNormal = strNormal;
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

        paint.setColor(buttonColor);
        canvas.drawRect(x, y, x + w, y + h, paint);

        paint.setColor(textColor);
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        if (buttonState == BUTTON_PRESSED && strPressed != null)
            canvas.drawText(strPressed, text_x, text_y, paint);
        else if (buttonState == BUTTON_TOUCHED && strTouched != null)
            canvas.drawText(strTouched, text_x, text_y, paint);
        else
            canvas.drawText(strNormal, text_x, text_y, paint);
    }

    @Override
    public void update(long elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            if (checkTouchPoint(touch_x, touch_y))
                setState(BUTTON_TOUCHED);
            else
                setState(BUTTON_NORMAL);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (checkTouchPoint(touch_x, touch_y)) {
                setState(BUTTON_PRESSED);
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

        if (buttonState != state) {
            buttonState = state;
            switch (state) {
                case BUTTON_NORMAL:
                    textColor = Color.BLACK;
                    buttonColor = Color.WHITE;
                    break;
                case BUTTON_TOUCHED:
                    textColor = Color.WHITE;
                    buttonColor = Color.BLACK;
                    GameSound.playSE(GameSound.ID_SE_CURSOR);
                    break;
                case BUTTON_PRESSED:
                    textColor = Color.WHITE;
                    buttonColor = Color.GRAY;
                    GameSound.playSE(GameSound.ID_SE_DECIDE);
                    break;
            }
        }
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
