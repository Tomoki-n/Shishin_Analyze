package com.company;

import java.awt.*;

/**
 * Created by tomoki-n on 2015/04/16.
 */
public class Controller_Demo  {

    Boardinfo info;

    public static final Point tower_l = new Point(1, 4);
    public static final Point tower_c = new Point(4, 4);
    public static final Point tower_r = new Point(7, 4);
    public static final Point tower[] = {tower_l, tower_c, tower_r};

    Controller_Demo(Boardinfo Binfo){
        info = Binfo;

    }

    private int[] move_demo01() {
        // codename: speedrunner

        //送信するコード
        int unit[] = new int[3];

        //pos[i][j] = i番目の塔からjだけ離れた場所 一直線にしか進まないので
        Point pos[][] = new Point[3][4];
        //各塔に到着したかを示す
        int arrived_t[] = new int[3];
        int arrived_p[] = new int[4];

        pos[0][0] = tower_l;
        pos[1][0] = tower_c;
        pos[2][0] = tower_r;

        switch (info.MyTeamID) {
            case 0:
                //先手
                pos[0][1] = new Point(2,5);
                pos[1][1] = new Point(4,5);
                pos[2][1] = new Point(6,5);
                pos[0][1] = new Point(3,6);
                pos[1][1] = new Point(4,6);
                pos[2][1] = new Point(5,6);
                pos[0][3] = pos[1][3] = pos[2][3] = info.Base0;
                break;
            case 1:
                //後手
                pos[0][1] = new Point(2,3);
                pos[1][1] = new Point(4,3);
                pos[2][1] = new Point(6,3);
                pos[0][1] = new Point(3,2);
                pos[1][1] = new Point(4,2);
                pos[2][1] = new Point(5,2);
                pos[0][3] = pos[1][3] = pos[2][3] = info.Base1;
                break;
            default:
                //どうせ起こりえないから先手のやつをコピペ
                pos[0][1] = new Point(2,5);
                pos[1][1] = new Point(4,5);
                pos[2][1] = new Point(6,5);
                pos[0][1] = new Point(3,6);
                pos[1][1] = new Point(4,6);
                pos[2][1] = new Point(5,6);
                pos[0][3] = pos[1][3] = pos[2][3] = info.Base0;
                break;
        }

        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 3; j++) {
                if(info.unitLocation[info.MyTeamID][i].equals(pos[j][0])) {
                    arrived_t[j] = i;
                    arrived_p[i] = j;
                } else if(info.unitLocation[info.MyTeamID][i].equals(pos[j][1])) {
                    unit[0] = i;
                    unit[1] = pos[j][0].x;
                    unit[2] = pos[j][0].y;
                    return unit;
                } else if(info.unitLocation[info.MyTeamID][i].equals(pos[j][2])) {
                    unit[0] = i;
                    unit[1] = pos[j][1].x;
                    unit[2] = pos[j][1].y;
                    return unit;
                } else {
                    arrived_p[i] = 0;
                }
            }
        }

        //1・2位置に駒がいない場合
        if(arrived_t[0] == 0 || arrived_t[1] == 0 || arrived_t[2] == 0) {
            //まだ陣取りされていない柱がある場合突進
            while(true) {
                //どの駒を進めるかはランダム(柱に着いていない駒限定で)
                int i = (int)(Math.random() * 4.0 + 1.0);
                if(arrived_p[i] == 0) {
                    unit[0] = i;
                    while(true) {
                        //どの柱に動かすかもランダム
                        int j = (int)(Math.random() * 3.0 + 1.0);
                        if(arrived_t[j] == 0) {
                            unit[1] = pos[j][2].x;
                            unit[2] = pos[j][2].x;
                            return unit;
                        }
                    }
                }
            }
        }


        //全ての柱に神がいる場合
        for(int i = 0; i < 4; i++) {
            if(arrived_p[i] == 0) {
                unit[0] = i;
                int j = (int)(Math.random() * 3.0 + 1.0);
                unit[1] = pos[j][2].x;
                unit[2] = pos[j][2].y;
            }
        }
        return unit;
    }










    /**  コマの移動命令 **/
    public int[] UnitOrder(){
        int unit[] = this.move_demo01();

        /** 0:コマの種類　1:X座標　2:Y座標 **/

        return unit;
    }

}
