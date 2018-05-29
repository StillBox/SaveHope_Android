package com.stillbox.game.savehope.gamedata;

import android.util.SparseArray;

import com.stillbox.game.savehope.MainView;
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

    public static void readSaveData() {

        currentLevel = GameLevel.EASY;
        currentChapter = StoryChapter.SNAKE;
        currentCharacter = CharaData.IDC_HINATA;

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
        //TODO
    }

    public static void saveInUseCharacter(int characterId) {
        //TODO
    }

    public static void saveClearState(int index) {
        //TODO
    }

    public static void saveUnlockState(int index) {
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

    public static class CharaStat {

        static final int SKILL_COUNT = 6;

        int id;
        int available;
        int stat[] = new int[SKILL_COUNT];
    }
}
