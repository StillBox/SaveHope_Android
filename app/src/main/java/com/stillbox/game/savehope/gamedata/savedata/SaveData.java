package com.stillbox.game.savehope.gamedata.savedata;

import android.util.SparseArray;

import com.stillbox.game.savehope.MainView;
import com.stillbox.game.savehope.gamedata.CharaData;
import com.stillbox.game.savehope.gameenum.GameLevel;
import com.stillbox.game.savehope.gameenum.StoryChapter;

public class SaveData {

    public static StoryChapter currentChapter;
    public static GameLevel currentLevel;
    public static int currentCharacter;

    public static int[] stageClear;
    public static int[] elementUnlock;
    public static int hopeFragment;
    public static int goldFragment;
    private static SparseArray<CharaStat> charaStats;

    public static ShooterRecord[][] shooterRecords;
    public static SnakeRecord[][] snakeRecords;
    public static UpstairsRecord[][] upstairsRecords;
    public static InfiniteRecord[] infiniteRecords;

    public static void readSaveData() {

        currentLevel = GameLevel.HARD;
        currentChapter = StoryChapter.TRUE_END;
        currentCharacter = CharaData.IDC_HINATA;

        stageClear = new int[4];
        for (int i = 0; i < stageClear.length; i++) {
            stageClear[i] = 0;
        }

        elementUnlock = new int[4];
        for (int i = 0; i < elementUnlock.length; i++) {
            elementUnlock[i] = 0;
        }

        hopeFragment = 0;
        goldFragment = 0;

        charaStats = new SparseArray<>();
        for (int id : CharaData.ARR_PLAYABLE_CHARA_ID) {
            CharaStat charaStat = new CharaStat();
            charaStat.id = id;
            charaStat.available = id == CharaData.IDC_NAEGI ? 1 :
                    id == CharaData.IDC_HINATA ? 1 : id == CharaData.IDC_SAIHARA ? 1 : 0;
            for (int i = 0; i < CharaStat.SKILL_COUNT; i++) {
                charaStat.stat[i] = 0;
            }
            charaStats.put(id, charaStat);
        }
    }

    public static void writeSaveData() {
        //TODO
    }

    public static void saveStoryProgress(StoryChapter chapter, GameLevel level) {

        currentChapter = chapter;
        currentLevel = level;

        //TODO
    }

    public static void saveInUseCharacter(int characterId) {
        //TODO
    }

    public static void saveClearState(int index, boolean bClear) {
        //TODO
    }

    public static void saveUnlockState(int index, boolean bUnlock) {

        if (bUnlock) {
            elementUnlock[index] = 1;
        } else {
            elementUnlock[index] = 0;
        }

        //TODO
    }

    public static void saveFragmentCount(int hopeFragment, int goldFragment) {
        //TODO
    }

    public static void saveCharacterAvailable(int id) {
        //TODO
    }

    public static void saveCharacterStat(int id, int stat, int level) {
        //TODO
    }

    public static void checkShooterRecord() {
        //TODO
    }

    public static void checkSnakeRecord() {
        //TODO
    }

    public static void checkUpstairsRecord() {
        //TODO
    }

    public static void checkInfiniteRecord() {
        //TODO
    }
}
