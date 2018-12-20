package com.beiwo.qnejqaz.helper.updatehelper;


public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}