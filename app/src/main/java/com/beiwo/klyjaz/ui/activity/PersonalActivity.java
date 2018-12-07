package com.beiwo.klyjaz.ui.activity;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;

public class PersonalActivity extends BaseComponentActivity {

    @BindView(R.id.parent)
    LinearLayout parentView;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.navigate)
    ImageView ivBack;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    @Override
    public int getLayoutId() {
        return R.layout.activity_personal;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }
}
