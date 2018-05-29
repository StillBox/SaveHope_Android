package com.stillbox.game.savehope.gamedata;

import android.util.SparseArray;

import com.stillbox.game.savehope.R;

import java.util.ArrayList;

public class CharaData {

    public static final int TOTAL_CHARA_COUNT = 41;
    public static final int PLAYABLE_CHARA_COUNT = 36;

    public static final ArrayList<Integer> ARR_CHARA_ID;
    public static final ArrayList<Integer> ARR_PLAYABLE_CHARA_ID;

    public static final int IDC_NAEGI = 100;
    public static final int IDC_KIRIGIRI = 101;
    public static final int IDC_TOGAMI = 102;
    public static final int IDC_FUKAWA = 103;
    public static final int IDC_ASAHINA = 104;
    public static final int IDC_HAGAKURE = 105;
    public static final int IDC_MAIZONO = 106;
    public static final int IDC_KUWATA = 107;
    public static final int IDC_FUJISAKI = 108;
    public static final int IDC_OWADA = 109;
    public static final int IDC_ISHIMARU = 110;
    public static final int IDC_YAMADA = 111;
    public static final int IDC_LUDENGERG = 112;
    public static final int IDC_OGAMI = 113;
    public static final int IDC_IKUSABA = 114;
    public static final int IDC_ENOSHIMA = 115;
    public static final int IDC_HINATA = 200;
    public static final int IDC_NANAMI = 201;
    public static final int IDC_KOMAEDA = 202;
    public static final int IDC_SAGISHI = 203;
    public static final int IDC_TANAKA = 204;
    public static final int IDC_SOUDA = 205;
    public static final int IDC_HANAMURA = 206;
    public static final int IDC_NIDAI = 207;
    public static final int IDC_KUZURYU = 208;
    public static final int IDC_OWARI = 209;
    public static final int IDC_SONIA = 210;
    public static final int IDC_KOIZUMI = 211;
    public static final int IDC_TSUMIKI = 212;
    public static final int IDC_MIODA = 213;
    public static final int IDC_SAIONJI = 214;
    public static final int IDC_PEKOYAMA = 215;
    public static final int IDC_SAIHARA = 300;
    public static final int IDC_OMA = 301;
    public static final int IDC_MONOKUMA = 900;
    public static final int IDC_USAMI = 901;
    public static final int IDC_MONOMI = 902;
    public static final int IDC_ALTEREGO = 903;
    public static final int IDC_HAKO = 942;
    public static final int IDC_HINATASUPER = 998;
    public static final int IDC_KAMUKURA = 999;

    private static SparseArray<Chara> characters;

