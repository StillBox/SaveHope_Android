package com.stillbox.game.savehope.gameobject.dialog;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.gamedata.savedata.SaveData;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameenum.GameMode;
import com.stillbox.game.savehope.gameenum.SceneTitle;
import com.stillbox.game.savehope.gameenum.StoryChapter;

public class LevelSelectDialog extends DialogBox {

    private static final int BTN_EASY = 10;
    private static final int BTN_NORMAL = 20;
    private static final int BTN_HARD = 30;
    private static final int BTN_IJIME = 40;

    public LevelSelectDialog(SceneTitle scene, GameMode mode) {

        super(1152f * MainView.rate, 256f * MainView.rate);

        float rate = MainView.rate;
        int textSize = (int) (64f * rate);
        setText("难度选择", -w / 2, -50f * rate - textSize / 2, w, textSize);

        float button_w = 160f * rate;
        float button_h = 80f * rate;
        float button_x = -440f * rate;
        float button_y = 50f * rate - button_h / 2;
        addButton(BTN_EASY, "简单", button_x, button_y, button_w, button_h).setTextSize(textSize);

        button_x += 220f * rate;
        addButton(BTN_NORMAL, "普通", button_x, button_y, button_w, button_h).setTextSize(textSize);

        button_x += 220f * rate;
        addButton(BTN_HARD, "困难", button_x, button_y, button_w, button_h).setTextSize(textSize);

        button_w = 220f * rate;
        button_x += 220f * rate;
        addButton(BTN_IJIME, "欺负人", button_x, button_y, button_w, button_h).setTextSize(textSize);

        setOnSelectedListener(BTN_EASY, createOnSelectedListener(scene, mode, GameLevel.EASY));
        setOnSelectedListener(BTN_NORMAL, createOnSelectedListener(scene, mode, GameLevel.NORMAL));
        setOnSelectedListener(BTN_HARD, createOnSelectedListener(scene, mode, GameLevel.HARD));
        setOnSelectedListener(BTN_IJIME, createOnSelectedListener(scene, mode, GameLevel.IJIME));
    }

    public LevelSelectDialog(StoryChapter chapter) {

        super(1152f * MainView.rate, 256f * MainView.rate);

        float rate = MainView.rate;
        int textSize = (int) (64f * rate);
        setText("难度选择", -w / 2, -50f * rate - textSize / 2, w, textSize);

        float button_w = 160f * rate;
        float button_h = 80f * rate;
        float button_x = -440f * rate;
        float button_y = 50f * rate - button_h / 2;
        addButton(BTN_EASY, "简单", button_x, button_y, button_w, button_h).setTextSize(textSize);

        button_x += 220f * rate;
        addButton(BTN_NORMAL, "普通", button_x, button_y, button_w, button_h).setTextSize(textSize);

        button_x += 220f * rate;
        addButton(BTN_HARD, "困难", button_x, button_y, button_w, button_h).setTextSize(textSize);

        button_w = 220f * rate;
        button_x += 220f * rate;
        addButton(BTN_IJIME, "欺负人", button_x, button_y, button_w, button_h).setTextSize(textSize);

        setOnSelectedListener(BTN_EASY, createOnSelectedListener(chapter, GameLevel.EASY));
        setOnSelectedListener(BTN_NORMAL, createOnSelectedListener(chapter, GameLevel.NORMAL));
        setOnSelectedListener(BTN_HARD, createOnSelectedListener(chapter, GameLevel.HARD));
        setOnSelectedListener(BTN_IJIME, createOnSelectedListener(chapter, GameLevel.IJIME));
    }

    private OnSelectedListener createOnSelectedListener(SceneTitle scene, GameMode mode, GameLevel level) {

        return () -> MainView.setSceneCurtain(127, 255, 500, () -> MainView.setScene(scene, mode, level));
    }

    private OnSelectedListener createOnSelectedListener(StoryChapter chapter, GameLevel level) {

        return () -> {
            SaveData.saveStoryProgress(chapter, level);
            MainView.setSceneCurtain(127, 255, 500, () -> MainView.setScene(SceneTitle.STORY));
        };
    }
}
