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
public class AI extends javax.swing.JFrame {



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
    private Connection sthread;
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

    private int AI_type;

    public static int PointValue2TrueValue(int v){
        return v+1;
    }

    /** Creates new form GameField */
    public AI(String address,String type) {
        this.serverIP = address;
        AI_type = Integer.parseInt(type);
        System.out.println("init");
        this.resetAll();
        this.paintComponents();
        this.sthread.sendName();
    }
    /** 状態をすべてリセット */
    public void resetAll() {
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
        this.sthread = new Connection(this.myName,this);
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
        this.playingTeamID = -1;
        this.firstTeamID = -1;
        this.ternCount = 0;
        this.gameCell = new Field[11][11];
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                this.gameCell[j][i] = new Field(this);
                if(i == 0 || i == 10){
                    this.gameCell[j][i].setBorderCell(true);
                }
                if(j == 0 || j == 10){
                    this.gameCell[j][i].setBorderCell(true);
                }
            }
        }
        //塔の設置
        this.gameCell[PointValue2TrueValue(1)][PointValue2TrueValue(4)].setTowerCell(true);
        this.gameCell[PointValue2TrueValue(4)][PointValue2TrueValue(4)].setTowerCell(true);
        this.gameCell[PointValue2TrueValue(7)][PointValue2TrueValue(4)].setTowerCell(true);

        //本陣の設置
        this.gameCell[PointValue2TrueValue(4)][PointValue2TrueValue(7)].setHonjin(0);
        this.gameCell[PointValue2TrueValue(4)][PointValue2TrueValue(1)].setHonjin(1);

        //ユニットの設置
        this.unitLocation = new Point[2][4];
        for(int i=0;i<4;i++){
            this.unitLocation[0][i] = new Point(4,7);
            this.unitLocation[1][i] = new Point(4,1);
        }

        this.towerHold = new int[towerCount];
        for(int i=0;i<towerCount;i++){
            this.towerHold[i] = -1;
        }

        this.teamPoint = new int[2];
        this.teamPoint[0] = 0;
        this.teamPoint[1] = 0;

        this.resetTurnState();


    }

    /** ターンの１手目に戻す */
    public void resetTurnState(){
        this.turnState = STATE_PLAY_TURN1;
        this.ternCount++;
    }

    /** 表示項目の一新 */
    private void paintComponents(){

        //塔の持ち主
        this.gameCell[PointValue2TrueValue(1)][PointValue2TrueValue(4)].setTowerHas(this.towerHold[0]);
        this.gameCell[PointValue2TrueValue(4)][PointValue2TrueValue(4)].setTowerHas(this.towerHold[1]);
        this.gameCell[PointValue2TrueValue(7)][PointValue2TrueValue(4)].setTowerHas(this.towerHold[2]);


        //ユニットの描画
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                this.gameCell[i][j].crearUnit();
            }
        }
        for(int i=0;i<4;i++){
            Point p0 = this.unitLocation[0][i];
            this.gameCell[PointValue2TrueValue(p0.x)][PointValue2TrueValue(p0.y)].setUnit(0, i);
            Point p1 = this.unitLocation[1][i];
            this.gameCell[PointValue2TrueValue(p1.x)][PointValue2TrueValue(p1.y)].setUnit(1, i);
        }
        for(int i=0;i<11;i++){
            for(int j=0;j<11;j++){
                this.gameCell[i][j].validate();
            }
        }
        this.validate();
        this.repaint();
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
    }

    /** 手番のプレイヤーを変更 */
    public void setPlayingTeamID(int i){
        this.playingTeamID = i;
        this.state = STATE_PLAY;
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
                this.unitLocation[team][unitnum] = pos;
            } else if(omc.matches()){
                int ovstnum = Integer.parseInt(omc.group(1));//障害物ID
                int xpos = Integer.parseInt(omc.group(2));
                int ypos = Integer.parseInt(omc.group(3));
                Point pos = new Point(xpos,ypos);
            } else if(tmc.matches()){
                int unitnum = Integer.parseInt(tmc.group(1));
                int team = Integer.parseInt(tmc.group(2));
                this.towerHold[unitnum] = team;
            } else if(smc.matches()){
                int team = Integer.parseInt(smc.group(1));
                int value = Integer.parseInt(smc.group(2));
                this.teamPoint[team] = value;
            }
        }
        this.paintComponents();
    }


    /** ユーザへのメッセージ表示 */
    public void addMessage(String msg) {

        if(this.sthread.state == STATE_PLAY){

            switch (AI_type){
                case 1:{
                    ai1();break;
                }
                case 2:{
                    ai2();break;
                }
                case 3:{
                    ai3();break;
                }
                case 4:{
                    ai_demo();break;
                }
                default:{
                    ai1();
                    break;
                }
            }
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

    void selectUnit(int unitID) {
        if(this.state == STATE_PLAY){
            if(this.MyTeamID == this.playingTeamID){
                this.state = STATE_PLAY_UNITSELECT;
                this.selectedUnit = unitID;
                this.addMessage(unitID + "が選択されました。移動先をクリックしてください。");

            }
        }
    }


    public void ai1(){

        this.sthread.sendPlayMessage(0,5,8);
    //higuchi
    }

    public void ai2(){

    //Nishinaka
    }

    public void ai3(){

    //Hosono
    }

    public void ai_demo(){

    //pre
    }



}
