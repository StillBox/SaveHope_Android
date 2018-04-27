package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gameobject.LoadingBox;
import com.stillbox.game.savehope.gamesound.GameSound;

public class ShooterScene extends GameScene {

    //Fields for loading
    private boolean bIsLoading;
    private LoadingBox loadingBox;

    //Fields for game field grid
    private static final int ROWS_MIN = 15;
    private static final int COLS_MIN = 30;

    //Fields for game mode
    private boolean bIsStoryMode;

    //Fields for sounds
    private static final int ID_BGM = R.raw.ekorosia;

    public ShooterScene() {

        bIsLoading = true;
        loadingBox = new LoadingBox();
    }

    @Override
    public void init() {

        loadingBox.setMaxProgress(1);
        loadingBox.setCurrentProgress(0);

        Thread thread = new Thread(() -> {

            GameSound.createBGM(0, ID_BGM, true);
            GameSound.startBGM(0);
            loadingBox.increaseProgress(1);

            bIsLoading = false;
        });

        thread.start();
    }

    @Override
    public void onDestroy() {

        GameSound.releaseBGM();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (bIsLoading) {
            loadingBox.draw(canvas, paint);
            return;
        }

        paint.setColor(Color.WHITE);
        canvas.drawText("Test", screen_w / 2, screen_h / 2, paint);
    }

    @Override
    public void update(long elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
