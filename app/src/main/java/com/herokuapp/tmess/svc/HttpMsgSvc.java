package com.herokuapp.tmess.svc;

import com.herokuapp.tmess.entity.Msg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HttpMsgSvc implements Runnable {
    public static final String URL_START = "https://s3spring.herokuapp.com/tMess";

    private HttpURLConnection connection;
    private HttpMsgAfterRead afterRead;
    private String sendWhat;

    public boolean open(
            String urlString, String method,
            String sendWhat, HttpMsgAfterRead afterRead) {
        this.sendWhat = sendWhat;
        this.afterRead = afterRead;

        boolean outcome = false;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            outcome = true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outcome;
    }

    public String paramsByTwoChattersAndTimeAfter(
            String chatter1, String chatter2, long after) {
        return "chatter1=" + chatter1 +
                "&chatter2=" + chatter2 +
                "&after=" + after;
    }

    private boolean send(String string) {
        boolean outcome = true;

        String method = connection.getRequestMethod();
        if ((method.toUpperCase().equals("POST")) ||
                (method.toUpperCase().equals("PUT")) ||
                (method.toUpperCase().equals("PATCH"))
        ) {
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");
            try {
                DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                out.writeBytes(string);
                out.flush();
                out.close();
            } catch (IOException e) {
                outcome = false;
                e.printStackTrace();
            }
        }

        return outcome;
    }

    public List<Msg> makeList(String jsonString) {
        List<Msg> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Msg msg = Msg.makeMsg(jsonObject);
                list.add(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Msg makeMsg(String jsonString) {
        Msg msg = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            msg = Msg.makeMsg(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return msg;
    }

    public void sendAndRead() {
        Thread thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        boolean outcome = send(sendWhat);
        String answer = "[]";

        if (outcome) {
            try {
                int code = connection.getResponseCode();
                System.out.println("Response Code: " + code + ".");
                if (code < 300) {
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );

                    StringBuilder builder = new StringBuilder();
                    String string = null;
                    while ((string = reader.readLine()) != null) {
                        System.out.println(string);
                        builder.append(string);
                    }
                    reader.close();
                    answer = (builder.length() > 0) ? builder.toString() : "[]";
                } else {
                    outcome = false;
                    answer = "Server returned code " + code + ".";
                }
            } catch (IOException e) {
                outcome = false;
                answer = "Could not get response code or read input stream.";
                e.printStackTrace();
            }
        }

        if (outcome) {
            afterRead.onRight(answer);
        } else {
            afterRead.onWrong(answer);
        }

        connection.disconnect();
    }
}
