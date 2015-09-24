package com.company.AI;

import com.company.Glyph;

import java.awt.*;
import java.io.IOException;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class AI5 extends AI {
    //ターン後半でどのような動きをとるかの指定
    private int turnMode = -1;
    private static final int TM_DIRECT = 10;    //前半の手を指す時点で後半の手を決定した場合。tempUnit, tempPosの指定が必要
    private static final int TM_DEFEND = 20;    //前半終了時点で、2つの柱に自軍の駒が2体と1体居り、その1体のみで守られる柱から距離1の場所に残りの1体が居る状態

    //一時的な指し手指示用
    private int[] unit = new int[2];
    private int route = -1;
    private static final int R_L = 10;
    private static final int R_R = 11;
    private static final int R_LC = 12;
    private static final int R_RC = 13;
    private Point tempPos = null;

    //チャットを行うかどうか
    private boolean chat;



    /**
     * Creates new form GameField
     * AI1の戦法のコピーみたいなもの
     * ただし、無駄な戦闘は最初から避ける
     */
    public AI5(String address, String type, String stype) throws InterruptedException, IOException {
        super(address, type, stype, "Glyph-Hack");
        this.chat = true;
    }

    public AI5(String address, String type, String stype, boolean isChatEnable) throws InterruptedException, IOException {
        super(address, type, stype, "Glyph-Hack");
        this.chat = isChatEnable;
    }

    /**
     * ユーザへのメッセージ表示
     */
    @Override
    public void addMessage(String msg) throws InterruptedException {
        if (msg == "Select Unit") {
            //chat
            if(chat) {
                if (Math.random() <= (11.0 / 19.0)) {
                    this.sthread.sendChatMessage(Glyph.getGlyphSeq());
                }
            }

            //ここからメイン
            int[] move = this.move_main();
            this.sthread.sendPlayMessage(move[0], move[1], move[2]);

            //chat
            if(chat) {
                if (Math.random() <= (2.0 / 19.0)) {
                    this.sthread.sendChatMessage(Glyph.getGlyphSeq());
                }
            }
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
        if(this.turnCount == 1 && (this.turnState == STATE_PLAY_TURN1 || this.turnState == STATE_PLAY_TURN2)) {
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
     * 初手の挙動
     * @return 指し手を指定する配列
     */
    private synchronized int[] move_firstTurn() {
        int[] move = new int[3];
        assert this.turnCount == 1 && (this.turnState == STATE_PLAY_TURN1 || this.turnState == STATE_PLAY_TURN2);

        if(this.turnState == STATE_PLAY_TURN1) {
            /*
             * 先攻時の初動
             * ランダムにどれかの駒を、後ろに出す
             */
            move[0] = (int) (Math.random() * 4.0);
            move[1] = 4;
            move[2] = 8;
        } else {
            /*
             * 後攻時の初動
             * ランダムにどれかの駒を、右前か左前に出す
             */
            move[0] = (int) (Math.random() * 4.0);
            if((int)(Math.random() * 2.0) == 0) {
                move[1] = 3;
                if((int)(Math.random() * 2.0) == 0) {
                    route = R_L;
                } else {
                    route = R_LC;
                }
            } else {
                move[1] = 5;
                if((int)(Math.random() * 2.0) == 0) {
                    route = R_R;
                } else {
                    route = R_RC;
                }
            }
            move[2] = 2;
        }

        unit[0] = move[0];
        unit[1] = pairUnit(move[0]);
        tempPos = new Point(move[1], move[2]);
        turnMode = TM_DIRECT;

        return move;
    }


    private synchronized int[] move_firstHalf() {
        /*
         * ターン前半を指す
         */

        int[] move = new int[3];

        if(existTower(this.MyTeamID) == 4) {
            //既に2つ柱を持っている場合
            //ランダムな柱からランダムな駒を一時的にランダムな方向に移動
            //ターン後半にて動かした駒をもと居た柱に戻すので、どの方向にどの駒が動こうが関係ない

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

            turnMode = TM_DEFEND;
        } else {
            if(tempPos.equals(new Point(4, 8))) {
                //バックアタック特殊処理
                move[0] = unit[0];
                move[1] = 4;
                move[2] = 9;
            } else if(unit[0] != -1) {
                //既に動いている
                if(distance(this.unitLocation[this.MyTeamID][unit[0]], getNearestTower(this.unitLocation[this.MyTeamID][unit[0]])) == 1) {
                    //あと1手で柱に着く場合
                    move[0] = unit[0];
                    move[1] = getNearestTower(this.unitLocation[this.MyTeamID][unit[0]]).x;
                    move[2] = getNearestTower(this.unitLocation[this.MyTeamID][unit[0]]).y;
                } else {
                    //あと2手で柱に着く場合
                    move[0] = unit[0];
                    //必要なら軌道修正
                    if(route == R_L && existUnit(tower_left, this.EnemyTeamID()) >= 3) {
                        route = R_LC;
                    } else if(route == R_LC && existUnit(tower_center, this.EnemyTeamID()) >= 3) {
                        route = R_L;
                    } else if(route == R_R && existUnit(tower_right, this.EnemyTeamID()) >= 3) {
                        route = R_RC;
                    } else if(route == R_RC && existUnit(tower_center, this.EnemyTeamID()) >= 3) {
                        route = R_R;
                    }
                    switch (route) {
                        case R_L:
                            move[1] = 2;
                            break;
                        case R_LC:
                            move[1] = 3;
                            break;
                        case R_RC:
                            move[1] = 5;
                            break;
                        case R_R:
                            move[1] = 6;
                            break;
                    }
                    if(this.MyTeamID == 0) {
                        move[2] = 5;
                    } else {
                        move[2] = 3;
                    }
                }
            } else {
                //柱から動いてない
                while (true) {
                    int rand = (int)(Math.random() * 4.0);
                    if(!this.unitLocation[this.MyTeamID][rand].equals(getNearestTower(this.unitLocation[this.MyTeamID][rand]))) {
                        unit[0] = rand;
                        unit[1] = pairUnit(rand);

                        move[0] = unit[0];
                        break;
                    }
                }

                boolean isExistNeutral = false; //中立な柱があるかどうか
                boolean isExistBlank = false;   //駒がひとつもない柱があるかどうか
                boolean isTowerDecided = false; //目的地の柱は決定したか
                int rand;

                for (int i = 0; i < 3; i++) {
                    if(this.towerHold[i] == -1) {
                        isExistNeutral = true;
                    }
                    if(existUnit(towerPos(i), 2) == 0) {
                        isExistBlank = true;
                    }
                }

                //行先の決定
                do {
                    rand = (int)(Math.random() * 3.0);
                    if(existUnit(towerPos(rand), this.MyTeamID) > 0) {
                        //既に占有している柱は対象外
                        continue;
                    }
                    if(isExistNeutral) {
                        //まだ誰の得点源にもなっていない柱があれば最優先
                        if(this.towerHold[rand] == -1) {
                            isTowerDecided = true;
                        }
                    } else if(isExistBlank) {
                        //誰もいない柱があれば次に優先
                        if(existUnit(towerPos(rand), 2) == 0) {
                            isTowerDecided = true;
                        }
                    } else {
                        //既に3体が占有する柱を避ける
                        if(existUnit(towerPos(rand), this.EnemyTeamID()) < 3) {
                            isTowerDecided = true;
                        }
                    }
                } while (!isTowerDecided);

                if(rand == 0) {
                    route = R_L;
                    move[1] = 3;
                } else if(rand == 2) {
                    route = R_R;
                    move[1] = 5;
                } else {
                    if((int)(Math.random() * 2.0) == 0) {
                        route = R_LC;
                        move[1] = 3;
                    } else {
                        route = R_RC;
                        move[1] = 5;
                    }
                }
                if(this.MyTeamID == 0) {
                    move[2] = 6;
                } else {
                    move[2] = 2;
                }
            }
            tempPos = new Point(move[1], move[2]);
            turnMode = TM_DIRECT;
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

                    if (!pos.equals(tower_left) && !pos.equals(tower_center) && !pos.equals(tower_right)) {
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
                move[0] = unit[1];
                move[1] = tempPos.x;
                move[2] = tempPos.y;
                if(tempPos.equals(getNearestTower(this.unitLocation[this.MyTeamID][unit[0]]))) {
                    //柱に着いたら一時的な変数を初期化
                    unit[0] = -1;
                    unit[1] = -1;
                    route = -1;
                }
                break;
            default:
                System.out.println("Error: turnModeが指定されていない");
        }

        //ターン後半指定用一時変数の初期化
        turnMode = -1;
        return move;
    }

    /**
     * 指定した駒と相対する駒を返す(緑⇔赤、黒⇔黄の組み合わせ)
     * @param unit ペアを調べたい駒
     * @return 指定された駒とペアになる駒
     */
    private static int pairUnit(int unit) {
        switch (unit) {
            case GREEN:
                return RED;
            case BLACK:
                return YELLOW;
            case RED:
                return GREEN;
            case YELLOW:
                return BLACK;
            default:
                return -1;
        }
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
     * 柱の位置を番号で示せるようにする
     * @param towerNumber 0なら左、1なら真ん中、2なら右
     * @return 指定した番号の柱の位置を返す
     */
    public static Point towerPos(int towerNumber) {
        switch (towerNumber) {
            case 0:
                return tower_left;
            case 1:
                return tower_center;
            case 2:
                return tower_right;
            default:
                return null;
        }
    }
}
