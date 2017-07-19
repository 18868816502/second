package com.beihui.market.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

public class EditJobGroupActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    View curSelectedView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_job_group;
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
