package com.herokuapp.tmess.entity;

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
}
