package com.company;


import java.awt.*;
import java.io.*;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class AI0 extends AI {

    /**
     * Creates new form GameField
     */
    public AI0(String address, String type, String stype) throws InterruptedException {
        super(address, type, stype);
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
