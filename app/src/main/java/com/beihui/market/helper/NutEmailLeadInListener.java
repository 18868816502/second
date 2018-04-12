package com.beihui.market.helper;

import com.beihui.market.ui.busevents.UserLogoutEvent;

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

    @Subscribe
    public void onUserLogout(UserLogoutEvent event) {
        //用户切换时直接放弃对导入任务的监听
        EventBus.getDefault().unregister(this);
        curMsg = null;
    }

    public void syncCurrentTask() {
        //如果未注册任务监听，则注册
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        //注册新的listener之后，如果当前有任务进行，则同步通知任务进度
        if (curMsg != null) {
            dispatchProgressEvent(curMsg.progress);
        }
    }

    public void addOnLeadInProgressListener(OnLeadInProgressListener listener) {
        if (onLeadInProgressListenerList == null) {
            onLeadInProgressListenerList = new ArrayList<>();
        }
        onLeadInProgressListenerList.add(listener);
    }

    public void removeInLeadInProgressListener(OnLeadInProgressListener listener) {
        if (onLeadInProgressListenerList != null) {
            onLeadInProgressListenerList.remove(listener);
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
