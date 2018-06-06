package com.stillbox.game.savehope.gamedata;

import com.stillbox.game.savehope.gamescene.ShooterScene;
import com.stillbox.game.savehope.gamescene.SnakeScene;
import com.stillbox.game.savehope.gamescene.UpstairsScene;
import com.stillbox.game.savehope.gamesound.GameSound;

public class GameSettings {

    //Common settings

    //Volume
    public static int mainVolume;
    public static int bgmVolume;
    public static int seVolume;

    //Control
    public static final int CONTROL_BUTTON = 10;
    public static final int CONTROL_GESTURE = 20;

    //Shooter
    public static int controlMode_st;
    public static int scannerAlpha_st;
    public static int adjustTime_st;

    //Snake
    public static int controlMode_sk;
    public static int scannerAlpha_sk;
    public static int adjustTime_sk;

    //Upstairs
    public static int controlMode_us;

    public static void loadSettings() {

        mainVolume = 6;
        bgmVolume = 6;
        seVolume = 6;

        controlMode_st = CONTROL_BUTTON;
        scannerAlpha_st = 63;
        adjustTime_st = 0;

        controlMode_sk = CONTROL_BUTTON;
        scannerAlpha_sk = 63;
        adjustTime_sk = 0;

        controlMode_us = CONTROL_BUTTON;
    }

    public static void saveSettings() {

    }

    public static void setMainVolume(int mainVolume) {
        GameSettings.mainVolume = mainVolume;
        GameSound.setMainVolume((float) mainVolume / 10f);
        saveSettings();
    }

    public static void setBgmVolume(int bgmVolume) {
        GameSettings.bgmVolume = bgmVolume;
        GameSound.setBgmVolume((float) bgmVolume / 10f);
        saveSettings();
    }

    public static void setSeVolume(int seVolume) {
        GameSettings.seVolume = seVolume;
        GameSound.setSeVolume((float) seVolume / 10f);
        saveSettings();
    }

    public static void setControlMode(int controlMode) {

        GameSettings.controlMode_st = controlMode;
        GameSettings.controlMode_sk = controlMode;
        GameSettings.controlMode_us = controlMode;
        ShooterScene.setControlMode(controlMode);
        SnakeScene.setControlMode(controlMode);
        UpstairsScene.setControlMode(controlMode);
        saveSettings();
    }

    public static void setControlMode_st(int controlMode) {
        GameSettings.controlMode_st = controlMode;
        ShooterScene.setControlMode(controlMode);
        saveSettings();
    }

    public static void setScannerAlpha_st(int scannerAlpha) {
        GameSettings.scannerAlpha_st = scannerAlpha;
        ShooterScene.setScannerAlpha(scannerAlpha);
        saveSettings();
    }

    public static void setAdjustTime_st(int adjustTime) {
        GameSettings.adjustTime_st = adjustTime;
        ShooterScene.setAdjustTime(adjustTime);
        saveSettings();
    }

    public static void setControlMode_sk(int controlMode) {
        GameSettings.controlMode_sk = controlMode;
        SnakeScene.setControlMode(controlMode);
        saveSettings();
    }

    public static void setScannerAlpha_sk(int scannerAlpha) {
        GameSettings.scannerAlpha_sk = scannerAlpha;
        SnakeScene.setScannerAlpha(scannerAlpha);
        saveSettings();
    }

    public static void setAdjustTime_sk(int adjustTime) {
        GameSettings.adjustTime_sk = adjustTime;
        SnakeScene.setAdjustTime(adjustTime);
        saveSettings();
    }

    public static void setControlMode_us(int controlMode) {
        GameSettings.controlMode_us = controlMode;
        UpstairsScene.setControlMode(controlMode);
        saveSettings();
    }
}
