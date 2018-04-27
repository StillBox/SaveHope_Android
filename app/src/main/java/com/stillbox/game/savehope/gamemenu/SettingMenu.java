package com.stillbox.game.savehope.gamemenu;

import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.Slider;
import com.stillbox.game.savehope.gamecontrol.TextBox;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class SettingMenu extends GameMenu {

    private static final int TXT_SETTING = 0;
    private static final int SLD_MAIN_VOL = 10;
    private static final int SLD_BGM_VOL = 20;
    private static final int SLD_SE_VOL = 30;

    public SettingMenu() {

        menuID = MENU_SETTING;
        setRequiredState(MenuScene.MENU_STATE_SIDE);
        setRequiredWidth(512f * rate);
        setRequiredHeight(128f * rate);

        float text_w = 400 * rate;
        float text_h = 64 * rate;
        float text_x = getRegionLeft() + getRequiredWidth() / 2 - text_w / 2;
        float text_y = screen_h / 2 + MenuScene.MENU_BOX_OFFSET_Y * rate - text_h / 2;
        addControl(TXT_SETTING, new TextBox(MainView.getString(R.string.game_setting), text_x, text_y, text_w, text_h));

        float slider_w = 200 * rate;
        float slider_h = 48 * rate;
        float slider_x = (screen_w - MenuScene.MENU_SIDE_SIZE * rate) * 2 / 3 - slider_w;
        float slider_y = screen_h / 5 - slider_h / 2;
        text_w = 200 * rate;
        text_x = (screen_w - MenuScene.MENU_SIDE_SIZE * rate) / 3 - text_w;
        text_y = screen_h / 5 - text_h / 2;
        Slider sliderMainVol = (Slider) addControl(SLD_MAIN_VOL, new Slider(MainView.getString(R.string.main_vol), text_x, text_y, text_w, text_h, slider_x, slider_y, slider_w, slider_h));
        sliderMainVol.setOnResetListener(() -> sliderMainVol.setCurrent((int) (GameSound.getMainVolume() * 10)));
        sliderMainVol.setOnValueChangeListener(() -> GameSound.setMainVolume(0.1f * sliderMainVol.getCurrent()));

        text_y += screen_h / 5;
        slider_y += screen_h / 5;
        Slider sliderBGMVol = (Slider) addControl(SLD_BGM_VOL, new Slider(MainView.getString(R.string.bgm_vol), text_x, text_y, text_w, text_h, slider_x, slider_y, slider_w, slider_h));
        sliderBGMVol.setOnResetListener(() -> sliderBGMVol.setCurrent((int) (GameSound.getBgmVolume() * 10)));
        sliderBGMVol.setOnValueChangeListener(() -> GameSound.setBgmVolume(0.1f * sliderBGMVol.getCurrent()));

        text_y += screen_h / 5;
        slider_y += screen_h / 5;
        Slider sliderSEVol = (Slider) addControl(SLD_SE_VOL, new Slider(MainView.getString(R.string.se_vol), text_x, text_y, text_w, text_h, slider_x, slider_y, slider_w, slider_h));
        sliderSEVol.setOnResetListener(() -> sliderSEVol.setCurrent((int) (GameSound.getSeVolume() * 10)));
        sliderSEVol.setOnValueChangeListener(() -> GameSound.setSeVolume(0.1f * sliderSEVol.getCurrent()));

        reset();
    }

    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (checkTouchPoint(touch_x, touch_y)) {
                setCurrentMenuID(MENU_MAIN);
                GameSound.playSE(GameSound.ID_SE_CANCEL);
                return;
            }
        }

        super.onTouchEvent(event);
    }

    private boolean checkTouchPoint(float touch_x, float touch_y) {

        return touch_x > screen_w - MenuScene.MENU_SIDE_SIZE * rate &&
                (touch_x > getRegionRight() || touch_y < getRegionTop() || touch_y > getRegionBottom());
    }
}
