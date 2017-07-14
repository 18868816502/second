package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.activity.ResetPsdActivity;
import com.beihui.market.ui.busevents.AuthNavigationEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.OnClick;


public class UserLoginFragment extends BaseFragment {
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_user_login;
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

    @OnClick({R.id.register, R.id.forget_psd})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register:
                EventBus.getDefault().post(new AuthNavigationEvent(AuthNavigationEvent.TAG_REGISTER));
                break;
            case R.id.forget_psd:
                Intent toResetPsd = new Intent(getActivity(), ResetPsdActivity.class);
                startActivity(toResetPsd);
                break;
        }
    }


}
