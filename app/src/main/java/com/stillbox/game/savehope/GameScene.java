package com.stillbox.game.savehope;

import android.graphics.Canvas;

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
}
