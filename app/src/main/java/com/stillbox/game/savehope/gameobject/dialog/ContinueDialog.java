package com.stillbox.game.savehope.gameobject.dialog;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.gameenum.SceneTitle;
import com.stillbox.game.savehope.gameenum.StoryChapter;

public class ContinueDialog extends DialogBox {

    private static final int BTN_CONTINUE = 10;
    private static final int BTN_NEW_STORY = 20;

    public ContinueDialog() {

        super(1152 * MainView.rate, 256f * MainView.rate);

        float rate = MainView.rate;
        int textSize = (int) (64f * rate);
        setText("进度选择", -w / 2, -50f * rate - textSize / 2, w, textSize);

        float button_w = 400f * rate;
        float button_h = 80f * rate;
        float button_x = -450f * rate;
        float button_y = 50f * rate - button_h / 2;
        addButton(BTN_CONTINUE, "继续旧进度", button_x, button_y, button_w, button_h).setTextSize(textSize);
        setOnSelectedListener(BTN_CONTINUE, () -> MainView.setSceneCurtain(0,255, 500, () -> MainView.setScene(SceneTitle.STORY)));

        button_x += 500f * rate;
        addButton(BTN_NEW_STORY, "开始新进度", button_x, button_y, button_w, button_h).setTextSize(textSize);
        setOnSelectedListener(BTN_NEW_STORY, () -> MainView.addDialog(new LevelSelectDialog(StoryChapter.INTRO)));
    }
}
