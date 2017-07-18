package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;

public class HelperAndFeedbackActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.help)
    TextView helpTv;
    @BindView(R.id.feedback)
    TextView feedbackTv;

    @Override
    public int getLayoutId() {
        return R.layout.activity_helper_and_feedback;
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
}
