package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;

public abstract class GameScene {

    protected float screenW;
    protected float screenH;

    public GameScene() {

        screenW = MainView.mainView.getWidth();
        screenH = MainView.mainView.getHeight();
    }

    public abstract void init();

    public abstract void draw(Canvas canvas);

    public abstract void update(long elapsedTime);

    public abstract void onTouchEvent(MotionEvent event);
}
