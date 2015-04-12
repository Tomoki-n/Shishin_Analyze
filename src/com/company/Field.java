package com.company;

/**
 * Created by tomoki-n on 4/12/15.
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Field extends javax.swing.JPanel {

    private int bolderLineWidth = 2;

    /** 塔が在るか無いか */
    private boolean towerCell = false;

    /** 塔がどちらの持ち物になっているか */
    private int towerHas = -1;

    /** 本陣になっているかどうか */
    private int honjinID = -1;

    /** 接続セル（はしっこ）かどうか */
    private boolean isBorder = false;

    /** 障害物があるかないか */
    private boolean hasObstacle = false;

    /** プレイヤー０ユニット一覧 */
    private boolean[] player0Units = new boolean[4];
    /** プレイヤー１ユニット一覧 */
    private boolean[] player1Units = new boolean[4];

    private AI mainFiled;
    /** Creates new form FieldPanel */
    public Field(AI main) {
        this.mainFiled = main;
    }



    private Polygon p0poly;
    private Polygon p1poly;

    private Polygon p00poly;
    private Polygon p01poly;
    private Polygon p02poly;
    private Polygon p03poly;
    private Polygon p10poly;
    private Polygon p11poly;
    private Polygon p12poly;
    private Polygon p13poly;


    private Polygon p0apoly;
    private Polygon p0bpoly;
    private Polygon p0cpoly;
    private Polygon p0dpoly;
    private Polygon p1apoly;
    private Polygon p1bpoly;
    private Polygon p1cpoly;
    private Polygon p1dpoly;


    private void makePolygons(int width, int height){
        //１体の時のポリゴン
        this.p0poly = new Polygon();
        this.p1poly = new Polygon();
        p0poly.addPoint(width/2, 0);
        p0poly.addPoint(0, height);
        p0poly.addPoint(width, height);

        p1poly.addPoint(width/2, height);
        p1poly.addPoint(0, 0);
        p1poly.addPoint(width, 0);


        //4体までのポリゴン
        this.p00poly = new Polygon();
        this.p01poly = new Polygon();
        this.p02poly = new Polygon();
        this.p03poly = new Polygon();
        this.p10poly = new Polygon();
        this.p11poly = new Polygon();
        this.p12poly = new Polygon();
        this.p13poly = new Polygon();
        p00poly.addPoint(width/4, height/2);
        p00poly.addPoint(0, height);
        p00poly.addPoint(width/2, height);
        p01poly.addPoint((width/4)*3, height/2);
        p01poly.addPoint(width/2 , height);
        p01poly.addPoint(width, height);
        p02poly.addPoint((width/4)*3, 0);
        p02poly.addPoint(width/2, height/2);
        p02poly.addPoint(width, height/2);
        p03poly.addPoint(width/4, 0);
        p03poly.addPoint(0, height/2);
        p03poly.addPoint(width/2, height/2);

        p10poly.addPoint(0, 0);
        p10poly.addPoint(width/2, 0);
        p10poly.addPoint(width/4, height/2);
        p11poly.addPoint(width/2, 0);
        p11poly.addPoint(width, 0);
        p11poly.addPoint((width/4)*3, height/2);
        p12poly.addPoint(width/2, height/2);
        p12poly.addPoint(width, height/2);
        p12poly.addPoint((width/4)*3, height);
        p13poly.addPoint(0, height/2);
        p13poly.addPoint(width/2, height/2);
        p13poly.addPoint(width/4, height);

        //5,6体のときのポリゴン
        this.p0apoly = new Polygon();
        this.p0bpoly = new Polygon();
        this.p0cpoly = new Polygon();
        this.p0dpoly = new Polygon();
        this.p1dpoly = new Polygon();
        this.p1cpoly = new Polygon();
        this.p1bpoly = new Polygon();
        this.p1apoly = new Polygon();

        int lw = width/2 - width/3;
        int lc = (lw+width/2)/2;
        int rw = width/2 + width/3;
        int rc = (rw+width/2)/2;

        p0apoly.addPoint(lw, height);
        p0apoly.addPoint(lc, (height/3)*2);
        p0apoly.addPoint(width/2, height);
        p0bpoly.addPoint(rw, height);
        p0bpoly.addPoint(rc, (height/3)*2);
        p0bpoly.addPoint(width/2, height);

        p0dpoly.addPoint(lw, (height/3)*2);
        p0dpoly.addPoint(lc, height/3);
        p0dpoly.addPoint(width/2, (height/3)*2);
        p0cpoly.addPoint(rw, (height/3)*2);
        p0cpoly.addPoint(rc, height/3);
        p0cpoly.addPoint(width/2, (height/3)*2);

        p1cpoly.addPoint(lw, height/3);
        p1cpoly.addPoint(lc, (height/3)*2);
        p1cpoly.addPoint(width/2, height/3);
        p1dpoly.addPoint(rw, height/3);
        p1dpoly.addPoint(rc, (height/3)*2);
        p1dpoly.addPoint(width/2, height/3);

        p1apoly.addPoint(lw, 0);
        p1apoly.addPoint(lc, height/3);
        p1apoly.addPoint(width/2, 0);
        p1bpoly.addPoint(rw, 0);
        p1bpoly.addPoint(rc, height/3);
        p1bpoly.addPoint(width/2, 0);



    }

    public void setBorderCell(boolean b){
        this.isBorder = b;
    }

    public void setObstacleCell(boolean b){
        this.hasObstacle = b;
    }

    public void setTowerCell(boolean t){
        this.towerCell = t;
    }

    public void setTowerHas(int player){
        this.towerHas = player;
    }

    public void setHonjin(int player){
        this.honjinID = player;
    }

    private int p0UnitCount(){
        int c = 0;
        for(boolean b:player0Units){
            if(b) {c++;}
        }
        return c;
    }
    private int p1UnitCount(){
        int c = 0;
        for(boolean b:player1Units){
            if(b) {c++;}
        }
        return c;

    }
    private int unitCount(){
        return p0UnitCount()+p1UnitCount();
    }

    public void setUnit(int teamId,int unitId){
        if(teamId == 0) {
            player0Units[unitId] = true;
        } else if(teamId == 1) {
            player1Units[unitId] = true;
        }
    }
    public void crearUnit(){
        for(int i=0;i<4;i++){
            player0Units[i] = false;
            player1Units[i] = false;
        }
        this.CurrentModels = new Polygon[0];
        this.PlayerOfModels = new int[0];
        this.UnitIDOfModels = new int[0];
    }


    private Polygon[] CurrentModels;
    private int[] PlayerOfModels;
    private int[] UnitIDOfModels;
    @Override
    public void paint(Graphics g){
        if(this.unitCount() == 1){
            this.CurrentModels = new Polygon[1];
            this.PlayerOfModels = new int[1];
            this.UnitIDOfModels = new int[1];
            if(this.p0UnitCount() == 1){
                //プレイヤー０のユニット１つのみがいる
                for(int i=0;i<4;i++){
                    if(this.player0Units[i]){
                        this.UnitIDOfModels[0] = i;
                        this.CurrentModels[0] = p0poly;
                        this.PlayerOfModels[0] = 0;
                    }
                }
            } else {
                //プレイヤー１のユニット１つのみがいる
                for(int i=0;i<4;i++){
                    if(this.player1Units[i]){
                        this.UnitIDOfModels[0] = i;
                        this.CurrentModels[0] = p1poly;
                        this.PlayerOfModels[0] = 1;
                    }
                }
            }
        } else if(this.unitCount() == 2){
            this.CurrentModels = new Polygon[2];
            this.PlayerOfModels = new int[2];
            this.UnitIDOfModels = new int[2];
            if(this.p1UnitCount() == 0){
                //全てプレイヤー０のユニット
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p01poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
            } else if(this.p1UnitCount() == 1){
                //プレイヤー０のユニットが1つ　１が1つ
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p10poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 1;
            } else if(this.p1UnitCount() == 2){
                //プレイヤー０のユニットが２つ
                this.CurrentModels[0] = p10poly;
                this.CurrentModels[1] = p11poly;
                this.PlayerOfModels[0] = 1;
                this.PlayerOfModels[1] = 1;
            }
            int mc = 0;
            for(int i=0;i<4;i++){
                if(this.player0Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
            for(int i=0;i<4;i++){
                if(this.player1Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }

        } else if(this.unitCount() == 3){
            this.CurrentModels = new Polygon[3];
            this.PlayerOfModels = new int[3];
            this.UnitIDOfModels = new int[3];
            if(this.p1UnitCount() == 0){
                //全てプレイヤー０のユニット
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p01poly;
                this.CurrentModels[2] = p02poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 0;
            } else if(this.p1UnitCount() == 1){
                //プレイヤー０のユニットが2つ　１が1つ
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p01poly;
                this.CurrentModels[2] = p10poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 1;
            } else if(this.p1UnitCount() == 2){
                //プレイヤー０のユニットが1つ　１が２つ
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p10poly;
                this.CurrentModels[2] = p11poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 1;
                this.PlayerOfModels[2] = 1;
            } else if(this.p1UnitCount() == 3){
                //プレイヤー０のユニットが0が３つ
                this.CurrentModels[0] = p10poly;
                this.CurrentModels[1] = p11poly;
                this.CurrentModels[2] = p12poly;
                this.PlayerOfModels[0] = 1;
                this.PlayerOfModels[1] = 1;
                this.PlayerOfModels[2] = 1;
            }
            int mc = 0;
            for(int i=0;i<4;i++){
                if(this.player0Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
            for(int i=0;i<4;i++){
                if(this.player1Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
        } else if(this.unitCount() == 4){
            this.CurrentModels = new Polygon[4];
            this.PlayerOfModels = new int[4];
            this.UnitIDOfModels = new int[4];
            if(this.p1UnitCount() == 0){
                //全てプレイヤー０のユニット
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p01poly;
                this.CurrentModels[2] = p02poly;
                this.CurrentModels[3] = p03poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 0;
                this.PlayerOfModels[3] = 0;
            } else if(this.p1UnitCount() == 1){
                //プレイヤー０のユニットが３つ　１が１つ
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p01poly;
                this.CurrentModels[2] = p02poly;
                this.CurrentModels[3] = p10poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 0;
                this.PlayerOfModels[3] = 1;
            } else if(this.p1UnitCount() == 2){
                //プレイヤー０のユニットが２つ　１が２つ
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p01poly;
                this.CurrentModels[2] = p10poly;
                this.CurrentModels[3] = p11poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 1;
                this.PlayerOfModels[3] = 1;
            } else if(this.p1UnitCount() == 3){
                //プレイヤー０のユニットが１つ　１が３つ
                this.CurrentModels[0] = p00poly;
                this.CurrentModels[1] = p10poly;
                this.CurrentModels[2] = p11poly;
                this.CurrentModels[3] = p12poly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 1;
                this.PlayerOfModels[2] = 1;
                this.PlayerOfModels[3] = 1;
            } else {
                //全てプレイヤー１のユニット
                this.CurrentModels[0] = p10poly;
                this.CurrentModels[1] = p11poly;
                this.CurrentModels[2] = p12poly;
                this.CurrentModels[3] = p13poly;
                this.PlayerOfModels[0] = 1;
                this.PlayerOfModels[1] = 1;
                this.PlayerOfModels[2] = 1;
                this.PlayerOfModels[3] = 1;
            }

            int mc = 0;
            for(int i=0;i<4;i++){
                if(this.player0Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
            for(int i=0;i<4;i++){
                if(this.player1Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
        } else if(this.unitCount() == 5){
            this.CurrentModels = new Polygon[5];
            this.PlayerOfModels = new int[5];
            this.UnitIDOfModels = new int[5];
            if(this.p1UnitCount() == 1){
                //プレイヤー０のユニット４つとプレイヤー１のユニット１つ
                this.CurrentModels[0] = p0apoly;
                this.CurrentModels[1] = p0bpoly;
                this.CurrentModels[2] = p0cpoly;
                this.CurrentModels[3] = p0dpoly;
                this.CurrentModels[4] = p1apoly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 0;
                this.PlayerOfModels[3] = 0;
                this.PlayerOfModels[4] = 1;
            } else if(this.p1UnitCount() == 2){
                //プレイヤー０のユニットが３つ　１が２つ
                this.CurrentModels[0] = p0apoly;
                this.CurrentModels[1] = p0bpoly;
                this.CurrentModels[2] = p0cpoly;
                this.CurrentModels[3] = p1apoly;
                this.CurrentModels[4] = p1bpoly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 0;
                this.PlayerOfModels[3] = 1;
                this.PlayerOfModels[4] = 1;
            } else if(this.p1UnitCount() == 3){
                //プレイヤー０のユニットが２つ　１が３つ
                this.CurrentModels[0] = p0apoly;
                this.CurrentModels[1] = p0bpoly;
                this.CurrentModels[2] = p1apoly;
                this.CurrentModels[3] = p1bpoly;
                this.CurrentModels[4] = p1cpoly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 1;
                this.PlayerOfModels[3] = 1;
                this.PlayerOfModels[4] = 1;
            } else if(this.p1UnitCount() == 4){
                //プレイヤー０のユニットが１つ　１が４つ
                this.CurrentModels[0] = p0apoly;
                this.CurrentModels[1] = p1apoly;
                this.CurrentModels[2] = p1bpoly;
                this.CurrentModels[3] = p1cpoly;
                this.CurrentModels[4] = p1dpoly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 1;
                this.PlayerOfModels[2] = 1;
                this.PlayerOfModels[3] = 1;
                this.PlayerOfModels[4] = 1;
            }
            int mc = 0;
            for(int i=0;i<4;i++){
                if(this.player0Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
            for(int i=0;i<4;i++){
                if(this.player1Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
        } else if(this.unitCount() == 6){
            this.CurrentModels = new Polygon[6];
            this.PlayerOfModels = new int[6];
            this.UnitIDOfModels = new int[6];
            if(this.p1UnitCount() == 2){
                //プレイヤー０のユニット４つとプレイヤー１のユニット２つ
                this.CurrentModels[0] = p0apoly;
                this.CurrentModels[1] = p0bpoly;
                this.CurrentModels[2] = p0cpoly;
                this.CurrentModels[3] = p0dpoly;
                this.CurrentModels[4] = p1apoly;
                this.CurrentModels[5] = p1bpoly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 0;
                this.PlayerOfModels[3] = 0;
                this.PlayerOfModels[4] = 1;
                this.PlayerOfModels[5] = 1;
            } else if(this.p1UnitCount() == 3){
                //プレイヤー０のユニットが３つ　１が３つ
                this.CurrentModels[0] = p0apoly;
                this.CurrentModels[1] = p0bpoly;
                this.CurrentModels[2] = p0cpoly;
                this.CurrentModels[3] = p1apoly;
                this.CurrentModels[4] = p1bpoly;
                this.CurrentModels[5] = p1cpoly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 0;
                this.PlayerOfModels[3] = 1;
                this.PlayerOfModels[4] = 1;
                this.PlayerOfModels[5] = 1;
            } else if(this.p1UnitCount() == 4){
                //プレイヤー０のユニットが２つ　１が４つ
                this.CurrentModels[0] = p0apoly;
                this.CurrentModels[1] = p0bpoly;
                this.CurrentModels[2] = p1apoly;
                this.CurrentModels[3] = p1bpoly;
                this.CurrentModels[4] = p1cpoly;
                this.CurrentModels[5] = p1dpoly;
                this.PlayerOfModels[0] = 0;
                this.PlayerOfModels[1] = 0;
                this.PlayerOfModels[2] = 1;
                this.PlayerOfModels[3] = 1;
                this.PlayerOfModels[4] = 1;
                this.PlayerOfModels[5] = 1;
            }
            int mc = 0;
            for(int i=0;i<4;i++){
                if(this.player0Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
            for(int i=0;i<4;i++){
                if(this.player1Units[i]){
                    this.UnitIDOfModels[mc] = i;
                }
            }
        }

    }

    /** 自分のユニットをクリックした際に何番のユニットなのかを探す */
    public int getUnitIDof(Point cpos,int MyID){
        int hitModelID = -1;
        if(this.CurrentModels != null){
            for(int i=0;i<this.CurrentModels.length;i++){
                if(this.CurrentModels[i].contains(cpos)){
                    hitModelID = i;
                }
            }
            if(hitModelID != -1){
                int pid = this.PlayerOfModels[hitModelID];
                if(pid == MyID){
                    //自分のユニットの場合ユニット番号を取得して返却
                    return this.UnitIDOfModels[hitModelID];
                }
            }
        }
        return -1;
    }

    /** Mouseでフィールドが押された場合に呼び出される */
    public void fieldClicked(int x, int y){
        //this.mainFiled.addMessage("("+x+","+y + ")がクリックされました。");
        this.mainFiled.selectPoint(x,y);

    }

    /** Mouseでユニットが押された場合に呼び出される */
    public void unitClicked(int unitID){
        this.mainFiled.selectUnit(unitID);

    }



}
