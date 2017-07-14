package com.beihui.market.ui.fragment;


import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.busevents.AuthNavigationEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;

public class UserRegisterVerifyCodeFragment extends BaseFragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_register_verify_code;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.head_to_login})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_to_login:
                EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_HEAD_TO_LOGIN));
                break;
        }
    }
}
