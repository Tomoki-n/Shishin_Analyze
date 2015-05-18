package com.company.AI;


import com.company.AI.AI;

import java.io.IOException;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class AI2 extends AI {

    /**
     * Creates new form GameField
     */
    public AI2(String address, String type, String stype) throws InterruptedException, IOException {
        super(address, type, stype, "kari-02 v0.0");
    }

    /**
     * ユーザへのメッセージ表示
     */
    @Override
    public void addMessage(String msg) throws InterruptedException {
        if (msg == "Select Unit") {
            //TODO: ここに自分のAI部分を書く
            //this.sthread.sendPlayMessage(unitID, X, Y); で指します
        }
        System.out.println(msg);
    }
}
