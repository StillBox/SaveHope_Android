package com.stillbox.game.savehope.gamesound.bgm;

import android.media.MediaPlayer;

import com.stillbox.game.savehope.MainView;

import java.io.IOException;

public class SingleBGM extends BGM {

    private int resid;
    private int duration;
    private MediaPlayer firstPlayer;

    public SingleBGM(int resid) {
        this.resid = resid;
        firstPlayer = MediaPlayer.create(MainView.mainView.getContext(), resid);
        firstPlayer.setNextMediaPlayer(null);
        duration = firstPlayer.getDuration();
        setVolume(1f, 1f);
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
        if (firstPlayer.isPlaying())
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
    public void setVolume() {
        super.setVolume();
        firstPlayer.setVolume(actVolume, actVolume);
    }
}
