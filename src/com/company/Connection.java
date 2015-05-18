package com.company;

import com.company.AI.AI;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tomoki-n on 4/9/15.
 */


public class Connection implements Runnable{

    public final int STATE_NOCONECTION = 00;
    public final int STATE_INIT = 01;
    public final int STATE_GAME = 10;
    public final int STATE_VIEW_GETBOARD = 11;
    public final int STATE_PLAY = 12;
    public final int STATE_PLAY_GETBOARD = 13;
    public final int STATE_FINISH = 20;
    public final int STATE_FINISH_GETBOARD = 21;

    private Socket connectedSocket;
    private BufferedReader reader;
    private PrintWriter writer;

    public int state;
    private String myName;
    private String address;
    private AI mainField;
    public boolean check =false;

    private HttpConnection hp;


    private int PlayerID;

    public Connection(String name,AI main) throws IOException {
        this.mainField = main;
        this.myName = name;
        this.hp = new HttpConnection();
        state = STATE_NOCONECTION;
    }

    public boolean connectToServer(String ip,int port) throws UnknownHostException, IOException {
        this.connectedSocket = new Socket(ip,port);
        if(this.connectedSocket.isConnected()){
            this.reader = new BufferedReader(new InputStreamReader(this.connectedSocket.getInputStream(),"UTF-8"));
            this.writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.connectedSocket.getOutputStream(),"UTF-8")));
            Thread msgwait = new Thread(this);
            msgwait.start();
            System.out.println("Connect to Server");
            this.state = STATE_INIT;
            return true;
        } else {
            return false;
        }
    }
    /** サーバへ名前を渡す **/
    public void sendName(){
        if(this.state != STATE_INIT){
            System.out.println("NO STATE_INIT");
            return;
        }
        this.sendMessage("101 NAME "+ this.myName);
        System.out.println("Send Name");
    }

    /** サーバへの再接続　**/
    public void reconnect(){
        if(this.state != STATE_INIT){
            return;
        }
        this.sendMessage("103 RECONECT");
    }


    /** サーバへのメッセージ送信 */
    public synchronized  void sendMessage(String message){
        if(this.writer != null){
            this.writer.println(message);
            this.writer.flush();
        }
    }


    /** サーバにユニットの行動を送信 **/
    public synchronized void sendPlayMessage(int selectedUnit, int x, int y) {
        //打った手を一時的に保存
        this.mainField.prevMove[0] = selectedUnit;
        this.mainField.prevMove[1] = x;
        this.mainField.prevMove[2] = y;

        //ユニットの行動をサーバ側に送信
        StringBuilder sbuf = new StringBuilder();
        sbuf.append("405 PLAY ");
        sbuf.append(selectedUnit);
        sbuf.append(" ");
        sbuf.append(x);
        sbuf.append(" ");
        sbuf.append(y);
        System.out.println(sbuf.toString());
        this.sendMessage(sbuf.toString());

    }
    private Pattern MSGPTN = Pattern.compile("([0-9]+) (.*)");
    private Pattern TEAMIDMSGPTN = Pattern.compile("102 TEAMID ([0-1])");
    private Pattern ADVERSARYMSGPTN = Pattern.compile("104 ADVERSARY (.*)");
    private Pattern GAMEENDMSGPTN = Pattern.compile("502 GAMEEND ([0-1])");
    private ArrayList<String> boardInfo;
    private int winner = -1;

    /** クライアントからのメッセ―ジ到着 */
    public synchronized void getMessage(String message) throws InterruptedException, IOException {
        this.mainField.addMessage("Server:" + message);

        //終了処理
        if(message.toUpperCase().equals("203 EXIT")){
            sendMessage("200 OK");
            try {
                this.connectedSocket.close();
            } catch (IOException ex) {
                System.out.println("サーバ切断時にエラーが発生しました");
                this.mainField.resetAll();
            }
            return;
        }

        //メッセージ解析
        Matcher mc = MSGPTN.matcher(message);
        if(mc.matches()){
            int num = Integer.parseInt(mc.group(1));
            //GAMEENDメッセージは重要度が高い
            if(num == 502){
                //502 GAMEEND [T]
                System.out.println("ゲーム終了");
                Matcher gnd = GAMEENDMSGPTN.matcher(message);
                if(gnd.matches()){
                    this.winner = Integer.parseInt(gnd.group(1));
                    this.state = STATE_FINISH_GETBOARD;
                    this.boardInfo = new ArrayList<String>();
                    sendMessage("400 GETBORD");
                }

            }

            if(this.state == STATE_INIT){
                if(num == 102){
                    //自分のサーバへの接続が完了
                    Matcher nmc = TEAMIDMSGPTN.matcher(message);
                    if(nmc.matches()){
                        this.PlayerID = Integer.parseInt(nmc.group(1));
                        this.mainField.setMyTeamID(this.PlayerID);
                    }
                }
                if(num == 104){
                    //相手ユーザの接続が完了
                    Matcher nmc = ADVERSARYMSGPTN.matcher(message);
                    if(nmc.matches()){
                        String advName = nmc.group(1);
                        this.mainField.adversHasCome(advName);
                        if(this.mainField.analyze.equals("0"))hp.GetGameId(this.myName,advName);
                        this.state = STATE_GAME;
                    }
                    if(this.PlayerID == 1){
                        //後攻から始まる場合
                        System.out.println("相手の手を待っています。");
                    }
                }
            } else if(this.state == STATE_GAME){
                if(num == 404){
                    //プレイ要求
                    this.state = STATE_PLAY;
                    //ボード状態の取得
                    this.mainField.setPlayingTeamID(this.PlayerID);
                    System.out.println("貴方の手番です。");
                    this.check = true;
                    this.state = STATE_PLAY_GETBOARD;
                    this.boardInfo = new ArrayList<String>();
                    sendMessage("400 GETBORD");
                } else if(num == 500){
                    //500 PLAYED
                    System.out.println("相手が１手打ちました。");
                    this.mainField.doPlay();
                    if(this.mainField.whoIsPlay() == -1){
                        //ターンが終わっていた場合は何もしない
                    } else if(this.PlayerID != this.mainField.whoIsPlay()){
                        //相手のターンだった場合
                        this.mainField.addMessage("相手の手を待っています。");
                        this.state = STATE_VIEW_GETBOARD;
                        this.boardInfo = new ArrayList<String>();
                        sendMessage("400 GETBORD");
                    }
                } else if(num == 501){
                    //501 PHASEEND
                    this.mainField.addMessage("ターン終了");
                    if(this.mainField.isFirstPlayer(this.PlayerID)){
                        //先攻でターンが終わった場合は次は待つことになる。
                        this.mainField.changeFirstTeam();
                        this.mainField.resetTurnState();
                        if(this.mainField.analyze.equals("0"))hp.SendScore(hp.Game_id,this.mainField.GetTurnCount(),this.mainField.GetTeamPoint()[0],this.mainField.GetTeamPoint()[1]);
                        this.mainField.setPlayingTeamID((this.PlayerID+1)%2);
                        this.mainField.addMessage("相手の手を待っています。");
                        this.boardInfo = new ArrayList<String>();
                        this.state = STATE_VIEW_GETBOARD;
                        sendMessage("400 GETBORD");
                    } else {
                        //後攻でターンが終わった場合はすぐに404が来るのでボードは読み直さない
                        this.mainField.changeFirstTeam();
                        this.mainField.resetTurnState();
                        if(this.mainField.analyze.equals("0"))hp.SendScore(hp.Game_id,this.mainField.GetTurnCount(),this.mainField.GetTeamPoint()[0],this.mainField.GetTeamPoint()[1]);
                    }
                } else if(num == 600){
                    //600 MSG
                    String chat = mc.group(2);
                    this.mainField.addMessage(chat);
                }
            } else if(this.state == STATE_PLAY){
                if(num == 200){
                    //プレイ結果がOKだった場合
                    this.state = STATE_GAME;
                    this.mainField.doPlay();

                    //ボード状態の取得
                    if(this.mainField.whoIsPlay() == -1){
                        //ターンが終わっていた場合は何もしない
                    } else if(this.PlayerID != this.mainField.whoIsPlay()){
                        //相手のターンだった場合
                        this.mainField.setPlayingTeamID(this.mainField.whoIsPlay());
                        this.mainField.addMessage("相手の手を待っています。");
                        this.state = STATE_VIEW_GETBOARD;
                        this.boardInfo = new ArrayList<String>();
                        sendMessage("400 GETBORD");
                    }
                } else if(num == 600){
                    //600 MSG
                    String chat = mc.group(2);
                    this.mainField.addMessage(chat);
                } else {
                    //不可能な手
                    this.mainField.addMessage("その位置には移動できません。");
                    this.state = STATE_GAME;
                }
            } else if(this.state == STATE_PLAY_GETBOARD || this.state == STATE_VIEW_GETBOARD || this.state == STATE_FINISH_GETBOARD ){
                if(num == 401){
                    //ユニット位置
                    this.boardInfo.add(message);
                } else if(num == 406){
                    //障害物状態
                    this.boardInfo.add(message);
                } else if(num == 402){
                    //タワー保持状態
                    this.boardInfo.add(message);
                } else if(num == 403){
                    //スコア
                    this.boardInfo.add(message);
                } else if(num == 202){
                    //行の終了
                    this.mainField.setBordState(this.boardInfo);
                    if(this.state == STATE_PLAY_GETBOARD){
                        this.state = STATE_PLAY;
                        if(check) {
                            this.mainField.addMessage("Select Unit");
                            this.check = false;
                        }
                    } else if(this.state == STATE_VIEW_GETBOARD){
                        this.state = STATE_GAME;
                    } else {

                        //試合終了時のスコアをDBに送信
                        if(this.mainField.analyze.equals("0")) {
                            hp.SendEndScore(hp.Game_id,this.mainField.GetTurnCount(),this.mainField.GetTeamPoint()[0],this.mainField.GetTeamPoint()[1]);
                        }

                        if(winner == -1){
                            System.out.println("ゲーム終了");
                            this.mainField.addMessage("引き分けでした。");
                        } else if(this.PlayerID == winner){
                            System.out.println("ゲーム終了");
                            this.mainField.addMessage("あなたの勝利です！");
                        } else {
                            System.out.println("ゲーム終了");
                            this.mainField.addMessage("あなたの敗北です！");
                        }
                        this.state = STATE_FINISH;
                        System.out.println("ゲーム終了");
                        this.mainField.addMessage("メニューからリセットしてください。");

                    }
                } else if(num == 600){
                    //600 MSG
                    String chat = mc.group(2);
                    this.mainField.addMessage(chat);
                }
            } else if(this.state == STATE_FINISH){
                if(num == 600){
                    //600 MSG
                    String chat = mc.group(2);
                    this.mainField.addMessage(chat);
                } else {
                    this.sendMessage("300 MESSAGE SYNTAX ERROR");
                }
            }
        } // end of message matcing
    }

    /** コンソールからIPアドレスを取得 **/
    public String init_address(){
        String address =null;

        System.out.print("input IP Address: ");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        try{
            this.address = br.readLine();

        }catch(Exception e){
            init_address();
        }

        System.out.println(address);
        return address;
    }

    @Override
    public void run() {
        String mssage;
        System.out.println("Call Run Method");
        try {
            while((mssage = this.reader.readLine())!= null){
                this.getMessage(mssage);
            }
            this.mainField.addMessage("サーバとの接続が切断しました");
            this.state = STATE_INIT;
            this.mainField.resetAll();
        } catch (IOException ex) {
            try {
                this.mainField.addMessage("サーバとの接続が切断しました");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.state = STATE_INIT;
            try {
                this.mainField.resetAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }




}
