package com.stillbox.game.savehope.gamescene;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamecontrol.GameControl;
import com.stillbox.game.savehope.gamecontrol.TextButton;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameobject.Border;
import com.stillbox.game.savehope.gameobject.GameObject;
import com.stillbox.game.savehope.gamesound.GameSound;

public class ExtraScene extends GameScene {

    //Fields for objects
    private MusicBox musicBox;
    private CharaBox charaBox;
    private RecordBox recordBox;
    private TextButton btnReturn;

    private static final int STATE_DEFAULT = 10;
    private static final int STATE_MAXIMIZED = 20;
    private static final int STATE_MINIMIZED = 30;

    //Fields for states

    private static final int FOCUS_MUSIC = 10;
    private static final int FOCUS_CHARA = 20;
    private static final int FOCUS_RECORD = 30;
    private int currentFocus;

    public ExtraScene() {

        MainView.setLoadingProgress(1, 0);
    }

    @Override
    public void init() {

        Thread thread = new Thread(() -> {

        });

        thread.start();
    }

    @Override
    public void onDestroy() {

        GameObject.release(musicBox);
        GameObject.release(charaBox);
        GameObject.release(recordBox);

        GameControl.release(btnReturn);

        GameSound.releaseSE();
        GameSound.releaseBGM();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        paint.setColor(Color.WHITE);
        canvas.drawText("(Extra Scene) Under Construction", screen_w / 2, screen_h / 2, paint);
    }

    @Override
    public void update(int elapsedTime) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }

    void setFocus(int index) {

        if (index == currentFocus) return;

        switch (index) {
            case FOCUS_MUSIC:
                musicBox.maximize();
                charaBox.restore();
                recordBox.restore();
                break;

            case FOCUS_CHARA:
                musicBox.minimize();
                charaBox.maximize();
                recordBox.minimize();
                break;

            case FOCUS_RECORD:
                musicBox.minimize();
                charaBox.minimize();
                recordBox.maximize();
                break;
        }
    }

    //classes for game objects

    class MusicBox extends GameObject {

        private static final int ID_BGM_PREPARE = R.raw.prepare;
        private static final int ID_BGM_GALLERY = R.raw.gallery;
        private static final int ID_BGM_EKOROSIA = R.raw.ekorosia;
        private static final int ID_BGM_SEARCHING = R.raw.searching;
        private static final int ID_BGM_BOX16 = R.raw.box16;
        private static final int ID_BGM_ALTER_EGO = R.raw.alter_ego;
        private static final int ID_BGM_NEW_WORLD = R.raw.new_world_order;
        private static final int ID_BGM_MONOMI = R.raw.monomi;
        private static final int ID_BGM_MIRAI = R.raw.mirai;
        private static final int ID_BGM_DISTRUST = R.raw.distrust;
        private int currentPlaying;
        private int currentSelected;

        private int state = STATE_MAXIMIZED;
        private Border border;

        MusicBox() {

            border = new Border(Border.BORDER_SIZE * rate, screen_h / 2,
                    screen_w / 3 - Border.BORDER_SIZE * 2 * rate, screen_h - Border.BORDER_SIZE * 2 * rate, rate);
            border.setAnchor(Border.Anchor.LEFT_CENTER);
            currentPlaying = ID_BGM_GALLERY;
            currentSelected = ID_BGM_PREPARE;
        }

        @Override
        public void onDestroy() {

            GameObject.release(border);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            border.draw(canvas, paint);

            switch (state) {
                case STATE_MAXIMIZED:
                    if (border.isReady()) {

                    }
                    break;

                case STATE_MINIMIZED:
                    if (border.isClosed()) {

                    }
                    break;
            }
        }

        @Override
        public void update(int elapsedTime) {

            border.update(elapsedTime);
        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        void setState(int state) {

            switch (state) {
                case STATE_MAXIMIZED:
                    maximize();
                    break;
                case STATE_MINIMIZED:
                    minimize();
                    break;
            }
        }

        void maximize() {

            state = STATE_MAXIMIZED;
            //TODO
        }

        void minimize() {

            state = STATE_MINIMIZED;
            //TODO
        }
    }

    class CharaBox extends GameObject {

        //TODO

        private int state = STATE_DEFAULT;
        private Border border;

        @Override
        public void onDestroy() {

            GameObject.release(border);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            border.draw(canvas, paint);

            switch (state) {
                case STATE_DEFAULT:
                    if (border.isReady()) {

                    }
                    break;

                case STATE_MAXIMIZED:
                    if (border.isReady()) {

                    }
                    break;

                case STATE_MINIMIZED:
                    if (border.isClosed()) {

                    }
                    break;
            }
        }

        @Override
        public void update(int elapsedTime) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        void setState(int state) {

            switch (state) {
                case STATE_DEFAULT:
                    restore();
                    break;
                case STATE_MAXIMIZED:
                    maximize();
                    break;
                case STATE_MINIMIZED:
                    minimize();
                    break;
            }
        }

        void restore() {

            state = STATE_DEFAULT;
            //TODO
        }

        void maximize() {

            state = STATE_MAXIMIZED;
            //TODO
        }

        void minimize() {

            state = STATE_MINIMIZED;
            //TODO
        }
    }

    class RecordBox extends GameObject {

        private static final int SHOOTER = 10;
        private static final int SNAKE = 20;
        private static final int UPSTAIRS = 30;
        private static final int INFINITE = 40;
        private int gameTitle;
        private GameLevel gameLevel;

        private int state = STATE_DEFAULT;
        private Border border;

        RecordBox() {

            border = new Border(screen_w - Border.BORDER_SIZE * rate, screen_h / 2,
                    screen_w / 3 - Border.BORDER_SIZE * 2 * rate, screen_h - Border.BORDER_SIZE * 2 * rate, rate);
        }

        @Override
        public void onDestroy() {

            GameObject.release(border);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            border.draw(canvas, paint);

            switch (state) {
                case STATE_DEFAULT:
                    if (border.isReady()) {

                    }
                    break;

                case STATE_MAXIMIZED:
                    if (border.isReady()) {

                    }
                    break;

                case STATE_MINIMIZED:
                    if (border.isClosed()) {

                    }
                    break;
            }
        }

        @Override
        public void update(int elapsedTime) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {
            super.onTouchEvent(event);
        }

        void setState(int state) {

            switch (state) {
                case STATE_DEFAULT:
                    restore();
                    break;
                case STATE_MAXIMIZED:
                    maximize();
                    break;
                case STATE_MINIMIZED:
                    minimize();
                    break;
            }
        }

        void restore() {

            state = STATE_DEFAULT;
            //TODO
        }

        void maximize() {

            state = STATE_MAXIMIZED;
            //TODO
        }

        void minimize() {

            state = STATE_MINIMIZED;
            //TODO
        }
    }
}
