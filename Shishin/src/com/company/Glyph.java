package com.company;

/**
 * Created by Yuta on 2015/09/21.
 */
public class Glyph {

    /**
     * グリフの個数を指定してグリフシーケンスを生成
     * @param seq グリフの個数
     * @return グリフシーケンス
     */
    public static String getGlyphSeq(int seq) {

        int rand;
        switch (seq) {
            case 1:
                rand = (int)(Math.random() * 113);
                switch (rand) {
                    case 0:
                        return "GAIN";
                    case 1:
                        return "OLD";
                    case 2:
                        return "PAST";
                    //TODO:以下同様にglyph1.txtを参考にして
                }
                return "";  //←本当はこれ要らない
            case 2:
                rand = (int)(Math.random() * 23);
                switch (rand) {
                    case 0:
                        return "CIVILIZATION CHAOS";
                    //TODO:glyph2.txt参照
                }
                return "";
            case 3:
                rand = (int)(Math.random() * 44);
                switch (rand) {
                    //TODO:glyph3.txt参照
                }
                return "";
            case 4:
                rand = (int)(Math.random() * 88);
                switch (rand) {
                    //TODO:glyph4.txt参照
                }
                return "";
            case 5:
                rand = (int)(Math.random() * 77);
                switch (rand) {
                    //TODO:glyph5.txt参照
                }
                return "";
            default:
                return "";
        }
    }






    
    /**
     * 適当なグリフシーケンスを生成
     * @return グリフシーケンス
     */
    public static String getGlyphSeq() {
        return getGlyphSeq(0.05, 0.1, 0.4, 0.4, 0.05);
    }

    /**
     * 1～5グリフシーケンスの確率を指定してグリフシーケンスを生成する。
     * 確率の和が1になる必要性はない。
     * @param pb1 1グリフシーケンスが出る確率
     * @param pb2 2グリフシーケンスが出る確率
     * @param pb3 3グリフシーケンスが出る確率
     * @param pb4 4グリフシーケンスが出る確率
     * @param pb5 5グリフシーケンスが出る確率
     * @return グリフシーケンス
     */
    public static String getGlyphSeq(double pb1, double pb2, double pb3, double pb4, double pb5) {
        double sum = pb1 + pb2 + pb3 + pb4 + pb5;
        double rand = Math.random() * sum;

        if(rand <= pb1) {
            return getGlyphSeq(1);
        } else if(rand <= (pb1 + pb2)) {
            return getGlyphSeq(2);
        } else if(rand <= (pb1 + pb2 + pb3)) {
            return getGlyphSeq(3);
        } else if(rand <= (pb1 + pb2 + pb3 + pb4)) {
            return getGlyphSeq(4);
        } else {
            return getGlyphSeq(5);
        }
    }

}
