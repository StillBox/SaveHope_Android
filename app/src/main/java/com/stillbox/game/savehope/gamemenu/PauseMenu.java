package com.stillbox.game.savehope.gamemenu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.gamecontrol.Button;
import com.stillbox.game.savehope.gameobject.Border;

public class PauseMenu extends GameMenu {

    public static final int BTN_CONTINUE = 10;
    public static final int BTN_RETRY = 20;
    public static final int BTN_SETTING = 30;
    public static final int BTN_MENU = 40;

    private Bitmap bmpBackground;
    private Border menuBorder;

    public PauseMenu() {

        menuID = MENU_PAUSE;
        setRequiredWidth(416f * rate);
        setRequiredHeight(448f * rate);

        bmpBackground = null;
        menuBorder = new Border(screen_w / 2, screen_h / 2, getRequiredWidth(), getRequiredHeight(), rate);

        int buttonTextSize = (int) (64f * rate);
        float button_w = 288f * rate;
        float button_h = 80f * rate;
        float button_spacing = 96f * rate;
        float button_x, button_y;

        button_x = screen_w / 2 - button_w / 2;
        button_y = screen_h / 2 - button_spacing * 3 / 2 - button_h / 2;
        Button btnContinue = (Button) addControl(BTN_CONTINUE, new Button("继续游戏", button_x, button_y, button_w, button_h));
        btnContinue.setTextSize(buttonTextSize);

        button_y += button_spacing;
        Button btnRetry = (Button) addControl(BTN_RETRY, new Button("重新开始", button_x, button_y, button_w, button_h));
        btnRetry.setTextSize(buttonTextSize);

        button_y += button_spacing;
        Button btnSetting = (Button) addControl(BTN_SETTING, new Button("游戏设定", button_x, button_y, button_w, button_h));
        btnSetting.setTextSize(buttonTextSize);

        button_y += button_spacing;
        Button btnMenu = (Button) addControl(BTN_MENU, new Button("返回菜单", button_x, button_y, button_w, button_h));
        btnMenu.setTextSize(buttonTextSize);
    }

    @Override
    public void onDestroy() {

        menuBorder.onDestroy();
        super.onDestroy();
    }

    @Override
    public void reset() {
        super.reset();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (bmpBackground != null) {
            canvas.drawBitmap(bmpBackground, 0f, 0f, null);
            canvas.drawColor(0x7F000000);
        }
        menuBorder.draw(canvas, paint);
        if (menuBorder.isReady()) {
            super.draw(canvas, paint);
        }
    }

    @Override
    public void update(int elapsedTime) {

        menuBorder.update(elapsedTime);
        super.update(elapsedTime);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (menuBorder.isReady())
            super.onTouchEvent(event);
    }

    public void setBackground(Bitmap bmp) {
        bmpBackground = bmp;
    }

    public void open(Border.OnOpenedListener listener) {
        menuBorder.open(listener);
    }

    public void close(Border.OnClosedListener listener) {
        reset();
        menuBorder.close(listener);
    }
}
