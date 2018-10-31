package com.beiwo.klyjaz.tang.rx.observer;

import com.beiwo.klyjaz.tang.exception.BaseException;
import com.beiwo.klyjaz.tang.rx.RxErrorHandler;
import com.beiwo.klyjaz.util.LogUtils;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/18
 */

public abstract class ApiObserver<T> extends DefaultObserver<T> {
    private RxErrorHandler errorHandler = new RxErrorHandler();

    @Override
    public void onSubscribe(@NonNull Disposable d) {
    }

    @Override
    public void onError(@NonNull Throwable t) {
        BaseException exception = errorHandler.errorHandle(t);
        if (exception == null) {
            LogUtils.i("exception is null");
        } else {
            errorHandler.showError(exception);
        }
    }

    @Override
    public abstract void onNext(@NonNull T data);

    @Override
    public void onComplete() {
    }
}