package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.GameControl;
import com.stillbox.game.savehope.gamecontrol.TextButton;
import com.stillbox.game.savehope.gamedata.CharaData;
import com.stillbox.game.savehope.gamedata.SaveData;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameenum.GameMode;
import com.stillbox.game.savehope.gameenum.SceneTitle;
import com.stillbox.game.savehope.gameenum.StoryChapter;
import com.stillbox.game.savehope.gamemenu.GameMenu;
import com.stillbox.game.savehope.gameobject.DigitalBackground;
import com.stillbox.game.savehope.gameobject.GameObject;
import com.stillbox.game.savehope.gameobject.SpriteObject;
import com.stillbox.game.savehope.gameobject.TextBox;
import com.stillbox.game.savehope.gamesound.GameSound;

import static com.stillbox.game.savehope.gamedata.CharaData.IDC_ALTEREGO;
import static com.stillbox.game.savehope.gamedata.CharaData.IDC_USAMI;
import static com.stillbox.game.savehope.gamedata.CharaData.IDC_NANAMI;
import static com.stillbox.game.savehope.gamedata.CharaData.IDC_ENOSHIMA;

public class StoryScene extends GameScene {

    private static final int BGM_ALTER_EGO = R.raw.alter_ego;
    private static final int BGM_NEW_WORLD = R.raw.new_world_order;
    private static final int BGM_MONOMI = R.raw.monomi;
    private static final int BGM_MIRAI = R.raw.mirai;
    private static final int BGM_DISTRUST = R.raw.distrust;
    private static final int BGM_UNLOCK = R.raw.stage_unlock;
    private static final int SE_TEXT = R.raw.text;
    private static final float BPM = 128.01f;
    private static final float BASE_SPB = 60000f / BPM;

    private TextBox textBox;
    private TextButton btnSkip;
    private IntroBackground introBackground;
    private DigitalBackground digitalBackground;

    private SparseArray<Character> characters;
    private Character alter_ego;
    private Character usami;
    private Character nanami;
    private Character enoshima;

    private StoryChapter chapter;
    private String[] scripts;
    private int currentIndex;
    private boolean bIsReadyForNext;
    private boolean bIsReadyToClose;
    private boolean bIsChapterOver;

    public StoryScene() {

        chapter = SaveData.currentChapter;

        int maxProgress = 0;
        switch (chapter) {
            case INTRO:
                maxProgress = 1;
                break;
            case SHOOTER:
                maxProgress = 1;
                break;
            case SNAKE:
                maxProgress = 1;
                break;
            case UPSTAIRS:
                maxProgress = 1;
                break;
            case END:
                maxProgress = 1;
                break;
        }
        MainView.setLoadingProgress(maxProgress, 0);
    }

    @Override
    public void init() {

        Thread thread = new Thread(() -> {

            characters = new SparseArray<>();

            if (chapter == StoryChapter.INTRO || chapter == StoryChapter.SHOOTER) {
                digitalBackground = new DigitalBackground(true, false);
            } else {
                digitalBackground = new DigitalBackground(false, false);
                digitalBackground.setActive(true);
            }
            MainView.increaseLoadingProgress(1);

            GameSound.addSE(SE_TEXT, SE_TEXT);
            MainView.increaseLoadingProgress(1);

            switch (chapter) {

                case INTRO:

                    introBackground = new IntroBackground();

                case SHOOTER:

                    scripts = MainView.resources.getStringArray(R.array.script_c1);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_ALTEREGO, new Character(IDC_ALTEREGO));
                    alter_ego = characters.get(IDC_ALTEREGO);
                    MainView.increaseLoadingProgress(1);

                    GameSound.createBGM(BGM_ALTER_EGO, BGM_ALTER_EGO, true);
                    MainView.increaseLoadingProgress(1);

                    GameSound.createBGM(BGM_NEW_WORLD, BGM_NEW_WORLD, true);
                    MainView.increaseLoadingProgress(1);

                    break;

                case SNAKE:

                    scripts = MainView.resources.getStringArray(R.array.script_c2);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_ALTEREGO, new Character(IDC_ALTEREGO));
                    alter_ego = characters.get(IDC_ALTEREGO);
                    alter_ego.setActive(true);
                    alter_ego.setState(Character.STATE_NORMAL);
                    alter_ego.setPosition(screen_w / 2, screen_h / 3);
                    MainView.increaseLoadingProgress(1);

                    GameSound.createBGM(BGM_ALTER_EGO, BGM_ALTER_EGO, true);
                    GameSound.startBGM(BGM_ALTER_EGO);
                    MainView.increaseLoadingProgress(1);

                    break;

                case UPSTAIRS:

                    scripts = MainView.resources.getStringArray(R.array.script_c3);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_ALTEREGO, new Character(IDC_ALTEREGO));
                    alter_ego = characters.get(IDC_ALTEREGO);
                    alter_ego.setActive(true);
                    alter_ego.setState(Character.STATE_STANDBY);
                    alter_ego.setPosition(screen_w / 3, screen_h / 3);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_USAMI, new Character(IDC_USAMI));
                    usami = characters.get(IDC_USAMI);
                    usami.setActive(true);
                    usami.setState(Character.STATE_CRY);
                    usami.setPosition(screen_w * 2 / 3, screen_h / 3);
                    MainView.increaseLoadingProgress(1);

