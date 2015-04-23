package com.company;

import java.awt.*;

/**
 * Created by tomoki-n on 2015/04/16.
 */
public class Boardinfo {

    public static final int BOARD_TYPE_UNDEFINED = -1;
    public static final int BOARD_TYPE_A = 1;
    public static final int BOARD_TYPE_B = 2;
    public static final int BOARD_TYPE_C = 3;
    public static final int BOARD_TYPE_D = 4;
    public static final int BOARD_TYPE_AB = 5;
    public static final int BOARD_TYPE_CD = 6;

    /** セル用配列 */
    private Field[][] gameCell;
    /** ユニットの位置 */
    private Point[][] unitLocation;

    /** 塔の保持状態 */
    private int[] towerHold;
    private int towerCount = 3;
    /** チームの得点 */
    private int[] teamPoint;
    /** 自分のチーム番号 */
    private int MyTeamID;

    /** 前のユニットの位置 */
    private Point[][] prevUnitLocation;

    /** 現在のボードのタイプ */
    private int boardType = BOARD_TYPE_UNDEFINED;

    /** ユニット位置を渡す */
    public Point[][] getUnitLocation() {
        return unitLocation;
    }

}
