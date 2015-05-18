package com.company.AI;


import com.company.HttpConnection;

import java.awt.*;
import java.io.IOException;
import java.util.Random;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class AI1 extends AI {

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
     * Creates new form GameField
     */
    public AI1(String address, String type, String stype) throws InterruptedException, IOException {
        super(address, type, stype);
    }

    int test = 1;
    int units[][] = new int[2][3];

    /**
     * ユーザへのメッセージ表示
     */
    @Override
    public void addMessage(String msg) throws InterruptedException{

        if (msg == "Select Unit") {
           if(MyTeamID == 0) { //int unit[][] = new int[2][3];
               if (test == 1) {
                   units = new int[2][3];
                   init();
                   synchronized (units) {
                       units = UnitOrder0();
                       Thread.sleep(500);
                   }
                   
                   this.sthread.sendPlayMessage(units[0][0], units[0][1], units[0][2]);
                   test = 2;
               } else if (test == 2) {
                   this.sthread.sendPlayMessage(units[1][0], units[1][1], units[1][2]);
                   test = 1;
               }
           }else if(MyTeamID == 1){
               if (test == 1) {
                   units = new int[2][3];
                   init();
                   synchronized (units) {
                       units = UnitOrder1();
                       Thread.sleep(500);
                   }
                   this.sthread.sendPlayMessage(units[0][0], units[0][1], units[0][2]);
                   test = 2;
               } else if (test == 2) {
                   this.sthread.sendPlayMessage(units[1][0], units[1][1], units[1][2]);
                   test = 1;
               }
           }
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
                        Random rnd1 = new Random();
                        int uni1 = rnd1.nextInt(3);
                        if(uni1 == 0) {
                            if (tower[1] != 1) {
                                if (base_unitpair == BLACK_YELLOW) {
                                    Random rnd = new Random();
                                    int uni = rnd.nextInt(2);
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route011.x;
                                        unit[0][2] = Route011.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route011.x;
                                        unit[1][2] = Route011.y;
                                        routeinfo = Route011_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route011.x;
                                        unit[0][2] = Route011.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route011.x;
                                        unit[1][2] = Route011.y;
                                        routeinfo = Route011_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                            } else if (tower[0] != 1) {
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
                            } else if (tower[2] != 1) {
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
                        else if(uni1 == 1) {
                             if (tower[0] != 1) {
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
                             else if (tower[2] != 1) {
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
                            else if (tower[1] != 1) {
                                if (base_unitpair == BLACK_YELLOW) {
                                    Random rnd = new Random();
                                    int uni = rnd.nextInt(2);
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route011.x;
                                        unit[0][2] = Route011.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route011.x;
                                        unit[1][2] = Route011.y;
                                        routeinfo = Route011_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route011.x;
                                        unit[0][2] = Route011.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route011.x;
                                        unit[1][2] = Route011.y;
                                        routeinfo = Route011_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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

                        }
                        else if(uni1 == 2) {

                             if (tower[2] != 1) {
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
                            else if (tower[1] != 1) {
                                if (base_unitpair == BLACK_YELLOW) {
                                    Random rnd = new Random();
                                    int uni = rnd.nextInt(2);
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route011.x;
                                        unit[0][2] = Route011.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route011.x;
                                        unit[1][2] = Route011.y;
                                        routeinfo = Route011_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route011.x;
                                        unit[0][2] = Route011.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route011.x;
                                        unit[1][2] = Route011.y;
                                        routeinfo = Route011_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                            if (tower[0] != 1) {
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
                        }

                    }
                    break;
                    case FIELD_0_TOWER_2_CAMP_0: {
                        tower = sendtowerhold();

                        if (tower[1] == 1) {
                            enableTowerUnit(1);
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route012.x;
                            unit[0][2] = Route012.y;
                            unit[1][0] = setupUnit(tower_unitpair)[0];
                            unit[1][1] = Route013.x;
                            unit[1][2] = Route013.y;
                            return unit;
                        }
                        else if (tower[0] == 1) {
                            enableTowerUnit(0);
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route002.x;
                            unit[0][2] = Route002.y;
                            unit[1][0] = setupUnit(tower_unitpair)[0];
                            unit[1][1] = Route003.x;
                            unit[1][2] = Route003.y;
                            return unit;
                        }
                        else if (tower[2] == 1) {
                            enableTowerUnit(2);
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route032.x;
                            unit[0][2] = Route032.y;
                            unit[1][0] = setupUnit(tower_unitpair)[0];
                            unit[1][1] = Route033.x;
                            unit[1][2] = Route033.y;
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
                    case FIELD_0_TOWER_1_CAMP_1:  {
                        tower = sendtowerhold();
                        enableBaseUnit();
                        Random rnd1 = new Random();
                        int uni1 = rnd1.nextInt(3);
                        if(uni1 == 0) {
                            if (tower[1] != 1) {
                                if (base_unitpair == BLACK_YELLOW) {
                                    Random rnd = new Random();
                                    int uni = rnd.nextInt(2);
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route111.x;
                                        unit[0][2] = Route111.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route111.x;
                                        unit[1][2] = Route111.y;
                                        routeinfo = Route111_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route111.x;
                                        unit[0][2] = Route111.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route111.x;
                                        unit[1][2] = Route111.y;
                                        routeinfo = Route111_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                            } else if (tower[0] != 1) {
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
                            } else if (tower[2] != 1) {
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
                        else if(uni1 == 1) {
                            if (tower[0] != 1) {
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
                            else if (tower[2] != 1) {
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
                            else if (tower[1] != 1) {
                                if (base_unitpair == BLACK_YELLOW) {
                                    Random rnd = new Random();
                                    int uni = rnd.nextInt(2);
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route111.x;
                                        unit[0][2] = Route111.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route111.x;
                                        unit[1][2] = Route111.y;
                                        routeinfo = Route111_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route111.x;
                                        unit[0][2] = Route111.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route111.x;
                                        unit[1][2] = Route111.y;
                                        routeinfo = Route111_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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

                        }
                        else if(uni1 == 2) {

                            if (tower[2] != 1) {
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
                            else if (tower[1] != 1) {
                                if (base_unitpair == BLACK_YELLOW) {
                                    Random rnd = new Random();
                                    int uni = rnd.nextInt(2);
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route111.x;
                                        unit[0][2] = Route111.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route111.x;
                                        unit[1][2] = Route111.y;
                                        routeinfo = Route111_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                                    if (uni == 0) {
                                        unit[0][0] = setupUnit(base_unitpair)[0];
                                        unit[0][1] = Route111.x;
                                        unit[0][2] = Route111.y;
                                        unit[1][0] = setupUnit(base_unitpair)[1];
                                        unit[1][1] = Route111.x;
                                        unit[1][2] = Route111.y;
                                        routeinfo = Route111_;
                                        moveunit = base_unitpair;
                                        return unit;
                                    } else if (uni == 1) {
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
                            if (tower[0] != 1) {
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
                        }

                    }
                    break;
                    case FIELD_0_TOWER_2_CAMP_0: {
                        tower = sendtowerhold();
                        Random rnd = new Random();
                        int rot = rnd.nextInt(3);

                        if (tower[1] == 1) {
                            enableTowerUnit(1);
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route112.x;
                            unit[0][2] = Route112.y;
                            unit[1][0] = setupUnit(tower_unitpair)[0];
                            unit[1][1] = Route113.x;
                            unit[1][2] = Route113.y;
                            return unit;
                        }
                        else if (tower[0] == 1) {
                            enableTowerUnit(0);
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route102.x;
                            unit[0][2] = Route102.y;
                            unit[1][0] = setupUnit(tower_unitpair)[0];
                            unit[1][1] = Route103.x;
                            unit[1][2] = Route103.y;
                            return unit;
                        }
                        else if (tower[2] == 1) {
                            enableTowerUnit(2);
                            unit[0][0] = setupUnit(tower_unitpair)[0];
                            unit[0][1] = Route132.x;
                            unit[0][2] = Route132.y;
                            unit[1][0] = setupUnit(tower_unitpair)[0];
                            unit[1][1] = Route133.x;
                            unit[1][2] = Route133.y;
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
        if (this.turnCount == FIRST_TURN) {
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

    public synchronized  void enableTowerUnit(int x) {
        Point pos = new Point(0, 0);
        if (x == 0) {
            pos = Tower0;
        }
        if (x == 1) {
            pos = Tower1;
        }
        if (x == 2) {
            pos = Tower2;
        }

        if (this.MyTeamID == 0) {
            for (int i = 0; i < 4; i++) {
                if (this.unitLocation[this.MyTeamID][i].equals(pos)) {
                    if (i == 0 || i == 2) {
                        tower_unitpair = RED_GREEN;
                        return;
                    }
                    if (i == 1 || i == 3) {
                        tower_unitpair = BLACK_YELLOW;
                        return;
                    }
                }

            }

        } else if (this.MyTeamID == 1) {
            for (int i = 0; i < 4; i++) {
                if (this.unitLocation[this.MyTeamID][i].equals(pos)) {
                    if (i == 0 || i == 2) {
                        tower_unitpair = RED_GREEN;
                        return;
                    }
                    if (i == 1 || i == 3) {
                        tower_unitpair = BLACK_YELLOW;
                        return;
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

    public int[] randint(int i){

        int[] x = new int[3];
        if(i == 0){
            x[0] = 0;
            x[1] = 1;
            x[2] = 2;
        }
        else if (i == 1){
            x[0] = 1;
            x[1] = 2;
            x[2] = 0;
        }
        else if (i == 2 ){
            x[0] = 2;
            x[1] = 0;
            x[2] = 1;
        }

        return x;
    }



}
