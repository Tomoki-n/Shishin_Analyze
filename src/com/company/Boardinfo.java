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

    /** 勝っているチームのID */
    private int victoryTeamID = -1;


    /** 二点間の距離 */
    public int distance(Point a, Point b) {
        if(a.x == b.x) {
            return Math.abs(a.y - b.y);
        } else if(a.y == b.y) {
            return Math.abs(a.x - b.x);
        } else {
            int xdef = Math.abs(a.x - b.x);
            int ydef = Math.abs(a.y - b.y);
            return xdef > ydef?xdef:ydef;
        }
    }

    /** ユニット位置を渡す */
    public Point[][] getUnitLocation() {
        return unitLocation;
    }

    /** 得点差 */
    public int getPoint(){
        int i,j;
        i=teamPoint[0]-teamPoint[1];
        if(i>0)  victoryTeamID=0;
        else if(i<0)  victoryTeamID=1;

        return Math.abs(i);
    }

    /** 最も近いタワー */
    public Point[][] gettower(Point[][] pos){
        Point t1 = Point[1][4];
        Point t2 = Point[4][4];
        Point t3 = Point[7][4];

        int i,j,k;
        i=distance(Point pos,Point t1);
        j=distance(Point pos,Point t2);
        k=distance(Point pos,Point t3);

        if(i<j&&i<k)  return(t1);
        else if(j<i&&j<k)  return(t2);
        else if(k<i&&k<j)  return(t3);
    }
}
