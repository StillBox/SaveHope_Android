package com.stillbox.game.savehope.gameobject;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.gamecontrol.TextButton;
import com.stillbox.game.savehope.gameenum.StoryChapter;

public class Tutorial extends GameObject {

    TextBox textBox;
    TextButton btnSkip;

    String[] texts;
    int currentIndex;

    public Tutorial(StoryChapter chapter) {

        switch (chapter) {
            case SHOOTER:
                break;
            case SNAKE:
                break;
            case UPSTAIRS:
                break;
        }

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
