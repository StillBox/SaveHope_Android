package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.gamecontrol.TextButton;

public class ShopScene extends GameScene {

    //Fields for states and etc...
    private static final int STATE_CHARA_LIST = 10;
    private static final int STATE_CHARA_DETAIL = 20;
    private int state;

    private int listBegCharaId;
    private int listCharaCount;

    private int charaID;

    //Fields for objects

    private TextButton btnReturn;

    //TODO shop scene

    public ShopScene() {

        MainView.setLoadingProgress(1, 0);
    }

    @Override
    public void init() {

        Thread thread = new Thread(() -> {

        });

        thread.start();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setColor(Color.WHITE);
        canvas.drawText("(Shop Scene) Under Construction", screen_w / 2, screen_h / 2, paint);
    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
