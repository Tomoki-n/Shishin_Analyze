package com.company;

import java.awt.*;

/**
 * Created by tomoki-n on 2015/04/16.
 */
public class Boardinfo {

    //盤面のタイプ
    /** 盤面のタイプが未確定 */
    public static final int BOARD_TYPE_UNDEFINED = -1;
    /** 盤面がケース1 */
    public static final int BOARD_TYPE_A = 1;
    /** 盤面がケース2 */
    public static final int BOARD_TYPE_B = 2;
    /** 盤面がケース3 */
    public static final int BOARD_TYPE_C = 3;
    /** 盤面がケース4 */
    public static final int BOARD_TYPE_D = 4;
    /** 盤面がケース1もしくは2 */
    public static final int BOARD_TYPE_AB = 5;
    /** 盤面がケース3もしくは4 */
    public static final int BOARD_TYPE_CD = 6;



    //状態
    public static final int STATE_WAITINGPLAYER = 0;
    public static final int STATE_PLAY = 10;
    public static final int STATE_PLAY_UNITSELECT = 15;
    public static final int STATE_BATTLE = 5;
    public static final int STATE_CALC = 6;
    public static final int STATE_FINISH = 7;

    /** 順番カウント用 */
    private int turnState = -1;
    public static final int STATE_PLAY_TURN1 = 11;
    public static final int STATE_PLAY_TURN2 = 12;
    public static final int STATE_PLAY_TURN3 = 13;
    public static final int STATE_PLAY_TURN4 = 14;

    /** 駒の種類*/
    public static final int BLACK  = 1;
    public static final int RED    = 2;
    public static final int GREEN  = 3;
    public static final int YELLOW = 4;


    //閾値設定
    /** ゲーム終了の点数 */
    private static final int MAXPOINT = 50;
    /** 無得点の最大ターン数 */
    private static final int NOPOINTTIME = 10;


    /** 経過ターン数 */
    private int turnCount;

    /** ユニットの位置 */
    private Point[][] unitLocation;

    /** 塔の保持状態 */
    private int[] towerHold;
    /** 塔の個数 */
    private final int towerCount = 3;

    /** チームの得点 */
    private int[] teamPoint;

    /** 自分のチーム番号 */
    private int MyTeamID;

    /** ボードの状態 */
    private int state = STATE_WAITINGPLAYER;

    /** どちらのプレイヤーがプレイ中か　0または1になる。-1はゲーム中ではない状態 */
    private int playingTeamID;

    /** 先攻プレイヤーはどちらか */
    private int firstTeamID;

    /** 本陣の位置 */
    private final Point Base0 = new Point(4,7);
    private final Point Base1 = new Point(4,1);

    /** 前のユニットの位置 */
    private Point[][] prevUnitLocation;

    /** 現在のボードのタイプ */
    private int boardType;


    /** 初期化 */
    public Boardinfo() {
        this.playingTeamID = -1;
        this.firstTeamID = -1;
        this.turnCount = 0;
        this.boardType = BOARD_TYPE_UNDEFINED;

        //ユニットの設置
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

    /** ユニット位置を取得する */
    public Point[][] getUnitLocation() {
        //実際はコピーを渡したい(勝手に変更されると後々困る)
        return this.unitLocation;
    }

    /**
     * ユニット位置を設定する
     * 【注意】AI以外のクラスから呼び出さないようにすること
     * @param unitLocate 現在のユニット位置
     */
    void setUnitLocation(Point[][] unitLocate) {
        this.unitLocation = unitLocate;
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
    public Point getNearestTower(Point pos) {
        Point t1 = new Point(1, 4);
        Point t2 = new Point(4, 4);
        Point t3 = new Point(7, 4);

        int i, j, k;
        i = distance(pos, t1);
        j = distance(pos, t2);
        k = distance(pos, t3);

        if (i < j && i < k) return (t1);
        else if (j < i && j < k) return (t2);
        else if (k < i && k < j) return (t3);
        else return (t2);
    }
    /** タワーをどちらが占拠しているかを示す配列を取得する */
    public int[] getTowerHold() {
        return this.towerHold;
    }

    /** 次の手がどちらかを返す */
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



    /** 1VS1の対戦
     * 戻り値：1:勝ち 0:負け 2:引き分け -1:エラー
     * 引数：  BLACK　1 RED 2 GREEN  3　YELLOW 4
     * */

    public int onevs(int A_char, int E_char){

        if(A_char == BLACK){
            if(E_char == BLACK){
                return 2;
            }
            else if (E_char == RED){
                return 1;
            }
            else if (E_char == GREEN){
                return 0;
            }
            else if (E_char == YELLOW){
                return 2;
            }
        }

        if(A_char == RED){
            if(E_char == BLACK){
                return 0;
            }
            else if (E_char == RED){
                return 2;
            }
            else if (E_char == GREEN){
                return 2;
            }
            else if (E_char == YELLOW){
                return 1;
            }
        }
        if(A_char == GREEN){
            if(E_char == BLACK){
                return 1;
            }
            else if (E_char == RED){
                return 2;
            }
            else if (E_char == GREEN){
                return 2;
            }
            else if (E_char == YELLOW){
                return 0;
            }
        }
        if(A_char == YELLOW){
            if(E_char == BLACK){
                return 2;
            }
            else if (E_char == RED){
                return 0;
            }
            else if (E_char == GREEN){
                return 1;
            }
            else if (E_char == YELLOW){
                return 2;
            }
        }

        return -1;
    }

    /** 2VS2の対戦 */
    public  boolean twovs(int A_char1, int A_char2, int E_char1, int E_char2){


        return true;
    }

}
