package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.gameobject.LoadingBox;
import com.stillbox.game.savehope.gameobject.TextBox;

public class StoryScene extends GameScene {

    private TextBox textBox;

    //TODO story scene

    public StoryScene() {

        MainView.setLoadingProgress(1, 0);
    }

    @Override
    public void init() {

        Thread thread = new Thread(() -> {

            textBox = new TextBox();
            textBox.setText("这是一条测试。\n这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。这是一条测试。");

            MainView.endLoadingProgress();
        });

        thread.start();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        textBox.draw(canvas, paint);
    }

    @Override
    public void update(int elapsedTime) {

        textBox.update(elapsedTime);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        textBox.onTouchEvent(event);
    }

    private class Character {
    }

    private class IntroBackground {
    }
}
