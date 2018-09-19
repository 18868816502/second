package com.beihui.market.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.SocialTopicBean;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.social.SocialRecommendAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.fragment
 * @descripe 社区推荐页面
 * @time 2018/9/19 9:57
 */
public class SocialRecommendFragment extends BaseComponentFragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private SocialRecommendAdapter adapter;

    public static Fragment getInstance(){
        return new SocialRecommendFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_social;
    }

    @Override
    public void configViews() {
        adapter = new SocialRecommendAdapter(getActivity());
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void initDatas() {
        List<SocialTopicBean> mList = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            SocialTopicBean bean = new SocialTopicBean();
            mList.add(bean);
        }
        adapter.setDatas(mList);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
