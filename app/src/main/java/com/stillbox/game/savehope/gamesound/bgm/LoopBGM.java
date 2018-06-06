package com.stillbox.game.savehope.gamesound.bgm;

import android.media.MediaPlayer;

import com.stillbox.game.savehope.MainView;

public class LoopBGM extends BGM {

    private int resid;
    private int duration;
    private int currentPlayer = 0;
    private MediaPlayer firstPlayer, secondPlayer;
    private MediaPlayer.OnCompletionListener firstListener, secondListener;
    private boolean bIsFirstOver = false, bIsSecondOver = false;

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
        setVolume(1f, 1f);
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
    public void setVolume() {
        super.setVolume();
        firstPlayer.setVolume(actVolume, actVolume);
        secondPlayer.setVolume(actVolume, actVolume);
    }
}
