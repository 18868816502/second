package com.beihui.market.ui.activity;


import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;

public class HelperAndFeedbackActivity extends BaseComponentActivity implements View.OnClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.help)
    TextView helpTv;
    @BindView(R.id.feedback)
    TextView feedbackTv;

    private View selected;

    @Override
    public int getLayoutId() {
        return R.layout.activity_helper_and_feedback;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        helpTv.setOnClickListener(this);
        feedbackTv.setOnClickListener(this);
    }

    @Override
    public void initDatas() {
        select(helpTv);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onClick(View v) {
        if (v != selected) {
            select(v);
        }
    }

    private void select(View view) {
        if (selected != null) {
            selected.setSelected(false);
        }
        selected = view;
        if (selected != null) {
            selected.setSelected(true);
        }
    }
}
