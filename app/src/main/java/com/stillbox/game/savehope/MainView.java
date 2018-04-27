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

import com.stillbox.game.savehope.gamescene.GameScene;
import com.stillbox.game.savehope.gamescene.MenuScene;
import com.stillbox.game.savehope.gamescene.OpeningScene;
import com.stillbox.game.savehope.gamescene.ShooterScene;
import com.stillbox.game.savehope.gamesound.GameSound;

import java.io.InputStream;

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
    public static final float FPS = 60f;
    public float timeStep = 1000f / FPS;

    private SurfaceHolder surfaceHolder;
    private Canvas canvas;
    private Paint paint;
    private long lastUpdateTime;

    //Misc fields for debugging, cheating, etc...
    public static boolean bIsDebugMode = true;

    //Fields for game
    public static final int GAME_OPENING = 0;
    public static final int GAME_MENU = 1;
    public static final int GAME_SHOOTER = 10;

    private GameScene gameScene;
    private int currentScene;
    private boolean bInitScene;

    public MainView(Context context) {
        super(context);
        Log.d("method_call", "Main View Constructor");

        mainView = this;
        resources = this.getResources();
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        paint = new Paint();
        paint.setAntiAlias(true);
        GameSound.init();
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
            long elapsedTime = start - lastUpdateTime;
            draw();
            update(elapsedTime < timeStep ? elapsedTime : (long) timeStep);
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

        gameScene.onTouchEvent(event);
        return true;
    }

    public void init() {

        setScene(GAME_OPENING);
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
                gameScene.draw(canvas, paint);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null)
                surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void update(long elapsedTime) {

        GameSound.updateBGM();

        if (bInitScene) {

            if (gameScene != null) {
                gameScene.onDestroy();
                gameScene = null;
            }

            switch (currentScene) {
                case GAME_OPENING:
                    gameScene = new OpeningScene();
                    break;
                case GAME_MENU:
                    gameScene = new MenuScene();
                    break;
                case GAME_SHOOTER:
                    gameScene = new ShooterScene();
                    break;
                default:
                    currentScene = GAME_OPENING;
                    gameScene = new OpeningScene();
                    break;
            }

            gameScene.init();
            bInitScene = false;
        }

        gameScene.update(elapsedTime);
    }

    public void setScene(int scene) {

        currentScene = scene;
        bInitScene = true;
    }

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
