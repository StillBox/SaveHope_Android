package com.stillbox.game.savehope.gamescene;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.Button;
import com.stillbox.game.savehope.gamecontrol.TextButton;
import com.stillbox.game.savehope.gamedata.CharaData;
import com.stillbox.game.savehope.gamedata.GameSettings;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameenum.GameMode;
import com.stillbox.game.savehope.gamemenu.GameMenu;
import com.stillbox.game.savehope.gamemenu.PauseMenu;
import com.stillbox.game.savehope.gamemenu.ShooterSettingMenu;
import com.stillbox.game.savehope.gameobject.SingleSpriteObject;
import com.stillbox.game.savehope.gameobject.SpriteObject;
import com.stillbox.game.savehope.gameobject.TextBox;
import com.stillbox.game.savehope.gamesound.GameSound;

import java.util.ArrayList;

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
    private static final int ID_BGM_INTRO = R.raw.underground;
    private static final int ID_BGM = R.raw.ekorosia;
    private static final float BPM = 128.01f;
    private static final float BEG_TIME = 484f;
    private static final float BASE_SPB = 60000f / BPM;
    private int currentTime;

    //Fields for game mode and player states
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
    private static float actSPB;

    //Fields for pause border
    private TextButton btnPause;
    private PauseMenu pauseMenu;
    private ShooterSettingMenu settingMenu;
    private GameMenu currentMenu;

    //Fields for game objects
    private GameMap map;
    private Scanner scanner;
    private Player player;
    private Beats beats;

    private ArrayList<Integer> pitcherIds;

    //Fields for states and timers
    private boolean bIsGameOver;
    private boolean bIsGamePaused;

    private int beatIndex;
    private int beatTime;

    public ShooterScene(GameMode mode, GameLevel level) {

        MainView.setLoadingProgress(1, 0);

        gameMode = mode;
        gameLevel = level;

        bIsGameOver = false;
        bIsGamePaused = false;
    }

    @Override
    public void init() {

        Thread thread = new Thread(() -> {

            pauseMenu = new PauseMenu();
            ((Button) pauseMenu.getControl(PauseMenu.BTN_CONTINUE)).setOnPressedListener(() -> pauseMenu.close(this::continueGame));
            ((Button) pauseMenu.getControl(PauseMenu.BTN_RETRY)).setOnPressedListener(() -> pauseMenu.close(this::continueGame));
            ((Button) pauseMenu.getControl(PauseMenu.BTN_SETTING)).setOnPressedListener(() -> pauseMenu.close(() -> currentMenu = settingMenu));
            ((Button) pauseMenu.getControl(PauseMenu.BTN_MENU)).setOnPressedListener(() ->
                pauseMenu.close(() -> {
                    bIsGamePaused = false;
                    /*setCurtain(127, 256, 5000, () -> {
                        MainView.mainView.setScene(SceneTitle.MENU);
                        GameMenu.setCurrentMenuID(GameMenu.MENU_SELECT);
                    });*/
                })
            );

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
                    break;
                case NORMAL:
                    beatRate = 1f;
                    break;
                case HARD:
                    beatRate = 0.5f;
                    break;
                case IJIME:
                    beatRate = 0.5f;
                    break;
            }

            actSPB = BASE_SPB * beatRate;
            beatIndex = (int) (2 * BASE_SPB / actSPB) + 4;
            beatTime = (int) (actSPB * beatRate);

            map = new GameMap();
            MainView.increaseLoadingProgress(1);

            scanner = new Scanner(scannerAlpha);
            MainView.increaseLoadingProgress(1);

            player = new Player();
            MainView.increaseLoadingProgress(1);

            beats = new Beats();
            MainView.increaseLoadingProgress(1);

            float button_w = 240 * rate;
            float button_h = 48 * rate;
            btnPause = new TextButton("暂停游戏", screen_w - button_w, button_h, button_w, button_h);
            btnPause.setOnPressedListener(this::pauseGame);

            GameSound.createBGM(ID_BGM, ID_BGM, true);
            GameSound.startBGM(ID_BGM);
            MainView.increaseLoadingProgress(1);

            MainView.endLoadingProgress();
        });

        thread.start();
    }

    @Override
    public void onDestroy() {

        pauseMenu.onDestroy();

        map.onDestroy();
        player.onDestroy();

        GameSound.releaseBGM();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (bIsGamePaused) {
            currentMenu.draw(canvas, paint);
            return;
        }

        map.draw(canvas, paint);
        scanner.draw(canvas, paint);

        player.draw(canvas, paint);
        beats.draw(canvas, paint);

        btnPause.draw(canvas, paint);
    }

    @Override
    public void update(int elapsedTime) {

        if (bIsGamePaused) {
            currentMenu.update(elapsedTime);
            return;
        }

        int elapsedBgmTime =  GameSound.getCurrentPosition() - currentTime;
        currentTime = GameSound.getCurrentPosition();

        beatTime += elapsedBgmTime;
        if (beatIndex < 100) {
            while (beatTime >= actSPB) {
                beats.addBeat(beatIndex++);
                beatTime -= actSPB;
            }
        }

        player.update(elapsedBgmTime);
        beats.update(elapsedBgmTime);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (bIsGamePaused) {
            currentMenu.onTouchEvent(event);
        } else {
            btnPause.onTouchEvent(event);

        }
    }

    private void pauseGame() {

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

    private void continueGame() {

        bIsGamePaused = false;
        currentMenu = null;
        btnPause.reset();
        GameSound.startBGM();
    }

    //classes for game play

    private class GameMap {

        Bitmap bmpMap;
        int[][] mapCode;

        GameMap() {

            float w = spacing * cols;
            float h = spacing * rows;

            Bitmap bmpRes = MainView.getBitmap(R.drawable.map);
            int resSize = bmpRes.getWidth();
            float scale = spacing / (resSize / 8);

            mapCode = new int[rows][cols];

            //Basic Map

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

            setMapCode(ROWS_MIN - 3, 7, 38);
            setMapCode(ROWS_MIN - 3, 8, 39);
            setMapCode(ROWS_MIN - 3, 9, 38);
            setMapCode(ROWS_MIN - 3, 10, 39);
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

        void onDestroy() {

            bmpMap.recycle();
            bmpMap = null;
        }

        public void draw(Canvas canvas, Paint paint) {

            canvas.drawBitmap(bmpMap, 0, 0, paint);
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

        private int row;
        private int updateTime;

        private SingleSpriteObject shadow;

        public Pitcher(int row, int type) {

            this.row = row;
            updateTime = 0;
            if (pitcherIds == null) {
                pitcherIds = new ArrayList<>();
            }

            int pitcherId = CharaData.IDC_HINATA;
            if (type == PITCHER_NORMAL) {
                boolean bNew = true;
                while (bNew) {
                    bNew = false;
                    pitcherId = CharaData.getRandomPlayableCharaID();

                    if (pitcherId <= CharaData.IDC_HINATA || pitcherId >= CharaData.IDC_SAIHARA ||
                            pitcherId == CharaData.IDC_NANAMI || pitcherId == CharaData.IDC_KOMAEDA)
                        bNew = true;

                    for (int id : pitcherIds) {
                        if (id == pitcherId)
                            bNew = true;
                    }
                }
            } else if (type == PITCHER_POISON) {
                pitcherId = Math.random() < 0.2 ? CharaData.IDC_MONOMI : CharaData.IDC_NANAMI;
            } else if (type == PITCHER_DEBUFF) {
                pitcherId = Math.random() < 0.25 ? CharaData.IDC_MONOKUMA : CharaData.IDC_OMA;
            }

            pitcherIds.add(pitcherId);
            if (pitcherIds.size() >= 6)
                pitcherIds.remove(0);

            setPosition(spacing * colBeg, spacing * (row + 1.5f));
            setSize(spacing * 2, spacing * 9 / 4);
            setOffset(-w / 2f, -h);

            loadResource(CharaData.getResID(pitcherId));
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

        private ArrayList<Pitcher> pitchers;
        private ArrayList<BeatData> beatData;

        public Beats() {

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

            pitchers = new ArrayList<>();
            beatData = new ArrayList<>();
        }

        @Override
        public void onDestroy() {

            for (Pitcher pitcher : pitchers)
                pitcher.onDestroy();
            pitchers.clear();
            pitchers = null;

            beatData.clear();
            beatData = null;

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

            for (BeatData data : beatData) {
                if (data.bActive) {
                    setState(data.type);
                    setPosition(data.x, data.y);
                    super.draw(canvas, paint);
                }
            }
        }

        @Override
        public void update(int elapsedTime) {

            for (BeatData data : beatData) {
                if (data.bIn && currentTime >= data.begTime + adjustTime - 2 * actSPB) {
                    pitchers.add(new Pitcher(data.row, Pitcher.PITCHER_NORMAL));
                    data.bIn = false;
                }
                if (currentTime >= data.begTime + adjustTime) {
                    if (data.bPitch) {
                        data.bActive = true;
                        data.bPitch = false;
                    }
                    data.x = spacing * (colBeg + 3f + 12f * (currentTime - data.begTime - adjustTime) / 4 / actSPB);
                }
                if (MainView.bIsDebugMode) {
                    if (currentTime >= data.beatTime + adjustTime) {
                        data.bActive = false;
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

        public void addBeat(int index) {

            BeatData data = new BeatData();

            data.row = rowBeg + 4 + (int) (Math.random() * 4f);
            data.type = BEAT_NORMAL;

            data.beatTime = (long) (BEG_TIME + index * actSPB);
            data.begTime = (long) (BEG_TIME + (index - 4) * actSPB);
            data.lastTime = (long) actSPB;
            data.x = 0f;
            data.y = spacing * (data.row + 0.5f);
            data.bIn = true;
            data.bPitch = true;
            data.bActive = false;

            beatData.add(data);
        }

        class BeatData {
            int type;
            int row;
            long begTime;
            long beatTime;
            long lastTime;
            float x;
            float y;
            boolean bIn;
            boolean bPitch;
            boolean bActive;
        }
    }

    class Scanner {

        int alpha;

        Scanner(int alpha) {

            this.alpha = alpha;
        }

        void setAlpha(int alpha) {

            this.alpha = alpha;
        }

        void draw(Canvas canvas, Paint paint) {

            for (int i = 0; i < 6; i++) {

                int color = Color.WHITE;
                switch (i) {
                    case 0:
                    case 5:
                        color = 0xFF888800;
                        break;
                    case 1:
                    case 4:
                        color = 0xFF008800;
                        break;
                    case 2:
                    case 3:
                        color = 0xFF000088;
                        break;
                }
                paint.setColor(color);
                paint.setAlpha(alpha);

                int col = colBeg + COLS_MIN - 8 + i;
                for (int row = rowBeg + 4; row < rowBeg + 8; row++) {

                    RectF rectScanner = new RectF(spacing * col, spacing * row,
                            spacing * (col + 15f / 16f), spacing * (row + 15f / 16f));
                    canvas.drawRect(rectScanner, paint);
                }

                paint.setAlpha(255);
            }
        }
    }

    class Fire {
    }

    class Smoke {
    }

    class FloatingInfo {

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
        }

        void draw(Canvas canvas, Paint paint) {

            for (TextInfo info : textInfos) {

                int alpha = 255 - (int) (255f * info.time / 2f / actSPB);
                paint.setColor(info.color);
                paint.setAlpha(alpha);
                paint.setTextSize(textSize);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(info.text, info.x, info.y, paint);
            }
        }

        void update(int elapsedTime) {

            for (int i = 0; i < textInfos.size(); i++) {

                TextInfo info = textInfos.get(i);
                info.time += elapsedTime;
                if (info.time >= actSPB * 2f) {
                    textInfos.remove(i--);
                }
            }
        }

        void add(int type, float x, float y) {

            TextInfo info = new TextInfo();

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

    class HUD {
    }
}
