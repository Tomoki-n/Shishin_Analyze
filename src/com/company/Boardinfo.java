package com.company;

import java.awt.*;

/**
 * Created by tomoki-n on 2015/04/16.
 */
public class Boardinfo {

    //�Ֆʂ̃^�C�v
    /** �Ֆʂ̃^�C�v�����m�� */
    public static final int BOARD_TYPE_UNDEFINED = -1;
    /** �Ֆʂ��P�[�X1 */
    public static final int BOARD_TYPE_A = 1;
    /** �Ֆʂ��P�[�X2 */
    public static final int BOARD_TYPE_B = 2;
    /** �Ֆʂ��P�[�X3 */
    public static final int BOARD_TYPE_C = 3;
    /** �Ֆʂ��P�[�X4 */
    public static final int BOARD_TYPE_D = 4;
    /** �Ֆʂ��P�[�X1��������2 */
    public static final int BOARD_TYPE_AB = 5;
    /** �Ֆʂ��P�[�X3��������4 */
    public static final int BOARD_TYPE_CD = 6;



    //���
    public static final int STATE_WAITINGPLAYER = 0;
    public static final int STATE_PLAY = 10;
    public static final int STATE_PLAY_UNITSELECT = 15;
    public static final int STATE_BATTLE = 5;
    public static final int STATE_CALC = 6;
    public static final int STATE_FINISH = 7;

    /** ���ԃJ�E���g�p */
    private int turnState = -1;
    public static final int STATE_PLAY_TURN1 = 11;
    public static final int STATE_PLAY_TURN2 = 12;
    public static final int STATE_PLAY_TURN3 = 13;
    public static final int STATE_PLAY_TURN4 = 14;

    //臒l�ݒ�
    /** �Q�[���I���̓_�� */
    private static final int MAXPOINT = 50;
    /** �����_�̍ő�^�[���� */
    private static final int NOPOINTTIME = 10;


    /** �o�߃^�[���� */
    private int turnCount;

    /** ���j�b�g�̈ʒu */
    private Point[][] unitLocation;

    /** ���̕ێ���� */
    private int[] towerHold;
    /** ���̌� */
    private final int towerCount = 3;

    /** �`�[���̓��_ */
    private int[] teamPoint;

    /** �����̃`�[���ԍ� */
    private int MyTeamID;

    /** �{�[�h�̏�� */
    private int state = STATE_WAITINGPLAYER;

    /** �ǂ���̃v���C���[���v���C�����@0�܂���1�ɂȂ�B-1�̓Q�[�����ł͂Ȃ���� */
    private int playingTeamID;

    /** ��U�v���C���[�͂ǂ��炩 */
    private int firstTeamID;

    /** �{�w�̈ʒu */
    private final Point Base0 = new Point(4,7);
    private final Point Base1 = new Point(4,1);

    /** �O�̃��j�b�g�̈ʒu */
    private Point[][] prevUnitLocation;

    /** ���݂̃{�[�h�̃^�C�v */
    private int boardType;


    /** ������ */
    public Boardinfo() {
        this.playingTeamID = -1;
        this.firstTeamID = -1;
        this.turnCount = 0;
        this.boardType = BOARD_TYPE_UNDEFINED;

        //���j�b�g�̐ݒu
        this.unitLocation = new Point[2][4];
        for(int i = 0; i < 4; i++){
            this.unitLocation[0][i] = new Point(4,7);
            this.unitLocation[1][i] = new Point(4,1);
        }

        this.towerHold = new int[towerCount];
        for(int i = 0; i <towerCount; i++){
            this.towerHold[i] = -1;
        }

        this.teamPoint = new int[2];
        this.teamPoint[0] = 0;
        this.teamPoint[1] = 0;
    }

    /** ���j�b�g�ʒu���擾���� */
    public Point[][] getUnitLocation() {
        //���ۂ̓R�s�[��n������(����ɕύX�����ƌ�X����)
        return this.unitLocation;
    }

    /**
     * ���j�b�g�ʒu��ݒ肷��
     * �y���ӁzAI�ȊO�̃N���X����Ăяo���Ȃ��悤�ɂ��邱��
     * @param unitLocate ���݂̃��j�b�g�ʒu
     */
    void setUnitLocation(Point[][] unitLocate) {
        this.unitLocation = unitLocate;
    }

    /** �^���[���ǂ��炪�苒���Ă��邩�������z����擾���� */
    public int[] getTowerHold() {
        return this.towerHold;
    }

    /** ���̎肪�ǂ��炩��Ԃ� */
    public int whoIsPlay(){
        if(this.turnState == STATE_PLAY_TURN1){
            return this.firstTeamID;
        } else if(this.turnState == STATE_PLAY_TURN2){
            return (this.firstTeamID+1)%2;
        } else if(this.turnState == STATE_PLAY_TURN3){
            return (this.firstTeamID+1)%2;
        } else if(this.turnState == STATE_PLAY_TURN4){
            return this.firstTeamID;
        }
        return -1;
    }
}
