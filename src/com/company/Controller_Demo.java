package com.company;

/**
 * Created by tomoki-n on 2015/04/16.
 */
public class Controller_Demo  {

    Boardinfo info;


    Controller_Demo(Boardinfo Binfo){
        info = Binfo;

    }












    /**  コマの移動命令 **/
    public int[] UnitOrder(){
        int unit[] = new int[3];

        /** 0:コマの種類　1:X座標　2:Y座標 **/
        unit[0]=0;
        unit[1]=0;
        unit[2]=0;

        return unit;
    }

}
