package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;


public class ResetPsdActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_reset_psd;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }
}
