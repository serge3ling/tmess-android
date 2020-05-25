package com.herokuapp.tmess.svc;

import com.herokuapp.tmess.entity.Msg;
import com.herokuapp.tmess.view.MsgView;

import java.util.ArrayList;
import java.util.List;

public class MsgSvc {
    private List<Msg> list = new ArrayList<>();
    private HttpMsgSvc httpMsgSvc;
    private HttpMsgAfterRead afterPost;
    private HttpMsgAfterRead afterGet;
    private RunAsk runAsk;
    private long after;
    private boolean working = false;

    public MsgSvc(HttpMsgSvc httpMsgSvc,
            HttpMsgAfterRead afterPost, HttpMsgAfterRead afterGet) {
        this.httpMsgSvc = httpMsgSvc;
        httpMsgSvc.setMsgSvc(this);
        this.afterPost = afterPost;
        this.afterGet = afterGet;

        runAsk = new RunAsk(this);
        /*list.add(new Msg(1, "Hello?",
                MsgView.TO, MsgView.MAIL, 1582664192002L));
        list.add(new Msg(2, "Hey, I'm here.",
                MsgView.MAIL, MsgView.TO, 1582664202002L));
        list.add(new Msg(3, "Ready?",
                MsgView.TO, MsgView.MAIL, 1582664212002L));
        list.add(new Msg(4, "Sure.",
                MsgView.MAIL, MsgView.TO, 1582664222002L));
        list.add(new Msg(5, "Alright. <b>Let's do it</b>.",
                MsgView.TO, MsgView.MAIL, 1582664232002L));*/
    }

    public List<Msg> fetchMsgs() {
        return list;
    }

    public void sendMsg(Msg msg) {
        setWorking(true);
        httpMsgSvc.go(HttpMsgSvc.URL_START, "POST", msg.toJsonString(), afterPost);
    }

    public void fetchLastMsgs(String chatter1, String chatter2/*, long after*/) {
        setWorking(true);
        String query = "?" + httpMsgSvc.paramsByTwoChattersAndTimeAfter(
                chatter1, chatter2, after);
        httpMsgSvc.go(HttpMsgSvc.URL_START + query,
                "GET", "", afterGet);
    }

    public synchronized long getAfter() {
        return after;
    }

    public synchronized void setAfter(long after) {
        this.after = after;
    }

    public synchronized void startRunAsk() {
        runAsk.startThread();
    }

    public synchronized boolean isWorking() {
        return working;
    }

    public synchronized void setWorking(boolean working) {
        this.working = working;
    }
}
