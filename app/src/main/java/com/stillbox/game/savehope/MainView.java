package com.stillbox.game.savehope;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MainView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    //Fields for main view
    public static MainView mainView;
    public Resources resources;

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
    private long lastUpdateTime;

    //Misc fields for debugging, cheating, etc...
    public static boolean bIsDebugMode = true;

    //Fields for game

    public MainView(Context context) {
        super(context);
        Log.d("method_call", "Main View Constructor");

        mainView = this;
        resources = this.getResources();
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
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

    public void init() {

    }

    public void draw() {

        canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas();
            if (canvas != null) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (canvas != null)
                surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void update(long elapsedTime) {

    }
}
