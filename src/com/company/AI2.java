package com.company;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.UnknownHostException;
import java.lang.Object.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class AI2 extends javax.swing.JFrame {

    public static final int DEFALUTPORT = 13306;
    public static Color BGColor = new Color(236,233,216);
    //public static int ObstacleCount = 4;

    /** ２点の距離を計算（上下左右、斜めのどこでも１歩） */
    public static int distance(Point a,Point b){
        if(a.x == b.x){
            return Math.abs(a.y - b.y);
        } else if(a.y == b.y){
            return Math.abs(a.x - b.x);
        } else {
            //斜めに近づく場合は長い方と同じだけで大丈夫
            int xdef = Math.abs(a.x - b.x);
            int ydef = Math.abs(a.y - b.y);
            if(xdef > ydef){
                return xdef;
            } else {
                return ydef;
            }
        }
    }

    //盤面のタイプ
    /** 現在のボードのタイプ */
    public int boardType;
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

    //閾値設定
    /** ゲーム終了の点数 */
    private static final int MAXPOINT = 50;
    /** 無得点の最大ターン数 */
    private static final int NOPOINTTIME = 10;

    /** ボードの状態 */
    public Boardinfo info;

    /** ボードの状態 */
    private int state = STATE_WAITINGPLAYER;
    /** どちらのプレイヤーがプレイ中か　0または1になる。-1はゲーム中ではない状態 */
    private int playingTeamID = -1;
    /** 先攻プレイヤーはどちらか */
    private int firstTeamID = -1;
    /** ターン数*/
    private int ternCount;

    /** 自分の名前 */
    private String myName;
    /** サーバのアドレス */
    private String serverIP;
    /** サーバポート */
    private int serverPort;
    /** サーバ待ち受けスレッド*/
    private Connection2 sthread;
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


    private boolean nextenable = false;
    private int[] nextorder;
    private int routeinfo = -1;

    /** 前のユニットの位置 */
    private Point[][] prevUnitLocation;


    /** 駒の種類*/
    public static final int GREEN   = 0;
    public static final int BLACK   = 1;
    public static final int RED     = 2;
    public static final int YELLOW  = 3;



    /** AIの種類 **/
    private int AI_type;

    public final Point Base0 = new Point(4,7);
    public final Point Base1 = new Point(4,1);

    /** タワーの位置 */
    public static final Point tower_left = new Point(1, 4);
    public static final Point tower_center = new Point(4, 4);
    public static final Point tower_right = new Point(7, 4);


    /** 勝っているチームのID */
    public int victoryTeamID = -1;


    public static int PointValue2TrueValue(int v){
        return v+1;
    }

    /** Creates new form GameField */
    public AI2(String address,String type,String stype) throws InterruptedException {
        this.serverIP = address;
        AI_type = Integer.parseInt(type);
        System.out.println("init");
        this.resetAll();
        this.paintComponents();
        this.sthread.sendName();
    }

    /** 状態をすべてリセット */
    public void resetAll() throws InterruptedException {
        this.state = STATE_WAITINGPLAYER;
        //名前の入力
        this.myName = null;
        this.myName = "TajimaLab";
        System.out.println("Playname:"+myName);
        if(this.myName == null){
            System.exit(0);
        }

        this.serverPort = DEFALUTPORT;

        //サーバに接続する
        this.sthread = new Connection2(this.myName,this);
        try {
            boolean connect = this.sthread.connectToServer(this.serverIP, this.serverPort);
            if(connect == false){
                this.addMessage("サーバへの接続に失敗しました。");
                System.exit(0);
            }
        } catch (UnknownHostException ex) {
            this.addMessage("サーバの指定が不正です。UnknownHostException");
            System.exit(0);
        } catch (IOException ex) {
            this.addMessage("サーバへの接続に失敗しました。IOException");
            System.exit(0);
        }
        //正しく接続できたら処理開始
        this.playingTeamID = -1; info.playingTeamID = -1;
        this.firstTeamID = -1; info.firstTeamID = -1;
        this.ternCount = 0; info.turnCount = 0;
        this.boardType = BOARD_TYPE_UNDEFINED;

        //ユニットの設置
        this.unitLocation = new Point[2][4];
        for(int i=0;i<4;i++){
            this.unitLocation[0][i] = new Point(4,7);info.unitLocation[0][i] = new Point(4,7);
            this.unitLocation[1][i] = new Point(4,1);info.unitLocation[1][i] = new Point(4,7);
        }

        this.towerHold = new int[towerCount];
        info.towerHold = new int[towerCount];
        for(int i=0;i<towerCount;i++){
            this.towerHold[i] = -1;
            info.towerHold[1] = -1;
        }

        this.teamPoint = new int[2];
        this.teamPoint[0] = 0;
        this.teamPoint[1] = 0;

        info.teamPoint = new int[2];
        info.teamPoint[0] = 0;
        info.teamPoint[1] = 0;

        this.resetTurnState();


    }

    /** ターンの１手目に戻す */
    public void resetTurnState(){
        this.turnState = STATE_PLAY_TURN1;
        this.ternCount++;
        info.turnState = STATE_PLAY_TURN1;
        info.turnCount++;

    }

    /** 表示項目の一新 */
    private void paintComponents(){


    }

    /** 先行かどうかを返す */
    public boolean isFirstPlayer(int myID){
        if(this.firstTeamID == myID){
            return true;
        } else {
            return false;
        }
    }

    /** 先攻、後攻の切り替え */
    public void changeFirstTeam(){
        this.firstTeamID = (this.firstTeamID + 1 )%2;
        info.firstTeamID = (this.firstTeamID + 1 )%2;
    }

    /** 手番のプレイヤーを変更 */
    public void setPlayingTeamID(int i){
        this.playingTeamID = i;
        this.state = STATE_PLAY;
        info.playingTeamID = i;
        info.state = STATE_PLAY;

    }


    /** チーム名の変更 */
    public void setTeamName(int teamNumber,String teamName){
        if(teamNumber == 0){
        }
        else if(teamNumber == 1){
        }
        this.paintComponents();
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


    /** 手が打たれたことをカウントする */
    public void doPlay(){
        if(this.turnState == STATE_PLAY_TURN1){
            this.turnState = STATE_PLAY_TURN2;
        } else if(this.turnState == STATE_PLAY_TURN2){
            this.turnState = STATE_PLAY_TURN3;
        } else if(this.turnState == STATE_PLAY_TURN3){
            this.turnState = STATE_PLAY_TURN4;
        } else if(this.turnState == STATE_PLAY_TURN4){
            this.turnState = -1;
        }
    }

    /** 自分のIDをセット */
    public int setMyTeamID(int id){
        if(this.state != STATE_WAITINGPLAYER){
            return -1;
        }
        this.MyTeamID = id;
        this.setTeamName(this.MyTeamID,this.myName);

        for(int x=-1;x<10;x++){
            for(int y=-1;y<10;y++){

            }
        }
        paintComponents();
        return 0;
    }

    /** 相手ユーザを追加 */
    public int adversHasCome(String name) {
        if(this.state != STATE_WAITINGPLAYER){
            return -1;
        }
        int otherid = (this.MyTeamID+1)%2;
        this.setTeamName(otherid,name);
        this.firstTeamID = 0;
        this.state = STATE_PLAY;
        paintComponents();
        return 0;
    }

    /** TeamID のユーザが切断*/
    public void disconnectUser(int TeamID) {
        this.state = STATE_WAITINGPLAYER;
        if(TeamID == 0){

        } else if(TeamID == 1){

        }
    }


    /** 現在状態の取得 */
    public int getStateNumber(){
        return this.state;
    }

    public static Pattern UNITPattern = Pattern.compile("401 UNIT ([0-1]) ([0-4]) ([0-8]) ([0-8])");
    public static Pattern OBSTACLEPattern = Pattern.compile("406 OBSTACLE ([0-5]) ([0-8]) ([0-8])");
    public static Pattern TOWERPattern = Pattern.compile("402 TOWER ([0-5]) ([0-1])");
    public static Pattern SCOREPattern = Pattern.compile("403 SCORE ([0-1]) ([0-9]+)");
    /** ボード状態をまとめて設定 */
    public void setBordState(ArrayList<String> list) {
        for(String dataline:list){
            Matcher umc = UNITPattern.matcher(dataline);
            Matcher omc = OBSTACLEPattern.matcher(dataline);
            Matcher tmc = TOWERPattern.matcher(dataline);
            Matcher smc = SCOREPattern.matcher(dataline);

            if(umc.matches()){
                int team = Integer.parseInt(umc.group(1));
                int unitnum = Integer.parseInt(umc.group(2));
                int xpos = Integer.parseInt(umc.group(3));
                int ypos = Integer.parseInt(umc.group(4));
                Point pos = new Point(xpos,ypos);
                this.prevUnitLocation[team][unitnum] = this.unitLocation[team][unitnum];
                info.prevUnitLocation[team][unitnum] = this.prevUnitLocation[team][unitnum];
                this.unitLocation[team][unitnum] = pos;
                info.unitLocation[team][unitnum] = pos;
            } else if(omc.matches()){
                int ovstnum = Integer.parseInt(omc.group(1));//障害物ID
                int xpos = Integer.parseInt(omc.group(2));
                int ypos = Integer.parseInt(omc.group(3));
                Point pos = new Point(xpos,ypos);
            } else if(tmc.matches()){
                int unitnum = Integer.parseInt(tmc.group(1));
                int team = Integer.parseInt(tmc.group(2));
                this.towerHold[unitnum] = team;
                info.towerHold[unitnum] = team;
            } else if(smc.matches()){
                int team = Integer.parseInt(smc.group(1));
                int value = Integer.parseInt(smc.group(2));
                this.teamPoint[team] = value;
                info.teamPoint[team] = value;
            }
        }
        this.paintComponents();
    }


    /** ユーザへのメッセージ表示 */
    public void addMessage(String msg) throws InterruptedException  {

        if(this.sthread.state == STATE_PLAY){


        }

        System.out.println(msg);
    }

        private int selectedUnit = -1;
    void selectPoint(int x, int y) {
        if(this.state == STATE_PLAY_UNITSELECT){
            this.state = STATE_PLAY;
            this.sthread.sendPlayMessage(this.selectedUnit,x,y);
        }
    }

    void selectUnit(int unitID) throws InterruptedException {
        if(this.state == STATE_PLAY){
            if(this.MyTeamID == this.playingTeamID){
                this.state = STATE_PLAY_UNITSELECT;
                this.selectedUnit = unitID;
                this.addMessage(unitID + "が選択されました。移動先をクリックしてください。");

            }
        }
    }











    /** 1VS1の対戦
     * 戻り値：1:勝ち 0:負け 2:引き分け -1:エラー
     * 引数：  GREEN:0, BLACK:1, RED:2, YELLOW:3
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

    /** 2VS2の対戦
     * 戻り値：1:勝ち 0:負け 2:引き分け -1:エラー
     * 引数：  GREEN:0, BLACK:1, RED:2, YELLOW:3
     * */
    public int twovs(int A_char1, int A_char2, int E_char1, int E_char2){

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
        int i, j, k;
        i = distance(pos, tower_left);
        j = distance(pos, tower_center);
        k = distance(pos, tower_right);

        if (i < j && i < k) return (tower_left);
        else if (j < i && j < k) return (tower_center);
        else if (k < i && k < j) return (tower_right);
        else return (tower_center);
    }

}
