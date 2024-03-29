package com.stillbox.game.savehope.gamemenu;

import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.Button;
import com.stillbox.game.savehope.gamedata.savedata.SaveData;
import com.stillbox.game.savehope.gameenum.SceneTitle;
import com.stillbox.game.savehope.gameenum.StoryChapter;
import com.stillbox.game.savehope.gameobject.dialog.ContinueDialog;
import com.stillbox.game.savehope.gameobject.dialog.LevelSelectDialog;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class SelectMenu extends GameMenu {

    private static final int BTN_STORY = 10;
    private static final int BTN_FREE = 20;
    private static final int BTN_SHOP = 30;
    private static final int BTN_EXTRA = 40;

    public SelectMenu() {

        menuID = MENU_SELECT;
        setRequiredWidth(512f * rate);
        setRequiredHeight(416f * rate);

        int buttonTextSize = (int) (64f * rate);
        float button_w = 400f * rate;
        float button_h = 80f * rate;
        float button_spacing = 96f * rate;
        float button_x, button_y;

        button_x = screen_w / 2 - button_w / 2;
        button_y = screen_h / 2 + MenuScene.MENU_BOX_OFFSET_Y * rate - button_spacing * 3 / 2 - button_h / 2;
        Button btnStory = (Button) addControl(BTN_STORY, new Button(MainView.getString(R.string.story_mode), button_x, button_y, button_w, button_h));
        btnStory.setTextSize(buttonTextSize);
        btnStory.setOnPressedListener(() -> {
            if (SaveData.currentChapter == StoryChapter.INTRO) {
                MainView.addDialog(new LevelSelectDialog(StoryChapter.INTRO));
            } else {
                MainView.addDialog(new ContinueDialog());
            }
            reset();
        });

        button_y += button_spacing;
        Button btnFree = (Button) addControl(BTN_FREE, new Button(MainView.getString(R.string.free_mode), button_x, button_y, button_w, button_h));
        btnFree.setTextSize(buttonTextSize);
        btnFree.setOnPressedListener(() -> {
            setCurrentMenuID(MENU_FREE);
            reset();
        });

        button_y += button_spacing;
        Button btnShop = (Button) addControl(BTN_SHOP, new Button(MainView.getString(R.string.shop_mode), button_x, button_y, button_w, button_h));
        btnShop.setTextSize(buttonTextSize);
        btnShop.setOnPressedListener(() -> {
            MainView.setSceneCurtain(127, 255, 500, () -> MainView.setScene(SceneTitle.SHOP));
            reset();
        });

        button_y += button_spacing;
        Button btnExtra = (Button) addControl(BTN_EXTRA, new Button(MainView.getString(R.string.extra_mode), button_x, button_y, button_w, button_h));
        btnExtra.setTextSize(buttonTextSize);
        btnExtra.setOnPressedListener(() -> {
            MainView.setSceneCurtain(127, 255, 500, () -> MainView.setScene(SceneTitle.EXTRA));
            reset();
        });
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

        return touch_x < getRegionLeft() || touch_x > getRegionRight() ||
                touch_y < getRegionTop() || touch_y > getRegionBottom();
    }
}
