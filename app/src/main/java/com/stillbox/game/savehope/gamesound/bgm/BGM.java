package com.stillbox.game.savehope.gamesound.bgm;


import static com.stillbox.game.savehope.gamesound.GameSound.bgmVolume;
import static com.stillbox.game.savehope.gamesound.GameSound.mainVolume;

public abstract class BGM {

    float volume;
    float fxVolume;
    float actVolume;

    public abstract void start();

    public abstract void pause();

    public abstract void stop();

    public abstract void seekTo(int msec);

    public abstract boolean isPlaying();

    public abstract int getDuration();

    public abstract int getCurrentPosition();

    public abstract void release();

    public abstract void update();

    public void setVolume() {
        actVolume = mainVolume * bgmVolume * volume * fxVolume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        setVolume();
    }

    public void setFxVolume(float fxVolume) {
        this.fxVolume = fxVolume;
        setVolume();
    }

    public void setVolume(float volume, float fxVolume) {
        this.volume = volume;
        this.fxVolume = fxVolume;
        setVolume();
    }
}
