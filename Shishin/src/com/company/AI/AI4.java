package com.company.AI;


import com.company.AI.AI;

import java.awt.Point;
import java.io.IOException;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class AI4 extends AI {

    /**
     * Creates new form GameField
     */
    public AI4(String address, String type, String stype) throws InterruptedException, IOException {
        super(address, type, stype, "FoolhardinessII");
    }

    /**
     * ユーザへのメッセージ表示
     */
    @Override
    public void addMessage(String msg) throws InterruptedException {

        if (msg == "Select Unit") {
            int unit[] = this.move_demo02();
            this.sthread.sendPlayMessage(unit[0], unit[1], unit[2]);
        }
        System.out.println(msg);
    }

    /**
     * 実質的なAI部分
     * @return 駒の動きを示す配列。[0]が駒のID、[1]が移動先のX座標、[2]が移動先のY座標を示す
     */
    private synchronized int[] move_demo02() {
        // codename: FoolhardinessII (蛮勇II)

        //送信するコード
        int unit[] = new int[3];

        //pos[i][j] = i番目の塔からjだけ離れた場所 ただしpos[3][]はバックルート
        Point pos[][] = new Point[4][4];
        //各塔に到着したかを示す([3]はダミー)
        int arrived_t[] = new int[4];
        //各駒が到着したかを示す
        int arrived_p[] = new int[4];

        //移動経路の設定
        pos[0][0] = tower_left;
        pos[1][0] = tower_center;
        pos[2][0] = tower_right;

        switch (this.MyTeamID) {
            case 0:
                //先手
                pos[0][1] = new Point(2,5);
                pos[1][1] = new Point(4,5);
                pos[2][1] = new Point(6,5);
                pos[3][1] = new Point(4,9);
                pos[0][2] = new Point(3,6);
                pos[1][2] = new Point(4,6);
                pos[2][2] = new Point(5,6);
                pos[3][2] = new Point(4,8);
                pos[0][3] = pos[1][3] = pos[2][3] = pos[3][3] = this.Base0;

                switch (this.boardType) {
                    case BOARD_TYPE_A:
                    case BOARD_TYPE_B:
                    case BOARD_TYPE_AB:
                        pos[3][0] = tower_left;
                        break;
                    case BOARD_TYPE_C:
                    case BOARD_TYPE_D:
                    case BOARD_TYPE_CD:
                        pos[3][0] = tower_right;
                        break;
                    default:
                        //ダミーデータ
                        pos[3][0] = new Point(99,99);
                }
                break;
            case 1:
                //後手
                pos[0][1] = new Point(2,3);
                pos[1][1] = new Point(4,3);
                pos[2][1] = new Point(6,3);
                pos[3][1] = new Point(4,-1);
                pos[0][2] = new Point(3,2);
                pos[1][2] = new Point(4,2);
                pos[2][2] = new Point(5,2);
                pos[3][2] = new Point(4,0);
                pos[0][3] = pos[1][3] = pos[2][3] = pos[3][3] = this.Base1;

                switch (this.boardType) {
                    case BOARD_TYPE_A:
                    case BOARD_TYPE_B:
                    case BOARD_TYPE_AB:
                        pos[3][0] = tower_right;
                        break;
                    case BOARD_TYPE_C:
                    case BOARD_TYPE_D:
                    case BOARD_TYPE_CD:
                        pos[3][0] = tower_left;
                        break;
                    default:
                        pos[3][0] = new Point(99,99);
                }
                break;
            default:
                //どうせ起こりえないから先手のやつをコピペ
                pos[0][1] = new Point(2,5);
                pos[1][1] = new Point(4,5);
                pos[2][1] = new Point(6,5);
                pos[3][1] = new Point(4,9);
                pos[0][2] = new Point(3,6);
                pos[1][2] = new Point(4,6);
                pos[2][2] = new Point(5,6);
                pos[3][2] = new Point(4,8);
                pos[0][3] = pos[1][3] = pos[2][3] = pos[3][3] = this.Base0;

                switch (this.boardType) {
                    case BOARD_TYPE_A:
                    case BOARD_TYPE_B:
                    case BOARD_TYPE_AB:
                        pos[3][0] = tower_left;
                        break;
                    case BOARD_TYPE_C:
                    case BOARD_TYPE_D:
                    case BOARD_TYPE_CD:
                        pos[3][0] = tower_right;
                        break;
                    default:
                        pos[3][0] = new Point(99,99);
                }
                break;
        }

        //盤面の現状を取得
        for(int i = 0; i < 4; i++) {
            arrived_p[i] = -1;
            arrived_t[i] = -1;
        }
        for(int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (this.unitLocation[this.MyTeamID][i].equals(pos[j][0])) {
                    //既に駒が柱に到着している場合
                    if(j != 3) {
                        arrived_t[j] = i;
                        arrived_p[i] = j;
                    }
                } else if (this.unitLocation[this.MyTeamID][i].equals(pos[j][1])) {
                    //柱に向かって移動する駒がすでにある場合(柱までの距離1)、指し手決定
                    unit[0] = i;
                    unit[1] = pos[j][0].x;
                    unit[2] = pos[j][0].y;
                    return unit;
                } else if (this.unitLocation[this.MyTeamID][i].equals(pos[j][2])) {
                    //柱に向かって移動する駒がすでにある場合(柱までの距離2)、指し手決定
                    unit[0] = i;
                    unit[1] = pos[j][1].x;
                    unit[2] = pos[j][1].y;
                    return unit;
                }
            }

            if(arrived_p[i] == -1) {
                if((pos[3][0] == tower_left && this.unitLocation[this.MyTeamID][i].equals(new Point(0,4))) || (pos[3][0] == tower_right && this.unitLocation[this.MyTeamID][i].equals(new Point(8,4)))) {
                    unit[0] = i;
                    unit[1] = pos[3][0].x;
                    unit[2] = pos[3][0].y;
                    return unit;
                }
            }
        }

        //1・2位置に駒がいない場合
        if(arrived_t[0] == -1 || arrived_t[1] == -1 || arrived_t[2] == -1) {
            //まだ陣取りされていない柱がある場合突進
            while(true) {
                //どの駒を進めるかはランダム(柱に着いていない駒限定で)
                int i = (int)(Math.random() * 4.0);
                if(arrived_p[i] == -1) {
                    unit[0] = i;
                    while(true) {
                        //どの柱に動かすかもランダム
                        if(boardType == BOARD_TYPE_UNDEFINED) {
                            //バック後の行き先がわからない場合
                            int j = (int) (Math.random() * 4.0);
                            if(arrived_t[j] == -1) {
                                unit[1] = pos[j][2].x;
                                unit[2] = pos[j][2].y;
                                return unit;
                            }
                        } else {
                            //バック後の行き先がわかる場合
                            int j = (int) (Math.random() * 3.0);
                            if (arrived_t[j] == -1) {
                                if(pos[j][0].equals(pos[3][0]) && Math.random() > 0.5) {
                                    j = 3;
                                }
                                unit[1] = pos[j][2].x;
                                unit[2] = pos[j][2].y;
                                return unit;
                            }
                        }
                    }
                }
            }
        }


        //全ての柱に駒がおり、残り一つが基地にいる場合
        for(int i = 0; i < 4; i++) {
            if(arrived_p[i] == -1) {
                unit[0] = i;
                int j = (int)(Math.random() * 4.0);
                unit[1] = pos[j][2].x;
                unit[2] = pos[j][2].y;
                return unit;
            }
        }

        //全ての駒がいずれかの柱にいる場合
        int arrived_cnt[] = new int[4]; //柱にいる駒の数
        for(int i = 0; i < 4; i++) {
            arrived_cnt[i] = 0;
        }
        for(int i = 0; i < 4; i++) {
            arrived_cnt[arrived_p[i]]++;
        }
        while (true) {
            int i = (int)(Math.random() * 4.0);
            if(arrived_cnt[arrived_p[i]] >= 2) {
                unit[0] = i;
                unit[1] = pos[arrived_p[i]][1].x;
                unit[2] = pos[arrived_p[i]][1].y;
                return unit;
            }
        }
    }
}
