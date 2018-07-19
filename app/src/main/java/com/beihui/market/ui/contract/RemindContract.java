package com.beihui.market.ui.contract;

import com.beihui.market.base.BasePresenter;
import com.beihui.market.base.BaseView;

/**
 * Copyright: kaola (C)2018
 * FileName: RemindContract
 * Author: jiang
 * Create on: 2018/7/18 15:28
 * Description: TODO
 */
public interface RemindContract {

    interface Presenter extends BasePresenter {
        void repaymentTime();

        void pushRemind();

        void messageRemind();
    }

    interface View extends BaseView<Presenter> {
        void showRepaymentTime();

        void selectPushRemindType();

        void selectmessageRemindType();

    }


}
