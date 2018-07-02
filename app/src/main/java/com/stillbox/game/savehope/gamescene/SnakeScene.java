package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.GameControl;
import com.stillbox.game.savehope.gamecontrol.TextButton;
import com.stillbox.game.savehope.gamedata.CharaData;
import com.stillbox.game.savehope.gamedata.GameSettings;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameenum.GameMode;
import com.stillbox.game.savehope.gamemenu.GameMenu;
import com.stillbox.game.savehope.gamemenu.PauseMenu;
import com.stillbox.game.savehope.gamemenu.SnakeSettingMenu;
import com.stillbox.game.savehope.gameobject.DigitalBackground;
import com.stillbox.game.savehope.gameobject.GameCamera;
import com.stillbox.game.savehope.gameobject.GameObject;
import com.stillbox.game.savehope.gameobject.SpriteObject;
import com.stillbox.game.savehope.gameobject.Tutorial;
import com.stillbox.game.savehope.gameobject.gamepad.GamePad;
import com.stillbox.game.savehope.gamesound.GameSound;

import java.util.ArrayList;

public class SnakeScene extends GameScene {

    //Fields and setters relating to settings
    private static int controlMode = GameSettings.controlMode_sk;
    private static int scannerAlpha = GameSettings.scannerAlpha_sk;
    private static int adjustTime = GameSettings.adjustTime_sk;

    public static void setControlMode(int controlMode) {
        SnakeScene.controlMode = controlMode;
    }

    public static void setScannerAlpha(int scannerAlpha) {
        SnakeScene.scannerAlpha = scannerAlpha;
    }

    public static void setAdjustTime(int adjustTime) {
        SnakeScene.adjustTime = adjustTime;
    }

    //Fields for game field grid
    private static final float DEST_SPACING = 96f;
    private static final int DEST_COLS = 20;
    private static final int DEST_ROWS = 11;

    private static float spacing;
    private static int rows, cols;

    //Fields for sounds
    private static final int ID_SE_STEP = R.raw.step;
    private static final int ID_SE_ITEM = R.raw.item;
    private static final int ID_SE_PRICK = R.raw.prick;
    private static final int ID_SE_DAMAGE = R.raw.damage;

    private static final int ID_BGM = R.raw.searching;
    private static final double BPM = 128.01;
    private static final double BASE_SPB = 60000 / BPM;
    private static final float BEG_TIME = 484f; //TODO check begin time
    private int currentTime;

    //Fields for game mode and playrer stats
    private static GameMode gameMode;
    private static GameLevel gameLevel;

    private static int charaID = CharaData.IDC_SAIHARA;
    private static int charaStr;
    private static int charaAgi;
    private static int charaEnd;
    private static int charaInt;
    private static int charaLuc;

    private static int maxLifePoint;

    //Fields relating to level
    private static int timeTotal;
    private static int timeBonus;

    private static ArrayList<MapInfo> mapInfos;

    //Fields for game tutorial
    private boolean bIsTutorial;
    private Tutorial tutorial;

    //Fields for game pause
    private boolean bOnPauseBtnEvent;
    private TextButton btnPause;
    private PauseMenu pauseMenu;
    private SnakeSettingMenu settingMenu;
    private GameMenu currentMenu;

    //Fields for game objects
    private GameCamera camera;
    private DigitalBackground background;
    private GameMap map;
    private Player player;
    private Scanner scanner;
    private Fragment fragment;
    private Monokuma monokuma;
    private Monomi monomi;
    private Punch punch;
    private Prick prick;
    private FloatingInfo floatingInfo;
    private HUD hud;

    //Fields for game controls
    private GamePad gamePad;
    private GestureDetector detector;
    private static final int ACT_UP = 10;
    private static final int ACT_DOWN = 20;
    private static final int ACT_LEFT = 30;
    private static final int ACT_RIGHT = 40;

    //Fields for states, counters and timers
    private boolean bIsGameOver;
    private boolean bIsGameClear;
    private boolean bIsGamePaused;
    private boolean bIsGameSummary;

    //
    public SnakeScene(GameMode mode, GameLevel level) {

        MainView.setLoadingProgress(1, 0);

        SnakeScene.gameMode = mode;
        SnakeScene.gameLevel = level;

        bIsGameOver = false;
        bIsGameClear = false;
        bIsGamePaused = false;
        bIsGameSummary = false;
    }

    @Override
    public void init() {

        Thread thread = new Thread(() -> {

            switch (gameLevel) {
                case EASY:
                    break;
                case NORMAL:
                    break;
                case HARD:
                    break;
                case IJIME:
                    break;
            }

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

        GameObject.release(camera);
        GameObject.release(background);
        GameObject.release(map);
        GameObject.release(player);
        GameObject.release(scanner);
        GameObject.release(fragment);
        GameObject.release(monokuma);
        GameObject.release(monomi);
        GameObject.release(punch);
        GameObject.release(prick);
        GameObject.release(floatingInfo);
        GameObject.release(hud);

        GameSound.releaseSE();
        GameSound.releaseBGM();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setColor(Color.WHITE);
        canvas.drawText("(Snake Scene) Under Construction", screen_w / 2, screen_h / 2, paint);

    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    public void gamePause() {

    }

    public void gameContinue() {

    }

    public void gameOver() {

    }

    public void gameClear() {

    }

    class GameMap extends GameObject {


        @Override
        public void onDestroy() {

        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

        }

        @Override
        public void update(int elapsedTime) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }
    }

    class MapInfo {
        int col;
        int row;
        int gap;
        int hole;
        int Item;
        int enemy;
        int punch;
    }

    class Player extends SpriteObject {

        //TODO
    }

    class Scanner extends GameObject {

        //TODO

        @Override
        public void onDestroy() {

        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

        }

        @Override
        public void update(int elapsedTime) {

        }
    }

    class Monokuma extends SpriteObject {

        //TODO
    }

    class Monomi extends SpriteObject {

        //TODO
    }

    class Fragment extends SpriteObject {

        //TODO
    }

    class Punch extends SpriteObject {

        //TODO
    }

    class Prick extends SpriteObject {

        //TODO
    }

    class FloatingInfo extends GameObject {

        //TODO

        @Override
        public void onDestroy() {

        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

        }

        @Override
        public void update(int elapsedTime) {

        }
    }

    class HUD extends GameObject {

        //TODO

        @Override
        public void onDestroy() {

        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

        }

        @Override
        public void update(int elapsedTime) {

        }
    }
}
