package com.company.AI;


import com.company.AI.AI;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by tomoki-n on 2015/04/10.
 */
public class CUI extends AI {
    /**
     * Creates new form GameField
     */
    public CUI(String address, String type, String stype) throws InterruptedException {
        super(address, type, stype);
    }

    /**
     * ユーザへのメッセージ表示、ならびに手の入力
     * 現在、入力は"idXY", "id X Y", "colorXY", "color X Y"の4通りに対応
     * idは各駒のIDに同じ0～3の数字、colorは緑:G, 黒:B, 赤:R, 黄:Yで、大文字・小文字両方に対応しています
     */
    @Override
    public void addMessage(String msg) throws InterruptedException {

        if (msg == "Select Unit") {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            Pattern pat = Pattern.compile("([0-3])(-?[0-9])(-?[0-9])");
            Pattern patsp = Pattern.compile("([0-3]) (-?[0-9]) (-?[0-9])");
            Pattern patcc = Pattern.compile("([bgryBGRY])(-?[0-9])(-?[0-9])");
            Pattern patccsp = Pattern.compile("([bgryBGRY]) (-?[0-9]) (-?[0-9])");
            boolean incomplete = true;

            System.out.println("Your turn. Please input your move. (format: \"idXY\" or \"id X Y\")");
            do {
                try {
                    String line = br.readLine();
                    Matcher mt = pat.matcher(line);
                    Matcher mtsp = patsp.matcher(line);
                    Matcher mtcc = patcc.matcher(line);
                    Matcher mtccsp = patccsp.matcher(line);
                    if(mt.matches()) {
                        int unitid = Integer.parseInt(mt.group(1));
                        int unitx = Integer.parseInt(mt.group(2));
                        int unity = Integer.parseInt(mt.group(3));
                        this.sthread.sendPlayMessage(unitid, unitx, unity);
                        incomplete = false;
                    } else if (mtsp.matches()) {
                        int unitid = Integer.parseInt(mtsp.group(1));
                        int unitx = Integer.parseInt(mtsp.group(2));
                        int unity = Integer.parseInt(mtsp.group(3));
                        this.sthread.sendPlayMessage(unitid, unitx, unity);
                        incomplete = false;
                    } else if (mtcc.matches()) {
                        int unitid = ColorToInt(mtcc.group(1));
                        int unitx = Integer.parseInt(mtcc.group(2));
                        int unity = Integer.parseInt(mtcc.group(3));
                        this.sthread.sendPlayMessage(unitid, unitx, unity);
                        incomplete = false;
                    } else if (mtccsp.matches()) {
                        int unitid = ColorToInt(mtccsp.group(1));
                        int unitx = Integer.parseInt(mtccsp.group(2));
                        int unity = Integer.parseInt(mtccsp.group(3));
                        this.sthread.sendPlayMessage(unitid, unitx, unity);
                        incomplete = false;
                    } else {
                        System.out.println("Input Format Error, Please input again");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } while(incomplete);
        }

        System.out.println(msg);
    }

    private int ColorToInt(String colorcode) {
        switch (colorcode.charAt(0)) {
            case 'g':
            case 'G':
                return GREEN;
            case 'b':
            case 'B':
                return BLACK;
            case 'r':
            case 'R':
                return RED;
            case 'y':
            case 'Y':
                return YELLOW;
            default:
                //error
                return -1;
        }
    }
}
