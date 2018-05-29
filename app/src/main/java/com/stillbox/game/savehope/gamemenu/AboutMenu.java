package com.stillbox.game.savehope.gamemenu;

import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.StaticText;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class AboutMenu extends GameMenu {

    private static final int TXT_ABOUT = 0;
    private static final int TXT_LINE1 = 10;
    private static final int TXT_LINE2 = 20;
    private static final int TXT_LINE3 = 30;

    public AboutMenu() {

        menuID = MENU_ABOUT;
        setRequiredState(MenuScene.MENU_STATE_SIDE);
        setRequiredWidth(512f * rate);
        setRequiredHeight(128f * rate);

        float text_w = 400 * rate;
        float text_h = 64 * rate;
        float text_x = getRegionLeft() + getRequiredWidth() / 2 - text_w / 2;
        float text_y = screen_h / 2 + MenuScene.MENU_BOX_OFFSET_Y * rate - text_h / 2;
        addControl(TXT_ABOUT, new StaticText(MainView.getString(R.string.game_about), text_x, text_y, text_w, text_h));

        text_x = (screen_w - MenuScene.MENU_SIDE_SIZE * rate) / 6;
        text_w = (screen_w - MenuScene.MENU_SIDE_SIZE * rate) / 2;
        text_y = screen_h / 5;
        text_h = 48f * rate;
        StaticText textLine1 = (StaticText) addControl(TXT_LINE1, new StaticText("本应用仅供学习交流使用", text_x, text_y, text_w, text_h));
        textLine1.setTextAlign(Paint.Align.LEFT);

        text_y += screen_h / 5;
        StaticText textLine2 = (StaticText) addControl(TXT_LINE2, new StaticText("非原创素材版权均归原制作组所有", text_x, text_y, text_w, text_h));
        textLine2.setTextAlign(Paint.Align.LEFT);

        text_y += screen_h / 4;
        StaticText textLine3 = (StaticText) addControl(TXT_LINE3, new StaticText("制作者 —— 箱子", text_x, text_y, text_w, text_h));
        textLine3.setTextAlign(Paint.Align.LEFT);
    }

    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (checkTouchPoint(touch_x, touch_y)) {
                setCurrentMenuID(MENU_MAIN);
                GameSound.playSE(GameSound.ID_SE_CANCEL);
            }
        }
    }

    private boolean checkTouchPoint(float touch_x, float touch_y) {

        return touch_x > screen_w - MenuScene.MENU_SIDE_SIZE * rate &&
                (touch_x > getRegionRight() || touch_y < getRegionTop() || touch_y > getRegionBottom());
    }
}
