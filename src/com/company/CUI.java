package com.company;


import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.UnknownHostException;
import java.lang.Object.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class CUI extends javax.swing.JFrame {

    public static final int DEFALUTPORT = 13306;
    public static Color BGColor = new Color(236, 233, 216);
    //public static int ObstacleCount = 4;

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
    private int turnState = -1;


    //閾値設定
    /**
     * ゲーム終了の点数
     */
    private static final int MAXPOINT = 50;
    /**
     * 無得点の最大ターン数
     */
    private static final int NOPOINTTIME = 10;

    /** ボードの状態 */
    //public Boardinfo info;

    /**
     * ボードの状態
     */
    private int state = STATE_WAITINGPLAYER;
    /**
     * どちらのプレイヤーがプレイ中か　0または1になる。-1はゲーム中ではない状態
     */
    private int playingTeamID = -1;
    /**
     * 先攻プレイヤーはどちらか
     */
    private int firstTeamID = -1;
    /**
     * ターン数
     */
    private int ternCount;

    /**
     * 自分の名前
     */
    private String myName;
    /**
     * サーバのアドレス
     */
    private String serverIP;
    /**
     * サーバポート
     */
    private int serverPort;
    /**
     * サーバ待ち受けスレッド
     */
    private ConnectionCUI sthread;
    /**
     * セル用配列
     */
    private Field[][] gameCell;
    /**
     * ユニットの位置
     */
    private Point[][] unitLocation;
    /**
     * 塔の保持状態
     */
    private int[] towerHold;
    private int towerCount = 3;
    /**
     * チームの得点
     */
    private int[] teamPoint;
    /**
     * 自分のチーム番号
     */
    private int MyTeamID;


    private boolean nextenable = false;
    private int[] nextorder;
    //private int routeinfo = -1;

    /**
     * 前のユニットの位置
     */
    private Point[][] prevUnitLocation;


    /**
     * 駒の種類
     */
    public static final int GREEN = 0;
    public static final int BLACK = 1;
    public static final int RED = 2;
    public static final int YELLOW = 3;


    /**
     * 手の種類
     */
    public int STATE;

    public static final int FIRST_TURN = 1;
    public static final int FIRST_MYTURN1 = 01;
    public static final int FIRST_MYTURN2 = 02;
    public static final int MYTURN_1 = 11;
    public static final int MYTURN_2 = 12;


    //ターン管理
    public static final int STATE_PLAY_TURN1 = 11;
    public static final int STATE_PLAY_TURN2 = 12;
    public static final int STATE_PLAY_TURN3 = 13;
    public static final int STATE_PLAY_TURN4 = 14;


    //コマの状態
    public static final int FIELD_0_TOWER_0_CAMP_2 = 11;
    public static final int FIELD_0_TOWER_1_CAMP_1 = 12;
    public static final int FIELD_0_TOWER_2_CAMP_0 = 13;
    public static final int FIELD_1_TOWER_0_CAMP_1 = 14;
    public static final int FIELD_1_TOWER_1_CAMP_0 = 15;
    public static final int FIELD_2_TOWER_0_CAMP_0 = 16;


    //タワー位置
    public static final Point Tower0 = new Point(1, 4);
    public static final Point Tower1 = new Point(4, 4);
    public static final Point Tower2 = new Point(7, 4);


    //上側：本陣位置
    //public static final Point Base0 = new Point(4, 1);

    public static final Point Route001 = new Point(3, 6);
    public static final Point Route002 = new Point(2, 5);
    public static final Point Route003 = new Point(1, 4);

    public static final Point Route011 = new Point(3, 6);
    public static final Point Route012 = new Point(3, 5);
    public static final Point Route013 = new Point(4, 4);

    public static final Point Route021 = new Point(5, 6);
    public static final Point Route022 = new Point(5, 5);
    public static final Point Route023 = new Point(4, 4);

    public static final Point Route031 = new Point(5, 6);
    public static final Point Route032 = new Point(6, 5);
    public static final Point Route033 = new Point(7, 4);

    public static final int Route001_ = 101;
    public static final int Route002_ = 102;
    public static final int Route003_ = 103;

    public static final int Route011_ = 111;
    public static final int Route012_ = 112;
    public static final int Route013_ = 113;

    public static final int Route021_ = 121;
    public static final int Route022_ = 122;
    public static final int Route023_ = 123;

    public static final int Route031_ = 131;
    public static final int Route032_ = 132;
    public static final int Route033_ = 133;


    //下側：本陣位置
    //public static final Point Base1 = new Point(4, 7);

    public static final Point Route101 = new Point(3, 2);
    public static final Point Route102 = new Point(2, 3);
    public static final Point Route103 = new Point(1, 4);

    public static final Point Route111 = new Point(3, 2);
    public static final Point Route112 = new Point(3, 3);
    public static final Point Route113 = new Point(4, 4);

    public static final Point Route121 = new Point(5, 2);
    public static final Point Route122 = new Point(5, 3);
    public static final Point Route123 = new Point(4, 4);

    public static final Point Route131 = new Point(5, 2);
    public static final Point Route132 = new Point(6, 3);
    public static final Point Route133 = new Point(7, 4);

    public static final int Route101_ = 101;
    public static final int Route102_ = 102;
    public static final int Route103_ = 103;

    public static final int Route111_ = 111;
    public static final int Route112_ = 112;
    public static final int Route113_ = 113;

    public static final int Route121_ = 121;
    public static final int Route122_ = 122;
    public static final int Route123_ = 123;

    public static final int Route131_ = 131;
    public static final int Route132_ = 132;
    public static final int Route133_ = 134;





    public int[] tower;

    public static final int RED_GREEN = 0;
    public static final int BLACK_YELLOW = 1;
    public int moveunit = -1;

    public int base_unitpair;
    public int tower_unitpair;
    public int routeinfo = -1;


    /**
     * AIの種類 *
     */
    private int AI_type;

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


    public static int PointValue2TrueValue(int v) {
        return v + 1;
    }

    /**
     * Creates new form GameField
     */
    public CUI(String address, String type, String stype) throws InterruptedException {
        this.serverIP = address;
        AI_type = Integer.parseInt(type);
        System.out.println("init");
        this.resetAll();
        this.paintComponents();
        this.sthread.sendName();
    }

    /**
     * 状態をすべてリセット
     */
    public void resetAll() throws InterruptedException {
        this.state = STATE_WAITINGPLAYER;
        //名前の入力
        this.myName = null;
        this.myName = "TajimaLab";
        System.out.println("Playname:" + myName);
        if (this.myName == null) {
            System.exit(0);
        }

        this.serverPort = DEFALUTPORT;

        //サーバに接続する
        this.sthread = new ConnectionCUI(this.myName, this);
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
        this.ternCount = 0;
        this.boardType = BOARD_TYPE_UNDEFINED;

        this.unitLocation = new Point[2][4];
        for (int i = 0; i < 4; i++) {
            this.unitLocation[0][i] = new Point(4, 7);
            this.unitLocation[1][i] = new Point(4, 1);

            this.towerHold = new int[towerCount];

            for (int j = 0; i < towerCount; i++) {
                this.towerHold[j] = -1;

            }

            this.teamPoint = new int[2];
            this.teamPoint[0] = 0;
            this.teamPoint[1] = 0;


            this.resetTurnState();

        }
    }

    /**
     * ターンの１手目に戻す
     */
    public void resetTurnState() {
        this.turnState = STATE_PLAY_TURN1;
        this.ternCount++;


    }

    /**
     * 表示項目の一新
     */
    private void paintComponents() {


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
        this.paintComponents();
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
        paintComponents();
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
        this.setTeamName(otherid, name);
        this.firstTeamID = 0;
        this.state = STATE_PLAY;
        paintComponents();
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
                //this.prevUnitLocation[team][unitnum] = this.unitLocation[team][unitnum];
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
        this.paintComponents();
    }

    int test = 1;
    int units[][] = new int[2][3];

    /**
     * ユーザへのメッセージ表示
     */
    public void addMessage(String msg) throws InterruptedException {

        if (msg == "Select Unit") {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Pattern pat = Pattern.compile("([0-3]) (-?[0-9]) (-?[0-9])");
            boolean incomplete = true;

            do {
                try {
                    String line = br.readLine();
                    Matcher mt = pat.matcher(line);
                    if(mt.matches()) {
                        int unitid = Integer.parseInt(mt.group(1));
                        int unitx = Integer.parseInt(mt.group(2));
                        int unity = Integer.parseInt(mt.group(3));
                        this.sthread.sendPlayMessage(unitid, unitx, unity);
                        incomplete = false;
                    } else {
                        System.out.println("Input Format Error, Please input again");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while(incomplete);
        }

        System.out.println(msg);
    }




    public synchronized int[][]  UnitOrder0() {

        /** 0:コマの種類　1:X座標　2:Y座標 3:ルート**/
        int unit[][] = new int[2][4];

        switch (this.MyTeamID) {
            case 0:
                switch (STATE) {
                    case FIELD_0_TOWER_0_CAMP_2:
                    case FIRST_MYTURN1:{
                        Random rnd = new Random();
                        int rot = rnd.nextInt(4);
                        int uni = rnd.nextInt(2);
                        int[] y = new int [2];
                        y[0] = rot; y[1] = uni;
                        if(y[0] == 0) {
                            unit[0][0] = setupUnit(y[1])[0];
                            unit[0][1] = Route001.x;
                            unit[0][2] = Route001.y;
                            unit[1][0] = setupUnit(y[1])[1];
                            unit[1][1] = Route001.x;
                            unit[1][2] = Route001.y;
                            routeinfo = Route001_;
                            moveunit = y[1];
                            return unit;
                        }
                        else if(y[0] == 1) {
                            unit[0][0] = setupUnit(y[1])[0];
                            unit[0][1] = Route011.x;
                            unit[0][2] = Route011.y;
                            unit[1][0] = setupUnit(y[1])[1];
                            unit[1][1] = Route011.x;
                            unit[1][2] = Route011.y;
                            routeinfo = Route011_;
                            moveunit = y[1];
                            return unit;
                        }
                        else if(y[0] == 2) {
                            unit[0][0] = setupUnit(y[1])[0];
                            unit[0][1] = Route021.x;
                            unit[0][2] = Route021.y;
                            unit[1][0] = setupUnit(y[1])[1];
                            unit[1][1] = Route021.x;
                            unit[1][2] = Route021.y;
                            routeinfo = Route021_;
                            moveunit = y[1];
                            return unit;
                        }
                        else if(y[0] == 3) {
                            unit[0][0] = setupUnit(y[1])[0];
                            unit[0][1] = Route031.x;
                            unit[0][2] = Route031.y;
                            unit[1][0] = setupUnit(y[1])[1];
                            unit[1][1] = Route031.x;
                            unit[1][2] = Route031.y;
                            routeinfo = Route031_;
                            moveunit = y[1];
                            return unit;
                        }

                    }

                    break;
                    case FIELD_0_TOWER_1_CAMP_1: {
                        tower = sendtowerhold();
                        enableBaseUnit();

                        if(tower[1] != 1) {
                            if (base_unitpair == BLACK_YELLOW) {
                                Random rnd = new Random();
                                int uni = rnd.nextInt(2);
                                if(uni == 0) {
                                    unit[0][0] = setupUnit(base_unitpair)[0];
                                    unit[0][1] = Route011.x;
                                    unit[0][2] = Route011.y;
                                    unit[1][0] = setupUnit(base_unitpair)[1];
                                    unit[1][1] = Route011.x;
                                    unit[1][2] = Route011.y;
                                    routeinfo = Route011_;
                                    moveunit = base_unitpair;
                                    return unit;
                                }
                                else if(uni == 1) {
                                    unit[0][0] = setupUnit(base_unitpair)[0];
                                    unit[0][1] = Route021.x;
                                    unit[0][2] = Route021.y;
                                    unit[1][0] = setupUnit(base_unitpair)[1];
                                    unit[1][1] = Route021.x;
                                    unit[1][2] = Route021.y;
                                    routeinfo = Route021_;
                                    moveunit = base_unitpair;
                                    return unit;
                                }
                            }
                            if (base_unitpair == RED_GREEN) {
                                Random rnd = new Random();
                                int uni = rnd.nextInt(2);
                                if(uni == 0) {
                                    unit[0][0] = setupUnit(base_unitpair)[0];
                                    unit[0][1] = Route011.x;
                                    unit[0][2] = Route011.y;
                                    unit[1][0] = setupUnit(base_unitpair)[1];
                                    unit[1][1] = Route011.x;
                                    unit[1][2] = Route011.y;
                                    routeinfo = Route011_;
                                    moveunit = base_unitpair;
                                    return unit;
                                }
                                else if(uni == 1) {
                                    unit[0][0] = setupUnit(base_unitpair)[0];
                                    unit[0][1] = Route021.x;
                                    unit[0][2] = Route021.y;
                                    unit[1][0] = setupUnit(base_unitpair)[1];
                                    unit[1][1] = Route021.x;
                                    unit[1][2] = Route021.y;
                                    routeinfo = Route021_;
                                    moveunit = base_unitpair;
                                    return unit;
                                }

                            }
                        }
                        if(tower[0] != 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(base_unitpair)[0];
                                unit[0][1] = Route001.x;
                                unit[0][2] = Route001.y;
                                unit[1][0] = setupUnit(base_unitpair)[1];
                                unit[1][1] = Route001.x;
                                unit[1][2] = Route001.y;
                                routeinfo = Route001_;
                                moveunit = base_unitpair;
                                return unit;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(base_unitpair)[0];
                                unit[0][1] = Route001.x;
                                unit[0][2] = Route001.y;
                                unit[1][0] = setupUnit(base_unitpair)[1];
                                unit[1][1] = Route001.x;
                                unit[1][2] = Route001.y;
                                routeinfo = Route001_;
                                moveunit = base_unitpair;
                                return unit;
                            }
                        }

                        if(tower[2] != 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(base_unitpair)[0];
                                unit[0][1] = Route031.x;
                                unit[0][2] = Route031.y;
                                unit[1][0] = setupUnit(base_unitpair)[1];
                                unit[1][1] = Route031.x;
                                unit[1][2] = Route031.y;
                                routeinfo = Route031_;
                                moveunit = base_unitpair;
                                return unit;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(base_unitpair)[0];
                                unit[0][1] = Route031.x;
                                unit[0][2] = Route031.y;
                                unit[1][0] = setupUnit(base_unitpair)[1];
                                unit[1][1] = Route031.x;
                                unit[1][2] = Route031.y;
                                routeinfo = Route031_;
                                moveunit = base_unitpair;
                                return unit;
                            }
                        }

                    }
                    break;
                    case FIELD_0_TOWER_2_CAMP_0: {
                        tower = sendtowerhold();
                        enableTowerUnit();
                        if (tower[1] == 1) {
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route012.x;
                            unit[0][2] = Route012.y;
                            unit[1][0] = setupUnit(tower_unitpair)[0];
                            unit[1][1] = Route013.x;
                            unit[1][2] = Route013.y;
                            return unit;
                        }
                        else if (tower[0] == 1) {
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route002.x;
                            unit[0][2] = Route002.y;
                            unit[1][0] = setupUnit(tower_unitpair)[1];
                            unit[1][1] = Route003.x;
                            unit[1][2] = Route003.y;
                            return unit;
                        }
                        else if (tower[2] == 1) {
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route002.x;
                            unit[0][2] = Route002.y;
                            unit[1][0] = setupUnit(tower_unitpair)[1];
                            unit[1][1] = Route003.x;
                            unit[1][2] = Route003.y;
                            return unit;
                        }
                    }
                        break;
                    case FIELD_1_TOWER_0_CAMP_1: {
                        if (routeinfo == Route001_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route002.x;
                            unit[0][2] = Route002.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route002.x;
                            unit[1][2] = Route002.y;
                            routeinfo = Route002_;
                            return unit;
                        } else if (routeinfo == Route002_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route003.x;
                            unit[0][2] = Route003.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route003.x;
                            unit[1][2] = Route003.y;
                            routeinfo = Route003_;
                        } else if (routeinfo == Route011_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route012.x;
                            unit[0][2] = Route012.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route012.x;
                            unit[1][2] = Route012.y;
                            routeinfo = Route012_;
                        } else if (routeinfo == Route012_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route013.x;
                            unit[0][2] = Route013.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route013.x;
                            unit[1][2] = Route013.y;
                            routeinfo = Route013_;
                        } else if (routeinfo == Route021_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route022.x;
                            unit[0][2] = Route022.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route022.x;
                            unit[1][2] = Route022.y;
                            routeinfo = Route022_;

                        } else if (routeinfo == Route022_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route023.x;
                            unit[0][2] = Route023.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route023.x;
                            unit[1][2] = Route023.y;
                            routeinfo = Route023_;
                        } else if (routeinfo == Route031_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route032.x;
                            unit[0][2] = Route032.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route032.x;
                            unit[1][2] = Route032.y;
                            routeinfo = Route032_;
                        } else if (routeinfo == Route032_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route033.x;
                            unit[0][2] = Route033.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route033.x;
                            unit[1][2] = Route033.y;
                            routeinfo = Route033_;
                        }
                    }
                        break;
                    case FIELD_1_TOWER_1_CAMP_0:
                        if(routeinfo == Route001_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route002.x;
                            unit[0][2] = Route002.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route002.x;
                            unit[1][2] = Route002.y;
                            routeinfo = Route002_;
                        }
                        else if(routeinfo == Route002_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route003.x;
                            unit[0][2] = Route003.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route003.x;
                            unit[1][2] = Route003.y;
                            routeinfo = Route003_;
                        }

                        else if(routeinfo == Route011_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route012.x;
                            unit[0][2] = Route012.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route012.x;
                            unit[1][2] = Route012.y;
                            routeinfo = Route012_;
                        }
                        else if(routeinfo == Route012_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route013.x;
                            unit[0][2] = Route013.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route013.x;
                            unit[1][2] = Route013.y;
                            routeinfo = Route013_;
                        }

                        else if(routeinfo == Route021_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route022.x;
                            unit[0][2] = Route022.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route022.x;
                            unit[1][2] = Route022.y;
                            routeinfo = Route022_;

                        }
                        else if(routeinfo == Route022_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route023.x;
                            unit[0][2] = Route023.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route023.x;
                            unit[1][2] = Route023.y;
                            routeinfo = Route023_;
                        }

                        else if(routeinfo == Route031_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route032.x;
                            unit[0][2] = Route032.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route032.x;
                            unit[1][2] = Route032.y;
                            routeinfo = Route032_;
                        }
                        else if(routeinfo == Route032_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route033.x;
                            unit[0][2] = Route033.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route033.x;
                            unit[1][2] = Route033.y;
                            routeinfo = Route033_;
                        }
                        break;

                    default:
                        break;
                }

            case 1:
                switch (STATE) {


                    default:
                        break;
                }
        }

        return unit;
    }
    public synchronized int[][]  UnitOrder1() {

        /** 0:コマの種類　1:X座標　2:Y座標 3:ルート**/
        int unit[][] = new int[2][4];

        switch (this.MyTeamID) {
            case 1:
                switch (STATE) {
                    case FIELD_0_TOWER_0_CAMP_2:
                    case FIRST_MYTURN1:{
                        Random rnd = new Random();
                        int rot = rnd.nextInt(4);
                        int uni = rnd.nextInt(2);
                        int[] y = new int [2];
                        y[0] = rot; y[1] = uni;
                        if(y[0] == 0) {
                            unit[0][0] = setupUnit(y[1])[0];
                            unit[0][1] = Route101.x;
                            unit[0][2] = Route101.y;
                            unit[1][0] = setupUnit(y[1])[1];
                            unit[1][1] = Route101.x;
                            unit[1][2] = Route101.y;
                            routeinfo = Route101_;
                            moveunit = y[1];
                            return unit;
                        }
                        else if(y[0] == 1) {
                            unit[0][0] = setupUnit(y[1])[0];
                            unit[0][1] = Route111.x;
                            unit[0][2] = Route111.y;
                            unit[1][0] = setupUnit(y[1])[1];
                            unit[1][1] = Route111.x;
                            unit[1][2] = Route111.y;
                            routeinfo = Route111_;
                            moveunit = y[1];
                            return unit;
                        }
                        else if(y[0] == 2) {
                            unit[0][0] = setupUnit(y[1])[0];
                            unit[0][1] = Route121.x;
                            unit[0][2] = Route121.y;
                            unit[1][0] = setupUnit(y[1])[1];
                            unit[1][1] = Route121.x;
                            unit[1][2] = Route121.y;
                            routeinfo = Route121_;
                            moveunit = y[1];
                            return unit;
                        }
                        else if(y[0] == 3) {
                            unit[0][0] = setupUnit(y[1])[0];
                            unit[0][1] = Route131.x;
                            unit[0][2] = Route131.y;
                            unit[1][0] = setupUnit(y[1])[1];
                            unit[1][1] = Route131.x;
                            unit[1][2] = Route131.y;
                            routeinfo = Route131_;
                            moveunit = y[1];
                            return unit;
                        }

                    }

                    break;
                    case FIELD_0_TOWER_1_CAMP_1: {
                        tower = sendtowerhold();
                        enableBaseUnit();

                        if(tower[1] != 1) {
                            if (base_unitpair == BLACK_YELLOW) {
                                Random rnd = new Random();
                                int uni = rnd.nextInt(2);
                                if(uni == 0) {
                                    unit[0][0] = setupUnit(base_unitpair)[0];
                                    unit[0][1] = Route111.x;
                                    unit[0][2] = Route111.y;
                                    unit[1][0] = setupUnit(base_unitpair)[1];
                                    unit[1][1] = Route111.x;
                                    unit[1][2] = Route111.y;
                                    routeinfo = Route111_;
                                    moveunit = base_unitpair;
                                    return unit;
                                }
                                if(uni == 1) {
                                    unit[0][0] = setupUnit(base_unitpair)[0];
                                    unit[0][1] = Route121.x;
                                    unit[0][2] = Route121.y;
                                    unit[1][0] = setupUnit(base_unitpair)[1];
                                    unit[1][1] = Route121.x;
                                    unit[1][2] = Route121.y;
                                    routeinfo = Route121_;
                                    moveunit = base_unitpair;
                                    return unit;
                                }
                            }
                            if (base_unitpair == RED_GREEN) {
                                Random rnd = new Random();
                                int uni = rnd.nextInt(2);
                                if(uni == 0) {
                                    unit[0][0] = setupUnit(base_unitpair)[0];
                                    unit[0][1] = Route111.x;
                                    unit[0][2] = Route111.y;
                                    unit[1][0] = setupUnit(base_unitpair)[1];
                                    unit[1][1] = Route111.x;
                                    unit[1][2] = Route111.y;
                                    routeinfo = Route111_;
                                    moveunit = base_unitpair;
                                    return unit;
                                }
                                if(uni == 1) {
                                    unit[0][0] = setupUnit(base_unitpair)[0];
                                    unit[0][1] = Route121.x;
                                    unit[0][2] = Route121.y;
                                    unit[1][0] = setupUnit(base_unitpair)[1];
                                    unit[1][1] = Route121.x;
                                    unit[1][2] = Route121.y;
                                    routeinfo = Route121_;
                                    moveunit = base_unitpair;
                                    return unit;
                                }

                            }
                        }
                        if(tower[0] != 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(base_unitpair)[0];
                                unit[0][1] = Route101.x;
                                unit[0][2] = Route101.y;
                                unit[1][0] = setupUnit(base_unitpair)[1];
                                unit[1][1] = Route101.x;
                                unit[1][2] = Route101.y;
                                routeinfo = Route101_;
                                moveunit = base_unitpair;
                                return unit;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(base_unitpair)[0];
                                unit[0][1] = Route101.x;
                                unit[0][2] = Route101.y;
                                unit[1][0] = setupUnit(base_unitpair)[1];
                                unit[1][1] = Route101.x;
                                unit[1][2] = Route101.y;
                                routeinfo = Route101_;
                                moveunit = base_unitpair;
                                return unit;
                            }
                        }

                        if(tower[2] != 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(base_unitpair)[0];
                                unit[0][1] = Route131.x;
                                unit[0][2] = Route131.y;
                                unit[1][0] = setupUnit(base_unitpair)[1];
                                unit[1][1] = Route131.x;
                                unit[1][2] = Route131.y;
                                routeinfo = Route131_;
                                moveunit = base_unitpair;
                                return unit;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(base_unitpair)[0];
                                unit[0][1] = Route131.x;
                                unit[0][2] = Route131.y;
                                unit[1][0] = setupUnit(base_unitpair)[1];
                                unit[1][1] = Route131.x;
                                unit[1][2] = Route131.y;
                                routeinfo = Route131_;
                                moveunit = base_unitpair;
                                return unit;
                            }
                        }

                    }
                    break;
                    case FIELD_0_TOWER_2_CAMP_0: {
                        tower = sendtowerhold();
                        enableTowerUnit();
                        if (tower[1] == 1) {
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route112.x;
                            unit[0][2] = Route112.y;
                            unit[1][0] = setupUnit(tower_unitpair)[0];
                            unit[1][1] = Route113.x;
                            unit[1][2] = Route113.y;
                            return unit;
                        }
                        else if (tower[0] == 1) {
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route102.x;
                            unit[0][2] = Route102.y;
                            unit[1][0] = setupUnit(tower_unitpair)[1];
                            unit[1][1] = Route103.x;
                            unit[1][2] = Route103.y;
                            return unit;
                        }
                        else if (tower[2] == 1) {
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route102.x;
                            unit[0][2] = Route102.y;
                            unit[1][0] = setupUnit(tower_unitpair)[1];
                            unit[1][1] = Route103.x;
                            unit[1][2] = Route103.y;
                            return unit;
                        }

                    }
                    break;
                    case FIELD_1_TOWER_0_CAMP_1: {
                        if (routeinfo == Route101_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route102.x;
                            unit[0][2] = Route102.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route102.x;
                            unit[1][2] = Route102.y;
                            routeinfo = Route102_;
                            return unit;
                        } else if (routeinfo == Route102_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route103.x;
                            unit[0][2] = Route103.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route103.x;
                            unit[1][2] = Route103.y;
                            routeinfo = Route103_;
                        } else if (routeinfo == Route111_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route112.x;
                            unit[0][2] = Route112.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route112.x;
                            unit[1][2] = Route112.y;
                            routeinfo = Route112_;
                        } else if (routeinfo == Route112_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route113.x;
                            unit[0][2] = Route113.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route113.x;
                            unit[1][2] = Route113.y;
                            routeinfo = Route113_;
                        } else if (routeinfo == Route121_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route122.x;
                            unit[0][2] = Route122.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route122.x;
                            unit[1][2] = Route122.y;
                            routeinfo = Route122_;

                        } else if (routeinfo == Route122_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route123.x;
                            unit[0][2] = Route123.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route123.x;
                            unit[1][2] = Route123.y;
                            routeinfo = Route123_;
                        } else if (routeinfo == Route131_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route132.x;
                            unit[0][2] = Route132.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route132.x;
                            unit[1][2] = Route132.y;
                            routeinfo = Route132_;
                        } else if (routeinfo == Route132_) {
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route133.x;
                            unit[0][2] = Route133.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route133.x;
                            unit[1][2] = Route133.y;
                            routeinfo = Route133_;
                        }
                    }
                    break;
                    case FIELD_1_TOWER_1_CAMP_0:
                        if(routeinfo == Route101_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route102.x;
                            unit[0][2] = Route102.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route102.x;
                            unit[1][2] = Route102.y;
                            routeinfo = Route102_;
                        }
                        else if(routeinfo == Route102_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route103.x;
                            unit[0][2] = Route103.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route103.x;
                            unit[1][2] = Route103.y;
                            routeinfo = Route103_;
                        }

                        else if(routeinfo == Route111_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route112.x;
                            unit[0][2] = Route112.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route112.x;
                            unit[1][2] = Route112.y;
                            routeinfo = Route112_;
                        }
                        else if(routeinfo == Route112_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route113.x;
                            unit[0][2] = Route113.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route113.x;
                            unit[1][2] = Route113.y;
                            routeinfo = Route113_;
                        }

                        else if(routeinfo == Route121_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route122.x;
                            unit[0][2] = Route122.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route122.x;
                            unit[1][2] = Route122.y;
                            routeinfo = Route122_;

                        }
                        else if(routeinfo == Route122_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route123.x;
                            unit[0][2] = Route123.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route123.x;
                            unit[1][2] = Route123.y;
                            routeinfo = Route123_;
                        }

                        else if(routeinfo == Route131_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route132.x;
                            unit[0][2] = Route132.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route132.x;
                            unit[1][2] = Route132.y;
                            routeinfo = Route132_;
                        }
                        else if(routeinfo == Route132_){
                            unit[0][0] = setupUnit(moveunit)[0];
                            unit[0][1] = Route133.x;
                            unit[0][2] = Route133.y;
                            unit[1][0] = setupUnit(moveunit)[1];
                            unit[1][1] = Route133.x;
                            unit[1][2] = Route133.y;
                            routeinfo = Route133_;
                        }
                        break;

                    default:
                        break;
                }

            case 0:
                switch (STATE) {


                    default:
                        break;
                }
        }

        return unit;
    }
    /**
     * 初期化
     */

    //unit[0] = base_count; unit[1] = tower_count; unit[2] = field_count;
    public synchronized int[] init() {
        int[] x = new int[2];
        if (this.ternCount == FIRST_TURN) {
            STATE = FIRST_MYTURN1;

        }
        else {
            int[] unit = new int[3];
            unit = search_pos_count();

            System.out.println(unit[0]+" "+unit[1]+" "+unit[2]);
            System.out.println("Set STATE");
            if (unit[0] == 4){
                STATE = FIELD_0_TOWER_0_CAMP_2;
                System.out.println("FIELD_0_TOWER_0_CAMP_2");
           }
            else if (unit[0] == 2 && unit[1] == 2){
                STATE = FIELD_0_TOWER_1_CAMP_1;
                System.out.println("FIELD_0_TOWER_1_CAMP_1");
            }
            else if (unit[1] == 4 ){
                STATE = FIELD_0_TOWER_2_CAMP_0;
                System.out.println("FIELD_0_TOWER_2_CAMP_0");
            }
            else if (unit[0]== 2 && unit[2] == 2 ){
                STATE = FIELD_1_TOWER_0_CAMP_1;
                System.out.println("FIELD_1_TOWER_0_CAMP_1");
            }
            else if (unit[1]== 2 && unit[2] == 2 ){
                STATE = FIELD_1_TOWER_1_CAMP_0;
                System.out.println("FIELD_1_TOWER_1_CAMP_0");
            }

        }
        return x;
    }


    //0:
    public synchronized int[] setupUnit(int type) {
        int pair[] = new int[2];

        if (type == 0) {
            pair[0] = 0;
            pair[1] = 2;
        } else if (type == 1) {
            pair[0] = 1;
            pair[1] = 3;
        } else {
            System.out.println("ERROR setUnit");
            pair[0] = -1;
            pair[1] = -1;
        }
        return pair;
    }

    public  int[] search_pos_count() {
        System.out.println("search_pos_count");
        int field_count = 4;
        int base_count = 0;
        int tower_count = 0;
        int[] unit1 = new int[3];
        if (this.MyTeamID == 0) {
            for (int i = 0; i < 4; i++) {
                if (this.unitLocation[this.MyTeamID][i].equals(Base0)) {
                    base_count++;
                }
                if (this.unitLocation[this.MyTeamID][i].equals(tower_left)) {
                    tower_count++;
                }
                if (this.unitLocation[this.MyTeamID][i].equals(tower_right)) {
                    tower_count++;
                }
                if (this.unitLocation[this.MyTeamID][i].equals(tower_center)) {
                    tower_count++;
                }
            }

            unit1[0] = base_count;
            unit1[1] = tower_count;
            unit1[2] = field_count - base_count - tower_count;
            return unit1;
        }
        if (this.MyTeamID == 1) {
            for (int i = 0; i < 4; i++) {
                if (this.unitLocation[this.MyTeamID][i].equals(Base1)) {
                    base_count++;
                }
                if (this.unitLocation[this.MyTeamID][i].equals(tower_left)) {
                    tower_count++;
                }
                if (this.unitLocation[this.MyTeamID][i].equals(tower_right)) {
                    tower_count++;
                }
                if (this.unitLocation[this.MyTeamID][i].equals(tower_center)) {
                    tower_count++;
                }
            }

            unit1[0] = base_count;
            unit1[1] = tower_count;
            unit1[2] = field_count - base_count - tower_count;
            return unit1;
        }
        return unit1;
    }
    public synchronized  void enableBaseUnit(){
        if (this.MyTeamID == 0) {
            for (int i = 0; i < 4; i++) {
                if (this.unitLocation[this.MyTeamID][i].equals(Base0)){
                    if (i == 0||i == 2){
                        base_unitpair = RED_GREEN;
                    }
                    if (i == 1 || i == 3){
                        base_unitpair = BLACK_YELLOW;
                    }
                }
            }
        } else if (this.MyTeamID == 1) {
            for (int i = 0; i < 4; i++) {
                if (this.unitLocation[this.MyTeamID][i].equals(Base1)){
                    if (i == 0||i == 2){
                        base_unitpair = RED_GREEN;
                    }
                    if (i == 1 || i == 3){
                        base_unitpair = BLACK_YELLOW;
                    }
                }
            }
        }
    }

    public synchronized  void enableTowerUnit() {
        if (this.MyTeamID == 0) {
            for (int i = 0; i < 4; i++) {
                if (this.unitLocation[this.MyTeamID][i].equals(Tower2)) {
                    if (i == 0 || i == 2) {
                        tower_unitpair = RED_GREEN;
                    }
                    if (i == 1 || i == 3) {
                        tower_unitpair = BLACK_YELLOW;
                    }
                }

                if (this.unitLocation[this.MyTeamID][i].equals(Tower0)) {
                    if (i == 0 || i == 2) {
                        tower_unitpair = RED_GREEN;
                    }
                    if (i == 1 || i == 3) {
                        tower_unitpair = BLACK_YELLOW;
                    }
                }

                if (this.unitLocation[this.MyTeamID][i].equals(Tower1)) {
                    if (i == 0 || i == 2) {
                        tower_unitpair = RED_GREEN;
                    }
                    if (i == 1 || i == 3) {
                        tower_unitpair = BLACK_YELLOW;
                    }
                }
            }
        } else if (this.MyTeamID == 1) {
            for (int i = 0; i < 4; i++) {
                if (this.unitLocation[this.MyTeamID][i].equals(Tower2)) {
                    if (i == 0 || i == 2) {
                        tower_unitpair = RED_GREEN;
                    }
                    if (i == 1 || i == 3) {
                        tower_unitpair = BLACK_YELLOW;
                    }
                }

                if (this.unitLocation[this.MyTeamID][i].equals(Tower0)) {
                    if (i == 0 || i == 2) {
                        tower_unitpair = RED_GREEN;
                    }
                    if (i == 1 || i == 3) {
                        tower_unitpair = BLACK_YELLOW;
                    }
                }

                if (this.unitLocation[this.MyTeamID][i].equals(Tower1)) {
                    if (i == 0 || i == 2) {
                        tower_unitpair = RED_GREEN;
                    }
                    if (i == 1 || i == 3) {
                        tower_unitpair = BLACK_YELLOW;
                    }
                }
            }
        }
    }

    public synchronized int[] sendtowerhold(){
         int[]towers= new int[3];
        if(this.towerHold[0] == this.MyTeamID){
            towers[0] = 1;
        }
        if(this.towerHold[1] == this.MyTeamID){
            towers[1] = 1;
        }
        if(this.towerHold[2] == this.MyTeamID){
            towers[2] = 1;
        }
        return towers;
    }

    public  synchronized int Send_Routeinfo(){
        return this.routeinfo;
    }




}
