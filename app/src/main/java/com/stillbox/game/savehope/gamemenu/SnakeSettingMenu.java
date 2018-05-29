package com.stillbox.game.savehope.gamemenu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.RadioGroup;
import com.stillbox.game.savehope.gamecontrol.Slider;
import com.stillbox.game.savehope.gamedata.GameSettings;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class SnakeSettingMenu extends GameMenu {

    private static final int SLD_MAIN_VOL = 10;
    private static final int SLD_BGM_VOL = 20;
    private static final int SLD_SE_VOL = 30;

    public static final int RADIO_CONTROL = 40;
    public static final int RADIO_CONTROL_BUTTON = 41;
    public static final int RADIO_CONTROL_GESTURE = 42;

    public static final int SLD_SCANNER_ALPHA = 100;
    public static final int SLD_ADJUST_TIME = 200;

    private float boardW;
    private Logo logo;
    private OnClosedListener listener;

    public SnakeSettingMenu() {

        menuID = MENU_SETTING_SNAKE;

        boardW = screen_w - MenuScene.MENU_SIDE_SIZE * rate;
        logo = new Logo();

        float spacing = screen_h / 8;
        float slider_w = 200 * rate;
        float slider_h = 48 * rate;
        float slider_x = boardW * 2 / 3 - slider_w;
        float slider_y = spacing - slider_h / 2;
        float text_w = 200 * rate;
        float text_h = 64 * rate;
        float text_x = boardW / 3 - text_w;
        float text_y = spacing - text_h / 2;
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
        float button_x = boardW * 2 / 3 - button_w * 3 / 2;
        float button_y = text_y + text_h / 2 - button_h / 2;
        RadioGroup radioControl = (RadioGroup) addControl(RADIO_CONTROL, new RadioGroup("控制模式", text_x, text_y, text_w, text_h));
        radioControl.addButton(RADIO_CONTROL_BUTTON, "按键", button_x, button_y, button_w, button_h, text_h);
        button_x += button_w * 3 / 2;
        radioControl.addButton(RADIO_CONTROL_GESTURE, "手势", button_x, button_y, button_w, button_h, text_h);
        radioControl.setOnResetListener(() -> {
            if (GameSettings.controlMode_sk == GameSettings.CONTROL_BUTTON) {
                radioControl.setChecked(RADIO_CONTROL_BUTTON);
            } else if (GameSettings.controlMode_sk == GameSettings.CONTROL_GESTURE) {
                radioControl.setChecked(RADIO_CONTROL_GESTURE);
            } else {
                radioControl.setChecked(0);
            }
        });
        radioControl.setOnCheckedListener(() -> {
            if (radioControl.getChecked() == RADIO_CONTROL_BUTTON) {
                GameSettings.setControlMode_sk(GameSettings.CONTROL_BUTTON);
            } else if (radioControl.getChecked() == RADIO_CONTROL_GESTURE) {
                GameSettings.setControlMode_sk(GameSettings.CONTROL_GESTURE);
            }
        });

        text_y += spacing * 3 / 2;
        slider_y += spacing * 3;
        Slider sliderAlpha = (Slider) addControl(SLD_SCANNER_ALPHA, new Slider("透明度", text_x, text_y, text_w, text_h, slider_x, slider_y, slider_w, slider_h));
        sliderAlpha.setMax(255);
        sliderAlpha.setCount(17);
        sliderAlpha.setOnResetListener(() -> sliderAlpha.setCurrent(GameSettings.scannerAlpha_sk));
        sliderAlpha.setOnValueChangeListener(() -> GameSettings.setScannerAlpha_sk(sliderAlpha.getCurrent()));

        text_y += spacing;
        slider_y += spacing;
        Slider sliderAdjust = (Slider) addControl(SLD_ADJUST_TIME, new Slider("节奏调整", text_x, text_y, text_w, text_h, slider_x, slider_y, slider_w, slider_h));
        sliderAdjust.setRange(-128, 128);
        sliderAdjust.setCount(16);
        sliderAdjust.setOnResetListener(() -> sliderAdjust.setCurrent(GameSettings.adjustTime_sk));
        sliderAdjust.setOnValueChangeListener(() -> GameSettings.setAdjustTime_sk(sliderAdjust.getCurrent()));

        reset();
    }

    @Override
    public void onDestroy() {
        logo.onDestroy();
        super.onDestroy();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        canvas.drawColor(Color.WHITE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(boardW, 0f, screen_w, screen_h, paint);
        logo.draw(canvas, paint);

        super.draw(canvas, paint);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (checkTouchPoint(touch_x)) {
                if (listener != null)
                    listener.onClosed();
                GameSound.playSE(GameSound.ID_SE_CANCEL);
                return;
            }
        }

        super.onTouchEvent(event);
    }

    private boolean checkTouchPoint(float touch_x) {

        return touch_x > boardW;
    }

    private class Logo {

        private static final float LOGO_OFFSET_X = 360f;
        private static final float LOGO_OFFSET_Y = 0f;
        private static final float LOGO_SIZE = 512f;
        private float x, y, w;
        private Bitmap bmpLogo;

        Logo() {

            x = boardW + LOGO_OFFSET_X * rate;
            y = screen_h / 3 + LOGO_OFFSET_Y * rate;
            w = LOGO_SIZE * rate;

            Bitmap bmpRes = MainView.getBitmap(R.drawable.logo);
            int width = bmpRes.getWidth();
            int height = bmpRes.getHeight();
            float scaleRes = w / width;
            Matrix mat = new Matrix();
            mat.postScale(scaleRes, scaleRes);
            if (mat.isIdentity())
                bmpLogo = bmpRes.copy(Bitmap.Config.ARGB_8888, true);
            else
                bmpLogo = Bitmap.createBitmap(bmpRes, 0, 0, width, height, mat, true);
            bmpRes.recycle();
        }

        void onDestroy() {

            bmpLogo.recycle();
            bmpLogo = null;
        }

        void draw(Canvas canvas, Paint paint) {

            canvas.drawBitmap(bmpLogo, x - w / 2, y - w / 2, paint);
        }
    }

    public interface OnClosedListener {

        void onClosed();
    }

    public void setOnClosedListener(OnClosedListener listener) {

        this.listener = listener;
    }
}
