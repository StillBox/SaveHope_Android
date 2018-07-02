package com.stillbox.game.savehope.gameobject.gamepad;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import static com.stillbox.game.savehope.gameobject.gamepad.GamePad.buttonRadius;

public class PadButton {

    private int id;
    private float x;
    private float y;
    private int color;
    private String text;
    private float textSize, text_y;
    private boolean bIsPressed;

    PadButton(int id, float x, float y, int color, String text) {

        this.id = id;
        this.x = x;
        this.y = y;
        this.color = color;
        this.text = text;

        textSize = (int) (GamePad.buttonRadius * 1.2f);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        Paint.FontMetrics metrics = paint.getFontMetrics();
        text_y = y - (metrics.bottom + metrics.top) / 2;
    }

    void draw(Canvas canvas, Paint paint) {

        paint.setColor(color);
        paint.setAlpha(bIsPressed ? 191 : 127);
        canvas.drawCircle(x, y, buttonRadius, paint);

        paint.setColor(bIsPressed ? Color.WHITE : Color.LTGRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(buttonRadius / 5);
        canvas.drawCircle(x, y, buttonRadius, paint);

        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        if (text != null) {
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, x, text_y, paint);
        }
        paint.setTypeface(Typeface.DEFAULT);
    }

    boolean isPressed() {

        return bIsPressed;
    }

    void setPressed(boolean bIsPressed) {

        this.bIsPressed = bIsPressed;
    }

    boolean checkTouchPoint(float touch_x, float touch_y) {

        float offset_x = touch_x - x;
        float offset_y = touch_y - y;
        return offset_x * offset_x + offset_y * offset_y < buttonRadius * buttonRadius;
    }
}
