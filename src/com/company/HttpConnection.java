package com.company;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by tomoki-n on 5/17/15.
 */
public class HttpConnection {

   public int Game_id;

   public HttpConnection() throws IOException {

   }

    public synchronized boolean GetGameId(String user0,String user1){


    return true;
   }

   public synchronized boolean SendScore(int game_id, int turn,int user0_score,int user1_score){

    return true;
   }

    public synchronized boolean SendEndScore(int game_id ,int turn,int user0_score,int user1_score){

    return true;
    }


}




