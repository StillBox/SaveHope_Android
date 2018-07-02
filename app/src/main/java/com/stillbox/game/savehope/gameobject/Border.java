package com.stillbox.game.savehope.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;

import java.util.ArrayList;

public class Border extends GameObject {

    public enum Anchor {
        LEFT_TOP,
        LEFT_CENTER,
        LEFT_BOTTOM,
        CENTER_TOP,
        CENTER,
        CENTER_BOTTOM,
        RIGHT_TOP,
        RIGHT_CENTER,
        RIGHT_BOTTOM
    }

    private Anchor anchor = Anchor.CENTER;

    private enum Action {
        OPEN,
        CLOSE,
        RESIZE
    }

    ArrayList<Action> actions;

    public static final float BORDER_SIZE = 64f;
    public static final int DEFAULT_DURATION = 100;
    private float borderSize;
    private float innerWidth;
    private float innerHeight;
    private float deltaWidth;
    private float deltaHeight;
    private float updateTime;

    private boolean bIsReady;
    private boolean bIsClosed;
    private OnOpenedListener onOpenedListener = null;
    private OnClosedListener onClosedListener = null;

    private float scale;
    private Bitmap bmpMenu;
    private NinePatch npMenu;

    public Border(float x, float y, float w, float h, float rate) {

        setPosition(x, y);
        setSize(0f, 0f);

        borderSize = BORDER_SIZE * rate;
        innerWidth = w - 2 * borderSize;
        innerHeight = h - 2 * borderSize;
        deltaWidth = innerWidth;
        deltaHeight = innerHeight;
        updateTime = 0;

        bIsReady = false;
        bIsClosed = true;
        open(null);

        bmpMenu = MainView.getBitmap(R.drawable.border);
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

        if (isClosed()) return;

        canvas.save();
        canvas.scale(scale, scale, x, y);

        float left, top, right, bottom;

        switch (anchor) {
            case LEFT_TOP:
            case LEFT_CENTER:
            case LEFT_BOTTOM:
                left = x;
                right = x + w / scale;
                break;
            case RIGHT_TOP:
            case RIGHT_CENTER:
            case RIGHT_BOTTOM:
                left = x - w / scale;
                right = x;
                break;
            case CENTER_TOP:
            case CENTER:
            case CENTER_BOTTOM:
            default:
                left = x - w / scale / 2;
                right = x + w / scale / 2;
                break;
        }

        switch (anchor) {
            case LEFT_TOP:
            case CENTER_TOP:
            case RIGHT_TOP:
                top = y;
                bottom = y + h / scale;
                break;
            case LEFT_BOTTOM:
            case CENTER_BOTTOM:
            case RIGHT_BOTTOM:
                top = y - h / scale;
                bottom = y;
                break;
            case LEFT_CENTER:
            case CENTER:
            case RIGHT_CENTER:
            default:
                top = y - h / scale / 2;
                bottom = y + h / scale / 2;
                break;
        }

        RectF rectF = new RectF(left, top, right, bottom);
        npMenu.draw(canvas, rectF);
        canvas.restore();
    }

    @Override
    public void update(int elapsedTime) {

        if (actions.isEmpty()) return;

        Action currentAction = actions.get(0);

        switch (currentAction) {
            case OPEN:
                if (!bIsReady) {
                    updateTime += elapsedTime;
                    if (updateTime >= DEFAULT_DURATION) {
                        updateTime = 0;
                        w = innerWidth + borderSize * 2;
                        h = innerHeight + borderSize * 2;
                        bIsReady = true;
                        actions.remove(currentAction);
                        if (onOpenedListener != null) {
                            onOpenedListener.onOpened();
                        }
                    } else {
                        w = deltaWidth * updateTime / DEFAULT_DURATION + borderSize * 2;
                        h = deltaHeight * updateTime / DEFAULT_DURATION + borderSize * 2;
                    }
                }
                break;

            case CLOSE:
                if (!bIsClosed) {
                    updateTime += elapsedTime;
                    if (updateTime > DEFAULT_DURATION) {
                        updateTime = 0;
                        w = borderSize * 2;
                        h = borderSize * 2;
                        deltaWidth = innerWidth;
                        deltaHeight = innerHeight;
                        bIsClosed = true;
                        actions.remove(currentAction);
                        if (onClosedListener != null) {
                            onClosedListener.onClosed();
                        }
                    } else {
                        w = deltaWidth - deltaWidth * updateTime / DEFAULT_DURATION + borderSize * 2;
                        h = deltaHeight - deltaHeight * updateTime / DEFAULT_DURATION + borderSize * 2;
                    }
                }
                break;

            case RESIZE:
                if (!bIsReady) {
                    updateTime += elapsedTime;
                    if (updateTime > DEFAULT_DURATION) {
                        updateTime = 0;
                        w = innerWidth + borderSize * 2;
                        h = innerHeight + borderSize * 2;
                        deltaWidth = innerWidth;
                        deltaHeight = innerHeight;
                        bIsReady = true;
                        actions.remove(currentAction);
                        if (onOpenedListener != null) {
                            onOpenedListener.onOpened();
                        }
                    }
                } else {
                    w = innerWidth - deltaWidth + deltaWidth * updateTime / DEFAULT_DURATION + borderSize * 2;
                    h = innerHeight - deltaHeight + deltaHeight * updateTime / DEFAULT_DURATION + borderSize * 2;
                }
                break;
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public void setAnchor(Anchor anchor) {

        this.anchor = anchor;
    }

    public void open(OnOpenedListener listener) {

        actions.add(Action.OPEN);
        onOpenedListener = listener;
        bIsClosed = false;
    }

    public void close(OnClosedListener listener) {

        actions.add(Action.CLOSE);
        onClosedListener = listener;
        bIsReady = false;
    }

    public void resize(float w, float h, boolean bClose, OnOpenedListener listener) {

        if (bClose) {
            actions.add(Action.CLOSE);
            actions.add(Action.OPEN);
        } else {
            actions.add(Action.RESIZE);
            deltaWidth = w - borderSize * 2 - innerWidth;
            deltaHeight = h - borderSize * 2 - innerHeight;
            innerWidth = w - borderSize * 2;
            innerHeight = h - borderSize * 2;
        }
        onOpenedListener = listener;
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