                    GameSound.createBGM(BGM_MONOMI, BGM_MONOMI, true);
                    GameSound.startBGM(BGM_MONOMI);
                    MainView.increaseLoadingProgress(1);

                    break;

                case END:

                    scripts = MainView.resources.getStringArray(R.array.script_c4);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_NANAMI, new Character(IDC_NANAMI));
                    nanami = characters.get(IDC_NANAMI);
                    nanami.setActive(true);
                    nanami.setState(Character.STATE_NORMAL);
                    nanami.setPosition(screen_w / 3, screen_h / 3);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_USAMI, new Character(IDC_USAMI));
                    usami = characters.get(IDC_USAMI);
                    usami.setActive(true);
                    usami.setState(Character.STATE_STANDBY);
                    usami.setPosition(screen_w * 2 / 3, screen_h / 3);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_ALTEREGO, new Character(IDC_ALTEREGO));
                    alter_ego = characters.get(IDC_ALTEREGO);
                    alter_ego.setActive(false);
                    alter_ego.setState(Character.STATE_STANDBY);
                    alter_ego.setPosition(screen_w / 2, screen_h / 3);
                    MainView.increaseLoadingProgress(1);

                    GameSound.createBGM(BGM_MIRAI, BGM_MIRAI, true);
                    GameSound.startBGM(BGM_MIRAI);
                    MainView.increaseLoadingProgress(1);

                    break;

                case TRUE_END:

                    scripts = MainView.resources.getStringArray(R.array.script_c5);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_ALTEREGO, new Character(IDC_ALTEREGO));
                    alter_ego = characters.get(IDC_ALTEREGO);
                    alter_ego.setActive(true);
                    alter_ego.setState(Character.STATE_NORMAL);
                    alter_ego.setPosition(screen_w / 2, screen_h / 3);
                    MainView.increaseLoadingProgress(1);

                    characters.put(IDC_ENOSHIMA, new Character(IDC_ENOSHIMA));
                    enoshima = characters.get(IDC_ENOSHIMA);
                    enoshima.setActive(false);
                    enoshima.setState(Character.STATE_STANDBY);
                    enoshima.setPosition(screen_w / 2, screen_h / 3);
                    MainView.increaseLoadingProgress(1);

                    GameSound.createBGM(BGM_DISTRUST, BGM_DISTRUST, true);
                    MainView.increaseLoadingProgress(1);

                    GameSound.createBGM(BGM_MIRAI, BGM_MIRAI, true);
                    GameSound.startBGM(BGM_MIRAI);
                    MainView.increaseLoadingProgress(1);

                    break;
            }

            textBox = new TextBox();
            textBox.setText(scripts[0]);
            currentIndex = 0;
            bIsReadyForNext = false;
            bIsReadyToClose = false;
            bIsChapterOver = false;
            MainView.increaseLoadingProgress(1);

            float button_w = 320 * rate;
            float button_h = 128 * rate;
            btnSkip = new TextButton("跳过剧情", screen_w - button_w, 0, button_w, button_h);
            btnSkip.setTextSize((int) (48f * rate));
            btnSkip.setOnPressedListener(() -> bIsReadyToClose = true);
            MainView.increaseLoadingProgress(1);

            MainView.endLoadingProgress();
        });

        thread.start();
    }

    @Override
    public void onDestroy() {

        GameObject.release(textBox);
        GameObject.release(introBackground);
        GameObject.release(digitalBackground);
        GameControl.release(btnSkip);
        if (characters != null) {
            for (int i = 0; i < characters.size(); i++) {
                characters.valueAt(i).onDestroy();
            }
            characters.clear();
        }
        GameSound.releaseSE();
        GameSound.releaseBGM();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {


        if (chapter == StoryChapter.INTRO) {
            introBackground.draw(canvas, paint);
        } else {
            digitalBackground.draw(canvas, paint);
            btnSkip.draw(canvas, paint);
            textBox.draw(canvas, paint);
            for (int i = 0; i < characters.size(); i++) {
                characters.valueAt(i).draw(canvas, paint);
            }
        }
    }

    @Override
    public void update(int elapsedTime) {

        if (chapter == StoryChapter.INTRO) {

            introBackground.update(elapsedTime);

            if (bIsReadyToClose) {
                chapter = StoryChapter.SHOOTER;
                bIsReadyToClose = false;
            }
        } else {

            digitalBackground.update(elapsedTime);
            btnSkip.update(elapsedTime);
            textBox.update(elapsedTime);
            for (int i = 0; i < characters.size(); i++) {
                characters.valueAt(i).update(elapsedTime);
            }

            if (bIsReadyForNext) {

                currentIndex++;

                if (currentIndex == scripts.length) {
                    bIsReadyToClose = true;
                } else {
                    if (chapter == StoryChapter.SHOOTER && currentIndex == 2) {
                        textBox.setText(scripts[currentIndex], (int) (96f * rate));
                        textBox.accelerate(0.3f);
                    } else {
                        textBox.setText(scripts[currentIndex]);
                    }
                }

                switch (chapter) {

                    case SHOOTER:
                        switch (currentIndex) {
                            case 3:
                                GameSound.startBGM(BGM_ALTER_EGO);
                                digitalBackground.setActive(true);
                                alter_ego.setActive(true);
                                alter_ego.setState(Character.STATE_NORMAL);
                                alter_ego.setPosition(screen_w / 2, screen_h / 3);
                                break;
                            case 27:
                                GameSound.stopBGM();
                                GameSound.startBGM(BGM_NEW_WORLD);
                                break;
                        }
                        break;

                    case UPSTAIRS:
                        switch (currentIndex) {
                            case 1:
                                usami.setState(Character.STATE_HAPPY);
                                break;
                            case 2:
                                usami.setState(Character.STATE_STANDBY);
                                alter_ego.setState(Character.STATE_NORMAL);
                                break;
                            case 3:
                                usami.setState(Character.STATE_SHOCK);
                                alter_ego.setState(Character.STATE_STANDBY);
                                break;
                            case 4:
                                usami.setState(Character.STATE_SMILE);
                                break;
                            case 5:
                                usami.setState(Character.STATE_NORMAL);
                                break;
                            case 7:
                                usami.setState(Character.STATE_STANDBY);
                                alter_ego.setState(Character.STATE_NORMAL);
                                break;
                            case 10:
                                usami.setState(Character.STATE_SHOCK);
                                alter_ego.setState(Character.STATE_STANDBY);
                                break;
                            case 11:
                                usami.setState(Character.STATE_HAPPY);
                                break;
                            case 13:
                                usami.setState(Character.STATE_STANDBY);
                                alter_ego.setState(Character.STATE_NORMAL);
                                break;
                        }
                        break;

                    case END:
                        switch (currentIndex) {
                            case 5:
                                nanami.setState(Character.STATE_STANDBY);
                                usami.setState(Character.STATE_NORMAL);
                                break;
                            case 6:
                                nanami.setState(Character.STATE_SMILE);
                                usami.setState(Character.STATE_SMILE);
                                break;
                            case 7:
                                nanami.setActive(false);
                                usami.setActive(false);
                                alter_ego.setActive(true);
                                alter_ego.setState(Character.STATE_NORMAL);
                                break;
                            case 14:
                                alter_ego.setActive(false);
                                break;
                        }
                        break;

                    case TRUE_END:
                        switch (currentIndex) {
                            case 8:
                                alter_ego.setState(Character.STATE_STANDBY);
                                digitalBackground.setActive(false);
                                GameSound.stopBGM();
                                break;
                            case 10:
                                alter_ego.setActive(false);
                                enoshima.setActive(true);
                                enoshima.setState(Character.STATE_HAPPY);
                                digitalBackground.setReverse(true);
                                digitalBackground.setActive(true);
                                GameSound.startBGM(BGM_DISTRUST);
                                break;
                            case 12:
                                enoshima.setState(Character.STATE_SHOCK);
                                break;
                            case 13:
                                enoshima.setState(Character.STATE_CRY);
                                break;
                            case 14:
                                enoshima.setState(Character.STATE_SMILE);
                                break;
                            case 16:
                                enoshima.setState(Character.STATE_CRY);
                                break;
                            case 17:
                                enoshima.setState(Character.STATE_HAPPY);
                                break;
                            case 21:
                                enoshima.setActive(false);
                                break;
                        }
                        break;
                }
                bIsReadyForNext = false;
            }

            if (bIsReadyToClose && !bIsChapterOver) {
                bIsReadyToClose = false;
                textBox.close(null);
                switch (chapter) {
                    case SHOOTER:
                        MainView.setSceneCurtain(0, 255, 500, () -> MainView.setScene(SceneTitle.SHOOTER, GameMode.STORY, SaveData.currentLevel));
                        bIsChapterOver = true;
                        break;
                    case SNAKE:
                        MainView.setSceneCurtain(0, 255, 500, () -> MainView.setScene(SceneTitle.SNAKE, GameMode.STORY, SaveData.currentLevel));
                        bIsChapterOver = true;
                        break;
                    case UPSTAIRS:
                        MainView.setSceneCurtain(0, 255, 500, () -> MainView.setScene(SceneTitle.UPSTAIRS, GameMode.STORY, SaveData.currentLevel));
                        bIsChapterOver = true;
                        break;
                    case END:
                    case TRUE_END:
                        chapter = StoryChapter.UNLOCK;
                    case UNLOCK:
                        if (SaveData.elementUnlock[0] == 0 && SaveData.currentLevel.compareTo(GameLevel.EASY) >= 0) {
                            SaveData.saveUnlockState(0, true);
                            scripts = MainView.resources.getStringArray(R.array.script_u0);
                        } else if (SaveData.elementUnlock[1] == 0 && SaveData.currentLevel.compareTo(GameLevel.EASY) >= 0) {
                            SaveData.saveUnlockState(1, true);
                            scripts = MainView.resources.getStringArray(R.array.script_u1);
                        } else if (SaveData.elementUnlock[2] == 0 && SaveData.currentLevel.compareTo(GameLevel.NORMAL) >= 0) {
                            SaveData.saveUnlockState(2, true);
                            scripts = MainView.resources.getStringArray(R.array.script_u2);
                        } else if (SaveData.elementUnlock[3] == 0 && SaveData.currentLevel.compareTo(GameLevel.HARD) >= 0) {
                            SaveData.saveUnlockState(3, true);
                            scripts = MainView.resources.getStringArray(R.array.script_u3);
                        } else {
                            bIsChapterOver = true;
                        }

                        if (bIsChapterOver) {
                            MainView.setSceneCurtain(0, 255, 500, () -> MainView.setScene(SceneTitle.MENU, GameMenu.MENU_SELECT));
                        } else {
                            MainView.setSceneCurtain(0, 255, 500, () -> {
                                GameObject.release(digitalBackground);
                                GameSound.createBGM(BGM_UNLOCK, BGM_UNLOCK, false);
                                GameSound.startBGM(BGM_UNLOCK);
                                btnSkip.setText("跳过说明");
                                if (characters != null) {
                                    for (int i = 0; i < characters.size(); i++) {
                                        characters.valueAt(i).onDestroy();
                                    }
                                    characters.clear();
                                }
                                characters.put(IDC_USAMI, new Character(IDC_USAMI));
                                usami = characters.get(IDC_USAMI);
                                usami.setActive(true);
                                usami.setState(Character.STATE_HAPPY);
                                usami.setPosition(screen_w / 2, screen_h / 3);
                                currentIndex = 0;
                                bIsReadyForNext = false;
                                bIsReadyToClose = false;
                                textBox.open(null);
                                textBox.setText(scripts[currentIndex]);
                            });

                        }

                        break;
                }
            }
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (bIsChapterOver) return;

        if (chapter == StoryChapter.INTRO) {

            if (introBackground.isMaximum()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    introBackground.shutdown();
                }
            } else if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                introBackground.accelerate(true);
            }

            if (event.getAction() == MotionEvent.ACTION_UP) {
                introBackground.accelerate(false);
            }

        } else {

            float touch_x = event.getX();
            float touch_y = event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN && !btnSkip.checkTouchPoint(touch_x, touch_y)) {
                if (textBox.isTextOver()) {
                    GameSound.playSE(SE_TEXT);
                    bIsReadyForNext = true;
                } else {
                    textBox.accelerate(16f);
                }
            } else {
                btnSkip.onTouchEvent(event);
            }
        }
    }

    private class Character extends SpriteObject {

        static final int SPRITE_STAY = 10;
        static final int SPRITE_BEND = 11;
        static final int SPRITE_REST = 12;
        static final int SPRITE_SMILE = 20;
        static final int SPRITE_SHOCK = 30;
        static final int SPRITE_CRY_0 = 40;
        static final int SPRITE_CRY_1 = 41;
        static final int SPRITE_HAPPY_0 = 50;
        static final int SPRITE_HAPPY_1 = 51;
        static final int SPRITE_STANDBY = 60;

        static final int STATE_NORMAL = 10;
        static final int STATE_SMILE = 20;
        static final int STATE_SHOCK = 30;
        static final int STATE_CRY = 40;
        static final int STATE_HAPPY = 50;
        static final int STATE_STANDBY = 60;

        private int charaId;
        private boolean bIsActive;

        Character(int charaId) {

            this.charaId = charaId;
            setSize(384f * rate, 432f * rate);
            setOffset(-w / 2, -h / 2);

            loadResource(CharaData.getResID(charaId));
            addSprite(SPRITE_STAY, 0f / 8f, 0f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_BEND, 1f / 8f, 0f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_REST, 7f / 8f, 0f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_SMILE, 0f / 8f, 36f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_SHOCK, 1f / 8f, 36f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_CRY_0, 2f / 8f, 36f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_CRY_1, 3f / 8f, 36f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_HAPPY_0, 4f / 8f, 36f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_HAPPY_1, 5f / 8f, 36f / 64f, 1f / 8f, 9f / 64f);
            addSprite(SPRITE_STANDBY, 0f / 8f, 45f / 64f, 1f / 8f, 9f / 64f);
            releaseResource();

            addState(STATE_NORMAL, STATE_LOOP, (int) BASE_SPB, SPRITE_STAY, SPRITE_STAY, SPRITE_STAY, SPRITE_STAY,
                    SPRITE_BEND, SPRITE_BEND, SPRITE_STAY, SPRITE_REST, SPRITE_STAY, SPRITE_STAY, SPRITE_BEND, SPRITE_BEND);
            addState(STATE_SMILE, STATE_SINGLE, 0, SPRITE_SMILE);
            addState(STATE_SHOCK, STATE_SINGLE, 0, SPRITE_SHOCK);
            addState(STATE_CRY, STATE_LOOP, (int) BASE_SPB, SPRITE_CRY_0, SPRITE_CRY_1);
            addState(STATE_HAPPY, STATE_LOOP, (int) BASE_SPB, SPRITE_HAPPY_0, SPRITE_HAPPY_1);
            addState(STATE_STANDBY, STATE_SINGLE, 0, SPRITE_STANDBY);

            setState(STATE_STANDBY);
            bIsActive = false;
            setScale(0f);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            if (bIsActive) {
                super.draw(canvas, paint);
            }
        }

        @Override
        public void update(int elapsedTime) {

            if (bIsActive) {
                if (scale < 1f) {
                    scale += 0.01f * elapsedTime;
                }
                if (scale > 1f) {
                    scale = 1f;
                }
            }

            super.update(elapsedTime);
        }

        int getCharaId() {

            return charaId;
        }

        boolean isActive() {

            return bIsActive;
        }

        void setActive(boolean bIsActive) {

            this.bIsActive = bIsActive;
        }

    }

    private class IntroBackground extends GameObject {

        String[] texts;
        TextInfo[] textInfos;
        float maxAlpha;
        boolean bIsMaximum;
        boolean bAccelerate;
        boolean bShutdown;

        IntroBackground() {

            texts = MainView.resources.getStringArray(R.array.script_c0);
            int count = (int) (MainView.w / MainView.rate / 160 * MainView.h / MainView.rate / 90);
            textInfos = new TextInfo[count];

            for (int i = 0; i < count; i++) {
                TextInfo info = textInfos[i] = new TextInfo();
                info.x = (float) Math.random() * MainView.w;
                info.y = (float) Math.random() * MainView.h;
                info.textSize = (int) ((24 + Math.random() * 40) * MainView.rate);
                info.textIndex = (int) (Math.random() * texts.length);
                info.alphaRate = -15f * (float) Math.random();
                info.alphaRateDelta = 0.001f;
            }

            maxAlpha = 15f;
            bIsMaximum = false;
            bAccelerate = false;
            bShutdown = false;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);

            for (TextInfo info : textInfos) {
                if (info.alphaRate > 0f) {
                    paint.setTextSize(info.textSize);
                    paint.setAlpha((int) (info.alphaRate * maxAlpha));
                    canvas.drawText(texts[info.textIndex], info.x, info.y, paint);
                }
            }
            paint.setAlpha(255);

            if (MainView.bIsDebugMode) {
                paint.setTextSize(16);
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(Float.toString(maxAlpha), 0, MainView.h, paint);
            }
        }

        @Override
        public void update(int elapsedTime) {

            if (!bIsMaximum) {
                maxAlpha += elapsedTime * (bAccelerate ? 0.2 : 0.005);
                if (maxAlpha >= 255f) {
                    maxAlpha = 255f;
                    bIsMaximum = true;
                }
            }

            for (TextInfo info : textInfos) {
                info.alphaRate += elapsedTime * info.alphaRateDelta * (bAccelerate ? 4 : 1);
                if (info.alphaRate >= 1f) {
                    info.alphaRate = 1f;
                    info.alphaRateDelta = -0.001f;
                } else if (info.alphaRate <= 0f && info.alphaRateDelta < 0f) {
                    if (!bShutdown) {
                        info.x = (float) Math.random() * MainView.w;
                        info.y = (float) Math.random() * MainView.h;
                        info.textSize = (int) ((24 + Math.random() * 40) * MainView.rate);
                        info.textIndex = (int) (Math.random() * texts.length);
                        info.alphaRate = -1f * (float) Math.random();
                        info.alphaRateDelta = 0.001f;
                    } else {
                        info.alphaRate = 0f;
                        info.alphaRateDelta = 0f;
                    }
                }
            }

            if (bShutdown) {
                boolean bAllClear = true;
                for (TextInfo info : textInfos) {
                    if (info.alphaRate > 0f) {
                        bAllClear = false;
                    }
                }
                if (bAllClear) {
                    bIsReadyToClose = true;
                }
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        void accelerate(boolean bAccelerate) {

            this.bAccelerate = bAccelerate;
        }

        boolean isMaximum() {

            return bIsMaximum;
        }

        void shutdown() {

            bShutdown = true;
            for (TextInfo info : textInfos) {
                info.alphaRateDelta = -0.001f;
            }
        }

        private class TextInfo {

            float x, y;
            int textSize;
            int textIndex;
            float alphaRate;
            float alphaRateDelta;
        }
    }
}
