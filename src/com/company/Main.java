package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {

    private String address;
    private static int GAME_STATUS = 0;

    public static void main(String[] args) {

        System.out.println("Starting Program");
        Connection connection = new Connection();


        while (true){
            switch (GAME_STATUS) {
                case 0: {
                    connection.init_address();
                    GAME_STATUS = 1;
                }
                case 1:{


                }

                default:
                    break;

            }
            break;
        }











    }

}
