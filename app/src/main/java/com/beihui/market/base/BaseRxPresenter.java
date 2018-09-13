package com.beihui.market.base;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.CallSuper;

import com.beihui.market.BuildConfig;
import com.beihui.market.util.LogUtils;

import java.net.SocketTimeoutException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public abstract class BaseRxPresenter implements BasePresenter {
    private static Handler DEBUG_MAIN_THREAD_HANDLER;
    /**
     * 用CompositeDisposable来管理订阅事件disposable，
     * 然后在acivity销毁的时候，调用compositeDisposable.dispose()就可以切断所有订阅事件，防止内存泄漏。
     */
    private CompositeDisposable compositeDisposable;

    protected void addDisposable(Disposable disposable) {
        if (compositeDisposable == null || compositeDisposable.isDisposed()) {
            compositeDisposable = new CompositeDisposable();
        }
        compositeDisposable.add(disposable);
    }

    @Override
    public void onStart() {
    }

    @CallSuper
    @Override
    public void onDestroy() {
        if (compositeDisposable != null && !compositeDisposable.isDisposed()) {
            compositeDisposable.clear();
        }
        compositeDisposable = null;
    }

    protected void logError(Object tag, final Throwable throwable) {
        final String tagStr = tag.getClass().getSimpleName();
        LogUtils.w(tagStr, "error " + throwable);
        if (BuildConfig.API_ENV) {
            //debug模式下把异常在主线程重新抛出，方便打印
            if (DEBUG_MAIN_THREAD_HANDLER == null) {
                DEBUG_MAIN_THREAD_HANDLER = new Handler(Looper.getMainLooper());
            }
            DEBUG_MAIN_THREAD_HANDLER.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        throw throwable;
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    protected String generateErrorMsg(Throwable throwable) {
        if (BuildConfig.API_ENV) {
            return throwable != null ? throwable.getMessage() : "未知错误";
        } else if (throwable instanceof SocketTimeoutException) {
            return "网络连接超时";
        } else {
            return "网络错误";
        }
    }
}