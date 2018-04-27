package com.stillbox.game.savehope.gamemenu;

import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.Button;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class MainMenu extends GameMenu {

    private static final int BTN_START = 10;
    private static final int BTN_SETTING = 20;
    private static final int BTN_ABOUT = 30;

    public MainMenu() {

        menuID = MENU_MAIN;
        setRequiredWidth(512f * rate);
        setRequiredHeight(320f * rate);

        int buttonTextSize = (int) (64f * rate);
        float button_w = 400f * rate;
        float button_h = 80f * rate;
        float button_spacing = 96f * rate;
        float button_x, button_y;

        button_x = screen_w / 2 - button_w / 2;
        button_y = screen_h / 2 + MenuScene.MENU_BOX_OFFSET_Y * rate - button_spacing - button_h / 2;
        Button btnStart = (Button) addControl(BTN_START, new Button(MainView.getString(R.string.game_start), button_x, button_y, button_w, button_h));
        btnStart.setTextSize(buttonTextSize);
        btnStart.setOnPressedListener(() -> {
            setCurrentMenuID(MENU_SELECT);
            reset();
        });

        button_y += button_spacing;
        Button btnSetting = (Button) addControl(BTN_SETTING, new Button(MainView.getString(R.string.game_setting), button_x, button_y, button_w, button_h));
        btnSetting.setTextSize(buttonTextSize);
        btnSetting.setOnPressedListener(() -> {
            setCurrentMenuID(MENU_SETTING);
            reset();
        });

        button_y += button_spacing;
        Button btnAbout = (Button) addControl(BTN_ABOUT, new Button(MainView.getString(R.string.game_about), button_x, button_y, button_w, button_h));
        btnAbout.setTextSize(buttonTextSize);
        btnAbout.setOnPressedListener(() -> {
            setCurrentMenuID(MENU_ABOUT);
            reset();
        });
    }

    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (checkTouchPoint(touch_x, touch_y)) {
                setCurrentMenuID(MENU_TITLE);
                GameSound.playSE(GameSound.ID_SE_CANCEL);
                return;
            }
        }

        super.onTouchEvent(event);
    }

    private boolean checkTouchPoint(float touch_x, float touch_y) {

        return touch_x < getRegionLeft() || touch_x > getRegionRight() ||
                touch_y < getRegionTop() || touch_y > getRegionBottom();
    }
}
