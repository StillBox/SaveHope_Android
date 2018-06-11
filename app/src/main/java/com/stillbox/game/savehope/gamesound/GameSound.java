package com.stillbox.game.savehope.gamesound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamedata.GameSettings;
import com.stillbox.game.savehope.gamesound.bgm.BGM;
import com.stillbox.game.savehope.gamesound.bgm.IntroLoopBGM;
import com.stillbox.game.savehope.gamesound.bgm.IntroSingleBGM;
import com.stillbox.game.savehope.gamesound.bgm.LoopBGM;
import com.stillbox.game.savehope.gamesound.bgm.SingleBGM;

public class GameSound {

    public static final int ID_SE_CURSOR = R.raw.cursor;
    public static final int ID_SE_DECIDE = R.raw.decide;
    public static final int ID_SE_CANCEL = R.raw.cancel;
    public static final int ID_SE_SELECT = R.raw.select;
    public static final int ID_SE_SCORE = R.raw.score;
    public static final int ID_SE_TEXT = R.raw.text;

    public static float mainVolume = 1f;
    public static float bgmVolume = 1f;
    public static float seVolume = 1f;
    private static ShadeTimer timer = null;

    //Fields for BGM
    private static SparseArray<BGM> bgms;
    private static int currentBgm;

    //Fields for Sound Effects
    private static SoundPool soundPool;
    private static SoundPool uiSoundPool;
    private static SparseIntArray ses;
    private static SparseIntArray uises;

    //Fields for control
    private static boolean bIsBgmPlaying;

    public static void init() {

        mainVolume = (float) GameSettings.mainVolume / 10f;
        bgmVolume = (float) GameSettings.bgmVolume / 10f;
        seVolume = (float) GameSettings.seVolume / 10f;

        bgms = new SparseArray<>();

        ses = new SparseIntArray();
        uises = new SparseIntArray();
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        uiSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        uises.put(ID_SE_CURSOR, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_CURSOR, 1));
        uises.put(ID_SE_DECIDE, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_DECIDE, 1));
        uises.put(ID_SE_CANCEL, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_CANCEL, 1));
        uises.put(ID_SE_SELECT, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_SELECT, 1));
        uises.put(ID_SE_SCORE, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_SCORE, 1));
        uises.put(ID_SE_TEXT, uiSoundPool.load(MainView.mainView.getContext(), ID_SE_TEXT, 1));

        bIsBgmPlaying = false;
    }

    public static void release() {

        releaseBGM();
        releaseSE();
        uiSoundPool.release();
        uises.clear();
        bIsBgmPlaying = false;
    }

    //Methods for BGM

    public static void startBGM(int bgmid) {
        stopBGM();
        currentBgm = bgmid;
        bgms.get(bgmid).start();
        bIsBgmPlaying = true;
    }

    public static void startBGM() {
        bgms.get(currentBgm).start();
        bIsBgmPlaying = true;
    }

    public static void pauseBGM() {
        bgms.get(currentBgm).pause();
        bIsBgmPlaying = false;
    }

    public static void stopBGM() {
        for (int i = 0; i < bgms.size(); i++)
            bgms.valueAt(i).stop();
        timer = null;
        bIsBgmPlaying = false;
    }

    public static boolean isBgmPlaying() {
        return bIsBgmPlaying;
    }

    public static boolean isBgmPlaying(int bgmid) {
        BGM bgm = bgms.get(bgmid);
        if (bgm == null) {
            return false;
        } else {
            return bgm.isPlaying();
        }
    }

    public static int getDuration(int bgmid) {
        return bgms.get(bgmid).getDuration();
    }

    public static int getCurrentPosition() {
        return bgms.get(currentBgm).getCurrentPosition();
    }

    public static void seekTo(int msec) {
        bgms.get(currentBgm).seekTo(msec);
    }

    public static void releaseBGM() {

        for (int i = 0; i < bgms.size(); i++)
            bgms.valueAt(i).release();
        bgms.clear();
        timer = null;
        bIsBgmPlaying = false;
    }

    public static void releaseBGM(int bgmid) {

        BGM bgm = bgms.get(bgmid);
        if (bgm != null) {
            bgm.release();
            bgms.remove(bgmid);
        }
        if (currentBgm == bgmid) {
            bIsBgmPlaying = false;
        }
    }

    public static void updateBGM(int elapsedTime) {

        BGM bgm = bgms.get(currentBgm);

        if (bgm != null) {
            bgm.update();
            if (timer != null) {
                timer.time += elapsedTime;
                if (elapsedTime > timer.duration) {
                    if (timer.listener != null) {
                        timer.listener.onShadeOver();
                    }
                    timer = null;
                } else {
                    float volume = timer.begVolume + (timer.endVolume - timer.begVolume) * timer.time / timer.duration;
                    bgm.setFxVolume(volume);
                }
            }
        }
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
            bgm.setVolume();
        }
    }

    public static void setBgmVolume(float bgmVolume) {
        GameSound.bgmVolume = bgmVolume;
        for (int i = 0; i < bgms.size(); i++) {
            BGM bgm = bgms.valueAt(i);
            bgm.setVolume();
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

    public static void shadeVolume(int duration, float begVolume, float endVolume) {

        shadeVolume(duration, begVolume, endVolume, null);
    }

    public static void shadeVolume(int duration, float begVolume, float endVolume, OnShadeOverListener listener) {

        timer = new ShadeTimer();
        timer.time = 0;
        timer.duration = duration;
        timer.begVolume = begVolume;
        timer.endVolume = endVolume;
        timer.listener = listener;
    }

    public static void fadeIn(int duration) {

        startBGM();
        shadeVolume(duration, 0f, 1f);
    }

    public static void fadeOut(int duration) {

        shadeVolume(duration, 1f, 0f, () -> pauseBGM());
    }

    private static class ShadeTimer {

        int time;
        int duration;
        float begVolume;
        float endVolume;
        OnShadeOverListener listener;
    }

    public interface OnShadeOverListener {

        void onShadeOver();
    }
}
