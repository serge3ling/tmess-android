package com.herokuapp.tmess.svc;

import com.herokuapp.tmess.entity.Msg;

import java.util.ArrayList;
import java.util.List;

public class MsgSvc {
    private List<Msg> list = new ArrayList<>();

    public MsgSvc() {
        list.add(new Msg(1, "Hello?",
                "axel@tmess.d54637.com", "unga@tmess.d54637.com", 1582664192002l));
        list.add(new Msg(2, "Hey, I'm here.",
                "unga@tmess.d54637.com", "axel@tmess.d54637.com", 1582664202002l));
        list.add(new Msg(3, "Ready?",
                "axel@tmess.d54637.com", "unga@tmess.d54637.com", 1582664212002l));
        list.add(new Msg(4, "Sure.",
                "unga@tmess.d54637.com", "axel@tmess.d54637.com", 1582664222002l));
        list.add(new Msg(5, "Alright. <b>Let's do it</b>.",
                "axel@tmess.d54637.com", "unga@tmess.d54637.com", 1582664232002l));
    }

    public List<Msg> fetchMsgs() {
        return list;
    }

    public void sendMsg(Msg msg) {
        list.add(msg);
    }
}
