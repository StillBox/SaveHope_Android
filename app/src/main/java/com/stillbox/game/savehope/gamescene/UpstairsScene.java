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
import com.stillbox.game.savehope.gamemenu.UpstairsSettingMenu;
import com.stillbox.game.savehope.gameobject.DigitalBackground;
import com.stillbox.game.savehope.gameobject.GameCamera;
import com.stillbox.game.savehope.gameobject.GameObject;
import com.stillbox.game.savehope.gameobject.SpriteObject;
import com.stillbox.game.savehope.gameobject.Tutorial;
import com.stillbox.game.savehope.gameobject.gamepad.GamePad;
import com.stillbox.game.savehope.gamesound.GameSound;

import java.util.ArrayList;

public class UpstairsScene extends GameScene {

    //Fields and setters relating to settings
    private static int controlMode = GameSettings.controlMode_us;

    public static void setControlMode(int controlMode) {
        UpstairsScene.controlMode = controlMode;
    }

    //Fields for game field grid
    private static final float DEST_SPACING = 96f;

    //Fields for sounds
    private static final int ID_SE_JUMP = R.raw.step;
    private static final int ID_SE_DASH = R.raw.dash;
    private static final int ID_SE_ITEM = R.raw.item;
    private static final int ID_SE_PRICK = R.raw.prick;
    private static final int ID_SE_DAMAGE = R.raw.damage;

    private static final int ID_BGM = R.raw.box16;
    private static final double BPM = 128.01;
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
    private static int returnTime;
    private static int dashTime;
    private static int jumpCount;

    //Fields relating to level
    private static int stageCount;
    private static int buffTime;
    private static ArrayList<MapInfo> mapInfos;

    //Fields for game tutorial
    private boolean bIsTutorial;
    private Tutorial tutorial;

    //Fields for game pause
    private boolean bOnPauseBtnEvent;
    private TextButton btnPause;
    private PauseMenu pauseMenu;
    private UpstairsSettingMenu settingMenu;
    private GameMenu currentMenu;

    //Fields for game objects
    private GameCamera camera;
    private DigitalBackground background;
    private GameMap map;
    private Player player;
    private Monokuma monokuma;
    private MonoMissile monoMissile;
    private Prick prick;
    private BuffItem buffItem;
    private Fragment fragment;
    private HUD hud;

    //Fields for game controls
    private GamePad gamePad;
    private GestureDetector detector;
    private static final int ACT_TURN = 10;
    private static final int ACT_DASH = 20;
    private static final int ACT_JUMP = 30;
    private static final int ACT_BUILD = 40;

    //Fields for states, counters and timers
    private boolean bIsGameOver;
    private boolean bIsGameClear;
    private boolean bIsGamePaused;
    private boolean bIsGameSummary;

    //Constructor
    public UpstairsScene(GameMode mode, GameLevel level) {

        MainView.setLoadingProgress(1, 0);

        UpstairsScene.gameMode = mode;
        UpstairsScene.gameLevel = level;

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
        GameObject.release(monokuma);
        GameObject.release(monoMissile);
        GameObject.release(prick);
        GameObject.release(buffItem);
        GameObject.release(fragment);
        GameObject.release(hud);

        GameSound.releaseSE();
        GameSound.releaseBGM();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setColor(Color.WHITE);
        canvas.drawText("(Upstairs Scene) Under Construction", screen_w / 2, screen_h / 2, paint);
    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    class MapInfo {

        //TODO
    }

    class GameMap extends GameObject {

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

    class Player extends SpriteObject {

        //TODO
    }

    class Monokuma extends SpriteObject {

        //TODO
    }

    class MonoMissile extends SpriteObject {

        //TODO
    }

    class Prick extends SpriteObject {

        //TODO
    }

    class BuffItem extends SpriteObject {

        //TODO
    }

    class Fragment extends SpriteObject {

        //TODO
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
