package com.stillbox.game.savehope.gamescene;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.Button;
import com.stillbox.game.savehope.gamecontrol.GameControl;
import com.stillbox.game.savehope.gamecontrol.StaticText;
import com.stillbox.game.savehope.gamecontrol.TextButton;
import com.stillbox.game.savehope.gamedata.CharaData;
import com.stillbox.game.savehope.gamedata.GameSettings;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameenum.GameMode;
import com.stillbox.game.savehope.gameenum.SceneTitle;
import com.stillbox.game.savehope.gameenum.StoryChapter;
import com.stillbox.game.savehope.gamemenu.GameMenu;
import com.stillbox.game.savehope.gamemenu.PauseMenu;
import com.stillbox.game.savehope.gamemenu.ShooterSettingMenu;
import com.stillbox.game.savehope.gameobject.Border;
import com.stillbox.game.savehope.gameobject.GameObject;
import com.stillbox.game.savehope.gameobject.SingleSpriteObject;
import com.stillbox.game.savehope.gameobject.SpriteObject;
import com.stillbox.game.savehope.gameobject.Tutorial;
import com.stillbox.game.savehope.gameobject.gamepad.GamePad;
import com.stillbox.game.savehope.gamesound.GameSound;

import java.util.ArrayList;
import java.util.Locale;

public class ShooterScene extends GameScene {

    //Fields and setters relating to settings
    private static int controlMode = GameSettings.controlMode_st;
    private static int scannerAlpha = GameSettings.scannerAlpha_st;
    private static int adjustTime = GameSettings.adjustTime_st;

    public static void setControlMode(int controlMode) {
        ShooterScene.controlMode = controlMode;
    }

    public static void setScannerAlpha(int scannerAlpha) {
        ShooterScene.scannerAlpha = scannerAlpha;
    }

    public static void setAdjustTime(int adjustTime) {
        ShooterScene.adjustTime = adjustTime;
    }

    //Fields for game field grid
    private static final float DEST_SPACING = 96f;
    private static final int ROWS_MIN = 11;
    private static final int COLS_MIN = 20;

    private static float spacing;
    private static int rows, cols, rowBeg, colBeg;

    //Fields for sounds
    private static final int ID_SE_PERFECT = R.raw.beat01;
    private static final int ID_SE_GOOD = R.raw.beat02;
    private static final int ID_SE_BAD = R.raw.beat03;

    private static final int ID_BGM = R.raw.ekorosia;
    private static final double BPM = 128.01;
    private static final double BASE_SPB = 60000 / BPM;
    private static final float BEG_TIME = 484f;
    private int currentTime;

    //Fields for game mode and player stats
    private static GameMode gameMode;
    private static GameLevel gameLevel;

    private static int charaID = CharaData.IDC_HINATA;
    private static int charaStr;
    private static int charaAgi;
    private static int charaEnd;
    private static int charaInt;
    private static int charaLuc;

    //Fields relating to level
    private static float beatRate;
    private static float beatPrepare; //TODO assign prepare rate to add beats
    private static double actSPB;

    private static int poisonGap;
    private static int debuffGap;
    private static boolean bAutoDebuff;
    private static boolean bCombDebuff;

    private static int totalTime;
    private static int poisonTime;
    private static int debuffTime;

    //Fields for game tutorial
    private boolean bIsTutorial;
    private Tutorial tutorial;

    //Fields for game pause
    private boolean bOnPauseBtnEvent;
    private TextButton btnPause;
    private PauseMenu pauseMenu;
    private ShooterSettingMenu settingMenu;
    private GameMenu currentMenu;

    //Fields for game objects
    private GameMap map;
    private Scanner scanner;
    private Player player;
    private Beats beats;
    private Fire fire;
    private Smoke smoke;
    private FloatingInfo floatingInfo;
    private HUD hud;

    //Fields for game controls
    private GamePad gamePad;
    private GestureDetector detector;
    private static final int ACT_A = 10;
    private static final int ACT_B = 20;

    //Fields for states, counters and timers
    private boolean bIsGameOver;
    private boolean bIsGameClear;
    private boolean bIsGamePaused;
    private boolean bIsGameSummary;

    private static final int DEATH_POISON = 10;
    private static final int DEATH_FIRE = 20;
    private int deathReason;

    //timers
    private int countDownTimer;

    //for debuff
    private int fireTimer = -1;
    private int smokeTimer = -1;
    private int hiddenTimer = -1;
    private int poisonTimer = (int) (-BASE_SPB * 2.5);

    //for finish
    private int overTimer = 0;
    private int clearTimer = 0;
    private int summaryTimer = 0;

    //counters
    private int comboCount = 0;
    private int perfectCount = 0;
    private int goodCount = 0;
    private int badCount = 0;
    private int missCount = 0;
    private int maxCombo = 0;
    private int score = 0;
    private int fragmentCount = 0;
    private int hopeFragmentCount = 0;
    private int goldFragmentCount = 0;
    private int fragmentCombo = 0;

    public ShooterScene(GameMode mode, GameLevel level) {

        MainView.setLoadingProgress(1, 0);

        gameMode = mode;
        gameLevel = level;

        bIsGameOver = false;
        bIsGameClear = false;
        bIsGamePaused = false;
        bIsGameSummary = false;
    }

    @Override
    public void init() {

        Thread thread = new Thread(() -> {

            if (gameMode == GameMode.STORY) {
                bIsTutorial = true;
                tutorial = new Tutorial(StoryChapter.SHOOTER);

            }
            MainView.increaseLoadingProgress(1);

            pauseMenu = new PauseMenu();
            ((Button) pauseMenu.getControl(PauseMenu.BTN_CONTINUE)).setOnPressedListener(() -> pauseMenu.close(this::gameContinue));
            ((Button) pauseMenu.getControl(PauseMenu.BTN_RETRY)).setOnPressedListener(() -> pauseMenu.close(this::gameContinue));
            ((Button) pauseMenu.getControl(PauseMenu.BTN_SETTING)).setOnPressedListener(() -> pauseMenu.close(() -> currentMenu = settingMenu));
            ((Button) pauseMenu.getControl(PauseMenu.BTN_MENU)).setOnPressedListener(() -> {
                        pauseMenu.close(null);
                        MainView.setSceneCurtain(127, 256, 500, () -> {
                            MainView.setScene(SceneTitle.MENU);
                            GameMenu.setCurrentMenuID(GameMenu.MENU_SELECT);
                        });
                    });

            MainView.increaseLoadingProgress(1);

            settingMenu = new ShooterSettingMenu();
            settingMenu.setOnClosedListener(() -> {
                currentMenu = pauseMenu;
                pauseMenu.open(null);
            });
            MainView.increaseLoadingProgress(1);

            currentMenu = null;

            spacing = DEST_SPACING * rate;
            cols = (int) Math.ceil(screen_w / spacing);
            rows = (int) Math.ceil(screen_h / spacing);
            colBeg = (cols - COLS_MIN) / 2;
            rowBeg = (rows - ROWS_MIN) / 2;

            switch (gameLevel) {
                case EASY:
                    beatRate = 2f;
                    beatPrepare = 3f;
                    poisonGap = 10;
                    debuffGap = 20;
                    poisonTime = 16000;
                    debuffTime = 5000;
                    totalTime = 100000;
                    bAutoDebuff = false;
                    bCombDebuff = false;
                    break;
                case NORMAL:
                    beatRate = 1f;
                    beatPrepare = 3f;
                    poisonGap = 8;
                    debuffGap = 16;
                    poisonTime = 20000;
                    debuffTime = 10000;
                    totalTime = 120000;
                    bAutoDebuff = true;
                    bCombDebuff = false;
                    break;
                case HARD:
                    beatRate = 0.5f;
                    beatPrepare = 4f;
                    poisonGap = 6;
                    debuffGap = 12;
                    poisonTime = 24000;
                    debuffTime = 16000;
                    totalTime = 120000;
                    bAutoDebuff = true;
                    bCombDebuff = false;
                    break;
                case IJIME:
                    beatRate = 0.5f;
                    beatPrepare = 4f;
                    poisonGap = 5;
                    debuffGap = 8;
                    poisonTime = 30000;
                    debuffTime = 24000;
                    totalTime = 150000;
                    bAutoDebuff = true;
                    bCombDebuff = true;
                    break;
            }

            actSPB = BASE_SPB * beatRate;

            map = new GameMap();
            MainView.increaseLoadingProgress(1);

            scanner = new Scanner(scannerAlpha);
            MainView.increaseLoadingProgress(1);

            player = new Player();
            MainView.increaseLoadingProgress(1);

            beats = new Beats();
            MainView.increaseLoadingProgress(1);

            fire = new Fire();
            MainView.increaseLoadingProgress(1);

            smoke = new Smoke();
            MainView.increaseLoadingProgress(1);

            floatingInfo = new FloatingInfo();
            MainView.increaseLoadingProgress(1);

            hud = new HUD();
            MainView.increaseLoadingProgress(1);

            gamePad = new GamePad();
            gamePad.addButton(ACT_A, screen_w - 3.5f * GamePad.buttonRadius, screen_h - 1.5f * GamePad.buttonRadius, 0xFFFF4444, "A",
                    new GamePad.PadButtonListener() {
                        @Override
                        public void OnPressed() {
                            beats.checkBeat(ACT_A);
                        }

                        @Override
                        public void OnReleased() {

                        }
                    });
            gamePad.addButton(ACT_B, screen_w - 1.5f * GamePad.buttonRadius, screen_h - 3.5f * GamePad.buttonRadius, 0xFF4444FF, "B",
                    new GamePad.PadButtonListener() {

                        @Override
                        public void OnPressed() {
                            beats.checkBeat(ACT_B);
                        }

                        @Override
                        public void OnReleased() {

                        }
                    });

            float button_w = 384 * rate;
            float button_h = 128 * rate;
            btnPause = new TextButton("暂停游戏", screen_w - button_w, 0, button_w, button_h);
            btnPause.setTextSize((int) (64f * MainView.rate));
            btnPause.setOnPressedListener(this::gamePause);
            bOnPauseBtnEvent = false;
            MainView.increaseLoadingProgress(1);

            GameSound.addSE(ID_SE_PERFECT, ID_SE_PERFECT);
            GameSound.addSE(ID_SE_GOOD, ID_SE_GOOD);
            GameSound.addSE(ID_SE_BAD, ID_SE_BAD);
            MainView.increaseLoadingProgress(1);

            GameSound.createBGM(ID_BGM, ID_BGM, false);
            if (!bIsTutorial)
                GameSound.startBGM(ID_BGM);
            MainView.increaseLoadingProgress(1);

            MainView.endLoadingProgress();
        });

        thread.start();
    }

