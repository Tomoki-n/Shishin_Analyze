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
            int[] move = this.move_diceistab();
            this.sthread.sendPlayMessage(move[0], move[1], move[2]);
        }
        System.out.println(msg);
    }

    private synchronized int[] move_diceistab() {
        int[] move = new int[3];

        //初手先攻の場合の最初に指す手
        if(this.turnCount == 1 && this.turnState == STATE_PLAY_TURN1) {
            //動かす駒はランダム
            move[0] = (int)(Math.random() * 4.0);
            //右前か左前のどちらかにランダムに指す
            move[2] = 6;
            if((int)(Math.random() * 2.0) == 0) {
                move[1] = 3;
            } else {
                move[1] = 5;
            }
            return move;
        }

        if(this.MyTeamID == 0) {
            for(int i = 0; i < 4; i++) {

            }
        } else {

        }
        return move;
    }
}
