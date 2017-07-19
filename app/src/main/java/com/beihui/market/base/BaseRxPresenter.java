package com.beihui.market.base;


import android.support.annotation.CallSuper;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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
}
