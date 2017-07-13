package com.beihui.market.ui.activity;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.beihui.market.R;
import com.beihui.market.base.BaseActivity;
import com.beihui.market.component.AppComponent;
import com.beihui.market.ui.adapter.AnnouncementAdapter;

import butterknife.BindView;

public class AnnouncementActivity extends BaseActivity {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private AnnouncementAdapter adapter;


    @Override
    public int getLayoutId() {
        return R.layout.activity_announcement;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AnnouncementAdapter();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }
}
