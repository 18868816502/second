package com.beihui.market.ui.fragment;


import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.busevents.ResetPsdNavigationEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class RequireVerifyCodeFragment extends BaseComponentFragment {
    @BindView(R.id.phone_number)
    EditText phoneNumberEt;
    @BindView(R.id.verify_code)
    EditText verifyCodeEt;
    @BindView(R.id.fetch_text)
    TextView fetchTv;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_require_verify_code;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick(R.id.next_step)
    void onNextStepClicked() {
        EventBus.getDefault().post(new ResetPsdNavigationEvent());
    }
}
