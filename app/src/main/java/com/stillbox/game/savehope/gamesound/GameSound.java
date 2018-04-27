package com.stillbox.game.savehope.gamesound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;

public class GameSound {

    public static final int ID_SE_CURSOR = R.raw.cursor;
    public static final int ID_SE_DECIDE = R.raw.decide;
    public static final int ID_SE_CANCEL = R.raw.cancel;
    public static final int ID_SE_SELECT = R.raw.select;

    private static float mainVolume = 1f;
    private static float bgmVolume = 1f;
    private static float seVolume = 1f;

    //Fields for BGM
    private static SparseArray<BGM> bgms;
    private static int currentBgm;

    //Fields for Sound Effects
    private static SoundPool soundPool;
    private static SoundPool uiSoundPool;
    private static SparseIntArray ses;
    private static SparseIntArray uises;

    public static void init() {
        bgms = new SparseArray<>();
        ses = new SparseIntArray();
        uises = new SparseIntArray();
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        uiSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        uises.put(ID_SE_CURSOR, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_CURSOR, 1));
        uises.put(ID_SE_DECIDE, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_DECIDE, 1));
        uises.put(ID_SE_CANCEL, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_CANCEL, 1));
        uises.put(ID_SE_SELECT, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_SELECT, 1));
    }

    public static void release() {
        releaseBGM();
        releaseSE();
        uiSoundPool.release();
        uises.clear();
    }

    //Methods for BGM

    public static void startBGM(int bgmid) {
        stopBGM();
        currentBgm = bgmid;
        bgms.get(bgmid).start();
    }

    public static void startBGM() {
        bgms.get(currentBgm).start();
    }

    public static void pauseBGM() {
        bgms.get(currentBgm).pause();
    }

    public static void stopBGM() {
        for (int i = 0; i < bgms.size(); i++)
            bgms.valueAt(i).stop();
    }

    public static void releaseBGM() {
        for (int i = 0; i < bgms.size(); i++)
            bgms.valueAt(i).release();
        bgms.clear();
    }

    public static void updateBGM() {
        BGM bgm = bgms.get(currentBgm);
        if (bgm != null)
            bgm.update();
    }

    public static void createBGM(int bgmid, int resid, boolean bIsLoop) {
        if (!bIsLoop)
            bgms.put(bgmid, new SingleBGM(resid));
        else
            bgms.put(bgmid, new LoopBGM(resid));
    }

    public static void createBGM(int bgmid, int resid_intro, int resid, boolean bIsLoop) {
        if (!bIsLoop)
            bgms.put(bgmid, new IntroSingleBGM(resid_intro, resid));
        else
            bgms.put(bgmid, new IntroLoopBGM(resid_intro, resid));
    }

    //Methods for sound effects

    public static void addSE(int seid, int resid) {

        int soundId = soundPool.load(MainView.mainView.getContext(), resid, 1);
        ses.put(seid, soundId);
    }

    public static void playSE(int seid) {

        float volume = mainVolume * seVolume;
        int soundId;
        if (uises.get(seid) != 0) {
            soundId = uises.get(seid);
            uiSoundPool.play(soundId, volume, volume, 0, 0, 1);
        }
        if (ses.get(seid) != 0) {
            soundId = ses.get(seid);
            soundPool.play(soundId, volume, volume, 0, 0, 1);
        }
    }

    public static void releaseSE() {

        soundPool.release();
        ses.clear();
    }

    //Methods for volume

    public static void setMainVolume(float mainVolume) {
        GameSound.mainVolume = mainVolume;
        for (int i = 0; i < bgms.size(); i++) {
            BGM bgm = bgms.valueAt(i);
            bgm.setVolume(bgm.volume);
        }
    }

    public static void setBgmVolume(float bgmVolume) {
        GameSound.bgmVolume = bgmVolume;
        for (int i = 0; i < bgms.size(); i++) {
            BGM bgm = bgms.valueAt(i);
            bgm.setVolume(bgm.volume);
        }
    }

    public static void setSeVolume(float seVolume) {
        GameSound.seVolume = seVolume;
    }

    public static void setBgmVolume(int bgmid, float volume) {
        bgms.get(bgmid).setVolume(volume);
    }

    public static float getMainVolume() {
        return mainVolume;
    }

    public static float getBgmVolume() {
        return bgmVolume;
    }

    public static float getSeVolume() {
        return seVolume;
    }

    //class for bgm

    static abstract class BGM {

        float volume;
        float actVolume;

        public abstract void start();

        public abstract void pause();

        public abstract void stop();

        public abstract void release();

        public abstract void update();

        public void setVolume(float volume) {
            this.volume = volume;
            actVolume = mainVolume * bgmVolume * volume;
        }
    }

    static class SingleBGM extends BGM {

        int resid;
        MediaPlayer firstPlayer;

        public SingleBGM(int resid) {
            this.resid = resid;
            firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            setVolume(1f);
        }

        @Override
        public void start() {
            firstPlayer.start();
        }

        @Override
        public void pause() {
            firstPlayer.pause();
        }

        @Override
        public void stop() {
            firstPlayer.stop();
        }

        @Override
        public void release() {
            firstPlayer.release();
        }

        @Override
        public void update() {

        }

        @Override
        public void setVolume(float volume) {
            super.setVolume(volume);
            firstPlayer.setVolume(actVolume, actVolume);
        }
    }

    static class LoopBGM extends BGM {

        int resid;
        int currentPlayer = 0;
        MediaPlayer firstPlayer, secondPlayer;
        MediaPlayer.OnCompletionListener firstListener, secondListener;
        boolean bIsFirstOver = false, bIsSecondOver = false;

        public LoopBGM(int resid) {
            this.resid = resid;
            firstListener = mp -> bIsFirstOver = true;
            secondListener = mp -> bIsSecondOver = true;
            firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            firstPlayer.setOnCompletionListener(firstListener);
            secondPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            secondPlayer.setOnCompletionListener(secondListener);
            firstPlayer.setNextMediaPlayer(secondPlayer);
            setVolume(1f);
        }

        @Override
        public void start() {
            switch (currentPlayer) {
                case 0:
                    firstPlayer.start();
                    currentPlayer = 1;
                    break;
                case 1:
                    firstPlayer.start();
                    break;
                case 2:
                    secondPlayer.start();
                    break;
            }
        }

        @Override
        public void pause() {
            switch (currentPlayer) {
                case 0:
                    break;
                case 1:
                    firstPlayer.pause();
                    break;
                case 2:
                    secondPlayer.pause();
                    break;
            }
        }

        @Override
        public void stop() {
            if (firstPlayer.isPlaying())
                firstPlayer.stop();
            if (secondPlayer.isPlaying())
                secondPlayer.stop();
        }

        @Override
        public void release() {
            stop();
            firstPlayer.release();
            secondPlayer.release();
        }

        @Override
        public void update() {
            if (bIsFirstOver) {
                bIsFirstOver = false;
                Thread thread = new Thread(() -> {
                    firstPlayer.release();
                    firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
                    firstPlayer.setOnCompletionListener(firstListener);
                    firstPlayer.setVolume(actVolume, actVolume);
                    secondPlayer.setNextMediaPlayer(firstPlayer);
                });
                thread.start();
            }
            if (bIsSecondOver) {
                bIsSecondOver = false;
                Thread thread = new Thread(() -> {
                    secondPlayer.release();
                    secondPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
                    secondPlayer.setOnCompletionListener(secondListener);
                    secondPlayer.setVolume(actVolume, actVolume);
                    firstPlayer.setNextMediaPlayer(secondPlayer);
                });
                thread.start();
            }
        }

        @Override
        public void setVolume(float volume) {
            super.setVolume(volume);
            firstPlayer.setVolume(actVolume, actVolume);
            secondPlayer.setVolume(actVolume, actVolume);
        }
    }

    static class IntroSingleBGM extends BGM {

        int resid_intro, resid;
        int currentPlayer = 0;
        MediaPlayer firstPlayer, secondPlayer;

        public IntroSingleBGM(int resid_intro, int resid) {
            this.resid_intro = resid_intro;
            this.resid = resid;
            firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid_intro);
            secondPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            firstPlayer.setNextMediaPlayer(secondPlayer);
            setVolume(1f);
        }

        @Override
        public void start() {
            switch (currentPlayer) {
                case 0:
                    firstPlayer.start();
                    currentPlayer = 1;
                    break;
                case 1:
                    firstPlayer.start();
                    break;
                case 2:
                    secondPlayer.start();
                    break;
            }
        }

        @Override
        public void pause() {
            switch (currentPlayer) {
                case 0:
                    break;
                case 1:
                    firstPlayer.pause();
                    break;
                case 2:
                    secondPlayer.pause();
                    break;
            }
        }

        @Override
        public void stop() {
            if (firstPlayer.isPlaying())
                firstPlayer.stop();
            if (secondPlayer.isPlaying())
                secondPlayer.stop();
        }

        @Override
        public void release() {
            stop();
            firstPlayer.release();
            secondPlayer.release();
        }

        @Override
        public void update() {

        }

        @Override
        public void setVolume(float volume) {
            super.setVolume(volume);
            firstPlayer.setVolume(actVolume, actVolume);
            secondPlayer.setVolume(actVolume, actVolume);
        }
    }

    static class IntroLoopBGM extends BGM {

        int resid, resid_intro;
        int currentPlayer = 0;
        MediaPlayer firstPlayer, secondPlayer;
        MediaPlayer.OnCompletionListener firstListener, secondListener;
        boolean bIsFirstOver = false, bIsSecondOver = false;

        public IntroLoopBGM(int resid_intro, int resid) {
            this.resid_intro = resid_intro;
            this.resid = resid;
            firstListener = mp -> bIsFirstOver = true;
            secondListener = mp -> bIsSecondOver = true;
            firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid_intro);
            firstPlayer.setOnCompletionListener(firstListener);
            secondPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            secondPlayer.setOnCompletionListener(secondListener);
            firstPlayer.setNextMediaPlayer(secondPlayer);
            setVolume(1f);
        }

        @Override
        public void start() {
            switch (currentPlayer) {
                case 0:
                    firstPlayer.start();
                    currentPlayer = 1;
                    break;
                case 1:
                    firstPlayer.start();
                    break;
                case 2:
                    secondPlayer.start();
                    break;
            }
        }

        @Override
        public void pause() {
            switch (currentPlayer) {
                case 0:
                    break;
                case 1:
                    firstPlayer.pause();
                    break;
                case 2:
                    secondPlayer.pause();
                    break;
            }
        }

        @Override
        public void stop() {
            if (firstPlayer.isPlaying())
                firstPlayer.stop();
            if (secondPlayer.isPlaying())
                secondPlayer.stop();
        }

        @Override
        public void release() {
            stop();
            firstPlayer.release();
            secondPlayer.release();
        }

        @Override
        public void update() {
            if (bIsFirstOver) {
                bIsFirstOver = false;
                Thread thread = new Thread(() -> {
                    firstPlayer.release();
                    firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
                    firstPlayer.setOnCompletionListener(firstListener);
                    firstPlayer.setVolume(actVolume, actVolume);
                    secondPlayer.setNextMediaPlayer(firstPlayer);
                });
                thread.start();
            }
            if (bIsSecondOver) {
                bIsSecondOver = false;
                Thread thread = new Thread(() -> {
                    secondPlayer.release();
                    secondPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
                    secondPlayer.setOnCompletionListener(secondListener);
                    secondPlayer.setVolume(actVolume, actVolume);
                    firstPlayer.setNextMediaPlayer(secondPlayer);
                });
                thread.start();
            }
        }

        @Override
        public void setVolume(float volume) {
            super.setVolume(volume);
            firstPlayer.setVolume(actVolume, actVolume);
            secondPlayer.setVolume(actVolume, actVolume);
        }
    }
}
