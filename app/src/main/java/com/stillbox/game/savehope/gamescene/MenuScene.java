package com.stillbox.game.savehope.gamescene;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.stillbox.game.savehope.CharaData;
import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamemenu.AboutMenu;
import com.stillbox.game.savehope.gamemenu.FreeMenu;
import com.stillbox.game.savehope.gamemenu.GameMenu;
import com.stillbox.game.savehope.gamemenu.MainMenu;
import com.stillbox.game.savehope.gamemenu.SelectMenu;
import com.stillbox.game.savehope.gamemenu.SettingMenu;
import com.stillbox.game.savehope.gamemenu.TitleMenu;
import com.stillbox.game.savehope.gameobject.LoadingBox;
import com.stillbox.game.savehope.gamesound.GameSound;

public class MenuScene extends GameScene {

    //Fields for Loading
    private boolean bIsLoading;
    private LoadingBox loadingBox;

    //Fields for Menu
    public static final int MENU_STATE_CENTER = 0;
    public static final int MENU_STATE_SIDE = 1;
    private int menuState;

    private TitleMenu titleMenu;
    private MainMenu mainMenu;
    private SelectMenu selectMenu;
    private SettingMenu settingMenu;
    private AboutMenu aboutMenu;
    private FreeMenu freeMenu;
    private GameMenu currentMenu;

    //Fields for Misc Resources
    private float boardW;
    public static final float MENU_SIDE_SIZE = 680f;
    private static final float MENU_MOVE_RATE = 2f;

    private static final float TITLE_OFFSET_X = 360f;
    private static final float TITLE_OFFSET_Y = 0f;
    private static final float TITLE_SIZE = 320f;
    private Title title;

    private static final float LOGO_OFFSET_X = 360f;
    private static final float LOGO_OFFSET_Y = 0f;
    private static final float LOGO_SIZE = 512f;
    private Logo logo;

    public static final float MENU_BOX_OFFSET_Y = 200f;
    public static final float MENU_BOX_SIZE_MIN = 128f;
    private MenuBox menuBox;

    private static final int CHARA_UPDATE_PERIOD = 6000;
    private SparseArray<Character> characters;
    private Character[] activeCharacters;
    private float charaUpdateTime;

    //Fields for Media Player
    private final int ID_BGM = R.raw.prepare;

    public MenuScene() {

        bIsLoading = true;
        loadingBox = new LoadingBox();
    }

    @Override
    public void init() {

        loadingBox.setMaxProgress(46);
        loadingBox.setCurrentProgress(0);

        Thread thread = new Thread(() -> {

            boardW = screen_w / 2;

            characters = new SparseArray<>();
            activeCharacters = new Character[2];

            for (int charaID : CharaData.ARR_PLAYABLE_CHARA_ID) {
                characters.put(charaID, new Character(charaID));
                loadingBox.increaseProgress(1);                                     //Total 36
            }

            int firstCharaID = Math.random() < 0.8 ? CharaData.IDC_HINATA :
                    Math.random() < 0.5 ? CharaData.IDC_HINATASUPER : CharaData.IDC_KAMUKURA;
            characters.get(firstCharaID).setActive();

            activeCharacters[0] = null;
            activeCharacters[1] = characters.get(firstCharaID);
            charaUpdateTime = 0;

            title = new Title();
            loadingBox.increaseProgress(1);

            logo = new Logo();
            loadingBox.increaseProgress(1);

            menuBox = new MenuBox();
            loadingBox.increaseProgress(1);                                         //Total 3

            titleMenu = new TitleMenu();
            loadingBox.increaseProgress(1);

            mainMenu = new MainMenu();
            loadingBox.increaseProgress(1);

            selectMenu = new SelectMenu();
            loadingBox.increaseProgress(1);

            settingMenu = new SettingMenu();
            loadingBox.increaseProgress(1);

            aboutMenu = new AboutMenu();
            loadingBox.increaseProgress(1);

            freeMenu = new FreeMenu();
            loadingBox.increaseProgress(1);                                         //Total 6

            menuState = MENU_STATE_CENTER;
            currentMenu = titleMenu;

            GameSound.createBGM(0, ID_BGM, true);
            GameSound.startBGM(0);
            loadingBox.increaseProgress(1);                                         //Total 1

            bIsLoading = false;
        });

        thread.start();
    }

