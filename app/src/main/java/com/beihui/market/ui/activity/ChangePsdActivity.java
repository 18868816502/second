package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePsdActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.origin_psd)
    EditText originPsdEt;
    @BindView(R.id.new_psd)
    EditText newPsdEt;
    @BindView(R.id.new_psd_confirm)
    EditText newPsdConfirmEt;


    @Override
    public int getLayoutId() {
        return R.layout.activity_change_psd;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick(R.id.confirm)
    void onConfirmClicked() {

    }
}
