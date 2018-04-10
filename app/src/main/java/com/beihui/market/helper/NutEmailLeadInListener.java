package com.beihui.market.helper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import app.u51.com.newnutsdk.net.msg.CrawlerStatusMessage;

public class NutEmailLeadInListener {

    private static NutEmailLeadInListener sInstance;

    private List<OnLeadInProgressListener> onLeadInProgressListenerList;
    private CrawlerStatusMessage curMsg;

    public static NutEmailLeadInListener getInstance() {
        if (sInstance == null) {
            synchronized (NutEmailLeadInListener.class) {
                if (sInstance == null) {
                    sInstance = new NutEmailLeadInListener();
                }
            }
        }
        return sInstance;
    }

    private NutEmailLeadInListener() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onLeadingInEvent(CrawlerStatusMessage msg) {
        curMsg = msg;
        dispatchProgressEvent(curMsg.progress);
        if (curMsg.progress == 100) {
            dispatchFinishedEvent(msg.msgType.equals("success"));
            //任务完成后，清除状态
            curMsg = null;
        }
    }

    public void addOnLeadInProgressListener(OnLeadInProgressListener listener) {
        if (onLeadInProgressListenerList == null) {
            onLeadInProgressListenerList = new ArrayList<>();
        }
        onLeadInProgressListenerList.add(listener);

        //注册新的listener之后，如果当前有任务进行，则同步通知任务进度
        if (curMsg != null) {
            listener.onProgressChanged(curMsg.progress);
            if (curMsg.progress == 100) {
                listener.onLeadInFinished(curMsg.msgType.equals("success"));
            }
        }
    }

    public void removeInLeadInProgressListener(OnLeadInProgressListener listener) {
        if (onLeadInProgressListenerList != null) {
            onLeadInProgressListenerList.remove(listener);
        }
    }

    void dispatchProgressEvent(int progress) {
        if (onLeadInProgressListenerList != null) {
            for (int i = 0; i < onLeadInProgressListenerList.size(); ++i) {
                onLeadInProgressListenerList.get(i).onProgressChanged(progress);
            }
        }
    }


    void dispatchFinishedEvent(boolean success) {
        if (onLeadInProgressListenerList != null) {
            for (int i = 0; i < onLeadInProgressListenerList.size(); ++i) {
                onLeadInProgressListenerList.get(i).onLeadInFinished(success);
            }
        }
    }

    public boolean hasUnFinishedTask() {
        return curMsg != null;
    }

    public String getCurTaskEmailSymbol() {
        return curMsg.subType;
    }


    public interface OnLeadInProgressListener {
        void onProgressChanged(int progress);

        void onLeadInFinished(boolean success);
    }
}
