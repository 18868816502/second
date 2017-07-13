package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class EditJobGroupActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    View curSelectedView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_job_group;
    }

    @Override
    public void configViews() {
        setupToolBar(toolbar);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @OnClick({R.id.freelance, R.id.worker, R.id.household})
    void OnJobGroupItemClicked(View view) {
        if (view != curSelectedView) {
            if (curSelectedView != null) {
                curSelectedView.setSelected(false);
            }
            curSelectedView = view;
            curSelectedView.setSelected(true);
        }
    }
}
