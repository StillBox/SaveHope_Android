package com.stillbox.game.savehope.gameobject.dialog;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.gamecontrol.Button;
import com.stillbox.game.savehope.gamecontrol.GameControl;
import com.stillbox.game.savehope.gamecontrol.StaticText;
import com.stillbox.game.savehope.gameobject.Border;
import com.stillbox.game.savehope.gameobject.GameObject;
import com.stillbox.game.savehope.gamesound.GameSound;

public class DialogBox extends GameObject {

    boolean bIsOn;
    private Border border;
    private SparseArray<GameControl> controls;
    private SparseArray<OnSelectedListener> listeners;

    public DialogBox(float x, float y, float w, float h) {

        setX(x);
        setY(y);
        setW(w);
        setH(h);
        border = new Border(x, y, w, h, MainView.rate);
        controls = new SparseArray<>();
        bIsOn = true;
    }

    public DialogBox(float w, float h) {

        setX(MainView.w / 2);;
        setY(MainView.h / 2);
        setW(w);
        setH(h);
        border = new Border(x, y, w, h, MainView.rate);
        controls = new SparseArray<>();
        listeners = new SparseArray<>();
        bIsOn = true;
    }

    @Override
    public void onDestroy() {

        border.onDestroy();
        for (int i = 0; i < controls.size(); i++) {
            controls.valueAt(i).onDestroy();
        }
        controls.clear();
        listeners.clear();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        border.draw(canvas, paint);
        if (border.isReady()) {
            for (int i = 0; i < controls.size(); i++) {
                controls.valueAt(i).draw(canvas, paint);
            }
        }
    }

    @Override
    public void update(int elapsedTime) {

        border.update(elapsedTime);
        if (border.isReady()) {
            for (int i = 0; i < controls.size(); i++) {
                controls.valueAt(i).update(elapsedTime);
            }
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (checkTouchPoint(touch_x, touch_y)) {
                border.close(() -> bIsOn = false);
                GameSound.playSE(GameSound.ID_SE_CANCEL);
            }
        }

        if (border.isReady()) {
            for (int i = 0; i < controls.size(); i++) {
                controls.valueAt(i).onTouchEvent(event);
            }
        }
    }

    public void setText(String text, float x, float y, float w, float h) {

        StaticText title = new StaticText(text, this.x + x, this.y + y, w, h);
        controls.put(0, title);
    }

    public Button addButton(int id, String text, float x, float y, float w, float h) {

        Button button = new Button(text, this.x + x, this.y + y, w, h);
        button.setOnPressedListener(() -> {
            border.close(() -> {
                OnSelectedListener listener = listeners.get(id);
                if (listener != null) {
                    listener.onSelected();
                }
                bIsOn = false;
            });
        });
        controls.put(id, button);

        return button;
    }

    public boolean isOn() {
        return bIsOn;
    }

    private boolean checkTouchPoint(float touch_x, float touch_y) {

        return !border.checkTouchPoint(touch_x, touch_y);
    }

    public interface OnSelectedListener {

        void onSelected();
    }

    public void setOnSelectedListener(int id, OnSelectedListener listener) {

        listeners.put(id, listener);
    }
}
