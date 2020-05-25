package com.herokuapp.tmess.svc;

import com.herokuapp.tmess.view.MsgView;

public class RunAsk implements Runnable {
    public static final int SLEEP_BETWEEN = 4096;
    public static final int SLEEP = 6144;

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
            try {
                while (msgSvc.isWorking()) {
                    thread.sleep(SLEEP_BETWEEN);
                    System.out.println(".");
                }
                System.out.println("Before fetchLastMsgs.");
                msgSvc.fetchLastMsgs(MsgView.MAIL, MsgView.TO);
                //thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void notifyRunAsk() {
        thread.notify();
    }
}
