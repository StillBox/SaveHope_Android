package com.stillbox.game.savehope;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.stillbox.game.savehope.gamedata.GameSettings;
import com.stillbox.game.savehope.gamedata.SaveData;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameenum.GameMode;
import com.stillbox.game.savehope.gameenum.SceneTitle;
import com.stillbox.game.savehope.gamemenu.GameMenu;
import com.stillbox.game.savehope.gameobject.dialog.DialogBox;
import com.stillbox.game.savehope.gameobject.LoadingBox;
import com.stillbox.game.savehope.gameobject.SceneCurtain;
import com.stillbox.game.savehope.gamescene.ExtraScene;
import com.stillbox.game.savehope.gamescene.GameScene;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamescene.OpeningScene;
import com.stillbox.game.savehope.gamescene.ShooterScene;
import com.stillbox.game.savehope.gamescene.ShopScene;
import com.stillbox.game.savehope.gamescene.SnakeScene;
import com.stillbox.game.savehope.gamescene.StoryScene;
import com.stillbox.game.savehope.gamescene.UpstairsScene;
import com.stillbox.game.savehope.gamesound.GameSound;

import java.io.InputStream;
import java.util.ArrayList;

public class MainView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //Fields for main view
    public static MainView mainView;
    public static Resources resources;

    //Fields for threads
    private boolean bExecute;
    private Thread thread;

    //Fields for drawing and updating
    public static final float DEST_WIDTH = 1920;
    public static final float DEST_HEIGHT = 1080;
    public static float w, h;
    public static float rate, rate_x, rate_y;
    public static final float FPS = 60f;
    public static float timeStep = 1000f / FPS;

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;
    private long lastUpdateTime;

    //Misc fields for debugging, cheating, etc...

    public static GameMode gameMode = GameMode.FREE;
    public static GameLevel gameLevel = GameLevel.EASY;

    public static boolean bIsDebugMode = true;

    //Fields for game

    private static final int FADE_TIME = 1000;
    private static SceneTitle scene;
    private GameScene gameScene;

    private static boolean bInitScene;
    private static boolean bIsLoading;
    private static LoadingBox loadingBox;
    private static SceneCurtain sceneCurtain;

    private static ArrayList<DialogBox> dialogs;
    private static int dialogCount;

    public MainView(Context context) {
        super(context);
        Log.d("method_call", "Main View Constructor");

        mainView = this;
        resources = this.getResources();
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);

        GameSettings.loadSettings();
        GameSound.init();

        SaveData.readSaveData();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("method_call", "Surface Created");

        init();
        bExecute = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("method_call", "Surface Changed");

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("method_call", "Surface Destroyed");

        bExecute = false;
    }

    @Override
    public void run() {

        while (bExecute) {
            long start = System.currentTimeMillis();
            int elapsedTime = start - lastUpdateTime < 5 * timeStep ? (int) (start - lastUpdateTime) : (int) timeStep;
            draw();
            update(elapsedTime);
            lastUpdateTime = start;
            long end = System.currentTimeMillis();
            try {
                if (end - start < timeStep) {
                    Thread.sleep((long) timeStep + start - end);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (dialogCount > 0) {
            dialogs.get(dialogCount - 1).onTouchEvent(event);
            return true;
        }
        if (bIsLoading) {
            loadingBox.onTouchEvent(event);
            return true;
        }
        if (sceneCurtain.isOn()) {
            sceneCurtain.onTouchEvent(event);
            return true;
        }
        if (gameScene != null) {
            gameScene.onTouchEvent(event);
        }
        return true;
    }

    public void init() {

        w = getWidth();
        h = getHeight();
        rate_x = w / DEST_WIDTH;
        rate_y = h / DEST_HEIGHT;
        rate = Math.min(rate_x, rate_y);
        loadingBox = new LoadingBox(0, 0, w, h, 48f * rate);
        sceneCurtain = new SceneCurtain(0, 0, w, h);
        dialogs = new ArrayList<>();

        setScene(SceneTitle.OPENING);
    }

    public void onDestroy() {

        if (gameScene != null) {
            gameScene.onDestroy();
        }
    }

    public void draw() {

        canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null && gameScene != null) {
                canvas.drawColor(Color.BLACK);

                if (bIsLoading) {
                    loadingBox.draw(canvas, paint);
                } else {
                    gameScene.draw(canvas, paint);
                }
                sceneCurtain.draw(canvas, paint);

                if (dialogCount > 0) {
                    for (int i = 0; i < dialogCount; i++) {
                        if (i == dialogCount - 1) {
                            canvas.drawColor(0x7F000000);
                        }
                        dialogs.get(i).draw(canvas, paint);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null)
                surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void update(int elapsedTime) {

        GameSound.updateBGM(elapsedTime);

        while (dialogCount > 0) {
            DialogBox dialog = dialogs.get(dialogCount - 1);
            dialog.update(elapsedTime);
            if (!dialog.isOn()) {
                dialogs.remove(dialog);
                dialogCount--;
            } else {
                break;
            }
        }

        sceneCurtain.update(elapsedTime);

        if (bInitScene) {

            if (gameScene != null) {
                gameScene.onDestroy();
                gameScene = null;
            }

            switch (scene) {
                case OPENING:
                    gameScene = new OpeningScene();
                    break;
                case MENU:
                    gameScene = new MenuScene();
                    break;
                case STORY:
                    gameScene = new StoryScene();
                    break;
                case SHOOTER:
                    gameScene = new ShooterScene(gameMode, gameLevel);
                    break;
                case SNAKE:
                    gameScene = new SnakeScene(gameMode, gameLevel);
                    break;
                case UPSTAIRS:
                    gameScene = new UpstairsScene(gameMode, gameLevel);
                    break;
                case SHOP:
                    gameScene = new ShopScene();
                    break;
                case EXTRA:
                    gameScene = new ExtraScene();
                    break;
                default:
                    scene = SceneTitle.OPENING;
                    gameScene = new OpeningScene();
                    break;
            }

            gameScene.init();
            bInitScene = false;
            gameMode = GameMode.FREE;
            gameLevel = GameLevel.EASY;
        }

        if (bIsLoading) {
            loadingBox.update(elapsedTime);
        } else {
            gameScene.update(elapsedTime);
        }
    }

    public static void setScene(SceneTitle scene) {

        MainView.scene = scene;
        bInitScene = true;
    }

    public static void setScene(SceneTitle scene, int menuId) {

        MainView.scene = scene;
        bInitScene = true;

        GameMenu.setCurrentMenuID(menuId);
    }

    public static void setScene(SceneTitle scene, GameMode mode, GameLevel level) {

        MainView.scene = scene;
        bInitScene = true;

        gameMode = mode;
        gameLevel = level;
    }

    //methods for loading

    public static void setLoadingProgress(int maxProgress, int begProgress) {

        bIsLoading = true;
        loadingBox.setMaxProgress(maxProgress);
        loadingBox.setCurrentProgress(begProgress);
        loadingBox.start();
    }

    public static void increaseLoadingProgress(int increment) {

        loadingBox.increaseProgress(increment);
    }

    public static void endLoadingProgress() {

        bIsLoading = false;
        loadingBox.end();
    }

    //methods for curtain

    public static void setSceneCurtain(int begAlpha, int endAlpha, int duration, SceneCurtain.OnEndListener listener) {

        GameSound.fadeOut(duration);

        sceneCurtain.set(begAlpha, endAlpha, duration, listener);
        sceneCurtain.start();
    }

    //methods for dialogs

    public static DialogBox createDialog(float x, float y, float w, float h) {
        DialogBox dialog = new DialogBox(x, y, w, h);
        dialogs.add(dialog);
        dialogCount++;
        return dialog;
    }

    public static DialogBox createDialog(float w, float h) {
        DialogBox dialog = new DialogBox(w, h);
        dialogs.add(dialog);
        dialogCount++;
        return dialog;
    }

    public static void addDialog(DialogBox dialog) {
        dialogs.add(dialog);
        dialogCount++;
    }

    //methods for getting resources

    public static String getString(int id) {
        Context context = mainView.getContext();
        return context.getString(id);
    }

    public static Bitmap getBitmap(int id) {

        InputStream inputStream = resources.openRawResource(id);
        return BitmapFactory.decodeStream(inputStream, null, null);
    }

    public static Bitmap getBitmap(int id, int width, int height) {

        InputStream inputStream = resources.openRawResource(id);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inputStream, null, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        int inSampleSize = 1;

        if (srcWidth > width || srcHeight > height)
            inSampleSize = Math.min(Math.round(srcWidth / width), Math.round(srcHeight / height));

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;

        return BitmapFactory.decodeStream(inputStream, null, options);
    }
}
