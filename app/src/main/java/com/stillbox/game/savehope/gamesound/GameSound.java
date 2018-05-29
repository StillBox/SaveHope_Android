package com.stillbox.game.savehope.gamesound;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.SparseArray;
import android.util.SparseIntArray;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.R;
import com.stillbox.game.savehope.gamedata.GameSettings;

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
        mainVolume = (float) GameSettings.mainVolume / 100f;
        bgmVolume = (float) GameSettings.bgmVolume / 100f;
        seVolume = (float) GameSettings.seVolume / 100f;
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

        public abstract void seekTo(int msec);

        public abstract int getDuration();

        public abstract int getCurrentPosition();

        public abstract void release();

        public abstract void update();

        public void setVolume(float volume) {
            this.volume = volume;
            actVolume = mainVolume * bgmVolume * volume;
        }
    }

    static class SingleBGM extends BGM {

        int resid;
        int duration;
        MediaPlayer firstPlayer;

        public SingleBGM(int resid) {
            this.resid = resid;
            firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            duration = firstPlayer.getDuration();
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
        public void seekTo(int msec) {
            firstPlayer.seekTo(msec);
        }

        @Override
        public int getDuration() {
            return duration;
        }

        @Override
        public int getCurrentPosition() {
            return firstPlayer.getCurrentPosition();
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
        int duration;
        int currentPlayer = 0;
        MediaPlayer firstPlayer, secondPlayer;
        MediaPlayer.OnCompletionListener firstListener, secondListener;
        boolean bIsFirstOver = false, bIsSecondOver = false;

        public LoopBGM(int resid) {
            this.resid = resid;
            firstListener = mp -> {
                bIsFirstOver = true;
                currentPlayer = 2;
            };
            secondListener = mp -> {
                bIsSecondOver = true;
                currentPlayer = 1;
            };

            firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            firstPlayer.setOnCompletionListener(firstListener);
            secondPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            secondPlayer.setOnCompletionListener(secondListener);
            firstPlayer.setNextMediaPlayer(secondPlayer);
            duration = firstPlayer.getDuration();
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
        public void seekTo(int msec) {
            switch (currentPlayer) {
                case 0:
                case 1:
                    firstPlayer.seekTo(msec);
                    break;
                case 2:
                    secondPlayer.seekTo(msec);
                    break;
            }
        }

        @Override
        public int getDuration() {
            return duration;
        }

        @Override
        public int getCurrentPosition() {
            switch (currentPlayer) {
                case 1:
                    return firstPlayer.getCurrentPosition();
                case 2:
                    return secondPlayer.getCurrentPosition();
                default:
                    return 0;
            }
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
        int duration_intro, duration;
        int currentPlayer = 0;
        MediaPlayer introPlayer, firstPlayer;
        MediaPlayer.OnCompletionListener introListener, firstListener;
        boolean bIsIntroOver = false, bIsFirstOver = false;

        public IntroSingleBGM(int resid_intro, int resid) {
            this.resid_intro = resid_intro;
            this.resid = resid;
            introListener = mp -> {
                bIsIntroOver = true;
                currentPlayer = 1;
            };
            firstListener = mp -> {
                bIsFirstOver = true;
                currentPlayer = 0;
            };
            introPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid_intro);
            introPlayer.setOnCompletionListener(introListener);
            firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            firstPlayer.setOnCompletionListener(firstListener);
            introPlayer.setNextMediaPlayer(firstPlayer);
            duration_intro = introPlayer.getDuration();
            duration = firstPlayer.getDuration();
            setVolume(1f);
        }

        @Override
        public void start() {
            switch (currentPlayer) {
                case -1:
                case 0:
                    introPlayer.start();
                    currentPlayer = -1;
                    break;
                case 1:
                    firstPlayer.start();
                    break;
            }
        }

        @Override
        public void pause() {
            switch (currentPlayer) {
                case -1:
                    introPlayer.pause();
                    break;
                case 1:
                    firstPlayer.pause();
                    break;
            }
        }

        @Override
        public void stop() {
            if (introPlayer.isPlaying())
                introPlayer.stop();
            if (firstPlayer.isPlaying())
                firstPlayer.stop();
        }

        @Override
        public void seekTo(int msec) {
            switch (currentPlayer) {
                case -1:
                    if (msec < duration_intro) {
                        introPlayer.seekTo(msec);
                    } else {
                        bIsIntroOver = true;
                        firstPlayer.seekTo(msec - duration_intro);
                        currentPlayer = 1;
                    }
                    break;
                case 0:
                    if (msec < duration_intro) {
                        introPlayer.seekTo(msec);
                        currentPlayer = -1;
                    } else {
                        firstPlayer.seekTo(msec - duration_intro);
                        currentPlayer = 1;
                    }
                    break;
                case 1:
                    if (msec < duration_intro) {
                        bIsFirstOver = true;
                        introPlayer.seekTo(msec);
                        currentPlayer = -1;
                    } else {
                        firstPlayer.seekTo(msec - duration_intro);
                    }
                    break;
            }
        }

        @Override
        public int getDuration() {
            return duration_intro + duration;
        }

        @Override
        public int getCurrentPosition() {
            switch (currentPlayer) {
                case -1:
                    return introPlayer.getCurrentPosition();
                case 1:
                    return duration_intro + firstPlayer.getCurrentPosition();
                default:
                    return 0;
            }
        }

        @Override
        public void release() {
            stop();
            introPlayer.release();
            firstPlayer.release();
        }

        @Override
        public void update() {
            if (bIsIntroOver) {
                bIsIntroOver = false;
                Thread thread = new Thread(() -> {
                    introPlayer.release();
                    introPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid_intro);
                    introPlayer.setOnCompletionListener(introListener);
                    introPlayer.setVolume(actVolume, actVolume);
                });
                thread.start();
            }
            if (bIsFirstOver) {
                bIsFirstOver = false;
                Thread thread = new Thread(() -> {
                    firstPlayer.release();
                    firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
                    firstPlayer.setOnCompletionListener(firstListener);
                    firstPlayer.setVolume(actVolume, actVolume);
                    introPlayer.setNextMediaPlayer(firstPlayer);
                });
                thread.start();
            }
        }

        @Override
        public void setVolume(float volume) {
            super.setVolume(volume);
            introPlayer.setVolume(actVolume, actVolume);
            firstPlayer.setVolume(actVolume, actVolume);
        }
    }

    static class IntroLoopBGM extends BGM {

        int resid, resid_intro;
        int duration, duration_intro;
        int currentPlayer = 0;
        MediaPlayer introPlayer, firstPlayer, secondPlayer;
        MediaPlayer.OnCompletionListener introListener, firstListener, secondListener;
        boolean bIsIntroOver = false, bIsFirstOver = false, bIsSecondOver = false;

        public IntroLoopBGM(int resid_intro, int resid) {
            this.resid_intro = resid_intro;
            this.resid = resid;
            introListener = mp -> {
                bIsIntroOver = true;
                currentPlayer = 1;
            };
            firstListener = mp -> {
                bIsFirstOver = true;
                currentPlayer = 2;
            };
            secondListener = mp -> {
                bIsSecondOver = true;
                currentPlayer = 1;
            };
            introPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid_intro);
            introPlayer.setOnCompletionListener(introListener);
            firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            firstPlayer.setOnCompletionListener(firstListener);
            secondPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
            secondPlayer.setOnCompletionListener(secondListener);
            introPlayer.setNextMediaPlayer(firstPlayer);
            firstPlayer.setNextMediaPlayer(secondPlayer);
            setVolume(1f);
        }

        @Override
        public void start() {
            switch (currentPlayer) {
                case -1:
                case 0:
                    introPlayer.start();
                    currentPlayer = -1;
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
                case -1:
                    introPlayer.pause();
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
            if (introPlayer.isPlaying())
                introPlayer.stop();
            if (firstPlayer.isPlaying())
                firstPlayer.stop();
            if (secondPlayer.isPlaying())
                secondPlayer.stop();
        }

        @Override
        public void seekTo(int msec) {
            switch (currentPlayer) {
                case -1:
                    if (msec < duration_intro) {
                        introPlayer.seekTo(msec);
                    } else {
                        bIsIntroOver = true;
                        firstPlayer.seekTo(msec - duration_intro);
                        currentPlayer = 1;
                    }
                    break;
                case 0:
                    if (msec < duration_intro) {
                        introPlayer.seekTo(msec);
                        currentPlayer = -1;
                    } else {
                        firstPlayer.seekTo(msec - duration_intro);
                        currentPlayer = 1;
                    }
                    break;
                case 1:
                    if (msec < duration_intro) {
                        bIsFirstOver = true;
                        introPlayer.seekTo(msec);
                        currentPlayer = -1;
                    } else {
                        firstPlayer.seekTo(msec - duration_intro);
                    }
                    break;
                case 2:
                    if (msec < duration_intro) {
                        bIsSecondOver = true;
                        introPlayer.seekTo(msec);
                        currentPlayer = -1;
                    } else {
                        secondPlayer.seekTo(msec - duration_intro);
                    }
                    break;
            }
        }

        @Override
        public int getDuration() {
            return duration_intro + duration;
        }

        @Override
        public int getCurrentPosition() {
            switch (currentPlayer) {
                case -1:
                    return introPlayer.getCurrentPosition();
                case 1:
                    return duration_intro + firstPlayer.getCurrentPosition();
                case 2:
                    return duration_intro + secondPlayer.getCurrentPosition();
                default:
                    return 0;
            }
        }

        @Override
        public void release() {
            stop();
            introPlayer.release();
            firstPlayer.release();
            secondPlayer.release();
        }

        @Override
        public void update() {
            if (bIsIntroOver) {
                bIsIntroOver = false;
                Thread thread = new Thread(() -> {
                    introPlayer.release();
                    introPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid_intro);
                    introPlayer.setOnCompletionListener(introListener);
                    introPlayer.setVolume(actVolume, actVolume);
                });
                thread.start();
            }
            if (bIsFirstOver) {
                bIsFirstOver = false;
                Thread thread = new Thread(() -> {
                    firstPlayer.release();
                    firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
                    firstPlayer.setOnCompletionListener(firstListener);
                    firstPlayer.setVolume(actVolume, actVolume);
                    introPlayer.setNextMediaPlayer(firstPlayer);
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
            introPlayer.setVolume(actVolume, actVolume);
            firstPlayer.setVolume(actVolume, actVolume);
            secondPlayer.setVolume(actVolume, actVolume);
        }
    }
}