    @Override
    public void onDestroy() {

        titleMenu.onDestroy();
        mainMenu.onDestroy();
        settingMenu.onDestroy();
        aboutMenu.onDestroy();
        selectMenu.onDestroy();
        freeMenu.onDestroy();

        title.onDestroyed();
        logo.onDestroy();
        menuBox.onDestroy();

        for (int i = 0; i < characters.size(); i++) {
            characters.valueAt(i).onDestroy();
        }

        GameSound.releaseBGM();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {

        if (bIsLoading) {
            loadingBox.draw(canvas, paint);
            return;
        }

        canvas.drawColor(Color.BLACK);
        paint.setColor(Color.WHITE);
        canvas.drawRect(0, 0, boardW, screen_h, paint);

        for (Character character : activeCharacters) {
            if (character != null) character.draw(canvas, paint);
        }

        title.draw(canvas, paint);
        logo.draw(canvas, paint);
        menuBox.draw(canvas);

        if (menuBox.isReady()) {
            currentMenu.draw(canvas, paint);
        }
    }

    @Override
    public void update(long elapsedTime) {

        if (bIsLoading) {
            loadingBox.update(elapsedTime);
            return;
        }

        if (menuState == MENU_STATE_CENTER) {
            charaUpdateTime += elapsedTime;
            if (charaUpdateTime >= CHARA_UPDATE_PERIOD) {
                charaUpdateTime -= CHARA_UPDATE_PERIOD;
                int nextCharaID;
                do {
                    nextCharaID = CharaData.getRandomPlayableCharaID();
                } while (isCharacterDuplicate(nextCharaID));
                activeCharacters[0] = activeCharacters[1];
                activeCharacters[0].setMovable();
                activeCharacters[1] = characters.get(nextCharaID);
                activeCharacters[1].setActive();
            }
        } else if (menuState == MENU_STATE_SIDE) {
            if (charaUpdateTime < CHARA_UPDATE_PERIOD)
                charaUpdateTime += elapsedTime;
            for (Character character : activeCharacters)
                if (character != null) character.setMovable();
        }
        for (Character character : activeCharacters) {
            if (character != null) character.update(elapsedTime);
        }

        if (currentMenu.menuID != GameMenu.getCurrentMenuID())
            setMenu(GameMenu.getCurrentMenuID());

        if (currentMenu.getRequiredState() != menuState) {
            if (menuBox.isMovable()) {
                if (currentMenu.getRequiredState() == MENU_STATE_SIDE) {
                    title.setShrink(true);
                    boardW += MENU_MOVE_RATE * rate * elapsedTime;
                    if (boardW >= screen_w - MENU_SIDE_SIZE * rate) {
                        boardW = screen_w - MENU_SIDE_SIZE * rate;
                        menuState = currentMenu.getRequiredState();
                        menuBox.setMovable(false);
                    }
                } else if (currentMenu.getRequiredState() == MENU_STATE_CENTER) {
                    title.setShrink(false);
                    boardW -= MENU_MOVE_RATE * rate * elapsedTime;
                    if (boardW <= screen_w / 2) {
                        boardW = screen_w / 2;
                        menuState = currentMenu.getRequiredState();
                        menuBox.setMovable(false);
                    }
                }
            }
        }

        title.update(elapsedTime);
        logo.update();
        menuBox.update(elapsedTime);

        if (menuBox.isReady()) {
            currentMenu.update(elapsedTime);
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

        if (bIsLoading)
            return;

        if (menuBox.isReady()) {
            currentMenu.onTouchEvent(event);
        }
    }

    private void setMenu(int menuID) {

        if (currentMenu.menuID == GameMenu.MENU_SETTING || currentMenu.menuID == GameMenu.MENU_ABOUT ||
                menuID == GameMenu.MENU_SETTING || menuID == GameMenu.MENU_ABOUT)
            menuBox.resize(true, true);
        else
            menuBox.resize(false, true);

        switch (menuID) {

            case GameMenu.MENU_TITLE:
                currentMenu = titleMenu;
                break;
            case GameMenu.MENU_MAIN:
                currentMenu = mainMenu;
                break;
            case GameMenu.MENU_SELECT:
                currentMenu = selectMenu;
                break;
            case GameMenu.MENU_SETTING:
                currentMenu = settingMenu;
                break;
            case GameMenu.MENU_ABOUT:
                currentMenu = aboutMenu;
                break;
            case GameMenu.MENU_FREE:
                currentMenu = freeMenu;
                break;
        }
    }

    private boolean isCharacterDuplicate(int charaID) {
        int[] activeCharaID = new int[2];
        activeCharaID[0] = activeCharacters[0] == null ? -1 : activeCharacters[0].getCharaID();
        activeCharaID[1] = activeCharacters[1] == null ? -1 : activeCharacters[1].getCharaID();
        if (charaID == activeCharaID[0] || charaID == activeCharaID[1])
            return true;
        if (charaID == CharaData.IDC_HINATA || charaID == CharaData.IDC_HINATASUPER || charaID == CharaData.IDC_KAMUKURA) {
            if (activeCharaID[0] == CharaData.IDC_HINATA || activeCharaID[0] == CharaData.IDC_HINATASUPER || activeCharaID[0] == CharaData.IDC_KAMUKURA ||
                    activeCharaID[1] == CharaData.IDC_HINATA || activeCharaID[1] == CharaData.IDC_HINATASUPER || activeCharaID[1] == CharaData.IDC_KAMUKURA)
                return true;
        }
        return false;
    }

    //Inner classes for menu scene

    private class Title {

        private float ZOOM_RATE = 0.01f;
        private float x, y, w;
        private float scale;
        private float angle;
        private float angularSpeed;
        private boolean bShrink;
        private Bitmap bmpTitle;
        private Bitmap bmpCircle;

        Title() {

            x = boardW - TITLE_OFFSET_X * rate;
            y = screen_h / 3 + TITLE_OFFSET_Y * rate;
            w = TITLE_SIZE * rate;
            scale = 1f;
            angle = -10f;
            angularSpeed = 0f;
            bShrink = false;

            Bitmap bmpRes = MainView.getBitmap(R.drawable.title);
            int width = bmpRes.getWidth();
            int height = bmpRes.getHeight();
            float scaleRes = w / height;
            Matrix mat = new Matrix();
            mat.postScale(scaleRes, scaleRes);
            bmpCircle = Bitmap.createBitmap(bmpRes, 0, 0, width / 2, height, mat, true);
            bmpTitle = Bitmap.createBitmap(bmpRes, width / 2, 0, width / 2, height, mat, true);
            bmpRes.recycle();
        }

        void onDestroyed() {

            bmpTitle.recycle();
            bmpTitle = null;
            bmpCircle.recycle();
            bmpCircle = null;
        }

        void draw(Canvas canvas, Paint paint) {

            if (scale != 0f) {
                canvas.save();
                canvas.scale(scale, scale, x, y);
                canvas.drawBitmap(bmpCircle, x - w / 2, y - w / 2, paint);
                Matrix mat = new Matrix();
                mat.postRotate(angle, w / 2, w / 2);
                mat.postTranslate(x - w / 2, y - w / 2);
                canvas.drawBitmap(bmpTitle, mat, paint);
                canvas.restore();
            }
        }

        void update(long elapsedTime) {

            if (bShrink) {
                if (scale > 0f) {
                    scale -= ZOOM_RATE * elapsedTime;
                    if (scale < 0f) scale = 0f;
                }
            } else {
                if (scale < 1f) {
                    scale += ZOOM_RATE * elapsedTime;
                    if (scale > 1f) scale = 1f;
                }
            }

            angle += elapsedTime * angularSpeed;
            angularSpeed -= elapsedTime * 0.0001f;
            if (angle <= -70f) {
                angle = -70f;
                angularSpeed = -angularSpeed;
            }
            if (angle > -10f) {
                angle = -10f;
                angularSpeed = 0.0f;
            }
        }

        public void setShrink(boolean bShrink) {
            this.bShrink = bShrink;
        }
    }

    private class Logo {

        private float x, y, w;
        private Bitmap bmpLogo;

        Logo() {

            x = boardW + LOGO_OFFSET_X * rate;
            y = screen_h / 3 + LOGO_OFFSET_Y * rate;
            w = LOGO_SIZE * rate;

            Bitmap bmpRes = MainView.getBitmap(R.drawable.logo);
            int width = bmpRes.getWidth();
            int height = bmpRes.getHeight();
            float scaleRes = w / width;
            Matrix mat = new Matrix();
            mat.postScale(scaleRes, scaleRes);
            if (mat.isIdentity())
                bmpLogo = bmpRes.copy(Bitmap.Config.ARGB_8888, true);
            else
                bmpLogo = Bitmap.createBitmap(bmpRes, 0, 0, width, height, mat, true);
            bmpRes.recycle();
        }

        void onDestroy() {

            bmpLogo.recycle();
            bmpLogo = null;
        }

        void draw(Canvas canvas, Paint paint) {

            canvas.drawBitmap(bmpLogo, x - w / 2, y - w / 2, paint);
        }

        void update() {

            x = boardW + 360f * rate;
        }
    }

    private class MenuBox {

        private float scale;
        private float x, y, w, h;
        private boolean bIsReady;
        private boolean bResizeX, bResizeY;
        private boolean bIsMovable;
        private Bitmap bmpMenu;
        private NinePatch npMenu;
        private static final float ZOOM_RATE = 2f;

        MenuBox() {

            x = boardW;
            y = screen_h / 2 + MENU_BOX_OFFSET_Y * rate;
            w = MENU_BOX_SIZE_MIN * rate;
            h = MENU_BOX_SIZE_MIN * rate;
            bIsReady = false;
            bResizeX = false;
            bResizeY = false;
            bIsMovable = false;

            bmpMenu = MainView.getBitmap(R.drawable.menu);
            int width = bmpMenu.getWidth();
            scale = 256f / width * rate;
            npMenu = new NinePatch(bmpMenu, bmpMenu.getNinePatchChunk(), null);
        }

        void onDestroy() {

            bmpMenu.recycle();
            bmpMenu = null;
            npMenu = null;
        }

        void draw(Canvas canvas) {

            canvas.save();
            canvas.scale(scale, scale, x, y);
            RectF rectF = menuState == MENU_STATE_CENTER ?
                    new RectF(x - w / scale / 2, y - h / scale / 2, x + w / scale / 2, y + h / scale / 2) :
                    new RectF(x - MENU_BOX_SIZE_MIN * rate / scale / 2, y - h / scale / 2, x + (w - MENU_BOX_SIZE_MIN * rate / 2) / scale, y + h / scale / 2);
            npMenu.draw(canvas, rectF);
            canvas.restore();
        }

        void update(long elapsedTime) {

            x = boardW;
            if (bResizeY) {
                if (h > MENU_BOX_SIZE_MIN * rate)
                    h -= ZOOM_RATE * rate * elapsedTime;
                if (h <= MENU_BOX_SIZE_MIN * rate) {
                    h = MENU_BOX_SIZE_MIN * rate;
                    bResizeY = false;
                }
            } else if (bResizeX) {
                if (w > MENU_BOX_SIZE_MIN * rate)
                    w -= ZOOM_RATE * rate * elapsedTime;
                if (w <= MENU_BOX_SIZE_MIN * rate) {
                    w = MENU_BOX_SIZE_MIN * rate;
                    bResizeX = false;
                    bIsMovable = true;
                }
            } else if (!bIsMovable) {
                if (w < currentMenu.getRequiredWidth()) {
                    w += ZOOM_RATE * rate * elapsedTime;
                    if (w > currentMenu.getRequiredWidth())
                        w = currentMenu.getRequiredWidth();
                } else if (h < currentMenu.getRequiredHeight()) {
                    h += ZOOM_RATE * rate * elapsedTime;
                    if (h > currentMenu.getRequiredHeight())
                        h = currentMenu.getRequiredHeight();
                } else {
                    if (!bIsReady) bIsReady = true;
                }
            }

        }

        void resize(boolean bResizeX, boolean bResizeY) {

            this.bResizeX = bResizeX;
            this.bResizeY = bResizeY;
            bIsReady = false;
            bIsMovable = false;
        }

        boolean isReady() {

            return bIsReady;
        }

        void setMovable(boolean bIsMovable) {

            this.bIsMovable = bIsMovable;
        }

        boolean isMovable() {

            return bIsMovable;
        }
    }

    private class Character {

        private static final float CHARA_U = 0.6f;
        private static final float CHARA_V = 0.9f;

        private static final int RIGHT_STAY = 0;
        private static final int RIGHT_STEP = 1;

        private int charaID;
        private float x, y;
        private float w, h;
        private int updateTime;
        private int currentSprite;
        private boolean bIsMovable;
        private boolean bIsActive;
        private SparseArray<Bitmap> sprites;

        Character(int charaID) {

            this.charaID = charaID;
            x = (screen_w / 2 - TITLE_OFFSET_X * rate) / 2;
            y = 0f;
            w = 192f * rate;
            h = 215f * rate;
            updateTime = 0;
            currentSprite = RIGHT_STEP;
            bIsActive = false;
            bIsMovable = true;

            sprites = new SparseArray<>();
            Bitmap bmpRes = MainView.getBitmap(CharaData.getResID(charaID));
            int res_w = bmpRes.getWidth() / 8;
            int res_h = bmpRes.getHeight() * 9 / 64;
            float scale = w / res_w;

            Bitmap bmpSprite;
            Bitmap bmpTemp = Bitmap.createBitmap(res_w, 2 * res_w, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bmpTemp);
            Paint paint = new Paint();

            Matrix mat = new Matrix();
            mat.postScale(scale, scale);

            canvas.clipRect(0, 0, res_w, res_h);
            canvas.drawBitmap(bmpRes, -2 * res_w, -3 * res_h, paint);
            if (mat.isIdentity())
                bmpSprite = bmpTemp.copy(Bitmap.Config.ARGB_8888, true);
            else
                bmpSprite = Bitmap.createBitmap(bmpTemp, 0, 0, res_w, 2 * res_w, mat, true);
            sprites.put(RIGHT_STAY, bmpSprite);

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            canvas.drawBitmap(bmpRes, -3 * res_w, -3 * res_h, paint);
            if (mat.isIdentity())
                bmpSprite = bmpTemp.copy(Bitmap.Config.ARGB_8888, true);
            else
                bmpSprite = Bitmap.createBitmap(bmpTemp, 0, 0, res_w, 2 * res_w, mat, true);
            sprites.put(RIGHT_STEP, bmpSprite);

            bmpTemp.recycle();
        }

        public void onDestroy() {

            for (int i = 0; i < sprites.size(); i++)
                sprites.valueAt(i).recycle();
            sprites.clear();
            sprites = null;
        }

        public void draw(Canvas canvas, Paint paint) {

            if (!bIsActive) return;

            canvas.drawBitmap(sprites.get(currentSprite), x - w / 2, y - h, paint);
        }

        public void update(long elapsedTime) {

            if (!bIsActive) return;

            if (y < screen_h) {
                y += CHARA_V * rate * elapsedTime;
                if (y > screen_h) y = screen_h;
            } else {
                updateTime += elapsedTime;
                if (updateTime >= 160) {
                    updateTime -= 160;
                    currentSprite = currentSprite == RIGHT_STEP ? RIGHT_STAY : RIGHT_STEP;
                }
                if (bIsMovable) {
                    x += 1.5f * CHARA_U * rate * elapsedTime;
                    if (x >= screen_w + w)
                        bIsActive = false;
                } else if (x < screen_w / 2f - TITLE_OFFSET_X * rate) {
                    x += CHARA_U * rate * elapsedTime;
                    if (x >= screen_w / 2f - TITLE_OFFSET_X * rate) {
                        x = screen_w / 2f - TITLE_OFFSET_X * rate;
                    }
                }
            }
        }

        int getCharaID() {
            return charaID;
        }

        void setActive() {

            x = (screen_w / 2 - TITLE_OFFSET_X * rate) / 2;
            y = 0f;
            updateTime = 0;
            currentSprite = RIGHT_STEP;
            bIsActive = true;
            bIsMovable = false;
        }

        void setMovable() {

            bIsMovable = true;
        }
    }
}
