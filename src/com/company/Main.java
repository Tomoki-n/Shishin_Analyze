package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {

    private static String addr;
    private static String ai1;
    private static int GAME_STATUS = 0;

    public static void main(String[] args) {

        System.out.println("Starting Program");


      for (int i=0; i<2; i++){
            switch (GAME_STATUS) {
                case 0: {
                    addr = init();
                    GAME_STATUS = 1;
                    break;
                }

                case 1:{
                    ai1 = AI_type();
                    GAME_STATUS = 2;
                }
                case 2: {
                    AI ai = new AI(addr,ai1);
                    break;
                }

                default:
                    break;

            }


        }

    }
    public static String init(){

        String address =null;

        System.out.print("input IP Address: ");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        try{
            addr = br.readLine();

        }catch(Exception e){
            init();
        }

        System.out.println(addr);
        return addr;

    }

    public static String AI_type(){
        String type = null;
        System.out.print("AI:1.Higuchi 2.Nishinaka 3.Hosono 4.Demo \n");
        System.out.print("input AI_NUMBER: ");
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

}
