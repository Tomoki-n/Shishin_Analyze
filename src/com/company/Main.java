package com.company;

import com.company.AI.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {

    private static String addr;
    private static String ai1;
    private static String type1;

    private static int GAME_STATUS = 0;

    public static void main(String[] args) throws InterruptedException, IOException {

        System.out.println("Starting Program");
       
        smain();

    }
    public static String init(){

        String address =null;

        System.out.println("input IP Address: ");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        try{

            addr = br.readLine();
            if(!checkIP(addr)){
                init();
            }
        }catch(Exception e){
            init();
        }

        System.out.println(addr);
        return addr;

    }

    public static String AI_type(){
        String type = null;
        System.out.println("AI:1.Higuchi 2.Nishinaka 3.Ishitani 4.Demo 5.CUI-Mode\n");
        System.out.println("input AI_NUMBER: ");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        try{
            ai1 = br.readLine();

        }catch(Exception e){
            AI_type();
        }
        type = ai1;
        return type;
    }

    public static boolean checkIP(String str) {
        String pattern = "^\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}$";
        return str.matches(pattern);
    }

    public static void smain() throws InterruptedException {
        if(GAME_STATUS ==3){
            System.exit(0);
        }

        switch (GAME_STATUS) {
            case 0: {
                addr = init();
                GAME_STATUS = 1;
                smain();
                break;
            }

            case 1:{
                ai1 = AI_type();
                GAME_STATUS = 2;
                smain();

                break;
            }
            case 2:{
                int ai2 = Integer.parseInt(ai1);
                if (ai2 == 1) {
                    AI ai = new AI0(addr, ai1, type1);

                }
                else if (ai2 == 2) {
                    AI1 ai = new AI1(addr, ai1, type1);


                }
                else if (ai2 == 3) {
                    AI2 ai = new AI2(addr, ai1, type1);

                }
                else if (ai2 == 4) {
                    AI3 ai = new AI3(addr, ai1, type1);

                }
                else if(ai2 == 5) {
                    CUI ai = new CUI(addr, ai1, type1);
                }

                GAME_STATUS = 3;
                break;
            }
            default:
                break;

        }


    }



}


