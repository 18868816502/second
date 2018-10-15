package com.beiwo.klyjaz.helper.updatehelper;


public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