    static {

        ARR_CHARA_ID = new ArrayList<>();
        ARR_PLAYABLE_CHARA_ID = new ArrayList<>();

        ARR_PLAYABLE_CHARA_ID.add(IDC_NAEGI);
        ARR_PLAYABLE_CHARA_ID.add(IDC_HINATA);
        ARR_PLAYABLE_CHARA_ID.add(IDC_SAIHARA);

        ARR_CHARA_ID.add(IDC_NAEGI);
        for (int id = IDC_NAEGI + 1; id <= IDC_ENOSHIMA; id++) {
            ARR_CHARA_ID.add(id);
            ARR_PLAYABLE_CHARA_ID.add(id);
        }

        ARR_CHARA_ID.add(IDC_HINATA);
        for (int id = IDC_HINATA + 1; id <= IDC_PEKOYAMA; id++) {
            ARR_CHARA_ID.add(id);
            ARR_PLAYABLE_CHARA_ID.add(id);
        }

        ARR_CHARA_ID.add(IDC_SAIHARA);
        for (int id = IDC_SAIHARA + 1; id <= IDC_OMA; id++) {
            ARR_CHARA_ID.add(id);
            ARR_PLAYABLE_CHARA_ID.add(id);
        }

        for (int id = IDC_MONOKUMA; id <= IDC_ALTEREGO; id++) {
            ARR_CHARA_ID.add(id);
        }

        ARR_CHARA_ID.add(IDC_HAKO);

        for (int id = IDC_HINATASUPER; id <= IDC_KAMUKURA; id++) {
            ARR_CHARA_ID.add(id);
            ARR_PLAYABLE_CHARA_ID.add(id);
        }

        characters = new SparseArray<>();

        characters.put(IDC_NAEGI, new Chara(R.drawable.c1_naegi, "苗木 诚", "ナエギ　マコト", "超高校级的「幸运」", "2月5日", "A型", "希望之峰学园第78期生\n未来机关成员", 160, 52, 75, new int[]{0, 1, 2, 3, 4, 5}));
        characters.put(IDC_KIRIGIRI, new Chara(R.drawable.c1_kirigiri, "雾切 响子", "キリギリ　キョウコ", "超高校级的「侦探」", "10月6日", "B型", "希望之峰学园第78期生\n未来机关成员", 167, 48, 82, new int[]{0, 1, 2, 3}));
        characters.put(IDC_TOGAMI, new Chara(R.drawable.c1_togami, "十神 白夜", "トガミ　ビャクヤ", "超高校级的「御曹司」", "5月5日", "B型", "希望之峰学园第78期生\n未来机关成员", 185, 68, 81, new int[]{1, 2, 3, 4}));
        characters.put(IDC_FUKAWA, new Chara(R.drawable.c1_fukawa, "腐川 冬子", "フカワ　トウコ", "超高校级的「文学少女」", "3月3日", "O型", "希望之峰学园第78期生\n未来机关成员", 164, 47, 79, new int[]{1, 3, 4}));
        characters.put(IDC_ASAHINA, new Chara(R.drawable.c1_asahina, "朝日奈 葵", "アサヒナ　アオイ", "超高校级的「游泳选手」", "4月24日", "B型", "希望之峰学园第78期生\n未来机关成员", 160, 50, 88, new int[]{1, 3}));
        characters.put(IDC_HAGAKURE, new Chara(R.drawable.c1_hagakure, "叶隐 康比吕", "ハガクレ　ヤスヒロ", "超高校级的「占卜师」", "7月25日", "B型", "希望之峰学园第78期生(留级)\n未来机关成员", 180, 71, 82, new int[]{1, 3}));
        characters.put(IDC_MAIZONO, new Chara(R.drawable.c1_maizono, "舞园 沙耶加", "マイゾノ　サヤカ", "超高校级的「偶像」", "7月7日", "B型", "希望之峰学园第78期生", 165, 49, 83, new int[]{1, 3, 5}));
        characters.put(IDC_KUWATA, new Chara(R.drawable.c1_kuwata, "桑田 怜恩", "クワタ　レオン", "超高校级的「棒球选手」", "1月3日", "A型", "希望之峰学园第78期生", 175, 67, 80, new int[]{1, 3}));
        characters.put(IDC_FUJISAKI, new Chara(R.drawable.c1_fujisaki, "不二咲 千寻", "フジサキ　チヒロ", "超高校级的「程序员」", "3月14日", "A型", "希望之峰学园第78期生", 148, 41, 70, new int[]{1, 3}));
        characters.put(IDC_OWADA, new Chara(R.drawable.c1_owada, "大和田 纹土", "オオワダ　モンド", "超高校级的「暴走族」", "6月9日", "O型", "希望之峰学园第78期生", 187, 76, 86, new int[]{1, 3}));
        characters.put(IDC_ISHIMARU, new Chara(R.drawable.c1_ishimaru, "石丸 清多夏", "イシマル　キヨタカ", "超高校级的「风纪委员」", "8月31日", "A型", "希望之峰学园第78期生", 176, 66, 79, new int[]{1, 3}));
        characters.put(IDC_YAMADA, new Chara(R.drawable.c1_yamada, "山田 一二三", "ヤマダ　ヒフミ", "超高校级的「同人作家」", "12月31日", "A型", "希望之峰学园第78期生", 170, 155, 150, new int[]{1, 3}));
        characters.put(IDC_LUDENGERG, new Chara(R.drawable.c1_ludenberg, "塞蕾丝缇雅·罗登贝克", "セレスティア·ルーデンベルク", "超高校级的「赌徒」", "11月23日", "A型", "希望之峰学园第78期生", 164, 46, 80, new int[]{1, 3}));
        characters.put(IDC_OGAMI, new Chara(R.drawable.c1_ogami, "大神 樱", "オオガミ　サクラ", "超高校级的「格斗家」", "9月13日", "A型", "希望之峰学园第78期生", 192, 99, 130, new int[]{1, 3}));
        characters.put(IDC_IKUSABA, new Chara(R.drawable.c1_ikusaba, "战刃 骸", "イクサバ　ムクロ", "超高校级的「军人」", "12月24日", "A型", "希望之峰学园第78期生", 169, 44, 80, new int[]{0, 1, 3}));
        characters.put(IDC_ENOSHIMA, new Chara(R.drawable.c1_enoshima, "江之岛 盾子", "エノシマ　ジュンコ", "超高校级的「绝望」", "12月24日", "AB型", "希望之峰学园第78期生", 169, 44, 90, new int[]{0, 1, 2, 3, 4}));

        characters.put(IDC_HINATA, new Chara(R.drawable.c2_hinata, "日向 创", "ヒナタ　ハジメ", "超高校级的「？？？」", "1月1日", "A型", "希望之峰学园第77期\n预备学科学生", 179, 67, 91, new int[]{2, 3, 5}));
        characters.put(IDC_NANAMI, new Chara(R.drawable.c2_nanami, "七海 千秋", "ナナミ　チアキ", "超高校级的「游戏玩家」", "3月14日", "O型", "希望之峰学园第77期生", 160, 46, 88, new int[]{2, 3}));
        characters.put(IDC_KOMAEDA, new Chara(R.drawable.c2_komaeda, "狛枝 凪斗", "コマエダ　ナギト", "超高校级的「幸运」", "4月28日", "O型", "希望之峰学园第77期生", 180, 65, 84, new int[]{2, 3, 4, 5}));
        characters.put(IDC_SAGISHI, new Chara(R.drawable.c2_sagishi, "？？？", "？？？", "超高校级的「欺诈师」", "??月??日", "?型", "希望之峰学园第77期生", 185, 130, 128, new int[]{2, 3, 5}));
        characters.put(IDC_TANAKA, new Chara(R.drawable.c2_tanaka, "田中 眼蛇梦", "タナカ　ガンダム", "超高校级的「饲育委员」", "12月14日", "B型", "希望之峰学园第77期生", 182, 74, 93, new int[]{2, 3, 5}));
        characters.put(IDC_SOUDA, new Chara(R.drawable.c2_souda, "左右田 和一", "ソウダ　カズイチ", "超高校级的「机械师」", "6月29日", "A型", "希望之峰学园第77期生", 172, 64, 86, new int[]{2, 3, 5}));
        characters.put(IDC_HANAMURA, new Chara(R.drawable.c2_hanamura, "花村 辉辉", "ハナムラ　テルテル", "超高校级的「料理人」", "9月2日", "A型", "希望之峰学园第77期生", 133, 69, 88, new int[]{2, 3, 5}));
        characters.put(IDC_NIDAI, new Chara(R.drawable.c2_nidai, "二大 猫丸", "ニダイ　ハジメ", "超高校级的「社团经理」", "2月22日", "A型", "希望之峰学园第77期生", 198, 122, 122, new int[]{2, 3, 5}));
        characters.put(IDC_KUZURYU, new Chara(R.drawable.c2_kuzuryu, "九头龙 冬彦", "クズリュウ　フユヒコ", "超高校级的「极道」", "8月16日", "AB型", "希望之峰学园第77期生", 157, 43, 73, new int[]{2, 3, 5}));
        characters.put(IDC_OWARI, new Chara(R.drawable.c2_owari, "终里 赤音", "オワリ　アカネ", "超高校级的「体操选手」", "7月15日", "B型", "希望之峰学园第77期生", 176, 56, 93, new int[]{2, 3, 5}));
        characters.put(IDC_SONIA, new Chara(R.drawable.c2_sonia, "索尼娅·内瓦曼", "ソニア·ネヴァ-マィンド", "超高校级的「王女」", "10月13日", "A型", "希望之峰学园第77期生", 174, 50, 83, new int[]{2, 3, 5}));
        characters.put(IDC_KOIZUMI, new Chara(R.drawable.c2_koizumi, "小泉 真昼", "コイズミ　マヒル", "超高校级的「摄影师」", "4月24日", "A型", "希望之峰学园第77期生", 165, 46, 77, new int[]{2, 3, 5}));
        characters.put(IDC_TSUMIKI, new Chara(R.drawable.c2_tsumiki, "罪木 蜜柑", "ツミキ　ミカン", "超高校级的「保健委员」", "5月12日", "A型", "希望之峰学园第77期生", 165, 57, 89, new int[]{2, 3, 5}));
        characters.put(IDC_MIODA, new Chara(R.drawable.c2_mioda, "澪田 唯吹", "ミオダ　イブキ", "超高校级的「轻音部」", "11月27日", "AB型", "希望之峰学园第77期生", 164, 42, 76, new int[]{2, 3, 5}));
        characters.put(IDC_SAIONJI, new Chara(R.drawable.c2_saionji, "西园寺 日寄子", "サイオンジ　ヒヨコ", "超高校级的「日本舞蹈家」", "3月9日", "B型", "希望之峰学园第77期生", 130, 31, 64, new int[]{2, 3, 5}));
        characters.put(IDC_PEKOYAMA, new Chara(R.drawable.c2_pekoyama, "边谷山 佩子", "ペコヤマ　ペコ", "超高校级的「剑道家」", "6月30日", "O型", "希望之峰学园第77期生", 172, 51, 85, new int[]{2, 3, 5}));

        characters.put(IDC_SAIHARA, new Chara(R.drawable.c3_saihara, "最原 终一", "サイハラ　シュウイチ", "超高校级的「侦探(？)」", "9月7日", "AB型", "才囚学园学生", 171, 58, 80, new int[]{6}));
        characters.put(IDC_OMA, new Chara(R.drawable.c3_oma, "王马 小吉", "オウマ　コキチ", "超高校级的「总统(？)」", "6月21日", "A型", "才囚学园学生", 156, 44, 70, new int[]{6}));

        characters.put(IDC_MONOKUMA, new Chara(R.drawable.c0_monokuma, "黑白熊", "モノクマ", "希望之峰学园校长(自称)", "??月??日", "?型", "亦为才囚学园管理人(自称)", 0, 0, 0, new int[]{1, 2, 3, 4, 6}));
        characters.put(IDC_USAMI, new Chara(R.drawable.c2_usami, "兔美", "ウサミ", "希望之峰学园教师(自称)", "??月??日", "?型", "未来机关所属监视者", 0, 0, 0, new int[]{2}));
        characters.put(IDC_MONOMI, new Chara(R.drawable.c2_monomi, "莫诺美", "モノミ", "希望之峰学园教师(改造后)", "??月??日", "?型", "未来机关所属监视者", 0, 0, 0, new int[]{2}));
        characters.put(IDC_ALTEREGO, new Chara(R.drawable.c0_alterego, "Alter Ego", "アルター　エゴ", "人工智能", "??月??日", "?型", "未来机关成员", 0, 0, 0, new int[]{1, 2, 4}));
        characters.put(IDC_HAKO, new Chara(R.drawable.c0_hako, "箱子", "ハコ", "超高校级的「懒癌」", "??月??日", "?型", "绝对希望救援幕后黑手", 0, 0, 0, new int[]{}));
        characters.put(IDC_HINATASUPER, new Chara(R.drawable.c2_hinatasuper, "日向 创", "ヒナタ　ハジメ", "超高校级的「希望」", "1月1日", "A型", "希望之峰学园第77期\n预备学科学生", 179, 67, 91, new int[]{2, 3, 5}));
        characters.put(IDC_KAMUKURA, new Chara(R.drawable.c2_kamukura, "神座 出流", "カムクラ　イズル", "超高校级的「希望」", "1月1日", "A型", "人工希望", 179, 67, 91, new int[]{2, 3, 4, 5}));
    }

    private static class Chara {

        int resID;
        String name;
        String kata;
        String title;
        String birth;
        String blood;
        String degree;
        int weight;
        int height;
        int bust;
        int[] stage;

        public Chara(int resID, String name, String kata, String title, String birth, String blood, String degree, int weight, int height, int bust, int[] stage) {

            this.resID = resID;
            this.name = name;
            this.kata = kata;
            this.title = title;
            this.birth = birth;
            this.blood = blood;
            this.degree = degree;
            this.weight = weight;
            this.height = height;
            this.bust = bust;
            this.stage = stage;
        }
    }

    public static int getRandomCharaID() {
        int index = (int) (Math.random() * TOTAL_CHARA_COUNT);
        return ARR_CHARA_ID.get(index);
    }

    public static int getRandomPlayableCharaID() {
        int index = (int) (Math.random() * PLAYABLE_CHARA_COUNT);
        return ARR_PLAYABLE_CHARA_ID.get(index);
    }

    public static int getResID(int charaID) {
        return characters.get(charaID).resID;
    }

    public static int[] getStage(int charaID) {
        return characters.get(charaID).stage;
    }
}
