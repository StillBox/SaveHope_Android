package com.stillbox.game.savehope.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;

public class Border extends GameObject {

    public static final float BORDER_SIZE = 64f;
    private float borderSize;
    private float innerWidth;
    private float innerHeight;

    private boolean bIsOpen;
    private boolean bIsReady;
    private boolean bIsClosed;
    private OnOpenedListener onOpenedListener = null;
    private OnClosedListener onClosedListener = null;

    private float size;
    private float scale;
    private Bitmap bmpMenu;
    private NinePatch npMenu;

    public Border(float x, float y, float w, float h, float rate) {

        setPosition(x, y);
        setSize(0f, 0f);

        borderSize = BORDER_SIZE * rate;
        innerWidth = w - 2 * borderSize;
        innerHeight = h - 2 * borderSize;
        size = 0f;

        bIsOpen = true;
        bIsReady = false;
        bIsClosed = false;

        bmpMenu = MainView.getBitmap(R.drawable.menu);
        int width = bmpMenu.getWidth();
        scale = 256f / width * rate;
        npMenu = new NinePatch(bmpMenu, bmpMenu.getNinePatchChunk(), null);
    }

    public Border(float x, float y, float w, float h, float rate, OnOpenedListener onOpenedListener) {

        this(x, y, w, h, rate);
        this.onOpenedListener = onOpenedListener;
    }

    @Override
    public void onDestroy() {

        bmpMenu.recycle();
        bmpMenu = null;
        npMenu = null;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        canvas.save();
        canvas.scale(scale, scale, x, y);

        RectF rectF = new RectF(x - w / scale / 2, y - h / scale / 2, x + w / scale / 2, y + h / scale / 2);
        npMenu.draw(canvas, rectF);
        canvas.restore();
    }

    @Override
    public void update(int elapsedTime) {

        if (bIsOpen) {
            if (!bIsReady) {
                size += elapsedTime * 0.01f;
                if (size >= 1f) {
                    size = 1f;
                    bIsReady = true;
                    if (onOpenedListener != null)
                        onOpenedListener.onOpened();
                }
                w = (innerWidth * size + borderSize * 2);
                h = (innerHeight * size + borderSize * 2);
            }
        } else {
            if (!bIsClosed) {
                size -= elapsedTime * 0.01f;
                if (size <= 0f) {
                    size = 0f;
                    bIsClosed = true;
                    if (onClosedListener != null)
                        onClosedListener.onClosed();
                }
                w = (innerWidth * size + borderSize * 2);
                h = (innerHeight * size + borderSize * 2);
            }
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public void open(OnOpenedListener listener) {

        onOpenedListener = listener;
        bIsOpen = true;
        bIsClosed = false;
    }

    public void close(OnClosedListener listener) {

        onClosedListener = listener;
        bIsOpen = false;
        bIsReady = false;
    }

    public boolean isReady() {

        return bIsReady;
    }

    public boolean isClosed() {

        return bIsClosed;
    }

    public boolean checkTouchPoint(float touch_x, float touch_y) {
        return touch_x >= x - w / 2 && touch_x <= x + w / 2 &&
                touch_y >= y - h / 2 && touch_y <= y + h / 2;
    }

    public interface OnOpenedListener {
        void onOpened();
    }

    public interface OnClosedListener {
        void onClosed();
    }
}
