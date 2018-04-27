package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;

public abstract class GameScene {

    protected static float screen_w;
    protected static float screen_h;
    protected static float rate_x;
    protected static float rate_y;
    protected static float rate;

    public GameScene() {

        screen_w = MainView.mainView.getWidth();
        screen_h = MainView.mainView.getHeight();
        rate_x = screen_w / MainView.DEST_WIDTH;
        rate_y = screen_h / MainView.DEST_HEIGHT;
        rate = Math.min(rate_x, rate_y);
    }

    public abstract void init();

    public abstract void onDestroy();

    public abstract void draw(Canvas canvas, Paint paint);

    public abstract void update(long elapsedTime);

    public abstract void onTouchEvent(MotionEvent event);
}
