package com.herokuapp.tmess.svc;

import com.herokuapp.tmess.view.MsgView;

public class RunAsk implements Runnable {
    public static final int SLEEP = 1024;

    private final MsgSvc msgSvc;
    private Thread thread;

    public RunAsk(MsgSvc msgSvc) {
        this.msgSvc = msgSvc;
        thread = new Thread(this);
    }

    public void startThread() {
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            msgSvc.fetchLastMsgs(MsgView.MAIL, MsgView.TO);
            try {
                if (msgSvc.isWorking()) {
                    thread.sleep(SLEEP);
                    System.out.println(".");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void notifyRunAsk() {
        thread.notify();
    }
}
