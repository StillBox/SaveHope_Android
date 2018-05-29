package com.stillbox.game.savehope.gamemenu;

import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.Button;
import com.stillbox.game.savehope.gameenum.GameMode;
import com.stillbox.game.savehope.gameenum.SceneTitle;
import com.stillbox.game.savehope.gameobject.dialog.DialogBox;
import com.stillbox.game.savehope.gameobject.dialog.LevelSelectDialog;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class FreeMenu extends GameMenu {

    private static final int BTN_SHOOTER = 10;
    private static final int BTN_SNAKE = 20;
    private static final int BTN_UPSTAIRS = 30;
    private static final int BTN_INFINITE = 40;

    public FreeMenu() {

        menuID = MENU_FREE;
        setRequiredWidth(512f * rate);
        setRequiredHeight(416f * rate);

        int buttonTextSize = (int) (64f * rate);
        float button_w = 400f * rate;
        float button_h = 80f * rate;
        float button_spacing = 96f * rate;
        float button_x, button_y;

        button_x = screen_w / 2 - button_w / 2;
        button_y = screen_h / 2 + MenuScene.MENU_BOX_OFFSET_Y * rate - button_spacing * 3 / 2 - button_h / 2;
        Button btnShooter = (Button) addControl(BTN_SHOOTER, new Button(MainView.getString(R.string.shooter), button_x, button_y, button_w, button_h));
        btnShooter.setTextSize(buttonTextSize);
        btnShooter.setOnPressedListener(() -> {
            MainView.addDialog(new LevelSelectDialog(SceneTitle.SHOOTER, GameMode.FREE));
            reset();
        });

        button_y += button_spacing;
        Button btnSnake = (Button) addControl(BTN_SNAKE, new Button(MainView.getString(R.string.snake), button_x, button_y, button_w, button_h));
        btnSnake.setTextSize(buttonTextSize);

        button_y += button_spacing;
        Button btnUpstairs = (Button) addControl(BTN_UPSTAIRS, new Button(MainView.getString(R.string.upstairs), button_x, button_y, button_w, button_h));
        btnUpstairs.setTextSize(buttonTextSize);

        button_y += button_spacing;
        Button btnInfinite = (Button) addControl(BTN_INFINITE, new Button(MainView.getString(R.string.upstairs_infinite), button_x, button_y, button_w, button_h));
        btnInfinite.setTextSize(buttonTextSize);
    }

    public void onTouchEvent(MotionEvent event) {

        float touch_x = event.getX();
        float touch_y = event.getY();

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (checkTouchPoint(touch_x, touch_y)) {
                setCurrentMenuID(MENU_SELECT);
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
