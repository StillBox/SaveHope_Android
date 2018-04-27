package com.stillbox.game.savehope.gamemenu;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.TextBox;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class TitleMenu extends GameMenu {

    private static final int TXT_TITLE = 0;

    private int updateTime = 0;
    private static final int PERIOD = 800;

    public TitleMenu() {

        menuID = MENU_TITLE;
        setRequiredWidth(512f * rate);
        setRequiredHeight(128f * rate);

        float button_w = 400 * rate;
        float button_h = 64 * rate;
        float button_x = screen_w / 2 - button_w / 2;
        float button_y = screen_h / 2 + MenuScene.MENU_BOX_OFFSET_Y * rate - button_h / 2;
        addControl(TXT_TITLE, new TextBox(MainView.getString(R.string.touch_screen), button_x, button_y, button_w, button_h));
    }

    public void reset() {

        updateTime = 0;
    }

    public void draw(Canvas canvas, Paint paint) {

        if (updateTime >= 0)
            super.draw(canvas, paint);
    }

    public void update(long elapsedTime) {

        updateTime += elapsedTime;
        if (updateTime >= PERIOD)
            updateTime -= 2 * PERIOD;
    }

    public void onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_UP) {
            setCurrentMenuID(MENU_MAIN);
            GameSound.playSE(GameSound.ID_SE_DECIDE);
            reset();
        }
    }
}