    @Override
    public void onDestroy() {

        GameObject.release(tutorial);

        GameControl.release(btnPause);
        GameMenu.release(pauseMenu);
        GameMenu.release(settingMenu);

        GameObject.release(map);
        GameObject.release(scanner);
        GameObject.release(player);
        GameObject.release(beats);
        GameObject.release(fire);
        GameObject.release(smoke);
        GameObject.release(floatingInfo);
        GameObject.release(hud);

        GameSound.releaseSE();
        GameSound.releaseBGM();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (bIsGamePaused) {
            currentMenu.draw(canvas, paint);
            return;
        }

        map.draw(canvas, paint);
        player.draw(canvas, paint);

        if (bIsTutorial) {
            tutorial.draw(canvas, paint);
            return;
        }

        if (!bIsGameSummary) {
            scanner.draw(canvas, paint);
            beats.draw(canvas, paint);
            fire.draw(canvas, paint);
            smoke.draw(canvas, paint);

            if (!bIsGameClear && !bIsGameOver) {
                if (controlMode == GameSettings.CONTROL_BUTTON) {
                    gamePad.draw(canvas, paint);
                }
                floatingInfo.draw(canvas, paint);
                btnPause.draw(canvas, paint);
            }
        }

        hud.draw(canvas, paint);
    }

    @Override
    public void update(int elapsedTime) {

        if (bIsGamePaused) {
            currentMenu.update(elapsedTime);
            return;
        }

        if (bIsTutorial) {
            tutorial.update(elapsedTime);
            if (tutorial.isOver()) {
                bIsTutorial = false;
                GameSound.startBGM(ID_BGM);
            }
            return;
        }

        int elapsedBgmTime = GameSound.getCurrentPosition() - currentTime;
        currentTime = GameSound.getCurrentPosition();
        if (currentTime < totalTime) {
            countDownTimer = totalTime - currentTime;
        } else {
            countDownTimer = 0;
            if (!bIsGameClear && !bIsGameOver) {
                bIsGameClear = true;
            }
        }

        player.update(elapsedBgmTime);
        beats.update(elapsedBgmTime);
        fire.update(elapsedBgmTime);
        smoke.update(elapsedBgmTime);
        floatingInfo.update(elapsedBgmTime);
        hud.update(elapsedBgmTime);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (bIsGamePaused) {
            currentMenu.onTouchEvent(event);
        } else if (bIsGameOver || bIsGameClear || bIsGameSummary) {
            hud.onTouchEvent(event);
        } else if (bIsTutorial) {
            tutorial.onTouchEvent(event);
        } else {
            float touch_x = event.getX();
            float touch_y = event.getY();
            if (event.getAction() == MotionEvent.ACTION_DOWN && btnPause.checkTouchPoint(touch_x, touch_y)) {
                bOnPauseBtnEvent = true;
            }
            if (bOnPauseBtnEvent) {
                btnPause.onTouchEvent(event);
            } else {
                if (controlMode == GameSettings.CONTROL_BUTTON && gamePad != null) {
                    gamePad.onTouchEvent(event);
                }
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                bOnPauseBtnEvent = false;
            }
        }
    }

    private void gamePause() {

        Bitmap bmpPause = Bitmap.createBitmap((int) screen_w, (int) screen_h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmpPause);
        Paint paint = new Paint();
        map.draw(canvas, paint);
        pauseMenu.setBackground(bmpPause);
        pauseMenu.open(null);

        bIsGamePaused = true;
        currentMenu = pauseMenu;
        GameSound.pauseBGM();
    }

    private void gameContinue() {

        bIsGamePaused = false;
        currentMenu = null;
        btnPause.reset();
        GameSound.startBGM();
    }

    private void gameOver() {

        bIsGameOver = true;
        GameSound.fadeOut(1000);
        ((Button) pauseMenu.getControl(PauseMenu.BTN_CONTINUE)).setState(Button.BUTTON_NORMAL);//TODO add disabled state
    }

    private void gameClear() {

        bIsGameClear = true;
        GameSound.fadeOut(1000);

    }

    private static final int BEAT_PERFECT = 10;
    private static final int BEAT_GOOD = 20;
    private static final int BEAT_BAD = 30;
    private static final int BEAT_MISS = 40;

    private void setBeatCombo(int type) {

        switch (type) {
            case BEAT_PERFECT:
                GameSound.playSE(ID_SE_PERFECT);
                player.setState(Player.PERFECT);
                comboCount++;
                break;
            case BEAT_GOOD:
                GameSound.playSE(ID_SE_GOOD);
                player.setState(Player.GOOD);
                comboCount++;
                break;
            case BEAT_BAD:
                GameSound.playSE(ID_SE_BAD);
                comboCount = 0;
                break;
            case BEAT_MISS:
                comboCount = 0;
                break;
        }

        if (comboCount > 0) {
            floatingInfo.add(FloatingInfo.INFO_COMBO, spacing * (colBeg + COLS_MIN - 5), spacing * (rowBeg + 4));
        }

        if (comboCount > maxCombo) {
            maxCombo = comboCount;
        }
        if (comboCount == 0) {
            fragmentCombo = 0;
        }

        if (comboCount / (gameLevel == GameLevel.IJIME ? 5 : 10) > fragmentCombo) {
            fragmentCount++;
            fragmentCombo++;
            if (Math.random() < (0.0002 * comboCount + 0.003 * charaLuc)) {
                goldFragmentCount++;
            } else {
                hopeFragmentCount++;
            }
        }
    }

    //classes for game play

    private class GameMap extends GameObject {

        Bitmap bmpMap;
        int[][] mapCode;

