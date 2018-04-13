package com.stillbox.game.savehope;

import android.graphics.Canvas;

public abstract class GameObject {

    protected float x, y, w, h;

    public abstract void draw(Canvas canvas);

    public abstract void update(long elapsedTime);
}
