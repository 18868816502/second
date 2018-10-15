package com.beihui.market.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.social.bean.SocialTopicBean;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.activity.CommunityPublishActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.adapter.social.SocialRecommendAdapter;
import com.beihui.market.util.ParamsUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        initListener();
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
        pageNo ++;
        fetchData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        fetchData();
    }

    @OnClick(R.id.iv_publish)
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.iv_publish:
                if(UserHelper.getInstance(getActivity()).isLogin()) {
                    startActivity(new Intent(getActivity(), CommunityPublishActivity.class));
                }else{
                    UserAuthorizationActivity.launch(getActivity());
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void fetchData(){
        String userId = "";
;        if(UserHelper.getInstance(getActivity()).isLogin()){
            userId = UserHelper.getInstance(getActivity()).getProfile().getId();
        }
        Api.getInstance().queryRecommendTopic(userId,pageNo,pageSize)
                .compose(RxUtil.<ResultEntity<SocialTopicBean>>io2main())
                .subscribe(new Consumer<ResultEntity<SocialTopicBean>>() {
                               @Override
                               public void accept(ResultEntity<SocialTopicBean> result){
                                   if (result.isSuccess()) {
                                       if(1 == pageNo){
                                           refreshLayout.finishRefresh();
                                           adapter.setDatas(result.getData().getForum());
                                       }else{
                                           refreshLayout.finishLoadMore();
                                           adapter.appendDatas(result.getData().getForum());
                                       }
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    @SuppressLint("CheckResult")
    private void fetchData1(){
        String userId = "";
        ;        if(UserHelper.getInstance(getActivity()) != null){
            userId = UserHelper.getInstance(getActivity()).getProfile().getId();
        }
        Api.getInstance().queryRecommendTopic(ParamsUtils.generateRecommendTopicParams(userId,pageNo,pageSize))
                .compose(RxUtil.<ResultEntity<SocialTopicBean>>io2main())
                .subscribe(new Consumer<ResultEntity<SocialTopicBean>>() {
                               @Override
                               public void accept(ResultEntity<SocialTopicBean> result){
                                   if (result.isSuccess()) {
                                       if(1 == pageNo){
                                           refreshLayout.finishRefresh();
                                           adapter.setDatas(result.getData().getForum());
                                       }else{
                                           refreshLayout.finishLoadMore();
                                           adapter.appendDatas(result.getData().getForum());
                                       }
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }
}
