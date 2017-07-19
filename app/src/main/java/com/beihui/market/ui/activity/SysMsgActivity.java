package com.beihui.market.ui.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.SysMsgAdapter;

import butterknife.BindView;

public class SysMsgActivity extends BaseComponentActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private SysMsgAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_sys_msg;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SysMsgAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
