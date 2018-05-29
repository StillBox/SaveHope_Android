package com.stillbox.game.savehope.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;

public abstract class SpriteObject extends GameObject {

    //State loop type
    public static final int STATE_SINGLE = 0;
    public static final int STATE_NO_LOOP = 1;
    public static final int STATE_LOOP = 2;

    private Bitmap bmpRes;
    protected SparseArray<Bitmap> sprites;
    protected SparseArray<StateData> states;
    protected int stateTime;
    protected int currentState;
    protected int currentSprite;

    public SpriteObject() {

        sprites = new SparseArray<>();
        states = new SparseArray<>();

        stateTime = 0;
        currentState = -1;
        currentSprite = -1;
    }

    @Override
    public void onDestroy() {

        releaseResource();
        for (int i = 0; i < sprites.size(); i++)
            sprites.valueAt(i).recycle();
        sprites.clear();
        states.clear();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        canvas.drawBitmap(sprites.get(currentSprite), x + offset_x, y + offset_y, paint);
    }

    @Override
    public void update(int elapsedTime) {

        StateData state = states.get(currentState);
        if (state == null || state.loopType == STATE_SINGLE) return;

        stateTime += elapsedTime;
        if (stateTime >= state.gapTime) {
            if (!state.bReverse) {
                if (state.currentIndex == state.arrSprite.length - 1) {
                    if (state.loopType == STATE_LOOP) {
                        state.currentIndex = 0;
                    } else if (state.endState != -1) {
                        state.bReverse = true;
                        currentState = state.endState;
                    }
                } else
                    state.currentIndex++;
            } else {
                if (state.currentIndex == 0) {
                    if (state.loopType == STATE_LOOP) {
                        state.currentIndex = state.arrSprite.length - 1;
                    } else if (state.begState != -1) {
                        state.bReverse = false;
                        currentState = state.begState;
                    }
                }
            }
            state = states.get(currentState);
            currentSprite = state.arrSprite[state.currentIndex];
            stateTime -= state.gapTime;
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public final void loadResource(int resid) {

        bmpRes = MainView.getBitmap(resid);
    }

    public final void addSprite(int id, int res_x, int res_y, int res_w, int res_h) {

        if (bmpRes == null) return;
        Bitmap bmpSprite = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpSprite);
        float scale_x = w / res_w;
        float scale_y = h / res_h;
        Matrix mat = new Matrix();
        mat.postTranslate(-res_x, -res_y);
        mat.postScale(scale_x, scale_y);
        canvas.drawBitmap(bmpRes, mat, null);

        sprites.put(id, bmpSprite);
    }

    public final void addSprite(int id, float res_x, float res_y, float res_w, float res_h) {

        if (bmpRes == null) return;
        Bitmap bmpSprite = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpSprite);

        int bmpRes_w = bmpRes.getWidth();
        int bmpRes_h = bmpRes.getHeight();

        float scale_x = w / (bmpRes_w * res_w);
        float scale_y = h / (bmpRes_h * res_h);
        Matrix mat = new Matrix();
        mat.postTranslate(-bmpRes_w * res_x, -bmpRes_h * res_y);
        mat.postScale(scale_x, scale_y);
        canvas.drawBitmap(bmpRes, mat, null);

        sprites.put(id, bmpSprite);
    }

    public final void releaseResource() {

        if (bmpRes != null) {
            bmpRes.recycle();
            bmpRes = null;
        }
    }

    public final void addState(int id, int loopType, int gapTime, int ...spriteIds) {

        StateData state = new StateData();
        state.stateId = id;
        state.loopType = loopType;
        state.begState = -1;
        state.endState = -1;
        state.gapTime = gapTime;
        state.arrSprite = new int[spriteIds.length];
        System.arraycopy(spriteIds, 0, state.arrSprite, 0, spriteIds.length);
        state.currentIndex = 0;
        state.bReverse = false;

        states.put(id, state);
    }

    public final void addStateChain(int stateId, int begState, int endState) {

        StateData state = states.get(stateId);
        state.begState = begState;
        state.endState = endState;
    }

    public final void setState(int id) {
        StateData state = states.get(currentState);
        if (state != null && state.loopType == STATE_LOOP) {
            state.currentIndex = 0;
        }
        currentState = id;
        state = states.get(id);
        currentSprite = state.arrSprite[state.currentIndex];
        stateTime = 0;
    }

    class StateData {

        int stateId;
        int loopType;
        int begState, endState;

        int gapTime;
        int[] arrSprite;
        int currentIndex;
        boolean bReverse;
    }
}
