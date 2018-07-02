package com.stillbox.game.savehope.gamemenu;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.gamecontrol.GameControl;
import com.stillbox.game.savehope.gamescene.GameScene;
import com.stillbox.game.savehope.gamescene.MenuScene;

public abstract class GameMenu {

    public static final int MENU_TITLE = 0;
    public static final int MENU_MAIN = 1;
    public static final int MENU_SELECT = 10;
    public static final int MENU_SETTING = 20;
    public static final int MENU_ABOUT = 30;
    public static final int MENU_FREE = 100;
    public static final int MENU_PAUSE = 1000;
    public static final int MENU_SETTING_SHOOTER = 1100;
    public static final int MENU_SETTING_SNAKE = 1200;
    public static final int MENU_SETTING_UPSTAIRS = 1300;

    private static int currentMenuID;

    static float screen_w;
    static float screen_h;
    static float rate_x;
    static float rate_y;
    static float rate;

    public int menuID;
    private GameScene parentScene;
    private float requiredWidth = 256f;
    private float requiredHeight = 256f;
    private float regionLeft;
    private float regionTop;
    private float regionRight;
    private float regionBottom;
    private int requiredState = MenuScene.MENU_STATE_CENTER;

    SparseArray<GameControl> controls;

    public GameMenu() {
        screen_w = MainView.mainView.getWidth();
        screen_h = MainView.mainView.getHeight();
        rate_x = screen_w / MainView.DEST_WIDTH;
        rate_y = screen_h / MainView.DEST_HEIGHT;
        rate = Math.min(rate_x, rate_y);
        controls = new SparseArray<>();
    }

    public void onDestroy() {
        for (int i = 0; i < controls.size(); i++)
            controls.valueAt(i).onDestroy();
        controls.clear();
    }

    public void reset() {
        for (int i = 0; i < controls.size(); i++)
            controls.valueAt(i).reset();
    }

    public void draw(Canvas canvas, Paint paint) {
        for (int i = 0; i < controls.size(); i++)
            controls.valueAt(i).draw(canvas, paint);
    }

    public void update(int elapsedTime) {
        for (int i = 0; i < controls.size(); i++)
            controls.valueAt(i).update(elapsedTime);
    }

    public void onTouchEvent(MotionEvent event) {
        for (int i = 0; i < controls.size(); i++)
            controls.valueAt(i).onTouchEvent(event);
    }

    public final GameControl addControl(int id, GameControl control) {
        controls.put(id, control);
        return control;
    }

    public final GameControl getControl(int id) {
        return controls.get(id);
    }

    public static int getCurrentMenuID() {
        return currentMenuID;
    }

    public static void setCurrentMenuID(int currentMenuID) {
        GameMenu.currentMenuID = currentMenuID;
    }

    public GameScene getParentScene() {
        return parentScene;
    }

    public final int getRequiredState() {
        return requiredState;
    }

    public final void setRequiredState(int requiredState) {
        this.requiredState = requiredState;
    }

    public final float getRequiredWidth() {
        return requiredWidth;
    }

    public final void setRequiredWidth(float requiredWidth) {
        this.requiredWidth = requiredWidth;
        if (requiredState == MenuScene.MENU_STATE_CENTER)
            regionLeft = screen_w / 2 - requiredWidth / 2;
        else if (requiredState == MenuScene.MENU_STATE_SIDE)
            regionLeft = screen_w - (MenuScene.MENU_SIDE_SIZE + MenuScene.MENU_BOX_SIZE_MIN / 2) * rate;
        regionRight = regionLeft + requiredWidth;
    }

    public final float getRequiredHeight() {
        return requiredHeight;
    }

    public final void setRequiredHeight(float requiredHeight) {
        this.requiredHeight = requiredHeight;
        regionTop = screen_h / 2 + MenuScene.MENU_BOX_OFFSET_Y * rate - requiredHeight / 2;
        regionBottom = regionTop + requiredHeight;
    }

    public final float getRegionLeft() {
        return regionLeft;
    }

    public final float getRegionTop() {
        return regionTop;
    }

    public final float getRegionRight() {
        return regionRight;
    }

    public final float getRegionBottom() {
        return regionBottom;
    }

    public static void release(GameMenu menu) {
        if (menu != null) {
            menu.onDestroy();
        }
    }
}