        GameMap() {

            float w = spacing * cols;
            float h = spacing * rows;

            Bitmap bmpRes = MainView.getBitmap(R.drawable.map);
            int resSize = bmpRes.getWidth();
            float scale = spacing / (resSize / 8);

            mapCode = new int[rows][cols];

            //Basic GameMap

            for (int i = 0; i < rows; i++)
                for (int j = 0; j < cols; j++)
                    mapCode[i][j] = Math.random() < 0.9 ? 0 : Math.random() < 0.5 ? 1 : 2;

            setMapCode(0, 3, 8);
            setMapCode(0, COLS_MIN - 1, 10);
            setMapCode(1, 3, 32);
            setMapCode(2, 3, 40);
            setMapCode(3, 3, 48);
            setMapCode(4, 3, 56);
            setMapCode(ROWS_MIN - 3, 3, 3);
            setMapCode(ROWS_MIN - 2, 3, 24);
            setMapCode(ROWS_MIN - 2, COLS_MIN - 1, 26);
            setMapCode(ROWS_MIN - 1, 3, 48);
            setMapCode(ROWS_MIN - 1, COLS_MIN - 1, 50);
            setMapCode(ROWS_MIN, 3, 48);
            setMapCode(ROWS_MIN, COLS_MIN - 1, 50);
            setMapCode(ROWS_MIN + 1, 3, 56);
            setMapCode(ROWS_MIN + 1, COLS_MIN - 1, 59);

            for (int c = 4; c < COLS_MIN - 1; c++) {
                if (c != COLS_MIN - 6 && c != COLS_MIN - 5) {
                    setMapCode(0, c, 9);
                    setMapCode(1, c, 17);
                    setMapCode(2, c, 17);
                    setMapCode(3, c, 59);
                }
                setMapCode(ROWS_MIN - 2, c, 25);
                setMapCode(ROWS_MIN - 1, c, 17);
                setMapCode(ROWS_MIN, c, 17);
                setMapCode(ROWS_MIN + 1, c, 59);
            }

            setMapCode(0, COLS_MIN - 7, 41);
            setMapCode(2, COLS_MIN - 6, 35);
            setMapCode(3, 4, 49);
            setMapCode(4, 4, 57);
            setMapCode(3, COLS_MIN - 6, 57);
            setMapCode(3, COLS_MIN - 4, 58);

            setMapCode(1, COLS_MIN - 1, 19);
            setMapCode(2, COLS_MIN - 1, 27);

            for (int r = 3; r < ROWS_MIN - 2; r++) {
                setMapCode(r, COLS_MIN - 1, 18);
            }

            //Curtain

            setMapCode(1, 11, 4);
            setMapCode(2, 11, 12);
            setMapCode(3, 11, 20);
            setMapCode(4, 11, 13);
            setMapCode(5, 11, 21);
            setMapCode(3, 12, 49);
            for (int r = 4; r < 6; r++) {
                setMapCode(r, 12, 43);
            }
            setMapCode(6, 12, 7);

            setMapCode(ROWS_MIN - 5, 11, 6);
            setMapCode(ROWS_MIN - 4, 11, 14);
            setMapCode(ROWS_MIN - 3, 11, 14);
            setMapCode(ROWS_MIN - 2, 11, 22);
            setMapCode(ROWS_MIN - 3, 12, 51);
            setMapCode(ROWS_MIN - 2, 12, 33);

            //Komaeda

            setMapCode(5, COLS_MIN - 4, 36);
            setMapCode(5, COLS_MIN - 3, 37);
            setMapCode(6, COLS_MIN - 4, 44);
            setMapCode(6, COLS_MIN - 3, 45);
            setMapCode(7, COLS_MIN - 4, 52);
            setMapCode(7, COLS_MIN - 3, 53);

            //Other

            setMapCode(4, COLS_MIN - 2, 15);
            setMapCode(5, COLS_MIN - 2, 23);
            setMapCode(6, COLS_MIN - 2, 31);
            setMapCode(ROWS_MIN - 4, COLS_MIN - 2, 46);
            setMapCode(ROWS_MIN - 3, COLS_MIN - 2, 54);

            setMapCode(ROWS_MIN - 3, 4, 38);
            setMapCode(ROWS_MIN - 3, 5, 39);
            setMapCode(ROWS_MIN - 4, 5, 38);
            setMapCode(ROWS_MIN - 4, 6, 39);
            setMapCode(ROWS_MIN - 3, COLS_MIN - 7, 38);
            setMapCode(ROWS_MIN - 3, COLS_MIN - 6, 39);

            setMapCode(4, 5, 28);
            setMapCode(4, 6, 29);
            setMapCode(4, 7, 30);
            setMapCode(5, 7, 28);
            setMapCode(5, 8, 29);
            setMapCode(5, 9, 29);
            setMapCode(5, 10, 30);

            //TODO modify the png resource, map code 33(border)

            //Create bitmap field for map

            bmpMap = Bitmap.createBitmap((int) w, (int) h, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmpMap);
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    canvas.save();
                    canvas.clipRect(spacing * j, spacing * i, spacing * (j + 1), spacing * (i + 1));
                    int cx = mapCode[i][j] % 8;
                    int cy = mapCode[i][j] / 8;
                    Matrix mat = new Matrix();
                    mat.postScale(scale, scale);
                    mat.postTranslate(spacing * (j - cx), spacing * (i - cy));
                    canvas.drawBitmap(bmpRes, mat, null);
                    canvas.restore();
                }
            }

