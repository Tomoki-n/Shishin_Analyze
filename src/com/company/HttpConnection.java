package com.company;



import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.w3c.dom.*;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

/**
 * Created by tomoki-n on 5/17/15.
 */
public class HttpConnection {

   public String Game_id = "";



    public HttpConnection() throws IOException {

   }

    public synchronized boolean GetGameId(String user0, String user1) throws IOException {

        URL apiUrl = new URL("http://133.242.149.177/user/regist/" + user0 + "/" + user1 + "/" );

        System.out.println(apiUrl);
        //webから取得していく
        String line, json = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(apiUrl.openStream(), "UTF-8"));
        while ((line = reader.readLine()) != null) {
            json += line;
        }
        reader.close();

        // JsonFactoryの生成
        JsonFactory factory = new JsonFactory();
        // JsonParserの取得
        JsonParser parser = factory.createJsonParser(json);

        //JSONのパース処理
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String name = parser.getCurrentName();
            if (name != null) {
                parser.nextToken();
                if (name.equals("Id")) {
                    //名前
                    this.setId(parser.getText());

                } else {
                    //想定外のものは無視して次へ
                    parser.skipChildren();
                }
            }
        }

        if (Game_id != "")
            return true;
        else
            return false;
    }



    public synchronized boolean SendScore(String game_id, int turn,int user0_score,int user1_score) throws IOException {

        URL apiUrl = new URL("http://133.242.149.177/score/regist/" + game_id +"/"+ turn + "/" + user0_score + "/" + user1_score + "/" );

        System.out.println(apiUrl);
        //webから取得していく
        String line, json = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(apiUrl.openStream(), "UTF-8"));
        while ((line = reader.readLine()) != null) {
            json += line;
        }
        reader.close();

        // JsonFactoryの生成
        JsonFactory factory = new JsonFactory();
        // JsonParserの取得
        JsonParser parser = factory.createJsonParser(json);

        //JSONのパース処理
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String name = parser.getCurrentName();
            if (name != null) {
                parser.nextToken();
                if (name.equals("Id")) {
                    //名前
                    if (parser.getText().equals("OK")) {
                        return true;
                    }
                } else {
                    //想定外のものは無視して次へ
                    parser.skipChildren();
                }
            }
        }

            return false;
    }



    public synchronized boolean SendEndScore(String game_id ,int turn,int user0_score,int user1_score) throws IOException {

        URL apiUrl = new URL("http://133.242.149.177/score/e_score/" + game_id +"/"+ turn + "/" + user0_score + "/" + user1_score + "/" );

        System.out.println(apiUrl);
        //webから取得していく
        String line, json = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(apiUrl.openStream(), "UTF-8"));
        while ((line = reader.readLine()) != null) {
            json += line;
        }
        reader.close();

        // JsonFactoryの生成
        JsonFactory factory = new JsonFactory();
        // JsonParserの取得
        JsonParser parser = factory.createJsonParser(json);

        //JSONのパース処理
        while (parser.nextToken() != JsonToken.END_OBJECT) {
            String name = parser.getCurrentName();
            if (name != null) {
                parser.nextToken();
                if (name.equals("Id")) {
                    //名前
                    if (parser.getText().equals("OK")) {
                        return true;
                    }
                } else {
                    //想定外のものは無視して次へ
                    parser.skipChildren();
                }
            }
        }

        return false;
    }


    public void setId(String id) {
        this.Game_id = id;
    }
}




