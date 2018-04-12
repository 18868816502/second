package com.beihui.market.helper;

import android.util.Log;

import com.beihui.market.App;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.ui.busevents.UserLogoutEvent;
import com.beihui.market.util.RxUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import app.u51.com.newnutsdk.net.msg.CrawlerStatusMessage;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class NutEmailLeadInListener {

    private final String TAG = NutEmailLeadInListener.class.getSimpleName();

    private static NutEmailLeadInListener sInstance;

    private List<OnLeadInProgressListener> onLeadInProgressListenerList;
    private CrawlerStatusMessage curMsg;

    private boolean hasEnterResult;
    private String lastCheckedEmail;

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

    public void checkLeadInResult(final OnCheckLeadInResultListener listener) {
        if (hasEnterResult) {
            Disposable dis = Api.getInstance().pollLeadInResult(UserHelper.getInstance(App.getInstance()).getProfile().getId(), lastCheckedEmail)
                    .compose(RxUtil.<ResultEntity<Boolean>>io2main())
                    .subscribe(new Consumer<ResultEntity<Boolean>>() {
                                   @Override
                                   public void accept(ResultEntity<Boolean> resultEntity) throws Exception {
                                       if (resultEntity.isSuccess()) {
                                           listener.onCheckLeadInResult(resultEntity.getData());
                                       }
                                   }
                               },
                            new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Log.e(TAG, "throwable " + throwable);
                                }
                            });
            hasEnterResult = false;
        }
    }

    @Subscribe
    public void onLeadingInEvent(CrawlerStatusMessage msg) {
        curMsg = msg;
        lastCheckedEmail = curMsg.mailName;
        dispatchProgressEvent(curMsg.progress);
        if (msg.msgType.equals("fail")) {
            dispatchFinishedEvent(false);
        } else if (msg.msgType.equals("success")) {
            dispatchFinishedEvent(true);
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

    public void hasEnterResult() {
        hasEnterResult = true;
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

    public interface OnCheckLeadInResultListener {
        void onCheckLeadInResult(boolean success);
    }
}
