package com.beihui.market.base;

import javax.inject.Inject;

/**
 * Created by Administrator on 2016/11/15.
 */

public abstract class BaseRVFragment<T1 extends BaseContract.BasePresenter> extends BaseFragment {

    @Inject
    protected T1 mPresenter;

    public final void attachView() {
        if (mPresenter != null)
            mPresenter.attachView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null)
            mPresenter.detachView();
    }
}
