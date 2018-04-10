package com.beihui.market.helper;

import android.util.Log;

import com.beihui.market.App;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.util.RxUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import app.u51.com.newnutsdk.net.msg.CrawlerStatusMessage;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

public class NutEmailLeadInListener {

    private static NutEmailLeadInListener sInstance;

    private List<OnLeadInProgressListener> onLeadInProgressListenerList;
    private CrawlerStatusMessage curMsg;
    private String curSubType;
    private boolean hasPollTask;

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

    void startPullTask(final String email) {
        Observable.interval(5, TimeUnit.SECONDS)
                //查询导入结果
                .flatMap(new Function<Long, ObservableSource<ResultEntity<Boolean>>>() {
                    @Override
                    public ObservableSource<ResultEntity<Boolean>> apply(Long aLong) throws Exception {
                        //30秒超时
                        if (aLong <= 6) {
                            return Api.getInstance().pollLeadInResult(UserHelper.getInstance(App.getInstance()).getProfile().getId(), email);
                        } else {
                            //轮询最长5秒钟，超时后当做注册失败处理
                            ResultEntity<Boolean> entity = new ResultEntity<>();
                            entity.setCode(1);
                            entity.setData(false);

                            Log.e("NutEmailLeadInListener", "轮询超时，失败处理");
                            return Observable.just(entity);
                        }
                    }
                })
                .compose(RxUtil.<ResultEntity<Boolean>>io2main())
                .subscribe(new Observer<ResultEntity<Boolean>>() {
                    Disposable disposable;

                    @Override
                    public void onSubscribe(Disposable d) {
                        hasPollTask = true;
                        disposable = d;
                    }

                    @Override
                    public void onNext(ResultEntity<Boolean> resultEntity) {
                        if (resultEntity.isSuccess()) {
                            //已经获取到数据，结束轮询
                            if (resultEntity.getData()) {
                                hasPollTask = false;
                                disposable.dispose();
                                dispatchFinishedEvent(true);
                            }
                        } else {
                            hasPollTask = false;
                            //失败则结束流程
                            disposable.dispose();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        hasPollTask = false;
                        Log.e("NutEmailLeadInListener", e.toString());
                        dispatchFinishedEvent(false);
                    }

                    @Override
                    public void onComplete() {
                        hasPollTask = false;
                        Log.e("NutEmailLeadInListener", "poll complete");
                        dispatchFinishedEvent(true);
                    }
                });
    }

    @Subscribe
    public void onLeadingInEvent(CrawlerStatusMessage msg) {
        curMsg = msg;
        curSubType = curMsg.subType;
        if (curMsg.progress == 100) {
            //sdk操作成功，轮询请求服务器是否取到数据
            if (msg.msgType.equals("success")) {
                dispatchProgressEvent(99);
                //开启轮询
                this.startPullTask(msg.mailName);
            } else {
                //失败直接跳转失败页面
                dispatchProgressEvent(100);
                dispatchFinishedEvent(false);
            }
            //任务完成后，清除状态
            curMsg = null;
        } else {
            dispatchProgressEvent(curMsg.progress);
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
        } else if (hasPollTask) {
            listener.onProgressChanged(99);
        }
    }

    public void removeInLeadInProgressListener(OnLeadInProgressListener listener) {
        if (onLeadInProgressListenerList != null) {
            onLeadInProgressListenerList.remove(listener);
        }
    }

    public boolean hasUnFinishedTask() {
        return curMsg != null || hasPollTask;
    }

    public String getCurTaskEmailSymbol() {
        return curSubType;
    }


    public interface OnLeadInProgressListener {
        void onProgressChanged(int progress);

        void onLeadInFinished(boolean success);
    }
}
