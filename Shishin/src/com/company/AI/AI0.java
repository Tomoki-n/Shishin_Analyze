package com.company.AI;

import com.company.Glyph;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by tomoki-n on 2015/04/10.
 * まとまらなかったので没りましたごめんなさい
 */
public class AI0 extends AI {

    //序盤時に先に動くユニットの番号
    private int preUnit_l = -1;
    private int preUnit_r = -1;

    //ターン後半でどのような動きをとるかの指定
    private int turnMode = -1;
    private static final int TM_DIRECT = 10;    //前半の手を指す時点で後半の手を決定した場合。tempUnit, tempPosの指定が必要
    private static final int TM_DEFEND = 20;    //前半終了時点で、2つの柱に自軍の駒が2体と1体居り、その1体のみで守られる柱から距離1の場所に残りの1体が居る状態
    private static final int TM_SINGLE_TO2 = 21;    //ターン後半で単独行動をとる場合。tempUnitとdestinationの指定が必要。
    
    //一時的な指し手指示用
    //実際の使い方は対応するturnMode同士を見比べてくださいな
    private Point destination = null;
    private int tempUnit = -1;
    private Point tempPos = null;



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
            /*
            if(Math.random() <= (5.0 / 18.0)) {
                this.sthread.sendChatMessage(Glyph.getGlyphSeq());
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
     * @return 指し手を指定する配列
     */
    private synchronized int[] move_main() {
        int[] move = new int[3];
        if(this.turnCount == 1) {
            //1手目は特殊指定
            move = move_firstTurn();
        } else {
            //指示処理をターンの前後半で分ける
            switch (this.turnState) {
                case STATE_PLAY_TURN1:
                case STATE_PLAY_TURN2:
                    move = move_firstHalf();
                    break;
                case STATE_PLAY_TURN3:
                case STATE_PLAY_TURN4:
                    move = move_secondHalf();
                    break;
            }
        }

        return move;
    }

