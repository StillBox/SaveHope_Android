package com.stillbox.game.savehope.gamemenu;

import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.RadioGroup;
import com.stillbox.game.savehope.gamecontrol.Slider;
import com.stillbox.game.savehope.gamecontrol.StaticText;
import com.stillbox.game.savehope.gamedata.GameSettings;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class SettingMenu extends GameMenu {

    private static final int TXT_SETTING = 0;
    private static final int SLD_MAIN_VOL = 10;
    private static final int SLD_BGM_VOL = 20;
    private static final int SLD_SE_VOL = 30;

    public static final int RADIO_CONTROL = 40;
    public static final int RADIO_CONTROL_BUTTON = 41;
    public static final int RADIO_CONTROL_GESTURE = 42;

    public SettingMenu() {

        menuID = MENU_SETTING;
        setRequiredState(MenuScene.MENU_STATE_SIDE);
        setRequiredWidth(512f * rate);
        setRequiredHeight(128f * rate);

        float spacing = screen_h * 2 / 11;

        float text_w = 400 * rate;
        float text_h = 64 * rate;
        float text_x = getRegionLeft() + getRequiredWidth() / 2 - text_w / 2;
        float text_y = screen_h / 2 + MenuScene.MENU_BOX_OFFSET_Y * rate - text_h / 2;
        addControl(TXT_SETTING, new StaticText(MainView.getString(R.string.game_setting), text_x, text_y, text_w, text_h));

        float slider_w = 200 * rate;
        float slider_h = 48 * rate;
        float slider_x = (screen_w - MenuScene.MENU_SIDE_SIZE * rate) * 2 / 3 - slider_w;
        float slider_y = spacing - slider_h / 2;
        text_w = 200 * rate;
        text_x = (screen_w - MenuScene.MENU_SIDE_SIZE * rate) / 3 - text_w;
        text_y = spacing - text_h / 2;
        Slider sliderMainVol = (Slider) addControl(SLD_MAIN_VOL, new Slider(MainView.getString(R.string.main_vol), text_x, text_y, text_w, text_h, slider_x, slider_y, slider_w, slider_h));
        sliderMainVol.setOnResetListener(() -> sliderMainVol.setCurrent(GameSettings.mainVolume));
        sliderMainVol.setOnValueChangeListener(() -> GameSettings.setMainVolume(sliderMainVol.getCurrent()));

        text_y += spacing;
        slider_y += spacing;
        Slider sliderBGMVol = (Slider) addControl(SLD_BGM_VOL, new Slider(MainView.getString(R.string.bgm_vol), text_x, text_y, text_w, text_h, slider_x, slider_y, slider_w, slider_h));
        sliderBGMVol.setOnResetListener(() -> sliderBGMVol.setCurrent(GameSettings.bgmVolume));
        sliderBGMVol.setOnValueChangeListener(() -> GameSettings.setBgmVolume(sliderBGMVol.getCurrent()));

        text_y += spacing;
        slider_y += spacing;
        Slider sliderSEVol = (Slider) addControl(SLD_SE_VOL, new Slider(MainView.getString(R.string.se_vol), text_x, text_y, text_w, text_h, slider_x, slider_y, slider_w, slider_h));
        sliderSEVol.setOnResetListener(() -> sliderSEVol.setCurrent(GameSettings.seVolume));
        sliderSEVol.setOnValueChangeListener(() -> GameSettings.setSeVolume(sliderSEVol.getCurrent()));

        text_y += spacing * 3 / 2;
        float button_w = 150 * rate;
        float button_h = 80 * rate;
        float button_x = (screen_w - MenuScene.MENU_SIDE_SIZE * rate) * 2 / 3 - button_w * 3 / 2;
        float button_y = text_y + text_h / 2 - button_h / 2;
        RadioGroup radioControl = (RadioGroup) addControl(RADIO_CONTROL, new RadioGroup("控制模式", text_x, text_y, text_w, text_h));
        radioControl.addButton(RADIO_CONTROL_BUTTON, "按键", button_x, button_y, button_w, button_h, text_h);
        button_x += button_w * 3 / 2;
        radioControl.addButton(RADIO_CONTROL_GESTURE, "手势", button_x, button_y, button_w, button_h, text_h);
        radioControl.setOnResetListener(() -> {
            if (GameSettings.controlMode_st == GameSettings.CONTROL_BUTTON &&
                    GameSettings.controlMode_sk == GameSettings.CONTROL_BUTTON &&
                    GameSettings.controlMode_us == GameSettings.CONTROL_BUTTON) {
                radioControl.setChecked(RADIO_CONTROL_BUTTON);
            } else if (GameSettings.controlMode_st == GameSettings.CONTROL_GESTURE &&
                    GameSettings.controlMode_sk == GameSettings.CONTROL_GESTURE &&
                    GameSettings.controlMode_us == GameSettings.CONTROL_GESTURE) {
                radioControl.setChecked(RADIO_CONTROL_GESTURE);
            } else {
                radioControl.setChecked(0);
                radioControl.setIndeterminate(RADIO_CONTROL_BUTTON);
                radioControl.setIndeterminate(RADIO_CONTROL_GESTURE);
            }
        });
        radioControl.setOnCheckedListener(() -> {
            if (radioControl.getChecked() == RADIO_CONTROL_BUTTON) {
                GameSettings.setControlMode(GameSettings.CONTROL_BUTTON);
            } else if (radioControl.getChecked() == RADIO_CONTROL_GESTURE) {
                GameSettings.setControlMode(GameSettings.CONTROL_GESTURE);
            }
        });

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
