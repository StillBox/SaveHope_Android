package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.GameControl;
import com.stillbox.game.savehope.gamecontrol.TextButton;
import com.stillbox.game.savehope.gameenum.StoryChapter;
import com.stillbox.game.savehope.gamesound.GameSound;

public class Tutorial extends GameObject {

    private static final int BGM_UNDERGROUND = R.raw.underground;

    private String[] scripts;
    private TextBox textBox;
    private int currentIndex;
    private TextButton btnSkip;

    private boolean bIsReadyForNext;
    private boolean bIsReadyToClose;
    private boolean bIsTutorialOver;

    private boolean bIsClosed;

    public Tutorial(StoryChapter chapter) {

        switch (chapter) {
            case SHOOTER:
                scripts = MainView.resources.getStringArray(R.array.script_t0);
                break;
            case SNAKE:
                scripts = MainView.resources.getStringArray(R.array.script_t1);
                break;
            case UPSTAIRS:
                scripts = MainView.resources.getStringArray(R.array.script_t2);
                break;
        }
        textBox = new TextBox();
        textBox.setText(scripts[0]);
        currentIndex = 0;

        float button_w = 320 * MainView.rate;
        float button_h = 128 * MainView.rate;
        btnSkip = new TextButton("跳过说明", MainView.w - button_w, 0, button_w, button_h);
        btnSkip.setTextSize((int) (64f * MainView.rate));
        btnSkip.setOnPressedListener(() -> bIsReadyToClose = true);

        bIsReadyForNext = false;
        bIsReadyToClose = false;
        bIsTutorialOver = false;

        bIsClosed = false;

        GameSound.createBGM(BGM_UNDERGROUND, BGM_UNDERGROUND, true);
    }

    @Override
    public void onDestroy() {

        GameObject.release(textBox);
        GameControl.release(btnSkip);
        GameSound.releaseBGM(BGM_UNDERGROUND);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        btnSkip.draw(canvas, paint);
        textBox.draw(canvas, paint);
    }

    @Override
    public void update(int elapsedTime) {

        if (!GameSound.isBgmPlaying()) {
            GameSound.startBGM(BGM_UNDERGROUND);
        }

        btnSkip.update(elapsedTime);
        textBox.update(elapsedTime);

        if (bIsReadyForNext) {
            currentIndex++;
            bIsReadyForNext = false;
            if (currentIndex == scripts.length) {
                bIsReadyToClose = true;
            } else {
                textBox.setText(scripts[currentIndex]);
            }
        }

        if (bIsReadyToClose) {
            bIsReadyToClose = false;
            bIsTutorialOver = true;
            GameSound.fadeOut(100);
            textBox.close(() -> {
                bIsClosed = true;
                GameSound.stopBGM();
            });
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (bIsTutorialOver) return;

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_DOWN && !textBox.checkTouchPoint(touch_x, touch_y)) {
            if (textBox.isTextOver()) {
                GameSound.playSE(GameSound.ID_SE_TEXT);
                bIsReadyForNext = true;
            } else {
                textBox.accelerate(16f);
            }
        } else {
            btnSkip.onTouchEvent(event);
        }
    }

    public boolean isOver() {

        return bIsClosed;
    }
}
