package com.herokuapp.tmess.svc;

public interface HttpMsgAfterRead {
    void onRight(String answer);
    void onWrong(String answer);
}
