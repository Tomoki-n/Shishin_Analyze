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

    /** �Z���p�z�� */
    private Field[][] gameCell;
    /** ���j�b�g�̈ʒu */
    private Point[][] unitLocation;

    /** ���̕ێ���� */
    private int[] towerHold;
    private int towerCount = 3;
    /** �`�[���̓��_ */
    private int[] teamPoint;
    /** �����̃`�[���ԍ� */
    private int MyTeamID;

    /** �O�̃��j�b�g�̈ʒu */
    private Point[][] prevUnitLocation;

    /** ���݂̃{�[�h�̃^�C�v */
    private int boardType = BOARD_TYPE_UNDEFINED;

    /** �����Ă���`�[����ID */
    private int victoryTeamID = -1;


    /** ��_�Ԃ̋��� */
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

    /** ���j�b�g�ʒu��n�� */
    public Point[][] getUnitLocation() {
        return unitLocation;
    }

    /** ���_�� */
    public int getPoint(){
        int i,j;
        i=teamPoint[0]-teamPoint[1];
        if(i>0)  victoryTeamID=0;
        else if(i<0)  victoryTeamID=1;

        return Math.abs(i);
    }

    /** �ł��߂��^���[ */
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
