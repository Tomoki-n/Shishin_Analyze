package com.company.AI;


import com.company.Connection;

import java.awt.*;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




/**
 * Created by tomoki-n on 2015/04/10.
 */
public abstract class AI {

    public static final int DEFAULTPORT = 13306;

    //盤面のタイプ
    /**
     * 現在のボードのタイプ
     */
    public int boardType;
    /**
     * 盤面のタイプが未確定
     */
    public static final int BOARD_TYPE_UNDEFINED = -1;
    /**
     * 盤面がケース1
     */
    public static final int BOARD_TYPE_A = 1;
    /**
     * 盤面がケース2
     */
    public static final int BOARD_TYPE_B = 2;
    /**
     * 盤面がケース3
     */
    public static final int BOARD_TYPE_C = 3;
    /**
     * 盤面がケース4
     */
    public static final int BOARD_TYPE_D = 4;
    /**
     * 盤面がケース1もしくは2
     */
    public static final int BOARD_TYPE_AB = 5;
    /**
     * 盤面がケース3もしくは4
     */
    public static final int BOARD_TYPE_CD = 6;

    //状態
    public static final int STATE_WAITINGPLAYER = 0;
    public static final int STATE_PLAY = 10;
    public static final int STATE_PLAY_UNITSELECT = 15;
    public static final int STATE_BATTLE = 5;
    public static final int STATE_CALC = 6;
    public static final int STATE_FINISH = 7;

    /**
     * 順番カウント用
     */
    protected int turnState = -1;

    //ターン管理
    public static final int STATE_PLAY_TURN1 = 11;
    public static final int STATE_PLAY_TURN2 = 12;
    public static final int STATE_PLAY_TURN3 = 13;
    public static final int STATE_PLAY_TURN4 = 14;


    //閾値設定
    /**
     * ゲーム終了の点数
     */
    public static final int MAXPOINT = 50;
    /**
     * 無得点の最大ターン数
     */
    public static final int NOPOINTTIME = 10;

    /**
     * ボードの状態
     */
    protected int state = STATE_WAITINGPLAYER;
    /**
     * どちらのプレイヤーがプレイ中か　0または1になる。-1はゲーム中ではない状態
     */
    protected int playingTeamID = -1;
    /**
     * 先攻プレイヤーはどちらか
     */
    protected int firstTeamID = -1;
    /**
     * ターン数
     */
    protected int turnCount;

    /**
     * 自分の名前
     */
    protected String myName;
    /**
     * サーバのアドレス
     */
    protected String serverIP;
    /**
     * サーバポート
     */
    protected int serverPort;
    /**
     * サーバ待ち受けスレッド
     */
    protected Connection sthread;
    /**
     * ユニットの位置
     */
    protected Point[][] unitLocation;
    /**
     * 塔の保持状態
     */
    protected int[] towerHold;
    protected static final int towerCount = 3;
    /**
     * チームの得点
     */
    protected int[] teamPoint;
    /**
     * 自分のチーム番号
     */
    protected int MyTeamID;

    /**
     * 前のユニットの位置
     */
    protected Point[][] prevUnitLocation;
    public int[] prevMove;


    /**
     * 駒の種類
     */
    public static final int GREEN = 0;
    public static final int BLACK = 1;
    public static final int RED = 2;
    public static final int YELLOW = 3;

    /**
     * AIの種類 *
     */
    protected int AI_type;

    public static final Point Base0 = new Point(4, 7);
    public static final Point Base1 = new Point(4, 1);

    /**
     * タワーの位置
     */
    public static final Point tower_left = new Point(1, 4);
    public static final Point tower_center = new Point(4, 4);
    public static final Point tower_right = new Point(7, 4);


    /**
     * 勝っているチームのID
     */
    public int victoryTeamID = -1;

    public String analyze = "";

    /**
     * Creates new form GameField
     */
    public AI(String address, String type, String stype) throws InterruptedException, IOException {
        this.serverIP = address;
        AI_type = Integer.parseInt(type);
        analyze = stype;
        System.out.println("init");
        this.resetAll();
        this.sthread.sendName();
    }

