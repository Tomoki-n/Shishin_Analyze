package com.company;

import java.awt.*;

/**
 * Created by tomoki-n on 2015/04/16.
 */
public class Controller_Nishinaka {


    public Boardinfo info;
    public Panel[][] panel;

    /**
     * 手の種類
     */
    public int STATE;

    public static final int FIRST_TURN = 0;
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
    public static final Point Towor0 = new Point(1, 4);
    public static final Point Towor1 = new Point(4, 4);
    public static final Point Towor2 = new Point(7, 4);


    //上側：本陣位置
    public static final Point Base0 = new Point(4, 1);

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


    //下側：本陣位置
    public static final Point Base1 = new Point(4, 7);

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
    public static final Point Route132 = new Point(6, 2);
    public static final Point Route133 = new Point(7, 4);


    public int field_count = 0;
    public int base_count = 0;
    public int tower_count = 0;

    public int[] unit;
    public int[] tower;

    public static final int RED_GREEN    = 100;
    public static final int BLACK_YELLOW = 101;

    public int base_unitpair;


    /**
     * コンストラクタ
     */
    Controller_Nishinaka(Boardinfo Binfo) {
        info = Binfo;
        init();

    }

    //1.2つ同時に動く
    //2.敵を無視する

    /**
     * コマの移動命令 *
     */
    public int[][] UnitOrder() {

        /** 0:コマの種類　1:X座標　2:Y座標 3:ルート**/
        int unit[][] = new int[2][4];

        switch (info.MyTeamID) {
            case 0:
                switch (STATE) {
                    case FIELD_0_TOWER_0_CAMP_2:{
                        unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                        unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                        break;
                    }
                    case FIELD_0_TOWER_1_CAMP_1: {
                        sendtowerhold();
                        enableBaseUnit();
                        if(tower[0] == 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                        }
                        if(tower[1] == 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                        }
                        if(tower[2] == 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                        }
                        break;
                    }
                    case FIELD_0_TOWER_2_CAMP_0: {
                        if(tower[0] == 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                        }
                        if(tower[1] == 1){
                            if (base_unitpair == BLACK_YELLOW) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }
                            if (base_unitpair == RED_GREEN) {
                                unit[0][0] = setupUnit(1)[0]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                unit[0][0] = setupUnit(1)[1]; unit[0][1] = Route001.x; unit[0][2] = Route001.y;
                                break;
                            }

                        }
                        break;
                    }

                    case FIELD_1_TOWER_0_CAMP_1:


                    case FIELD_1_TOWER_1_CAMP_0:


                    case FIELD_2_TOWER_0_CAMP_0:

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

    /**
     * 初期化
     */

    //unit[0] = base_count; unit[1] = tower_count; unit[2] = field_count;
    public void init() {
        if (info.turnCount == FIRST_TURN) {
            if (info.turnState == STATE_PLAY_TURN1) STATE = FIRST_MYTURN1;
            if (info.turnState == STATE_PLAY_TURN2) STATE = FIRST_MYTURN1;
            if (info.turnState == STATE_PLAY_TURN3) STATE = FIRST_MYTURN2;
            if (info.turnState == STATE_PLAY_TURN4) STATE = FIRST_MYTURN2;
        } else if (info.turnCount != FIRST_TURN) {
            unit = search_pos_count();

            if (unit[0] == 4){
                STATE = FIELD_0_TOWER_0_CAMP_2;
            }
            if (unit[0] == 2 && unit[1] == 2){
                STATE = FIELD_0_TOWER_1_CAMP_1;
            }
            if (unit[1] == 4 ){
                STATE = FIELD_0_TOWER_2_CAMP_0;
            }
            if (unit[0]== 2 && unit[2] == 2 ){
                STATE = FIELD_1_TOWER_0_CAMP_1;
            }
            if (unit[1]== 2 && unit[2] == 2 ){
                STATE = FIELD_1_TOWER_1_CAMP_0;
            }
            if (unit[2] == 4 ){
                STATE = FIELD_2_TOWER_0_CAMP_0;
            }
        }
    }


    //0:
    public int[] setupUnit(int type) {
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

    public int[] search_pos_count() {

        base_count = 0; field_count = 0; tower_count = 0;
        int[] unit = new int[3];

        if (info.MyTeamID == 0) {
            for (int i = 0; i < 4; i++) {
                if (info.unitLocation[info.MyTeamID][i] == Base0) base_count++;
                if (info.unitLocation[info.MyTeamID][i] == Towor0) tower_count++;
                if (info.unitLocation[info.MyTeamID][i] == Towor1) tower_count++;
                if (info.unitLocation[info.MyTeamID][i] == Towor2) tower_count++;
            }
            field_count = 4 - base_count - tower_count;
            unit[0] = base_count; unit[1] = tower_count; unit[2] = field_count;
        } else if (info.MyTeamID == 1) {
            for (int i = 0; i < 4; i++) {
                if (info.unitLocation[info.MyTeamID][i] == Base0) base_count++;
                if (info.unitLocation[info.MyTeamID][i] == Towor0) tower_count++;
                if (info.unitLocation[info.MyTeamID][i] == Towor1) tower_count++;
                if (info.unitLocation[info.MyTeamID][i] == Towor2) tower_count++;
            }
            field_count = 4 - base_count - tower_count;
            unit[0] = base_count; unit[1] = tower_count; unit[2] = field_count;
        }
        return unit;
    }

    public void enableBaseUnit(){
        if (info.MyTeamID == 0) {
           for (int i = 0; i < 4; i++) {
               if (info.unitLocation[info.MyTeamID][i] == Base0){
                   if (i == 0||i == 2){
                       base_unitpair = RED_GREEN;
                   }
                   if (i == 1 || i == 3){
                       base_unitpair = BLACK_YELLOW;
                   }
               }
           }
        } else if (info.MyTeamID == 1) {
            for (int i = 0; i < 4; i++) {
                if (info.unitLocation[info.MyTeamID][i] == Base0){
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

    public void sendtowerhold(){
        int tower[] = new int[3];
        if(info.towerHold[0] == info.MyTeamID){
            tower[0] = 1;
        }
        if(info.towerHold[1] == info.MyTeamID){
            tower[1] = 1;
        }
        if(info.towerHold[2] == info.MyTeamID){
            tower[2] = 1;
        }
    }

    public void search_field(){
        if (info.MyTeamID == 0) {
            for (int i = 0; i < 4; i++) {
                if (info.unitLocation[info.MyTeamID][i] == Route001){

                }
                if (info.unitLocation[info.MyTeamID][i] == Route002){

                }
                if (info.unitLocation[info.MyTeamID][i] == Route003){

                }

            }
        } else if (info.MyTeamID == 1) {
            for (int i = 0; i < 4; i++) {
                if (info.unitLocation[info.MyTeamID][i] == Base0){




                }
            }
        }


    }


}
