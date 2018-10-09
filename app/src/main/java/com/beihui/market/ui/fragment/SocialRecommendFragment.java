package com.beihui.market.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.social.bean.SocialTopicBean;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.CommunityPublishActivity;
import com.beihui.market.ui.adapter.social.SocialRecommendAdapter;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

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
    @BindView(R.id.iv_publish)
    ImageView ivPublish;
    private SocialRecommendAdapter adapter;
    private int pageSize = 30;
    private int pageNo = 1;

    public static SocialRecommendFragment getInstance(){
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
//        List<SocialTopicBean.ForumBean> mList = new ArrayList<>();
//        for(int i = 0 ; i < 10 ; i++){
//            SocialTopicBean.ForumBean bean = new SocialTopicBean.ForumBean();
//            mList.add(bean);
//        }
//        adapter.setDatas(mList);
//        initListener();
        fetchData();
    }

    private void initListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //向上
                if(dy < 0){
                    ivPublish.setVisibility(View.GONE);
                }else{
                    ivPublish.setVisibility(View.VISIBLE);
                }
            }
        });
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

    @OnClick(R.id.iv_publish)
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_publish:
                startActivity(new Intent(getActivity(), CommunityPublishActivity.class));
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void fetchData(){
        Api.getInstance().queryRecommendTopic(pageNo,pageSize)
                .compose(RxUtil.<ResultEntity<SocialTopicBean>>io2main())
                .subscribe(new Consumer<ResultEntity<SocialTopicBean>>() {
                               @Override
                               public void accept(ResultEntity<SocialTopicBean> result){
                                   if (result.isSuccess()) {
                                       adapter.setDatas(result.getData().getForum());
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                //Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }
}
