package com.stillbox.game.savehope.gameobject;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;

public class SingleSpriteObject extends GameObject {

    Bitmap sprite;

    public SingleSpriteObject() {

    }

    @Override
    public void onDestroy() {

        if (sprite != null) {
            sprite.recycle();
            sprite = null;
        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (scale != 1f) {
            canvas.save();
            canvas.scale(scale, scale, x, y);
            canvas.drawBitmap(sprite, x + offset_x, y + offset_y, paint);
            canvas.restore();
        } else {
            canvas.drawBitmap(sprite, x + offset_x, y + offset_y, paint);
        }
    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public void setSprite(int resid, int res_x, int res_y, int res_w, int res_h) {

        Bitmap bmpRes = MainView.getBitmap(resid);
        if (bmpRes == null) return;
        sprite = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(sprite);
        float scale_x = w / res_w;
        float scale_y = h / res_h;
        Matrix mat = new Matrix();
        mat.postTranslate(-res_x, -res_y);
        mat.postScale(scale_x, scale_y);
        canvas.drawBitmap(bmpRes, mat, null);
        bmpRes.recycle();
    }

    public void setSprite(int resid, float res_x, float res_y, float res_w, float res_h) {

        Bitmap bmpRes = MainView.getBitmap(resid);
        if (bmpRes == null) return;
        sprite = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(sprite);

        int bmpRes_w = bmpRes.getWidth();
        int bmpRes_h = bmpRes.getHeight();

        float scale_x = w / (bmpRes_w * res_w);
        float scale_y = h / (bmpRes_h * res_h);
        Matrix mat = new Matrix();
        mat.postTranslate(-bmpRes_w * res_x, -bmpRes_h * res_y);
        mat.postScale(scale_x, scale_y);
        canvas.drawBitmap(bmpRes, mat, null);

        bmpRes.recycle();
    }
}
