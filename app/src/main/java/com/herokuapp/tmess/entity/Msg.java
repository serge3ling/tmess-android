package com.herokuapp.tmess.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class Msg {
    private long id;
    private String text;
    private String from;
    private String to;
    private long time;

    public Msg(long id, String text, String from, String to, long time) {
        this.id = id;
        this.text = text;
        this.from = from;
        this.to = to;
        this.time = time;
    }

    public static Msg makeMsg(JSONObject jsonObject) throws JSONException {
        return new Msg(jsonObject.getLong("id"),
                jsonObject.getString("text"),
                jsonObject.getString("from"),
                jsonObject.getString("to"),
                jsonObject.getLong("time")
        );
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public long getTime() {
        return time;
    }

    public String toJsonString() {
        String outcome = "{}";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("text", text);
            jsonObject.put("from", from);
            jsonObject.put("to", to);
            jsonObject.put("time", time);
            outcome = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return outcome;
    }

    @Override
    public String toString() {
        return "Msg: " + toJsonString();
    }
}
