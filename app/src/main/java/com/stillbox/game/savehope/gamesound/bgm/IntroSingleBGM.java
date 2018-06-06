package com.stillbox.game.savehope.gamesound.bgm;

import android.media.MediaPlayer;

import com.stillbox.game.savehope.MainView;

public class IntroSingleBGM extends BGM {

    private int resid_intro, resid;
    private int duration_intro, duration;
    private int currentPlayer = 0;
    private MediaPlayer introPlayer, firstPlayer;
    private MediaPlayer.OnCompletionListener introListener, firstListener;
    private boolean bIsIntroOver = false, bIsFirstOver = false;

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
        setVolume(1f, 1f);
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
    public void setVolume() {
        super.setVolume();
        introPlayer.setVolume(actVolume, actVolume);
        firstPlayer.setVolume(actVolume, actVolume);
    }
}
