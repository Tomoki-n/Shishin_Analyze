/**
 * ah [Diceistab]
 * ad crysi en [Dstorv]
 *
 *      --- Dstorv より抜粋 (Lyric:Feryquitous)
 */



package com.company.AI;


import com.company.AI.AI;

import java.io.IOException;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class AI0 extends AI {

    /**
     * Creates new form GameField
     */
    public AI0(String address, String type, String stype) throws InterruptedException, IOException {
        super(address, type, stype, "[Diceistab] v0.0");
    }

    /**
     * ユーザへのメッセージ表示
     */
    @Override
    public void addMessage(String msg) throws InterruptedException {
        if (msg == "Select Unit") {
            int[] move = this.move_main();
            this.sthread.sendPlayMessage(move[0], move[1], move[2]);
        }
        System.out.println(msg);
    }

    private synchronized int[] move_main() {
        int[] move = new int[3];
        if(this.MyTeamID == 0) {
            for(int i = 0; i < 4; i++) {

            }
        } else {

        }
        return move;
    }

    private synchronized int[] move_first() {
        int[] move = new int[3];

        switch(this.turnState) {
            case STATE_PLAY_TURN1:
                /*
                 * 先攻時の初動
                 * ランダムにどれかの駒を、右前もしくは左前に指す
                 */
                move[0] = (int)(Math.random() * 4.0);
                if((int)(Math.random() * 2.0) == 0) {
                    move[1] = 3;
                } else {
                    move[1] = 5;
                }
                move[2] = 6;
                break;
            case STATE_PLAY_TURN2:
                /*
                 * 後攻時の初動
                 * 相手が前に出た場合：
                 * 相手が後ろに出た場合：
                 */
                break;
            case STATE_PLAY_TURN3:
                break;
            case STATE_PLAY_TURN4:
                break;
            default:
                System.out.println("Error: 存在しないturnStateの値");
        }

        return move;
    }



    /* 以下utility */

    /**
     * 相手の駒に勝てる駒を返す(1対1)
     * @param enemy 相手の駒
     * @return 相手の駒に勝つことができる駒
     */
    public static int winUnit1(int enemy) {
        switch(enemy) {
            case GREEN:
                return YELLOW;
            case BLACK:
                return GREEN;
            case RED:
                return BLACK;
            case YELLOW:
                return RED;
            default:
                return -1;
        }
    }

    /**
     * 相手の駒に勝てる駒の組み合わせを返す(2対2)
     * @param enemy1 相手の駒1つ目
     * @param enemy2 相手の駒2つ目
     * @return 相手の駒に勝つことができる駒の組み合わせ(無い場合はnullを返す)
     */
    public static int[] winUnit2(int enemy1, int enemy2) {
        int[] unit = new int[2];

        //条件分岐させやすいよう、予めenemy1 < enemy2になるようにする(enemy1 == enemy2はありえない)
        if(enemy1 > enemy2) {
            int swaptmp = enemy1;
            enemy1 = enemy2;
            enemy2 = swaptmp;
        }

        if(enemy1 == GREEN && enemy2 == BLACK) {
            unit[0] = GREEN;
            unit[1] = YELLOW;
        } else if (enemy1 == GREEN && enemy2 == YELLOW) {
            unit[0] = RED;
            unit[1] = YELLOW;
        } else if (enemy1 == BLACK && enemy2 == RED) {
            unit[0] = GREEN;
            unit[1] = BLACK;
        } else if (enemy1 == RED && enemy2 == YELLOW) {
            unit[0] = BLACK;
            unit[1] = RED;
        } else {
            return null;
        }
        return unit;
    }
}
