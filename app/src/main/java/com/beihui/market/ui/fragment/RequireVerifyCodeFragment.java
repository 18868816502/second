package com.beihui.market.ui.fragment;


import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseFragment;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class RequireVerifyCodeFragment extends BaseFragment {
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
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @OnClick(R.id.next_step)
    void onNextStepClicked() {

    }
}