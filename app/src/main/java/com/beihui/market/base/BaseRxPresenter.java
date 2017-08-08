package com.beihui.market.base;


import android.support.annotation.CallSuper;

import com.beihui.market.util.LogUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

public abstract class BaseRxPresenter implements BasePresenter {
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

    protected void logError(Object tag, Throwable throwable) {
        LogUtils.e(tag.getClass().getSimpleName(), "throwable " + throwable);
    }

    protected String generateErrorMsg(Throwable throwable) {
        if (throwable instanceof HttpException
                || throwable instanceof ConnectException) {
            return "网络错误";
        } else if (throwable instanceof SocketTimeoutException) {
            return "网络连接超时";
        }
        return throwable != null ? throwable.getMessage() : "未知错误";
    }
}
