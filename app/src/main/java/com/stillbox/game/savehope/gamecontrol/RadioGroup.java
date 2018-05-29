package com.stillbox.game.savehope.gamecontrol;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.stillbox.game.savehope.gamesound.GameSound;

public class RadioGroup extends GameControl {

    private StaticText title;
    private SparseArray<RadioButton> buttons;
    private int checkedId;
    private OnResetListener onResetListener;
    private OnCheckedListener onCheckedListener;

    public RadioGroup(String text, float x, float y, float w, float h) {

        title = new StaticText(text, x, y, w, h);
        buttons = new SparseArray<>();
        checkedId = 0;
        reset();
    }

    @Override
    public void reset() {

        RadioButton button = buttons.valueAt(0);
        if (button != null) {
            button.setChecked(true);
            checkedId = buttons.keyAt(0);
        } else {
            checkedId = 0;
        }

        if (onResetListener != null) {
            onResetListener.onReset();
        }
    }

    @Override
    public void onDestroy() {

        title.onDestroy();
        for (int i = 0; i < buttons.size(); i++) {
            buttons.valueAt(i).onDestroy();
        }
        buttons.clear();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        title.draw(canvas, paint);
        for (int i = 0; i < buttons.size(); i++) {
            buttons.valueAt(i).draw(canvas, paint);
        }
    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        for (int i = 0; i < buttons.size(); i++) {
            RadioButton button = buttons.valueAt(i);
            if (button.checkPosition(touch_x, touch_y)) {
                int touch_id = buttons.keyAt(i);
                if (touch_id != checkedId) {
                    setChecked(touch_id);
                    GameSound.playSE(GameSound.ID_SE_SELECT);
                }
            }
        }
    }

    private class RadioButton extends GameControl {

        private String text;
        private int textSize;
        private boolean bIsChecked;
        private boolean bIsIndeterminate;
        private float text_x, text_y;

        RadioButton(String text, float x, float y, float w, float h, float textSize) {

            this.text = text;
            setX(x);
            setY(y);
            setW(w);
            setH(h);
            this.textSize = (int) textSize;

            Paint paint = new Paint();
            paint.setTextSize(textSize);
            Paint.FontMetrics metrics = paint.getFontMetrics();
            text_x = x + w / 2;
            text_y = y + h / 2 - (metrics.bottom + metrics.top) / 2;
        }

        @Override
        public void reset() {

            bIsChecked = false;
            bIsIndeterminate = false;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            if (bIsChecked) {
                paint.setColor(Color.BLACK);
                canvas.drawRect(x, y, x + w, y + h, paint);
            } else if (bIsIndeterminate) {
                paint.setColor(Color.GRAY);
                canvas.drawRect(x, y, x + w, y + h, paint);
            }

            paint.setColor(bIsChecked || bIsIndeterminate ? Color.WHITE : Color.BLACK);
            paint.setTextSize(textSize);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(text, text_x, text_y, paint);
        }

        @Override
        public void update(int elapsedTime) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        void setChecked(boolean bIsChecked) {

            this.bIsChecked = bIsChecked;
        }

        void setIndeterminate(boolean bIsIndeterminate) {

            this.bIsIndeterminate = bIsIndeterminate;
        }

        boolean checkPosition(float touch_x, float touch_y) {
            return touch_x >= x && touch_x <= x + w &&
                    touch_y >= y && touch_y <= y + h;
        }
    }

    public void addButton(int id, String text, float x, float y, float w, float h, float textSize) {

        RadioButton button = new RadioButton(text, x, y, w, h, textSize);
        buttons.put(id, button);
    }

    public void setChecked(int id) {

        if (buttons.get(checkedId) != null) {
            buttons.get(checkedId).setChecked(false);
        }
        if (buttons.get(id) != null) {
            checkedId = id;
            buttons.get(id).setChecked(true);
        } else {
            checkedId = 0;
        }

        if (onCheckedListener != null) {
            onCheckedListener.onChecked();
        }
    }

    public void setIndeterminate(int id) {

        buttons.get(id).setIndeterminate(true);
    }

    public int getChecked() {

        return checkedId;
    }

    public interface OnResetListener {
        void onReset();
    }

    public interface OnCheckedListener {
        void onChecked();
    }

    public void setOnResetListener(OnResetListener listener) {
        onResetListener = listener;
    }

    public void setOnCheckedListener(OnCheckedListener listener) {
        onCheckedListener = listener;
    }
}
