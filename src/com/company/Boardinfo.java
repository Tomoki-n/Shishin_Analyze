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

    /** ���j�b�g�ʒu��n�� */
    public Point[][] getUnitLocation() {
        return unitLocation;
    }

}
