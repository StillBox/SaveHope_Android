package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.gamedata.GameSettings;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameenum.GameMode;
import com.stillbox.game.savehope.gameobject.LoadingBox;

public class SnakeScene extends GameScene {

    //Fields for loading
    private boolean bIsLoading;
    private LoadingBox loadingBox;

    //Fields and setters relating to settings
    private static int controlMode = GameSettings.controlMode_sk;
    private static int scannerAlpha = GameSettings.scannerAlpha_sk;
    private static int adjustTime = GameSettings.adjustTime_sk;

    //TODO

    public static void setControlMode(int controlMode) {
        SnakeScene.controlMode = controlMode;
    }

    public static void setScannerAlpha(int scannerAlpha) {
        SnakeScene.scannerAlpha = scannerAlpha;
    }

    public static void setAdjustTime(int adjustTime) {
        SnakeScene.adjustTime = adjustTime;
    }

    public SnakeScene(GameMode mode, GameLevel level) {
    }

    @Override
    public void init() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setColor(Color.WHITE);
        canvas.drawText("(Snake Scene) Under Construction", screen_w / 2, screen_h / 2, paint);

    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
