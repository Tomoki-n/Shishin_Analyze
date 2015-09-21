package com.company.AI;


import com.company.AI.AI;

import java.awt.*;
import java.io.IOException;
import java.nio.file.attribute.PosixFileAttributes;
import java.util.ArrayList;
import java.util.IntSummaryStatistics;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class AI0 extends AI {

    //序盤時に先に動くユニットの番号
    private int preunit_l = -1;
    private int preunit_r = -1;

    //現在どの戦術で動くかを指定(今のところ使ってない)
    private int mode = MODE_OPENING;
    private static final int MODE_DEFEND = 10;
    private static final int MODE_OPENING = 11;

    //ターン後半でどのような動きをとるかの指定
    private int turnmode = -1;
    private static final int TM_DEFEND = 10;

    /**
     * Creates new form GameField
     */
    public AI0(String address, String type, String stype) throws InterruptedException, IOException {
        super(address, type, stype, "Glyph-Hack");
    }

    /**
     * ユーザへのメッセージ表示
     */
    @Override
    public void addMessage(String msg) throws InterruptedException {
        if (msg == "Select Unit") {

            //chat送信
            //さすがにネタなので、ここの実装は本編が片付いてからで…
            /*
            if(Math.random() <= (5.0 / 18.0)) {
                this.sthread.sendMessage("600 " + Glyph.getGlyphSeq());
            }
             */

            //ここからメイン
            int[] move = this.move_main();
            this.sthread.sendPlayMessage(move[0], move[1], move[2]);
        }
        System.out.println(msg);
    }

    /**
     * 一応核となる部分ではありますが、ここでは分岐させているだけです
     * 詳しい挙動は各関数にて
     * @return
     */
    private synchronized int[] move_main() {
        int[] move = new int[3];
        if(this.turnCount == 1) {
            move = move_first();
        } else {
            switch (this.turnState) {
                case STATE_PLAY_TURN1:
                case STATE_PLAY_TURN2:
                    move = move_firsthalf();
                    break;
                case STATE_PLAY_TURN3:
                case STATE_PLAY_TURN4:
                    move = move_secondhalf();
                    break;
            }
            /*
            //一旦没
            switch (this.mode) {
                case MODE_DEFEND:
                    move = move_defend();
                    break;
                case MODE_OPENING:
                    move = move_opening();
                    break;
            }
            */
        }

        return move;
    }

    /**
     * 1ターン目の挙動
     * @return
     */
    private synchronized int[] move_first() {
        int[] move = new int[3];

        if(this.turnCount != 1) {
            System.out.println("Error: 無効なタイミングでのmove_first呼び出し");
        }

        switch(this.turnState) {
            case STATE_PLAY_TURN1:
                /*
                 * 先攻時の初動
                 * ランダムにどれかの駒を、右前もしくは左前に指す
                 */
                move[0] = (int)(Math.random() * 4.0);
                if((int)(Math.random() * 2.0) == 0) {
                    move[1] = 3;
                    preunit_l = move[0];
                } else {
                    move[1] = 5;
                    preunit_r = move[0];
                }
                move[2] = 6;
                break;
            case STATE_PLAY_TURN2:
                /*
                 * 後攻時の初動
                 * 相手が前左右or横に出た場合：その方向に有利なユニットを出す
                 * 相手が前中に出た場合：左右どちらかに有利なユニットを出す
                 * 相手が後ろに出た場合：左右どちらかに「不利な」ユニットを出す(真ん中を目指すため)
                 */
                for(int unit = 0; unit < 4; unit++) {
                    if(!this.unitLocation[0][unit].equals(Base0)) {
                        Point enemypos = this.unitLocation[0][unit];

                        if(enemypos.y == 8) {
                            //後ろ
                            move[0] = loseUnit1(unit);
                            if((int)(Math.random() * 2.0) == 0) {
                                move[1] = 3;
                                preunit_l = move[0];
                            } else {
                                move[1] = 5;
                                preunit_r = move[0];
                            }
                        } else {
                            //前か横
                            move[0] = winUnit1(unit);
                            switch (enemypos.x) {
                                case 3:
                                    move[1] = 3;
                                    preunit_r = move[0];
                                    break;
                                case 4:
                                    if((int)(Math.random() * 2.0) == 0) {
                                        move[1] = 3;
                                        preunit_l = move[0];
                                    } else {
                                        move[1] = 5;
                                        preunit_r = move[0];
                                    }
                                    break;
                                case 5:
                                    move[1] = 3;
                                    preunit_r = move[0];
                                    break;
                                default:
                                    System.out.println("Error: 先手がありえない手を指した");
                            }
                        }

                        //どうせ前に出すのでここは固定です
                        move[2] = 2;
                        break;
                    }
                }
                break;
            case STATE_PLAY_TURN3:
                /*
                 * 最初の駒の移動と逆方向前側に、最初に前に出した駒と対にならない駒を移動
                 */

                //最初に動かした方向によって、次に動かす方向が決定
                int preunit;
                boolean preleft;
                if(this.preunit_l != -1) {
                    preunit = preunit_l;
                    preleft = true;
                    move[1] = 5;
                } else {
                    preunit = preunit_r;
                    preleft = false;
                    move[1] = 3;
                }

                //最初に動かした
                move[2] = 2;

                switch (preunit) {
                    case GREEN:
                    case RED:
                        if((int)(Math.random() * 2.0) == 0) {
                            move[0] = BLACK;
                        } else {
                            move[0] = YELLOW;
                        }
                        break;
                    case BLACK:
                    case YELLOW:
                        if((int)(Math.random() * 2.0) == 0) {
                            move[0] = BLACK;
                        } else {
                            move[0] = YELLOW;
                        }
                        break;
                }

                if(preleft) {
                    preunit_r = move[0];
                } else {
                    preunit_l = move[0];
                }
                break;
            case STATE_PLAY_TURN4:
                /*
                 * 先攻・初手終わり
                 * 相手が既に柱前まで来ている場合：
                 *      相手ユニットが自分ユニットに対し有利もしくは相殺ならばその柱を避ける方向に、
                 *      自分ユニットが相手ユニットに対し有利ならばその柱に進む方向に
                 * 相手が
                 */

                //TODO:ここ
                break;
            default:
                System.out.println("Error: 存在しないturnStateの値");
        }

        return move;
    }


    private synchronized int[] move_firsthalf() {
        /*
         * ターン前半を指す
         * muzui.
         */

        int[] move = new int[3];

        if(existTower(this.MyTeamID) == 4) {
            //既に2つ柱を持っている場合
            //ランダムな柱からランダムな駒を一時的にランダムな方向に移動

            turnmode = TM_DEFEND;
            move[0] = (int) (Math.random() * 4.0);
            int direction = (int)(Math.random() * 8.0);
            Point tower = getNearestTower(this.unitLocation[this.MyTeamID][move[0]]);

            switch (direction) {
                case 0:
                case 3:
                case 5:
                    move[1] = tower.x - 1;
                    break;
                case 1:
                case 6:
                    move[1] = tower.x;
                    break;
                case 2:
                case 4:
                case 7:
                    move[1] = tower.x + 1;
                    break;
            }

            switch (direction) {
                case 0:
                case 1:
                case 2:
                    move[2] = tower.y - 1;
                    break;
                case 3:
                case 4:
                    move[2] = tower.y;
                    break;
                case 5:
                case 6:
                case 7:
                    move[2] = tower.y + 1;
                    break;
            }
        } else if(existTower(this.MyTeamID) == 3) {
            //既に3つの駒が柱にいる場合
            Point tower;
            if(this.existUnit(tower_center, this.MyTeamID) == 1) {
                tower = tower_center;
            } else if(this.existUnit(tower_left, this.MyTeamID) == 1) {
                tower = tower_left;
            } else {
                tower = tower_right;
            }

            for (int unit = 0; unit < 4; unit++) {
                Point pos = this.unitLocation[this.MyTeamID][unit];

                if (pos != tower_center && pos != tower_left && pos != tower_right) {
                    move[0] = unit;

                    if(distance(pos, tower) == 1) {
                        //そのまま柱に入ってしまうと、ターン後半にて一度柱から出る必要が出てしまうため
                        //一時的に場所をずらす(一手分損だが仕方ない)
                        if(pos.x == tower.x) {
                            if((int)(Math.random() * 2.0) == 0) {
                                move[1] = tower.x - 1;
                            } else {
                                move[1] = tower.x + 1;
                            }
                        } else {
                            move[1] = tower.x;
                        }

                        if(pos.y == tower.y) {
                            if((int)(Math.random() * 2.0) == 0) {
                                move[2] = tower.y - 1;
                            } else {
                                move[2] = tower.y + 1;
                            }
                        } else {
                            move[2] = tower.y;
                        }

                        turnmode = TM_DEFEND;
                    } else {
                        //柱に近づく
                        //このときのturnmodeまだ決めてない
                        //TODO:
                    }

                    break;
                }
            }
        } else {
            //TODO:残り
        }



        return move;
    }

    private synchronized int[] move_secondhalf() {
        assert this.turnState == STATE_PLAY_TURN3 || this.turnState == STATE_PLAY_TURN4;

        int[] move = new int[3];
        switch (turnmode) {
            case TM_DEFEND:
                //2つの柱を守っている状態から、一歩退避させた状態
                //2つの柱を守っている状態に戻す
                assert existTower(this.MyTeamID) == 3;

                for (int unit = 0; unit < 4; unit++) {
                    Point pos = this.unitLocation[this.MyTeamID][unit];

                    if (pos != tower_center && pos != tower_left && pos != tower_right) {
                        assert distance(pos, tower_center) == 1 || distance(pos, tower_left) == 1 || distance(pos, tower_right) == 1;
                        move[0] = unit;
                        move[1] = getNearestTower(pos).x;
                        move[2] = getNearestTower(pos).y;
                        break;
                    }
                }
                break;
            //TODO:残り(前半の手が決まり次第)
        }

        //turnmodeのリセット
        turnmode = -1;
        return move;
    }

    /* 以下utility
     * 親クラスであるAIの方に移植してもOK
     */


    /**
     * 指定したチームの駒のうち、何個が柱に居るか
     * @param teamID 0ならば1ターン目先攻チーム、1ならば1ターン目後攻チーム、それ以外なら両チーム合計
     * @return 指定したチームの駒のうち、何個が柱に居るか(0～4)
     */
    public int existTower(int teamID) {
        return existUnit(tower_left, teamID) + existUnit(tower_center, teamID) + existUnit(tower_right, teamID);
    }

}
