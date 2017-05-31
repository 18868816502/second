package com.beihui.market.ui.contract;


import com.beihui.market.base.BaseContract;

/**
 * Created by Administrator on 2017/1/16.
 */

public interface Main1Contract {

    interface View extends BaseContract.BaseView {
    }

    interface Presenter<T> extends BaseContract.BasePresenter<T> {
    }
}
