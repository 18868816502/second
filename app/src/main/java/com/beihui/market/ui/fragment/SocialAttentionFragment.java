package com.beihui.market.ui.fragment;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.social.bean.SocialTopicBean;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.SocialTopicDetailActivity;
import com.beihui.market.ui.adapter.social.SocialAttentionAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
 * @descripe 社区关注页面
 * @time 2018/9/19 9:57
 */
public class SocialAttentionFragment extends BaseComponentFragment implements OnRefreshListener, OnLoadMoreListener,
        BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    private SocialAttentionAdapter adapter;

    public static Fragment getInstance(){
        return new SocialAttentionFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_social;
    }

    @Override
    public void configViews() {
        adapter = new SocialAttentionAdapter(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
        adapter.setOnItemClickListener(this);
    }

    @Override
    public void initDatas() {
        List<SocialTopicBean> mList = new ArrayList<>();
        for(int i = 0 ; i < 10 ; i++){
            SocialTopicBean bean = new SocialTopicBean();
//            bean.setName("高军");
//            bean.setTime("刚刚");
//            bean.setTitle("论坛获取积分、禁言、打赏、提问规则-贷款...");
//            bean.setContent("书内机柜变一变个月续租西。产局共可在没法度越发便面贴牌。轮成都管管徐昂要领不研祥中增加。菜之一星级已审理资源十层华人下富源");
//            bean.setPariseNum(3*i+"");
//            bean.setCommentNum(8*i+"");
            mList.add(bean);
        }
        adapter.notifySocialTopicChanged(mList);
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

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(new Intent(getActivity(),SocialTopicDetailActivity.class));
    }
}
