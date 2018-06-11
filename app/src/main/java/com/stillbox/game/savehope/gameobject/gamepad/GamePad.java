package com.stillbox.game.savehope.gameobject.gamepad;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.gameobject.GameObject;

public class GamePad extends GameObject {

    private static final int DEST_BUTTON_RADIUS = 80;
    public static float buttonRadius;

    private SparseArray<PadButton> buttons;
    private SparseArray<PadButtonListener> listeners;

    private PadStick stick;

    public GamePad() {

        buttonRadius = DEST_BUTTON_RADIUS * MainView.rate;
        buttons = new SparseArray<>();
        listeners = new SparseArray<>();
    }

    @Override
    public void onDestroy() {

        buttons.clear();
        listeners.clear();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        for (int i = 0; i < buttons.size(); i++) {
            buttons.valueAt(i).draw(canvas, paint);
        }

        if (stick != null) {
            stick.draw(canvas, paint);
        }
    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        int motion = event.getAction();
        float touch_x = event.getX();
        float touch_y = event.getY();

        for (int i = 0; i < buttons.size(); i++) {
            PadButton button = buttons.valueAt(i);
            PadButtonListener listener = listeners.valueAt(i);
            if ((motion == MotionEvent.ACTION_DOWN || motion == MotionEvent.ACTION_MOVE) && button.checkTouchPoint(touch_x, touch_y)) {
                button.setPressed(true);
                if (listener != null) {
                    listener.OnPressed();
                }
            } else {
                button.setPressed(false);
                if (listener != null) {
                    listener.OnReleased();
                }
            }
        }
    }

    public void addButton(int id, float x, float y, PadButtonListener listener) {

        buttons.put(id, new PadButton(id, x, y, Color.GRAY, null));
        listeners.put(id, listener);
    }

    public void addButton(int id, float x, float y, int color, String text, PadButtonListener listener) {

        buttons.put(id, new PadButton(id, x, y, color, text));
        listeners.put(id, listener);
    }

    public void setListener(int id, PadButtonListener listener) {

        listeners.put(id, listener);
    }

    public interface PadButtonListener {

        void OnPressed();

        void OnReleased();
    }
}
