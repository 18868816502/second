package com.beiwo.klyjaz.tang.rx.observer;

import com.beiwo.klyjaz.tang.exception.BaseException;
import com.beiwo.klyjaz.tang.rx.RxErrorHandler;
import com.beiwo.klyjaz.util.LogUtils;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

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
    public static Queue<Disposable> disposables = new ConcurrentLinkedQueue<>();

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        disposables.add(d);
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

    public static void cancel() {
        if (disposables.size() > 0) {
            for (Disposable disposable : disposables) {
                if (disposable != null && !disposable.isDisposed()) {
                    disposable.dispose();
                    disposables.remove(disposable);
                }
            }
        }
    }
}