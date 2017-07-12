package com.beihui.market.ui.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.component.AppComponent;
import com.beihui.market.component.DaggerMainComponent;
import com.beihui.market.ui.adapter.NewsRVAdapter;
import com.beihui.market.ui.contract.Main1Contract;
import com.beihui.market.ui.rvdecoration.NewsItemDeco;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TabNewsFragment extends BaseTabFragment implements Main1Contract.View {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private NewsRVAdapter adapter;

    public static TabNewsFragment newInstance() {
        TabNewsFragment f = new TabNewsFragment();
        Bundle b = new Bundle();
        b.putString("type", "TabLoanFragment");
        f.setArguments(b);
        return f;
    }

    @Override
    public void attachView() {

    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_news;
    }

    @Override
    public void configViews() {
        adapter = new NewsRVAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        float density = getContext().getResources().getDisplayMetrics().density;
        int padding = (int) (density * 8);
        recyclerView.addItemDecoration(new NewsItemDeco((int) (density * 0.5), padding, padding));
    }

    @Override
    public void initDatas() {
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            tempList.add("" + i);
        }
        adapter.notifyNewsSetChanged(tempList);
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerMainComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    public void showError(String err) {
        dismissDialog();
    }

    @Override
    public void complete() {
        dismissDialog();
    }


}
