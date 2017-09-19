package com.iyoho.social.server;

public interface ServerCallback {
    void onSuccess(String result);
    void onFailure(int errorCode, String strMsg);

}