    /**
     * 1ターン目の挙動
     * @return 指し手を指定する配列
     */
    private synchronized int[] move_firstTurn() {
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
                    preUnit_l = move[0];
                } else {
                    move[1] = 5;
                    preUnit_r = move[0];
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
                        Point enemyPos = this.unitLocation[0][unit];

                        if(enemyPos.y == 8) {
                            //後ろ
                            move[0] = loseUnit1(unit);
                            if((int)(Math.random() * 2.0) == 0) {
                                move[1] = 3;
                                preUnit_l = move[0];
                            } else {
                                move[1] = 5;
                                preUnit_r = move[0];
                            }
                        } else {
                            //前か横
                            move[0] = winUnit1(unit);
                            switch (enemyPos.x) {
                                case 3:
                                    move[1] = 3;
                                    preUnit_r = move[0];
                                    break;
                                case 4:
                                    if((int)(Math.random() * 2.0) == 0) {
                                        move[1] = 3;
                                        preUnit_l = move[0];
                                    } else {
                                        move[1] = 5;
                                        preUnit_r = move[0];
                                    }
                                    break;
                                case 5:
                                    move[1] = 3;
                                    preUnit_r = move[0];
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

                int preUnit;        //最初に動かした駒の種類
                boolean preLeft;    //最初に左前に駒を出せばtrue, 右前に駒を出せばfalse

                //最初に動かした方向によって、次に動かす方向が決定
                if(this.preUnit_l != -1) {
                    preUnit = preUnit_l;
                    preLeft = true;
                    move[1] = 5;
                } else {
                    preUnit = preUnit_r;
                    preLeft = false;
                    move[1] = 3;
                }

                move[2] = 2;

                switch (preUnit) {
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

                if(preLeft) {
                    preUnit_r = move[0];
                } else {
                    preUnit_l = move[0];
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
                //仮実装

                //最初に動かした方向によって、次に動かす方向が決定
                if(this.preUnit_l != -1) {
                    preUnit = preUnit_l;
                    preLeft = true;
                    move[1] = 5;
                } else {
                    preUnit = preUnit_r;
                    preLeft = false;
                    move[1] = 3;
                }

                move[2] = 6;

                switch (preUnit) {
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

                if(preLeft) {
                    preUnit_r = move[0];
                } else {
                    preUnit_l = move[0];
                }
                break;
            default:
                System.out.println("Error: 存在しないturnStateの値");
        }

        return move;
    }


    private synchronized int[] move_firstHalf() {
        /*
         * ターン前半を指す
         * muzui.
         */

        int[] move = new int[3];

        if(existTower(this.MyTeamID) == 4) {
            //既に2つ柱を持っている場合
            //ランダムな柱からランダムな駒を一時的にランダムな方向に移動
            //ターン後半にて動かした駒をもと居た柱に戻すので、どの方向にどの駒が動こうが関係ない

            turnMode = TM_DEFEND;
            move[0] = (int) (Math.random() * 4.0);
            int direction = (int)(Math.random() * 8.0);
            Point tower = getNearestTower(this.unitLocation[this.MyTeamID][move[0]]);

            //わざわざランダム要素入れる必要もないですが、在った方が面白いじゃん？って話です
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
            Point tower;    //目的地の柱
            if(this.existUnit(tower_left, this.MyTeamID) == 1) {
                tower = tower_left;
            } else if(this.existUnit(tower_center, this.MyTeamID) == 1) {
                tower = tower_center;
            } else {
                tower = tower_right;
                
            }

            for (int unit = 0; unit < 4; unit++) {
                Point pos = this.unitLocation[this.MyTeamID][unit];
                
                //まだどの柱の上にも居ない駒を次に動かす
                if (pos != tower_center && pos != tower_left && pos != tower_right) {
                    move[0] = unit;

                    //柱までの距離に応じ移動先の位置を指定
                    if(distance(pos, tower) == 1) {
                        turnMode = TM_DEFEND;
                        
                        //そのまま柱に入ってしまうと、ターン後半にてどれかの駒が柱から出る必要が出てしまうため
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
                    } else if(distance(pos, tower) == 2) {                        
                        //陣地から一歩進んだ状態
                        //柱に近づく
                        //次の移動箇所は柱との距離が1になる場所からランダムに選ばれる
                        turnMode = TM_DEFEND;

                        //次の移動位置として使用できるかどうか
                        boolean[][] isNext = new boolean[3][3];

                        for(int x = 0; x < 3; x++) {
                            for(int y = 0; y < 3; y++) {
                                Point nextPos = new Point(pos.x + x - 1, pos.y + y - 1);
                                isNext[x][y] = (distance(nextPos, tower) == 1);
                            }
                        }

                        while (true) {
                            int x = (int)(Math.random() * 3);
                            int y = (int)(Math.random() * 3);
                            if(isNext[x][y]) {
                                move[1] = pos.x + x - 1;
                                move[2] = pos.y + y - 1;
                                break;
                            }
                        }
                    } else {
                        //残る1駒が自陣に残っている場合
                        tempUnit = move[0];
                        destination = tower;
                        turnMode = TM_SINGLE_TO2;

                        if(tower.equals(tower_left)) {
                            move[1] = 3;
                        } else if(tower.equals(tower_right)) {
                            move[1] = 5;
                        } else {
                            if((int)(Math.random() * 2) == 0) {
                                move[1] = 3;
                            } else {
                                move[1] = 5;
                            }
                        }

                        if(this.MyTeamID == 0) {
                            move[2] = 6;
                        } else {
                            move[2] = 2;
                        }
                    }

                    break;
                }
            }
        } else {
            //TODO:残り
            if(preUnit_l != -1 && preUnit_r != -1) {
                //どっち先に動かそう…
            } else if(preUnit_l != -1) {

            } else if(preUnit_r != -1) {

            } else {

            }
        }



        return move;
    }

    private synchronized int[] move_secondHalf() {
        assert this.turnState == STATE_PLAY_TURN3 || this.turnState == STATE_PLAY_TURN4;

        int[] move = new int[3];
        switch (turnMode) {
            case TM_DEFEND:
                //2つの柱を守っている状態から、一歩退避させた状態及び、それと同じ状況
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
            case TM_DIRECT:
                //前半で既に手が指定されている場合
                move[0] = tempUnit;
                move[1] = tempPos.x;
                move[2] = tempPos.y;
                break;
            case TM_SINGLE_TO2:
                //単独で距離2以内の地点に動く場合
                move[0] = tempUnit;
                Point unitPos = this.unitLocation[this.MyTeamID][tempUnit];
                Point winUnitPos = this.unitLocation[this.EnemyTeamID()][loseUnit1(tempUnit)];  //戦ったらこちらが勝利する相手
                Point loseUnitPos = this.unitLocation[this.EnemyTeamID()][winUnit1(tempUnit)];  //戦ったら相手が勝利する相手
                ArrayList<Point> nextPosList = new ArrayList<Point>(8);
                for(int x = -1; x <= 1; x++) {
                    for(int y = -1; y <= 1; y++) {
                        Point nextPos = new Point(unitPos.x + x - 1, unitPos.y + y - 1);
                        if(distance(nextPos, destination) <= 2 && !(x == 0 && y == 0)) {
                            nextPosList.add(nextPos);
                        }
                    }
                }

                if(this.turnState == STATE_PLAY_TURN4) {
                    if(nextPosList.contains(winUnitPos)) {
                        //相手と戦って勝てるなら積極的に攻撃する
                        move[1] = winUnitPos.x;
                        move[2] = winUnitPos.y;
                    } else {
                        //自らやられる方向には動かない
                        ArrayList<Point> removeLose = new ArrayList<Point>();
                        removeLose.addAll(nextPosList);
                        removeLose.remove(loseUnitPos);
                        assert !removeLose.isEmpty();

                        //TODO:相手が二体いるところを避ける
                        int rand = (int) (Math.random() * removeLose.size());
                        move[1] = removeLose.get(rand).x;
                        move[2] = removeLose.get(rand).y;
                    }
                } else {
                    if(nextPosList.contains(winUnitPos)) {
                        boolean isOtherNear = false;
                        for(int unit = 0; unit < 4; unit++) {
                            if(distance(winUnitPos, this.unitLocation[this.EnemyTeamID()][unit]) == 1) {
                                isOtherNear = true;
                            }
                        }

                        if(!isOtherNear) {
                            move[1] = winUnitPos.x;
                            move[2] = winUnitPos.y;
                        } else {
                            ArrayList<Point> removeLose = new ArrayList<Point>();
                            removeLose.addAll(nextPosList);
                            removeLose.remove(winUnitPos);
                            //TODO:負ける場所を避ける
                        }
                    }
                }

                break;

            //TODO:残り(前半の手が決まり次第)
            default:
                System.out.println("Error: turnModeが指定されていない");
        }

        //ターン後半指定用一時変数の初期化
        turnMode = -1;
        tempUnit = -1;
        tempPos = null;
        return move;
    }

    
    /* 以下utility
     * 親クラスであるAIの方に移植してもOK
     * (実際、いくつかのutility関数はAIクラスに移植した)
     */
    
    /**
     * 指定したチームの駒のうち、何個が柱に居るか(どの柱かは関係ない)
     * @param teamID 0ならば1ターン目先攻チーム、1ならば1ターン目後攻チーム、それ以外なら両チーム合計
     * @return 指定したチームの駒のうち、何個が柱に居るか(0～4)
     */
    public int existTower(int teamID) {
        return existUnit(tower_left, teamID) + existUnit(tower_center, teamID) + existUnit(tower_right, teamID);
    }

    /**
     * 指定した駒に隣接するマスのリストを返す
     * @param teamID 調べたい駒のあるチーム
     * @param unit 調べたい駒の番号
     * @return 指定した駒に隣接するマスのリスト
     */
    ArrayList<Point> unitNeighbor(int teamID, int unit) {
        ArrayList<Point> list = new ArrayList<Point>();
        Point unitPos = this.unitLocation[teamID][unit];
        for(int xAdd = -1; xAdd <= 1; xAdd++) {
            for(int yAdd = -1; yAdd <= 1; yAdd++) {
                if(xAdd == 0 && yAdd == 0) {
                    continue;
                }
                Point neighbor = new Point(unitPos.x + xAdd, unitPos.y + yAdd);
                int x = neighbor.x;
                int y = neighbor.y;
                if(x >= 0 && x <= 8 && y >= 0 && y <= 8) {
                    list.add(neighbor);
                } else {

                }
            }
        }
        return list;
    }
}