            bmpRes.recycle();
        }

        @Override
        public void onDestroy() {

            bmpMap.recycle();
            bmpMap = null;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            canvas.drawBitmap(bmpMap, 0, 0, paint);
        }

        @Override
        public void update(int elapsedTime) {

        }

        private void setMapCode(int row, int col, int code) {

            int r = rowBeg + row;
            int c = colBeg + col;
            if (r >= 0 && r < rows && c >= 0 && c < cols)
                mapCode[rowBeg + row][colBeg + col] = code;
        }
    }

    private class Player extends SpriteObject {

        static final int NORMAL = 0;
        static final int NORMAL_BEND = 1;
        static final int GOOD = 10;
        static final int PERFECT = 20;

        private SingleSpriteObject shadow;

        Player() {

            x = spacing * (colBeg + COLS_MIN - 5);
            y = spacing * (rowBeg + 2.75f);
            w = spacing * 2;
            h = spacing * 9 / 4;
            offset_x = -w / 2;
            offset_y = -h;

            loadResource(CharaData.getResID(charaID));
            addSprite(NORMAL, 0f, 0f, 1f / 8f, 9f / 64f);
            addSprite(NORMAL_BEND, 1f / 8f, 0f, 1f / 8f, 9f / 64f);
            addSprite(GOOD, 0f, 36f / 64f, 1f / 8f, 9f / 64f);
            addSprite(PERFECT, 5f / 8f, 36f / 64f, 1f / 8f, 9f / 64f);
            releaseResource();

            addState(NORMAL, STATE_LOOP, (int) BASE_SPB, NORMAL, NORMAL_BEND);
            addState(GOOD, STATE_NO_LOOP, (int) actSPB / 2, GOOD);
            addState(PERFECT, STATE_NO_LOOP, (int) actSPB / 2, PERFECT);
            addStateChain(GOOD, NORMAL, NORMAL);
            addStateChain(PERFECT, NORMAL, NORMAL);
            setState(NORMAL);

            shadow = new SingleSpriteObject();
            shadow.setPosition(x, y);
            shadow.setSize(spacing * 2f, spacing / 2f);
            shadow.setOffset(-spacing, -spacing / 4f);
            shadow.setSprite(R.drawable.shadow, 0f, 0f, 0.5f, 0.5f);
        }

        @Override
        public void onDestroy() {

            super.onDestroy();
            shadow.onDestroy();
            shadow = null;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            shadow.draw(canvas, paint);
            super.draw(canvas, paint);
        }

        @Override
        public void update(int elapsedTime) {

            shadow.setPosition(x, y);
            super.update(elapsedTime);
        }
    }

    class Pitcher extends SpriteObject {

        static final int PITCHER_NORMAL = 10;
        static final int PITCHER_POISON = 20;
        static final int PITCHER_DEBUFF = 30;

        static final int R_STAY = 10;
        static final int R_STEP = 11;
        static final int L_STAY = 20;
        static final int L_STEP = 21;

        static final int R_MOVE = 10;
        static final int L_MOVE = 20;

        private int id;
        private int row;
        private int updateTime;

        private SingleSpriteObject shadow;

        Pitcher(int row, int type) {

            this.row = row;
            updateTime = 0;

            ArrayList<Integer> ids = beats == null ? new ArrayList<>() : beats.getPitcherIds();

            int id = CharaData.IDC_HINATA;
            if (type == PITCHER_NORMAL) {
                boolean bNew = true;
                while (bNew) {
                    bNew = false;
                    id = CharaData.getRandomPlayableCharaID();

                    if (id <= CharaData.IDC_HINATA || id >= CharaData.IDC_SAIHARA ||
                            id == CharaData.IDC_NANAMI || id == CharaData.IDC_KOMAEDA)
                        bNew = true;

                    for (int existId : ids) {
                        if (existId == id)
                            bNew = true;
                    }
                }
            } else if (type == PITCHER_POISON) {
                id = Math.random() < 0.2 ? CharaData.IDC_MONOMI : CharaData.IDC_NANAMI;
            } else if (type == PITCHER_DEBUFF) {
                id = Math.random() < 0.25 ? CharaData.IDC_MONOKUMA : CharaData.IDC_OMA;
            }

            setPosition(spacing * colBeg, spacing * (row + 1.5f));
            setSize(spacing * 2, spacing * 9 / 4);
            setOffset(-w / 2f, -h);

            loadResource(CharaData.getResID(id));
            addSprite(R_STAY, 2f / 8f, 27f / 64f, 1f / 8f, 9f / 64f);
            addSprite(R_STEP, 3f / 8f, 27f / 64f, 1f / 8f, 9f / 64f);
            addSprite(L_STAY, 2f / 8f, 18f / 64f, 1f / 8f, 9f / 64f);
            addSprite(L_STEP, 3f / 8f, 18f / 64f, 1f / 8f, 9f / 64f);
            releaseResource();

            addState(R_MOVE, STATE_LOOP, (int) actSPB / 3, R_STAY, R_STEP);
            addState(L_MOVE, STATE_LOOP, (int) actSPB / 3, L_STAY, L_STEP);
            setState(R_MOVE);

            shadow = new SingleSpriteObject();
            shadow.setPosition(x, y);
            shadow.setSize(spacing * 2f, spacing / 2f);
            shadow.setOffset(-spacing, -spacing / 4f);
            shadow.setSprite(R.drawable.shadow, 0f, 0f, 0.5f, 0.5f);
        }

        @Override
        public void onDestroy() {

            super.onDestroy();
            shadow.onDestroy();
            shadow = null;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            shadow.draw(canvas, paint);
            super.draw(canvas, paint);
        }

        @Override
        public void update(int elapsedTime) {

            updateTime += elapsedTime;

            if (updateTime <= 2 * actSPB) {
                x += elapsedTime * 1.2f * spacing / actSPB;
            } else {
                if (currentState != L_MOVE) setState(L_MOVE);
                x -= elapsedTime * 1.2f * spacing / actSPB;
            }

            shadow.setPosition(x, y);
            super.update(elapsedTime);
        }

        int getId() {

            return id;
        }
    }

    class Beats extends SpriteObject {

        static final int BEAT_NORMAL = 10;
        static final int BEAT_POISON = 20;
        static final int BEAT_DEBUFF = 30;
        static final int BEAT_HIDDEN = 40;
        static final int BEAT_NORMAL_SHINE = 11;
        static final int BEAT_POISON_SHINE = 21;
        static final int BEAT_DEBUFF_SHINE = 31;
        static final int BEAT_HIDDEN_SHINE = 41;

        private float bad_left;
        private float bad_right;
        private float good_left;
        private float good_right;
        private float perfect_left;
        private float perfect_right;
        private float perfect_center;

        private ArrayList<Pitcher> pitchers;
        private ArrayList<BeatState> beatStates;

        private int lastPoison;
        private int lastDebuff;

        private int beatCount;
        private int currentBeat;
        private int pretreatBeat;
        private boolean bIsCurrentBeaten;

        Beats() {

            setSize(spacing, spacing / 2f);
            setOffset(-w / 2f, -h / 2f);

            loadResource(R.drawable.item01);
            addSprite(BEAT_NORMAL, 0f / 4f, 0f / 8f, 1f / 4f, 1f / 8f);
            addSprite(BEAT_POISON, 1f / 4f, 0f / 8f, 1f / 4f, 1f / 8f);
            addSprite(BEAT_DEBUFF, 2f / 4f, 0f / 8f, 1f / 4f, 1f / 8f);
            addSprite(BEAT_HIDDEN, 3f / 4f, 0f / 8f, 1f / 4f, 1f / 8f);
            addSprite(BEAT_NORMAL_SHINE, 0f / 4f, 1f / 8f, 1f / 4f, 1f / 8f);
            addSprite(BEAT_POISON_SHINE, 1f / 4f, 1f / 8f, 1f / 4f, 1f / 8f);
            addSprite(BEAT_DEBUFF_SHINE, 2f / 4f, 1f / 8f, 1f / 4f, 1f / 8f);
            addSprite(BEAT_HIDDEN_SHINE, 3f / 4f, 1f / 8f, 1f / 4f, 1f / 8f);
            releaseResource();

            addState(BEAT_NORMAL, STATE_SINGLE, 0, BEAT_NORMAL);
            addState(BEAT_POISON, STATE_SINGLE, 0, BEAT_POISON);
            addState(BEAT_DEBUFF, STATE_SINGLE, 0, BEAT_DEBUFF);
            addState(BEAT_HIDDEN, STATE_SINGLE, 0, BEAT_HIDDEN);
            addState(BEAT_NORMAL_SHINE, STATE_SINGLE, 0, BEAT_NORMAL_SHINE);
            addState(BEAT_POISON_SHINE, STATE_SINGLE, 0, BEAT_POISON_SHINE);
            addState(BEAT_DEBUFF_SHINE, STATE_SINGLE, 0, BEAT_DEBUFF_SHINE);
            addState(BEAT_HIDDEN_SHINE, STATE_SINGLE, 0, BEAT_HIDDEN_SHINE);

            bad_left = spacing * (colBeg + 11);
            good_left = spacing * (colBeg + 13);
            perfect_left = spacing * (colBeg + 14);
            perfect_center = spacing * (colBeg + 15);
            perfect_right = spacing * (colBeg + 16);
            good_right = spacing * (colBeg + 17);
            bad_right = spacing * (colBeg + 18);

            pitchers = new ArrayList<>();
            beatStates = new ArrayList<>();

            int beatBegin = (int) (BASE_SPB * 2 / actSPB) + 4;
            int beatEnd = (int) ((totalTime - BEG_TIME) / actSPB);
            beatCount = beatEnd - beatBegin;
            lastPoison = poisonGap / 2;
            lastDebuff = debuffGap / 2;
            for (int i = beatBegin; i < beatEnd; i++) {
                addBeat(i);
            }
            currentBeat = 0;
            pretreatBeat = Math.min(currentBeat + 20, beatCount);
            bIsCurrentBeaten = false;
        }

        @Override
        public void onDestroy() {

            for (Pitcher pitcher : pitchers)
                pitcher.onDestroy();
            pitchers.clear();
            pitchers = null;

            beatStates.clear();
            beatStates = null;

            super.onDestroy();
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            for (int row = rowBeg + 4; row < rowBeg + 8; row++) {
                for (Pitcher pitcher : pitchers) {
                    if (pitcher.row == row)
                        pitcher.draw(canvas, paint);
                }
            }

            for (int i = currentBeat; i < pretreatBeat; i++) {
                BeatState state = beatStates.get(i);
                if (state.bActive) {
                    if (hiddenTimer >= 0 && (int) (hiddenTimer / BASE_SPB) % 2 == 0) {
                        setState(state.x >= bad_left ? BEAT_HIDDEN_SHINE : BEAT_HIDDEN);
                    } else {
                        setState(state.x >= bad_left ? state.type + 1 : state.type);
                    }
                    setPosition(state.x, state.y);
                    super.draw(canvas, paint);
                }
            }
        }

        @Override
        public void update(int elapsedTime) {

            if (bIsCurrentBeaten) {
                bIsCurrentBeaten = false;
                beatStates.get(currentBeat).bActive = false;
                increaseIndexRange();
            }

            for (int i = currentBeat; i < pretreatBeat; i++) {

                BeatState state = beatStates.get(i);

                if (state.bIn && currentTime >= state.begTime + adjustTime - 2 * actSPB) {
                    switch (state.type) {
                        case BEAT_NORMAL:
                            pitchers.add(new Pitcher(state.row, Pitcher.PITCHER_NORMAL));
                            break;
                        case BEAT_POISON:
                            pitchers.add(new Pitcher(state.row, Pitcher.PITCHER_POISON));
                            break;
                        case BEAT_DEBUFF:
                            pitchers.add(new Pitcher(state.row, Pitcher.PITCHER_DEBUFF));
                            break;
                    }
                    state.bIn = false;
                }

                if (currentTime >= state.begTime + adjustTime) {
                    if (state.bPitch) {
                        state.bActive = true;
                        state.bPitch = false;
                    }
                    state.x = computePosition(currentTime, state);
                }

                if (state.bActive) {

                    if (state.x >= perfect_center) {
                        if (state.type == BEAT_DEBUFF) {
                            setBeatCombo(BEAT_PERFECT);
                            floatingInfo.add(FloatingInfo.INFO_PERFECT, state.x, state.y - spacing / 2);
                            state.bActive = false;
                            increaseIndexRange();
                            if (bAutoDebuff) {
                                if (Math.random() < 0.01 * charaLuc) {
                                    floatingInfo.add(FloatingInfo.INFO_NO_EFFECT, state.x, state.y);
                                } else {
                                    if (!bCombDebuff) {
                                        fireTimer = -1;
                                        smokeTimer = -1;
                                        hiddenTimer = -1;
                                    }
                                    switch ((int) (Math.random() * 3)) {
                                        case 0:
                                            floatingInfo.add(FloatingInfo.INFO_FIRE, state.x, state.y);
                                            fireTimer = debuffTime;
                                            fire.setCount(4);
                                            break;
                                        case 1:
                                            floatingInfo.add(FloatingInfo.INFO_SMOKE, state.x, state.y);
                                            smokeTimer = debuffTime;
                                            smoke.putOn();
                                            break;
                                        case 2:
                                            floatingInfo.add(FloatingInfo.INFO_HIDDEN, state.x, state.y);
                                            hiddenTimer = debuffTime;
                                            break;
                                    }
                                }
                            }
                        } else if (MainView.bIsDebugMode) {
                            if (state.type == BEAT_POISON && poisonTimer <= 0) {
                                setBeatCombo(BEAT_MISS);
                                poisonTimer = poisonTime;
                            } else {
                                setBeatCombo(BEAT_PERFECT);
                            }
                            state.bActive = false;
                            increaseIndexRange();
                        }
                    }

                    if (state.x >= bad_right) {

                        addBeat(BEAT_MISS);
                        floatingInfo.add(FloatingInfo.INFO_MISS, state.x, state.y - spacing / 2);
                        state.bActive = false;
                        increaseIndexRange();
                        if (state.type == BEAT_NORMAL) {
                            fire.increase();
                        } else if (state.type == BEAT_POISON) {
                            if (Math.random() < 0.01 * charaLuc) {
                                floatingInfo.add(FloatingInfo.INFO_NO_EFFECT, spacing * (colBeg + COLS_MIN - 4), spacing * (rowBeg + 7));
                            } else {
                                floatingInfo.add(FloatingInfo.INFO_POISON, spacing * (colBeg + COLS_MIN - 4), spacing * (rowBeg + 7));
                                if (poisonTimer > 0) {
                                    if (bIsGameOver) {
                                        bIsGameOver = true;
                                        deathReason = DEATH_POISON;
                                    }
                                } else {
                                    poisonTimer = poisonTime;
                                }
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < pitchers.size(); i++) {
                Pitcher pitcher = pitchers.get(i);
                pitcher.update(elapsedTime);
                if (pitcher.getX() < spacing * colBeg) {
                    pitchers.remove(i--);
                }
            }
        }

        ArrayList<Integer> getPitcherIds() {

            ArrayList<Integer> ids = new ArrayList<>();
            for (Pitcher pitcher : pitchers) {
                ids.add(pitcher.getId());
            }
            return ids;
        }

        void addBeat(int index) {

            BeatState state = new BeatState();

            state.row = rowBeg + 4 + (int) (Math.random() * 4f);
            state.type = BEAT_NORMAL;

            if (lastPoison > poisonGap && Math.random() < 0.5) {
                state.type = BEAT_POISON;
                lastPoison = 0;
                lastDebuff++;
            } else if (lastDebuff > (debuffGap + charaStr / 6) && Math.random() < (0.5 - charaStr * 0.016)) {
                state.type = BEAT_DEBUFF;
                lastPoison++;
                lastDebuff = 0;
            } else {
                lastPoison++;
                lastDebuff++;
            }

            state.beatTime = (int) (BEG_TIME + actSPB * index);
            state.begTime = (int) (BEG_TIME + actSPB * (index - 2.5));
            state.lastTime = (int) actSPB;
            state.x = 0f;
            state.y = spacing * (state.row + 0.5f);
            state.bIn = true;
            state.bPitch = true;
            state.bActive = false;

            beatStates.add(state);
        }

        void increaseIndexRange() {

            currentBeat++;
            if (pretreatBeat < beatCount) {
                pretreatBeat++;
            }
        }

        void checkBeat(int action) {

            if (MainView.bIsDebugMode) return;

            if (currentBeat >= beatStates.size()) return;

            BeatState state = beatStates.get(currentBeat);
            if (!state.bActive) return;

            int beatTime = GameSound.getCurrentPosition();
            float beatPosition = computePosition(beatTime, state);

            if (action == ACT_A) {

                switch (state.type) {

                    case BEAT_NORMAL:
                        if (beatPosition > perfect_left && beatPosition < perfect_right) {
                            setBeatCombo(BEAT_PERFECT);
                            fire.decrease();
                            floatingInfo.add(FloatingInfo.INFO_PERFECT, beatPosition, state.y);
                        } else if (beatPosition > good_left && beatPosition < good_right) {
                            setBeatCombo(BEAT_GOOD);
                            fire.decrease();
                            floatingInfo.add(FloatingInfo.INFO_GOOD, beatPosition, state.y);
                        } else if (beatPosition > bad_left && beatPosition < bad_right) {
                            setBeatCombo(BEAT_BAD);
                            floatingInfo.add(FloatingInfo.INFO_BAD, beatPosition, state.y);
                        } else {
                            setBeatCombo(BEAT_MISS);
                            fire.increase();
                            floatingInfo.add(FloatingInfo.INFO_MISS, beatPosition, state.y);
                        }
                        break;

                    case BEAT_POISON:
                        setBeatCombo(BEAT_MISS);
                        floatingInfo.add(FloatingInfo.INFO_MISS, beatPosition, state.y);
                        if (Math.random() < 0.01 * charaLuc) {
                            floatingInfo.add(FloatingInfo.INFO_NO_EFFECT, spacing * (colBeg + COLS_MIN - 4), spacing * (rowBeg + 7));
                        } else {
                            floatingInfo.add(FloatingInfo.INFO_POISON, spacing * (colBeg + COLS_MIN - 4), spacing * (rowBeg + 7));
                            if (poisonTimer > 0) {
                                if (bIsGameOver) {
                                    bIsGameOver = true;
                                    deathReason = DEATH_POISON;
                                }
                            } else {
                                poisonTimer = poisonTime;
                            }
                        }
                        break;

                    case BEAT_DEBUFF:

                        if (Math.random() < 0.01 * charaLuc) {
                            floatingInfo.add(FloatingInfo.INFO_NO_EFFECT, beatPosition, state.y);
                        } else {
                            if (!bCombDebuff) {
                                fireTimer = -1;
                                smokeTimer = -1;
                                hiddenTimer = -1;
                            }
                            switch ((int) (Math.random() * 3)) {
                                case 0:
                                    floatingInfo.add(FloatingInfo.INFO_FIRE, beatPosition, state.y);
                                    fireTimer = debuffTime;
                                    fire.setCount(4);
                                    break;
                                case 1:
                                    floatingInfo.add(FloatingInfo.INFO_SMOKE, beatPosition, state.y);
                                    smokeTimer = debuffTime;
                                    smoke.putOn();
                                    break;
                                case 2:
                                    floatingInfo.add(FloatingInfo.INFO_HIDDEN, beatPosition, state.y);
                                    hiddenTimer = debuffTime;
                                    break;
                            }
                        }
                        break;
                }
            }

            if (action == ACT_B) {

                switch (state.type) {

                    case BEAT_NORMAL:
                        setBeatCombo(BEAT_MISS);
                        fire.increase();
                        floatingInfo.add(FloatingInfo.INFO_MISS, beatPosition, state.y);
                        break;

                    case BEAT_POISON:
                        if (beatPosition > perfect_left && beatPosition < perfect_right) {
                            setBeatCombo(BEAT_PERFECT);
                            floatingInfo.add(FloatingInfo.INFO_PERFECT, beatPosition, state.y);
                        } else if (beatPosition > good_left && beatPosition < good_right) {
                            setBeatCombo(BEAT_GOOD);
                            floatingInfo.add(FloatingInfo.INFO_GOOD, beatPosition, state.y);
                        } else if (beatPosition > bad_left && beatPosition < bad_right) {
                            setBeatCombo(BEAT_BAD);
                            floatingInfo.add(FloatingInfo.INFO_BAD, beatPosition, state.y);
                        } else {
                            setBeatCombo(BEAT_MISS);
                            floatingInfo.add(FloatingInfo.INFO_MISS, beatPosition, state.y);
                            if (Math.random() < 0.01 * charaLuc) {
                                floatingInfo.add(FloatingInfo.INFO_NO_EFFECT, spacing * (colBeg + COLS_MIN - 4), spacing * (rowBeg + 7));
                            } else {
                                floatingInfo.add(FloatingInfo.INFO_POISON, spacing * (colBeg + COLS_MIN - 4), spacing * (rowBeg + 7));
                                if (poisonTimer > 0) {
                                    if (bIsGameOver) {
                                        bIsGameOver = true;
                                        deathReason = DEATH_POISON;
                                    }
                                } else {
                                    poisonTimer = poisonTime;
                                }
                            }
                        }
                        break;

                    case BEAT_DEBUFF:
                        if (Math.random() < 0.01 * charaLuc) {
                            floatingInfo.add(FloatingInfo.INFO_NO_EFFECT, beatPosition, state.y);
                        } else {
                            if (!bCombDebuff) {
                                fireTimer = -1;
                                smokeTimer = -1;
                                hiddenTimer = -1;
                            }
                            switch ((int) (Math.random() * 3)) {
                                case 0:
                                    floatingInfo.add(FloatingInfo.INFO_FIRE, beatPosition, state.y);
                                    fireTimer = debuffTime;
                                    fire.setCount(4);
                                    break;
                                case 1:
                                    floatingInfo.add(FloatingInfo.INFO_SMOKE, beatPosition, state.y);
                                    smokeTimer = debuffTime;
                                    smoke.putOn();
                                    break;
                                case 2:
                                    floatingInfo.add(FloatingInfo.INFO_HIDDEN, beatPosition, state.y);
                                    hiddenTimer = debuffTime;
                                    break;
                            }
                        }
                        break;
                }
            }

            bIsCurrentBeaten = true;
        }

        float computePosition(int time, BeatState state) {

            return spacing * (colBeg + 3f + 12f * (time - state.begTime - adjustTime) / (state.beatTime - state.begTime));
        }

        class BeatState {
            int type;
            int row;
            int begTime;
            int beatTime;
            int lastTime;
            float x;
            float y;
            boolean bIn;
            boolean bPitch;
            boolean bActive;
        }
    }

    class Scanner extends GameObject {

        int alpha;

        Scanner(int alpha) {

            this.alpha = alpha;
        }

        void setAlpha(int alpha) {

            this.alpha = alpha;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            for (int i = 0; i < 7; i++) {

                int color = Color.WHITE;
                switch (i) {
                    case 0:
                    case 1:
                    case 6:
                        color = 0xFF888800;
                        break;
                    case 2:
                    case 5:
                        color = 0xFF008800;
                        break;
                    case 3:
                    case 4:
                        color = 0xFF000088;
                        break;
                }
                paint.setColor(color);
                paint.setAlpha(alpha);

                int col = colBeg + COLS_MIN - 9 + i;
                for (int row = rowBeg + 4; row < rowBeg + 8; row++) {

                    RectF rectScanner = new RectF(spacing * col, spacing * row,
                            spacing * (col + 15f / 16f), spacing * (row + 15f / 16f));
                    canvas.drawRect(rectScanner, paint);
                }

                paint.setAlpha(255);
            }
        }

        @Override
        public void update(int elapsedTime) {

            if (alpha != ShooterScene.scannerAlpha) {
                setAlpha(ShooterScene.scannerAlpha);
            }
        }
    }

    class Fire extends SpriteObject {

        static final int MAX_COUNT = 6;
        static final int ROW_COUNT = 6;
        static final int COL_COUNT = 7;
        private int mapFireStates[];
        private int fireStates[][];
        private int minCount;
        private int currentCount;
        private int updateTime;

        public Fire() {

            setSize(spacing, spacing);
            loadResource(R.drawable.item01);
            addSprite(0, 0f / 4f, 1f / 4f, 1f / 4f, 1f / 4f);
            addSprite(1, 1f / 4f, 1f / 4f, 1f / 4f, 1f / 4f);
            addSprite(2, 2f / 4f, 1f / 4f, 1f / 4f, 1f / 4f);
            addSprite(3, 3f / 4f, 1f / 4f, 1f / 4f, 1f / 4f);
            releaseResource();

            mapFireStates = new int[4];
            for (int i = 0; i < mapFireStates.length; i++) {
                mapFireStates[i] = (int) (Math.random() * 4);
            }

            fireStates = new int[COL_COUNT][ROW_COUNT];
            for (int col = 0; col < COL_COUNT; col++) {
                for (int row = 0; row < ROW_COUNT; row++) {
                    fireStates[col][row] = -1;
                }
            }

            minCount = 0;
            currentCount = 0;
            updateTime = 0;
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            for (int i = 0; i < mapFireStates.length; i++) {
                setGridPosition(7, i >= 2 ? i + 2 : i + 1);
                setSprite(mapFireStates[i]);
                super.draw(canvas, paint);
            }
        }

        @Override
        public void update(int elapsedTime) {

            updateTime += elapsedTime;
            if (updateTime >= BASE_SPB / 4) {
                updateTime -= BASE_SPB / 4;
                for (int i = 0; i < mapFireStates.length; i++) {
                    mapFireStates[i] = (mapFireStates[i] + 1) % 4;
                }
            }
        }

        private void setGridPosition(int col, int row) {

            setPosition(spacing * (colBeg + 11 + col), spacing * (rowBeg + 3 + row));
        }

        public void decrease() {

            if (currentCount > minCount) {
                currentCount--;
                putOffRandomFire();
            }
        }

        public void increase() {

            currentCount++;
            if (currentCount > MAX_COUNT) {
                if (!bIsGameOver) {
                    bIsGameOver = true;
                    deathReason = DEATH_FIRE;
                }
            }
            addRandomFire();
        }

        public void setCount(int count) {

            minCount = count;
            while (currentCount < minCount) {
                increase();
            }
        }

        private void setFire(int col, int row, boolean bIsFireOn) {

            if (bIsFireOn) {
                fireStates[col][row] = (int) (Math.random() * 4);
            } else {
                fireStates[col][row] = -1;
            }
        }

        private void addRandomFire() {

            if (!bIsGameOver) {

                ArrayList<Point> availableGrids = getRequiredGrids(LIFE_FIRE, false);
                if (availableGrids.isEmpty()) return;
                int i = (int) (Math.random() * availableGrids.size());
                int col = availableGrids.get(i).x;
                int row = availableGrids.get(i).y;
                setFire(col, row, true);

                availableGrids = getRequiredGrids(CURTAIN_FIRE, false);
                if (availableGrids.isEmpty()) return;
                i = (int) (Math.random() * availableGrids.size());
                col = availableGrids.get(i).x;
                row = availableGrids.get(i).y;
                setFire(col, row, true);
            } else {

                int freeCount = 1;
                ArrayList<Point> availableGrids = getRequiredGrids(BODY_FIRE, false);
                if (availableGrids.isEmpty()) {
                    freeCount = 2;
                } else {
                    int i = (int) (Math.random() * availableGrids.size());
                    int col = availableGrids.get(i).x;
                    int row = availableGrids.get(i).y;
                    setFire(col, row, true);
                }

                availableGrids = getRequiredGrids(FREE_FIRE, false);
                while (freeCount > 0 && !availableGrids.isEmpty()) {
                    int i = (int) (Math.random() * availableGrids.size());
                    int col = availableGrids.get(i).x;
                    int row = availableGrids.get(i).y;
                    setFire(col, row, true);
                    freeCount--;
                    availableGrids = getRequiredGrids(FREE_FIRE, false);
                }
            }
        }

        private void putOffRandomFire() {

            if (currentCount == 0) return;

            ArrayList<Point> onFireGrids = getRequiredGrids(LIFE_FIRE, true);
            int i = (int) (Math.random() * onFireGrids.size());
            int col = onFireGrids.get(i).x;
            int row = onFireGrids.get(i).y;
            setFire(col, row, false);

            onFireGrids = getRequiredGrids(CURTAIN_FIRE, true);
            i = (int) (Math.random() * onFireGrids.size());
            col = onFireGrids.get(i).x;
            row = onFireGrids.get(i).y;
            setFire(col, row, false);
        }

        private static final int CURTAIN_FIRE = 10;
        private static final int LIFE_FIRE = 20;
        private static final int BODY_FIRE = 30;
        private static final int FREE_FIRE = 40;

        private ArrayList<Point> getRequiredGrids(int type, boolean bIsFireOn) {

            ArrayList<Point> grids = new ArrayList<>();

            switch (type) {

                case CURTAIN_FIRE:
                    for (int row = 0; row < ROW_COUNT; row++) {
                        if (bIsFireOn) {
                            if (fireStates[0][row] >= 0) {
                                grids.add(new Point(0, row));
                            }
                        } else {
                            if (fireStates[0][row] == -1) {
                                grids.add(new Point(0, row));
                            }
                        }
                    }
                    break;

                case LIFE_FIRE:
                    for (int col = 5; col < COL_COUNT; col++) {
                        for (int row = 0; row < ROW_COUNT; row++) {
                            if (row == 0 || row == 1 || row == 5) {
                                if (bIsFireOn) {
                                    if (fireStates[col][row] >= 0) {
                                        grids.add(new Point(col, row));
                                    }
                                } else {
                                    if (fireStates[col][row] == -1) {
                                        grids.add(new Point(col, row));
                                    }
                                }
                            }
                        }
                    }
                    break;

                case BODY_FIRE:
                    for (int col = 5; col < COL_COUNT; col++) {
                        for (int row = 2; row < 5; row++) {
                            if (bIsFireOn) {
                                if (fireStates[col][row] >= 0) {
                                    grids.add(new Point(col, row));
                                }
                            } else {
                                if (fireStates[col][row] == -1) {
                                    grids.add(new Point(col, row));
                                }
                            }
                        }
                    }
                    break;

                case FREE_FIRE:
                    for (int col = 1; col < 5; col++) {
                        for (int row = 0; row < ROW_COUNT; row++) {
                            if (bIsFireOn) {
                                if (fireStates[col][row] >= 0) {
                                    grids.add(new Point(col, row));
                                }
                            } else {
                                if (fireStates[col][row] == -1) {
                                    grids.add(new Point(col, row));
                                }
                            }
                        }
                    }
                    break;
            }
            return grids;
        }
    }

    class Smoke extends SpriteObject {

        private static final int DEFAULT_COUNT = 128;
        private int count;
        private RectF region;
        private ArrayList<SmokeState> smokeStates;
        private boolean bIsOn;
        private boolean bIsAllOff;

        public Smoke() {

            setSize(spacing, spacing);

            loadResource(R.drawable.item01);
            addSprite(0, 0f / 4f, 1f / 2f, 1f / 4f, 1f / 4f);
            addSprite(1, 1f / 4f, 1f / 2f, 1f / 4f, 1f / 4f);
            addSprite(2, 2f / 4f, 1f / 2f, 1f / 4f, 1f / 4f);
            addSprite(3, 3f / 4f, 1f / 2f, 1f / 4f, 1f / 4f);
            releaseResource();

            region = new RectF();
            //TODO set region

            smokeStates = new ArrayList<>();
            count = DEFAULT_COUNT;
            for (int i = 0; i < count; i++) {
                SmokeState state = new SmokeState();
                state.x = region.left + (float) Math.random() * region.width();
                state.y = region.top + (float) Math.random() * region.height();
                state.updateTime = (int) (-Math.random() * BASE_SPB);
            }

            bIsOn = false;
            bIsAllOff = true;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            smokeStates.clear();
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            if (bIsAllOff) return;

            for (SmokeState state : smokeStates) {
                if (state.updateTime > 0) {
                    int iSprite = (int) (4f * state.updateTime / BASE_SPB);
                    if (iSprite > 3) {
                        iSprite = 3;
                    }
                    setSprite(iSprite);
                    setPosition(state.x, state.y);
                    super.draw(canvas, paint);
                }
            }
        }

        @Override
        public void update(int elapsedTime) {

            bIsAllOff = true;

            for (SmokeState state : smokeStates) {
                if (state.updateTime > 0) {
                    state.updateTime += elapsedTime;
                    state.y -= elapsedTime * 0.001f * spacing;
                    if (state.updateTime >= BASE_SPB) {
                        state.updateTime -= BASE_SPB;
                        state.x = region.left + (float) Math.random() * region.width();
                        state.y = region.top + (float) Math.random() * region.height();
                    }
                    bIsAllOff = false;
                } else {
                    if (bIsOn) {
                        state.updateTime += elapsedTime;
                    }
                }
            }
        }

        public void putOn() {

            bIsOn = true;
        }

        public void putOff() {

            bIsOn = false;
        }

        private class SmokeState {
            float x;
            float y;
            int updateTime;
        }
    }

    class FloatingInfo extends GameObject {

        static final int INFO_PERFECT = 10;
        static final int INFO_GOOD = 11;
        static final int INFO_BAD = 12;
        static final int INFO_MISS = 13;
        static final int INFO_COMBO = 14;
        static final int INFO_POISON = 20;
        static final int INFO_SMOKE = 21;
        static final int INFO_FIRE = 22;
        static final int INFO_HIDDEN = 23;
        static final int INFO_NO_EFFECT = 24;

        private int textSize;
        private ArrayList<TextInfo> textInfos;

        FloatingInfo() {

            textSize = (int) (40f * rate);
            textInfos = new ArrayList<>();
        }

        @Override
        public void onDestroy() {

            textInfos.clear();
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            for (int i = 0; i < textInfos.size(); i++) {

                TextInfo info = textInfos.get(i);
                int alpha = 255 - (int) (255f * info.time / BASE_SPB);
                paint.setColor(info.color);
                paint.setAlpha(alpha);
                paint.setTextSize(textSize);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(info.text, info.x, info.y, paint);
            }
            paint.setAlpha(255);
        }

        @Override
        public void update(int elapsedTime) {

            for (int i = 0; i < textInfos.size(); i++) {

                TextInfo info = textInfos.get(i);
                info.time += elapsedTime;
                info.y -= 0.2f * rate * elapsedTime;
                if (info.time >= BASE_SPB) {
                    textInfos.remove(i--);
                }
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        void add(int type, float x, float y) {

            TextInfo info = new TextInfo();
            info.type = type;
            info.time = 0;
            info.x = x;
            info.y = y;

            switch (info.type) {
                case INFO_PERFECT:
                    info.text = "Perfect!";
                    info.color = Color.YELLOW;
                    break;
                case INFO_GOOD:
                    info.text = "Good!";
                    info.color = Color.GREEN;
                    break;
                case INFO_BAD:
                    info.text = "Bad";
                    info.color = Color.WHITE;
                    break;
                case INFO_MISS:
                    info.text = "Miss";
                    info.color = Color.RED;
                    break;
                case INFO_POISON:
                    info.text = "Poisoned";
                    info.color = 0xFF008800;
                    break;
                case INFO_SMOKE:
                    info.text = "Smoke!";
                    info.color = Color.GRAY;
                    break;
                case INFO_FIRE:
                    info.text = "Fire!";
                    info.color = 0xFFFF4422;
                    break;
                case INFO_HIDDEN:
                    info.text = "Hidden!";
                    info.color = Color.GRAY;
                    break;
                case INFO_NO_EFFECT:
                    info.text = "No Effect!";
                    info.color = 0xFFFFFF88;
                    break;
                case INFO_COMBO:
                    info.text = "Combo x " + comboCount;
                    info.color = Color.YELLOW;
                    break;
            }

            textInfos.add(info);
        }

        class TextInfo {
            int type;
            int color;
            float x, y;
            float time;
            String text;
        }
    }

    class HUD extends GameObject {

        SpriteObject hopeFragment;
        SpriteObject goldFragment;
        StaticText hopeFragmentCounter;
        StaticText goldFragmentCounter;

        Indicator scoreIndicator;
        Indicator countDownIndicator;
        Indicator fireTimerIndicator;
        Indicator smokeTimerIndicator;
        Indicator hiddenTimerIndicator;
        Indicator poisonTimerIndicator;
        StaticText recoveryText;

        boolean bAccelerate;
        int curtainAlpha;
        int currentScore;
        Border border;

        public HUD() {

            hopeFragment = new SpriteObject();
            hopeFragment.setSize(spacing, 2 * spacing);
            hopeFragment.setOffset(-spacing / 2, -spacing);
            hopeFragment.setPosition(spacing, spacing);
            hopeFragment.loadResource(R.drawable.item00);
            hopeFragment.addSprite(0, 0f / 8f, 0f / 4f, 1f / 8f, 1f / 4f);
            hopeFragment.addSprite(1, 1f / 8f, 0f / 4f, 1f / 8f, 1f / 4f);
            hopeFragment.addSprite(2, 2f / 8f, 0f / 4f, 1f / 8f, 1f / 4f);
            hopeFragment.addSprite(3, 3f / 8f, 0f / 4f, 1f / 8f, 1f / 4f);
            hopeFragment.releaseResource();
            hopeFragment.addState(0, SpriteObject.STATE_LOOP, (int) BASE_SPB / 4, 0, 1, 2, 3);
            hopeFragment.setState(0);

            goldFragment = new SpriteObject();
            goldFragment.setSize(spacing, 2 * spacing);
            goldFragment.setOffset(-spacing / 2, -spacing);
            goldFragment.setPosition(spacing, spacing * 2.5f);
            goldFragment.loadResource(R.drawable.item00);
            goldFragment.addSprite(0, 0f / 8f, 1f / 4f, 1f / 8f, 1f / 4f);
            goldFragment.addSprite(1, 1f / 8f, 1f / 4f, 1f / 8f, 1f / 4f);
            goldFragment.addSprite(2, 2f / 8f, 1f / 4f, 1f / 8f, 1f / 4f);
            goldFragment.addSprite(3, 3f / 8f, 1f / 4f, 1f / 8f, 1f / 4f);
            goldFragment.releaseResource();
            goldFragment.addState(0, SpriteObject.STATE_LOOP, (int) BASE_SPB / 4, 0, 1, 2, 3);
            goldFragment.setState(0);

            hopeFragmentCounter = new StaticText("×  " + 0, spacing, spacing / 2, spacing * 2, spacing);
            hopeFragmentCounter.setTextSize((int) (56f * rate));
            hopeFragmentCounter.setTextAlign(Paint.Align.CENTER);
            hopeFragmentCounter.setTextColor(Color.WHITE);

            goldFragmentCounter = new StaticText("×  " + 0, spacing, spacing * 2, spacing * 2, spacing);
            goldFragmentCounter.setTextSize((int) (56f * rate));
            goldFragmentCounter.setTextAlign(Paint.Align.CENTER);
            goldFragmentCounter.setTextColor(Color.WHITE);

            scoreIndicator = new Indicator("Score:", spacing / 2, screen_h - spacing * 2, spacing * 2, spacing, spacing * 2 / 3);
            scoreIndicator.setTextSize((int) (48f * rate));
            scoreIndicator.setTextAlign(Paint.Align.LEFT);
            scoreIndicator.setTextColor(Color.WHITE);

            countDownIndicator = new Indicator("Time:", spacing / 2, screen_h - spacing * 3.5f, spacing * 2, spacing, spacing * 2 / 3);
            countDownIndicator.setTextSize((int) (48f * rate));
            countDownIndicator.setTextAlign(Paint.Align.LEFT);
            countDownIndicator.setTextColor(Color.WHITE);

            fireTimerIndicator = new Indicator("Fire:", spacing * (colBeg + 10f), spacing * (rowBeg + 8f), spacing * 3, spacing / 2, spacing * 2 / 3);
            fireTimerIndicator.setTextSize((int) (48f * rate));
            fireTimerIndicator.setTextAlign(Paint.Align.LEFT);
            fireTimerIndicator.setTextColor(0xFFFF6633);

            smokeTimerIndicator = new Indicator("Smoke:", spacing * (colBeg + 8f), spacing * (rowBeg + 8f), spacing * 3, spacing / 2, spacing * 2 / 3);
            smokeTimerIndicator.setTextSize((int) (48f * rate));
            smokeTimerIndicator.setTextAlign(Paint.Align.LEFT);
            smokeTimerIndicator.setTextColor(0xFFCCCCCC);

            hiddenTimerIndicator = new Indicator("Hidden:", spacing * (colBeg + 6f), spacing * (rowBeg + 8f), spacing * 3, spacing / 2, spacing * 2 / 3);
            hiddenTimerIndicator.setTextSize((int) (48f * rate));
            hiddenTimerIndicator.setTextAlign(Paint.Align.LEFT);
            hiddenTimerIndicator.setTextColor(0xFFCCCCCC);

            poisonTimerIndicator = new Indicator("Poison:", spacing * (colBeg + 16.2f), spacing * (rowBeg + 2.4f), spacing * 4, spacing / 2, spacing * 2 / 3);
            poisonTimerIndicator.setTextSize((int) (48f * rate));
            poisonTimerIndicator.setTextAlign(Paint.Align.LEFT);
            poisonTimerIndicator.setTextColor(0xFF66FF33);

            recoveryText = new StaticText("Recovered", spacing * (colBeg + 16f), spacing * (rowBeg + 2.4f), spacing * 4, spacing / 2);
            recoveryText.setTextSize((int) (48f * rate));
            recoveryText.setTextAlign(Paint.Align.LEFT);
            recoveryText.setTextColor(0xFFFFFF88);

            bAccelerate = false;
            curtainAlpha = 0;
            currentScore = 0;
            border = new Border(screen_w / 2, screen_h / 2, 256f * rate, 288f * rate, rate);
        }

        @Override
        public void onDestroy() {

            GameObject.release(hopeFragment);
            GameObject.release(goldFragment);
            GameObject.release(border);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            if (curtainAlpha != 0) {
                paint.setColor(Color.BLACK);
                paint.setAlpha(curtainAlpha);
                canvas.drawRect(0, 0, screen_w, screen_h, paint);
                paint.setAlpha(255);
            }

            if (bIsGameOver) {
                if (overTimer > 1000) {
                    int alpha = (int) (255f * (overTimer > 2000 ? 1000 : (overTimer - 1000)) / 1000);
                    paint.setColor(Color.RED);
                    paint.setAlpha(alpha);
                    paint.setTextSize((int) (128f * rate));
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText("Mission Faild...", screen_w / 2, screen_h / 2, paint);
                    paint.setAlpha(255);
                }
                return;
            }

            if (bIsGameClear) {
                if (clearTimer > 1000) {
                    float x = screen_w * (clearTimer > 2000 ? 1000 : (clearTimer - 1000)) / 1000;
                    paint.setTextSize((int) (128f * rate));
                    paint.setTextAlign(Paint.Align.CENTER);
                    canvas.drawText("Mission Complete", x, screen_h / 2, paint);
                }
                return;
            }

            if (bIsGameSummary) {
                border.draw(canvas, paint);
                if (border.isReady()) {
                    paint.setColor(Color.BLACK);
                    paint.setTextSize((int) (48f * rate));
                    switch (currentScore) {
                        case 6://TODO add summary data
                        case 5:
                        case 4:
                        case 3:
                        case 2:
                        case 1:
                            break;
                    }
                }
                return;
            }

            hopeFragment.draw(canvas, paint);
            goldFragment.draw(canvas, paint);
            hopeFragmentCounter.draw(canvas, paint);
            goldFragmentCounter.draw(canvas, paint);

            countDownIndicator.draw(canvas, paint);
            scoreIndicator.draw(canvas, paint);

            if (fireTimer >= 0) {
                fireTimerIndicator.draw(canvas, paint);
            }

            if (smokeTimer >= 0) {
                smokeTimerIndicator.draw(canvas, paint);
            }

            if (hiddenTimer >= 0) {
                hiddenTimerIndicator.draw(canvas, paint);
            }

            if (poisonTimer >= 0) {
                poisonTimerIndicator.draw(canvas, paint);
            } else if (poisonTimer >= -BASE_SPB * 2.4) {
                if ((int) (poisonTimer * 2 / BASE_SPB) % 2 == 0) {
                    recoveryText.draw(canvas, paint);
                }
            }
        }

        @Override
        public void update(int elapsedTime) {

            hopeFragment.update(elapsedTime);
            goldFragment.update(elapsedTime);
            hopeFragmentCounter.setText("×  " + hopeFragmentCount);
            goldFragmentCounter.setText("×  " + goldFragmentCount);

            int min = countDownTimer / 60000;
            int sec = countDownTimer / 1000 - min * 60;
            int mil = countDownTimer % 1000 / 10;
            countDownIndicator.setValue(String.format(Locale.getDefault(), "%d'%02d\"%02d", min, sec, mil));
            scoreIndicator.setValue("" + score);

            if (fireTimer >= 0) {
                fireTimer -= elapsedTime;
                if (fireTimer < 0) {
                    fire.setCount(0);
                }
                sec = fireTimer / 1000;
                mil = fireTimer % 1000 / 10;
                fireTimerIndicator.setValue(String.format(Locale.getDefault(), "%d\"%02d", sec, mil));
            }

            if (smokeTimer >= 0) {
                smokeTimer -= elapsedTime;
                if (smokeTimer < 0) {
                    smoke.putOff();
                }
                sec = smokeTimer / 1000;
                mil = smokeTimer % 1000 / 10;
                smokeTimerIndicator.setValue(String.format(Locale.getDefault(), "%d\"%02d", sec, mil));
            }

            if (hiddenTimer >= 0) {
                hiddenTimer -= elapsedTime;
                sec = hiddenTimer / 1000;
                mil = hiddenTimer % 1000 / 10;
                hiddenTimerIndicator.setValue(String.format(Locale.getDefault(), "%d\"%02d", sec, mil));
            }

            if (poisonTimer >= -BASE_SPB * 2.4) {
                poisonTimer -= elapsedTime;
                if (poisonTimer >= 0) {
                    sec = poisonTimer / 1000;
                    mil = poisonTimer % 1000 / 10;
                    poisonTimerIndicator.setValue(String.format(Locale.getDefault(), "%d\"%02d", sec, mil));
                }
            }

            if (bIsGameOver) {
                overTimer += elapsedTime * (bAccelerate ? 3 : 1);
                if (curtainAlpha < 127) {
                    curtainAlpha = (int) (127f * (overTimer > 1000 ? 1000 : overTimer) / 1000);
                }
            }

            if (bIsGameClear) {
                clearTimer += elapsedTime * (bAccelerate ? 3 : 1);
                if (curtainAlpha < 127) {
                    curtainAlpha = (int) (127f * (clearTimer > 1000 ? 1000 : clearTimer) / 1000);
                }
            }

            if (bIsGameSummary) {
                if (summaryTimer < 3000) {
                    summaryTimer += elapsedTime * (bAccelerate ? 3 : 1);
                    if (summaryTimer > 3000) {
                        summaryTimer = 3000;
                    }
                }
                if (summaryTimer / 500 > currentScore) {
                    currentScore = summaryTimer / 500;
                    GameSound.playSE(GameSound.ID_SE_SCORE);
                }
                border.update(elapsedTime);
            }
        }

        @Override
        public void onTouchEvent(MotionEvent event) {

            if (bIsGameOver) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (overTimer < 3000) {
                        bAccelerate = true;
                    } else {
                        bIsGamePaused = true;
                        bIsGameOver = false;
                        bAccelerate = false;
                    }
                }
            }

            if (bIsGameOver) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (clearTimer < 3000) {
                        bAccelerate = true;
                    } else {
                        bIsGameSummary = true;
                        bIsGameClear = false;
                        bAccelerate = false;
                    }
                }
            }

            if (bIsGameSummary) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (summaryTimer < 3000) {
                        bAccelerate = true;
                    } else {
                        border.close(null);
                        MainView.setSceneCurtain(127, 256, 500, () -> {
                            MainView.setScene(SceneTitle.MENU);
                            GameMenu.setCurrentMenuID(GameMenu.MENU_SELECT);
                        });
                    }
                }
            }
        }

        class Indicator {

            StaticText name;
            StaticText value;

            Indicator(String text, float x, float y, float w, float h, float offset) {

                name = new StaticText(text, x, y, w, h);
                value = new StaticText(null, x, y + offset, w, h);
            }

            void draw(Canvas canvas, Paint paint) {

                name.draw(canvas, paint);
                value.draw(canvas, paint);
            }

            void setTextSize(int textSize) {

                name.setTextSize(textSize);
                value.setTextSize(textSize);
            }

            void setTextColor(int color) {

                name.setTextColor(color);
                value.setTextColor(color);
            }

            void setTextAlign(Paint.Align align) {

                name.setTextAlign(align);
                value.setTextAlign(align);
            }

            void setValue(String text) {

                value.setText(text);
            }
        }
    }
}
