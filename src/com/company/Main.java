import com.sun.tools.javac.comp.Check;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class Main {

    private static String addr;
    private static String ai1;
    private static String type1;

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
                    if (ai1 == "1"){
                        AI ai = new AI(addr,ai1,type1);
                    }
                    else if (ai1 == "2"){
                        AI1 ai = new AI1(addr,ai1,type1);
                    }
                    else if (ai1 == "3"){
                        AI2 ai = new AI2(addr,ai1,type1);
                    }
                    else if (ai1 == "4"){
                        AI3 ai = new AI3(addr,ai1,type1);
                    }
                    else {
                        AI ai = new AI(addr, ai1,type1);
                    }
                    break;
                }

                default:
                    break;

            }


        }

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
        System.out.println("AI:1.Higuchi 2.Nishinaka 3.Hosono 4.Demo \n");
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

    public static String Connect_type(){
        String type = null;
        System.out.println("0:CONNECT 1:RECONNECT \n");
        System.out.println("input NUMBER: ");
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        try{
            type = br.readLine();

        }catch(Exception e){
            AI_type();
        }
        type = ai1;
        return type;
    }


}