    /**
     * 状態をすべてリセット
     */
    public void resetAll() throws InterruptedException, IOException {
        this.state = STATE_WAITINGPLAYER;
        //名前の入力
        this.myName = null;
        this.myName = "TajimaLab2";
        System.out.println("Playname:" + myName);
        if (this.myName == null) {
            System.exit(0);
        }

        this.serverPort = DEFAULTPORT;

        //サーバに接続する
        this.sthread = new Connection(this.myName, this);
        try {
            boolean connect = this.sthread.connectToServer(this.serverIP, this.serverPort);
            if (connect == false) {
                this.addMessage("サーバへの接続に失敗しました。");
                System.exit(0);
            }
        } catch (UnknownHostException ex) {
            this.addMessage("サーバの指定が不正です。UnknownHostException");
            System.exit(0);
        } catch (IOException ex) {
            this.addMessage("サーバへの接続に失敗しました。IOException");
            System.exit(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //正しく接続できたら処理開始
        this.playingTeamID = -1;
        this.firstTeamID = -1;
        this.turnCount = 0;
        this.boardType = BOARD_TYPE_UNDEFINED;

        this.unitLocation = new Point[2][4];
        this.prevUnitLocation = new Point[2][4];
        this.prevMove = new int[3];
        for (int i = 0; i < 4; i++) {
            this.unitLocation[0][i] = new Point(4, 7);
            this.unitLocation[1][i] = new Point(4, 1);
            this.prevUnitLocation[0][i] = new Point(4, 7);
            this.prevUnitLocation[1][i] = new Point(4, 1);
        }

        this.towerHold = new int[towerCount];

         for (int i = 0; i < towerCount; i++) {
             this.towerHold[i] = -1;
         }

         this.teamPoint = new int[2];
         this.teamPoint[0] = 0;
         this.teamPoint[1] = 0;


         this.resetTurnState();
    }

    /**
     * 敵軍のIDを求める
     * @return 敵軍のチームID
     */
    public int EnemyTeamID() {
        if(this.MyTeamID == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * ターンの１手目に戻す
     */
    public void resetTurnState() {
        this.turnState = STATE_PLAY_TURN1;
        this.turnCount++;


    }

    /**
     * 先行かどうかを返す
     */
    public boolean isFirstPlayer(int myID) {
        if (this.firstTeamID == myID) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 先攻、後攻の切り替え
     */
    public void changeFirstTeam() {
        this.firstTeamID = (this.firstTeamID + 1) % 2;

    }

    /**
     * 手番のプレイヤーを変更
     */
    public void setPlayingTeamID(int i) {
        this.playingTeamID = i;
        this.state = STATE_PLAY;


    }


    /**
     * チーム名の変更
     */
    public void setTeamName(int teamNumber, String teamName) {
        if (teamNumber == 0) {
        } else if (teamNumber == 1) {
        }
    }

    /**
     * 次の手がどちらかを返す
     */
    public int whoIsPlay() {
        if (this.turnState == STATE_PLAY_TURN1) {
            return this.firstTeamID;
        } else if (this.turnState == STATE_PLAY_TURN2) {
            return (this.firstTeamID + 1) % 2;
        } else if (this.turnState == STATE_PLAY_TURN3) {
            return (this.firstTeamID + 1) % 2;
        } else if (this.turnState == STATE_PLAY_TURN4) {
            return this.firstTeamID;
        }
        return -1;
    }


    /**
     * 手が打たれたことをカウントする
     */
    public void doPlay() {
        if (this.turnState == STATE_PLAY_TURN1) {
            this.turnState = STATE_PLAY_TURN2;
        } else if (this.turnState == STATE_PLAY_TURN2) {
            this.turnState = STATE_PLAY_TURN3;
        } else if (this.turnState == STATE_PLAY_TURN3) {
            this.turnState = STATE_PLAY_TURN4;
        } else if (this.turnState == STATE_PLAY_TURN4) {
            this.turnState = -1;
        }
    }

    /**
     * 自分のIDをセット
     */
    public int setMyTeamID(int id) {
        if (this.state != STATE_WAITINGPLAYER) {
            return -1;
        }
        this.MyTeamID = id;
        this.setTeamName(this.MyTeamID, this.myName);

        for (int x = -1; x < 10; x++) {
            for (int y = -1; y < 10; y++) {

            }
        }
        return 0;
    }

    /**
     * 相手ユーザを追加
     */
    public int adversHasCome(String name) {
        if (this.state != STATE_WAITINGPLAYER) {
            return -1;
        }
        int otherid = (this.MyTeamID + 1) % 2;
        //game_idを取得
        this.setTeamName(otherid, name);
        this.firstTeamID = 0;
        this.state = STATE_PLAY;
        return 0;
    }

    /**
     * TeamID のユーザが切断
     */
    public void disconnectUser(int TeamID) {
        this.state = STATE_WAITINGPLAYER;
        if (TeamID == 0) {

        } else if (TeamID == 1) {

        }
    }


    /**
     * 現在状態の取得
     */
    public int getStateNumber() {
        return this.state;
    }

    public static Pattern UNITPattern = Pattern.compile("401 UNIT ([0-1]) ([0-4]) ([0-8]) ([0-8])");
    public static Pattern OBSTACLEPattern = Pattern.compile("406 OBSTACLE ([0-5]) ([0-8]) ([0-8])");
    public static Pattern TOWERPattern = Pattern.compile("402 TOWER ([0-5]) ([0-1])");
    public static Pattern SCOREPattern = Pattern.compile("403 SCORE ([0-1]) ([0-9]+)");

    /**
     * ボード状態をまとめて設定
     */
    public void setBordState(ArrayList<String> list) {
        for (String dataline : list) {
            Matcher umc = UNITPattern.matcher(dataline);
            Matcher omc = OBSTACLEPattern.matcher(dataline);
            Matcher tmc = TOWERPattern.matcher(dataline);
            Matcher smc = SCOREPattern.matcher(dataline);

            if (umc.matches()) {
                int team = Integer.parseInt(umc.group(1));
                int unitnum = Integer.parseInt(umc.group(2));
                int xpos = Integer.parseInt(umc.group(3));
                int ypos = Integer.parseInt(umc.group(4));
                Point pos = new Point(xpos, ypos);
                this.prevUnitLocation[team][unitnum] = this.unitLocation[team][unitnum];
                this.unitLocation[team][unitnum] = pos;

            } else if (omc.matches()) {
                int ovstnum = Integer.parseInt(omc.group(1));//障害物ID
                int xpos = Integer.parseInt(omc.group(2));
                int ypos = Integer.parseInt(omc.group(3));
                Point pos = new Point(xpos, ypos);
            } else if (tmc.matches()) {
                int unitnum = Integer.parseInt(tmc.group(1));
                int team = Integer.parseInt(tmc.group(2));
                this.towerHold[unitnum] = team;

            } else if (smc.matches()) {
                int team = Integer.parseInt(smc.group(1));
                int value = Integer.parseInt(smc.group(2));
                this.teamPoint[team] = value;

            }
        }

        //盤面タイプ判定
        this.analyzeBoardType();
    }

    /**
     * ボードの種類を判定する
     */
    private void analyzeBoardType() {
        switch (this.boardType) {
            case BOARD_TYPE_A:
            case BOARD_TYPE_B:
            case BOARD_TYPE_C:
            case BOARD_TYPE_D:
                //盤面状態確定済みであるため、何もしない
                return;
        }

        //TODO:自分の指した手による判断

        //移動前後の場所でのみ判断
        for(int team = 0; team < 2; team++) {
            for(int unit = 0; unit < 4; unit++) {
                //わざわざ配列の位置で指定するのがめんどいのと、見易さのため、以降移動前後の位置をaとbで表記
                Point a = prevUnitLocation[team][unit];
                Point b = unitLocation[team][unit];

                //移動距離が1以下の場合、動いていないか、動いていてもワープを使用していない可能性があるので判定に用いない
                if(distance(a, b) <= 1) {
                    continue;
                }

                //左と下、右と上が繋がっていればAかB、左と上、右と下が繋がっていればCかD
                if((a.x == 0 && b.y == 8) || (a.x == 8 && b.y == 0)) {
                    if(a.y == 4 || b.x == 4) {
                        this.boardType = BOARD_TYPE_AB;
                    } else if((a.y < 4 && b.x < 4) || (a.y > 4 && b.x > 4)) {
                        this.boardType = BOARD_TYPE_A;
                    } else {
                        this.boardType = BOARD_TYPE_B;
                    }
                } else if((a.y == 0 && b.x == 8) || (a.y == 8 && b.x == 0)) {
                    if(a.x == 4 || b.y == 4) {
                        this.boardType = BOARD_TYPE_AB;
                    } else if((a.x < 4 && b.y < 4) || (a.x > 4 && b.y > 4)) {
                        this.boardType = BOARD_TYPE_A;
                    } else {
                        this.boardType = BOARD_TYPE_B;
                    }
                } else if((a.x == 0 && b.y == 0) || (a.x == 8 && b.y == 8)) {
                    if(a.y == 4 || b.x == 4) {
                        this.boardType = BOARD_TYPE_CD;
                    } else if((a.y < 4 && b.x > 4) || (a.y > 4 && b.x < 4)) {
                        this.boardType = BOARD_TYPE_C;
                    } else {
                        this.boardType = BOARD_TYPE_D;
                    }
                } else if((a.y == 0 && b.x == 0) || (a.y == 8 && b.x == 8)) {
                    if(a.x == 4 || b.y == 4) {
                        this.boardType = BOARD_TYPE_CD;
                    } else if((a.x < 4 && b.y > 4) || (a.x > 4 && b.y < 4)) {
                        this.boardType = BOARD_TYPE_C;
                    } else {
                        this.boardType = BOARD_TYPE_D;
                    }
                } else {
                    //盤面タイプ決定に使用されない。この後の表示部分を行わないようにするためcontinueをはさんだ
                    continue;
                }

                switch (this.boardType) {
                    case BOARD_TYPE_A:
                        System.out.println("盤面タイプ確定：A");
                        break;
                    case BOARD_TYPE_B:
                        System.out.println("盤面タイプ確定：B");
                        break;
                    case BOARD_TYPE_C:
                        System.out.println("盤面タイプ確定：C");
                        break;
                    case BOARD_TYPE_D:
                        System.out.println("盤面タイプ確定：D");
                        break;
                    case BOARD_TYPE_AB:
                        System.out.println("盤面タイプ半確定：AまたはB");
                        break;
                    case BOARD_TYPE_CD:
                        System.out.println("盤面タイプ半確定：CまたはD");
                        break;
                }
            }
        }
    }

    /**
     * ユーザへのメッセージ表示。各AIが実装する部分
     */
    public abstract void addMessage(String msg) throws InterruptedException;

    /**
     * 自軍の駒が、本陣、タワー、それ以外の場所に何個いるかを求める
     * @return 自軍の、本陣にいる駒の数を[0]に、タワーにいる駒の数を[1]に、それ以外のマスにいる駒の数[2]に格納した配列
     */
    public int[] search_pos_count() {
        return search_pos_count(this.MyTeamID);
    }

    /**
     * 指定した軍の駒が、本陣、タワー、それ以外の場所に何個いるかを求める
     * @param teamID 駒の数を求める軍のID。先手が0、後手が1
     * @return teamIDで指定した軍の、本陣にいる駒の数を[0]に、タワーにいる駒の数を[1]に、それ以外のマスにいる駒の数[2]に格納した配列
     */
    public int[] search_pos_count(int teamID) {
        System.out.printf("search_pos_count : %d\n", teamID);
        int field_count = 4;
        int base_count = 0;
        int tower_count = 0;
        int[] unit1 = new int[3];
        for (int i = 0; i < 4; i++) {
            if (teamID == 0 && this.unitLocation[teamID][i].equals(Base0)) {
                base_count++;
            } else if (teamID == 1 && this.unitLocation[teamID][i].equals(Base1)) {
                base_count++;
            } else if (this.unitLocation[teamID][i].equals(tower_left)) {
                tower_count++;
            } else if (this.unitLocation[teamID][i].equals(tower_right)) {
                tower_count++;
            } else if(this.unitLocation[teamID][i].equals(tower_center)) {
                tower_count++;
            }
        }

        unit1[0] = base_count;
        unit1[1] = tower_count;
        unit1[2] = field_count - base_count - tower_count;
        return unit1;
    }

    /** 1VS1の対戦
     * @param A_char 味方側の駒(GREEN:0, BLACK:1, RED:2, YELLOW:3)
     * @param E_char 相手側の駒(GREEN:0, BLACK:1, RED:2, YELLOW:3)
     * @return 1:勝ち 0:負け 2:引き分け -1:エラー
     * */

    public static int onevs(int A_char, int E_char){

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

    /** 2VS2の対戦
     * @param A_char1 味方側の駒・1つ目(GREEN:0, BLACK:1, RED:2, YELLOW:3)
     * @param A_char2 味方側の駒・2つ目(GREEN:0, BLACK:1, RED:2, YELLOW:3)
     * @param E_char1 相手側の駒・1つ目(GREEN:0, BLACK:1, RED:2, YELLOW:3)
     * @param E_char2 相手側の駒・2つ目(GREEN:0, BLACK:1, RED:2, YELLOW:3)
     * @return 1:勝ち 0:負け 2:引き分け -1:エラー
     * */
    public static int twovs(int A_char1, int A_char2, int E_char1, int E_char2){

        if((A_char1 == BLACK && A_char2 == GREEN)||(A_char1 == GREEN && A_char2 == BLACK)){
            if((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == RED && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == RED)){
                return 2;
            }
            else if ((E_char1 == GREEN && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == GREEN)){
                return 0;
            }
            else if ((E_char1 == BLACK && E_char2 == RED)||(E_char1 == RED && E_char2 == BLACK)){
                return 1;
            }
            else if ((E_char1 == BLACK && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 2;
            }
        }

        if((A_char1 == BLACK && A_char2 == RED)||(A_char1 == RED && A_char2 == BLACK)){
            if((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 1;
            }
            else if ((E_char1 == RED && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == RED)){
                return 2;
            }
            else if ((E_char1 == GREEN && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == GREEN)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == RED)||(E_char1 == RED && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 1;
            }
        }
        if((A_char1 == GREEN && A_char2 == RED)||(A_char1 == RED && A_char2 == GREEN)){
            if((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == RED && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == RED)){
                return 2;
            }
            else if ((E_char1 == GREEN && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == GREEN)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == RED)||(E_char1 == RED && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 2;
            }
        }
        if((A_char1 == BLACK && A_char2 == YELLOW)||(A_char1 == YELLOW && A_char2 == BLACK)){
            if((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == RED && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == RED)){
                return 2;
            }
            else if ((E_char1 == GREEN && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == GREEN)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == RED)||(E_char1 == RED && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 2;
            }
        }
        if((A_char1 == GREEN && A_char2 == YELLOW)||(A_char1 == YELLOW && A_char2 == GREEN)){
            if((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 0;
            }
            else if ((E_char1 == RED && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == RED)){
                return 2;
            }
            else if ((E_char1 == GREEN && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == GREEN)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == RED)||(E_char1 == RED && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 0;
            }
        }
        if((A_char1 == RED && A_char2 == YELLOW)||(A_char1 == YELLOW && A_char2 == RED)){
            if((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == RED && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == RED)){
                return 2;
            }
            else if ((E_char1 == GREEN && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == GREEN)){
                return 0;
            }
            else if ((E_char1 == BLACK && E_char2 == RED)||(E_char1 == RED && E_char2 == BLACK)){
                return 1;
            }
            else if ((E_char1 == BLACK && E_char2 == YELLOW)||(E_char1 == YELLOW && E_char2 == BLACK)){
                return 2;
            }
            else if ((E_char1 == BLACK && E_char2 == GREEN)||(E_char1 == GREEN && E_char2 == BLACK)){
                return 2;
            }
        }

        return -1;
    }
    
    /**
     * ２点の距離を計算（上下左右、斜めのどこでも１歩）
     */
    public static int distance(Point a, Point b) {
        if (a.x == b.x) {
            return Math.abs(a.y - b.y);
        } else if (a.y == b.y) {
            return Math.abs(a.x - b.x);
        } else {
            //斜めに近づく場合は長い方と同じだけで大丈夫
            int xdef = Math.abs(a.x - b.x);
            int ydef = Math.abs(a.y - b.y);
            if (xdef > ydef) {
                return xdef;
            } else {
                return ydef;
            }
        }
    }


    /** 得点差 */
    public int getPoint(){
        int i, j;
        i = teamPoint[0] - teamPoint[1];
        if(i > 0) victoryTeamID = 0;
        else if(i < 0) victoryTeamID = 1;

        return Math.abs(i);
    }

    /** 最も近いタワー */
    public static Point getNearestTower(Point pos) {
        int i, j, k;
        i = distance(pos, tower_left);
        j = distance(pos, tower_center);
        k = distance(pos, tower_right);

        if (i < j && i < k) return (tower_left);
        else if (j < i && j < k) return (tower_center);
        else if (k < i && k < j) return (tower_right);
        else return (tower_center);
    }
    public int GetTurnCount(){
        return this.turnCount;
    }
    public int[] GetTeamPoint(){

        return this.teamPoint;
    }
}
