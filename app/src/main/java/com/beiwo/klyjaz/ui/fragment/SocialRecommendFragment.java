package com.beiwo.klyjaz.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.social.bean.SocialTopicBean;
import com.beiwo.klyjaz.ui.activity.CommunityPublishActivity;
import com.beiwo.klyjaz.ui.activity.UserAuthorizationActivity;
import com.beiwo.klyjaz.ui.adapter.social.SocialRecommendAdapter;
import com.beiwo.klyjaz.umeng.NewVersionEvents;
import com.beiwo.klyjaz.util.ParamsUtils;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

    public static SocialRecommendFragment getInstance() {
        return new SocialRecommendFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_social;
    }

    @Override
    public void configViews() {
        adapter = new SocialRecommendAdapter(getActivity());
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(null);
        adapter.notifyDataSetChanged();
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadMoreListener(this);
    }

    @Override
    public void initDatas() {
        initListener();

    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
    }

    private void initListener() {
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //向上
                if (dy < 0) {
                    ivPublish.setVisibility(View.GONE);
                } else {
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
//        pageNo++;
        fetchData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        fetchData();
    }

    @OnClick(R.id.iv_publish)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_publish:
                DataStatisticsHelper.getInstance().onCountUvPv(NewVersionEvents.COMMUNITY_PUBLISH_PAGE, "");
                if (UserHelper.getInstance(getActivity()).isLogin()) {
                    startActivity(new Intent(getActivity(), CommunityPublishActivity.class));
                } else {
                    UserAuthorizationActivity.launch(getActivity());
                }
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void fetchData() {
        String userId = "";
        if (UserHelper.getInstance(getActivity()).isLogin()) {
            userId = UserHelper.getInstance(getActivity()).getProfile().getId();
        }
        Api.getInstance().queryRecommendTopic(userId, pageNo, pageSize)
                .compose(RxUtil.<ResultEntity<SocialTopicBean>>io2main())
                .subscribe(new Consumer<ResultEntity<SocialTopicBean>>() {
                               @Override
                               public void accept(ResultEntity<SocialTopicBean> result) {
                                   if (result.isSuccess()) {
                                       if (1 == pageNo) {
                                           refreshLayout.finishRefresh();
                                           adapter.setDatas(result.getData().getForum());
                                       } else {
                                           refreshLayout.finishLoadMore();
                                           adapter.appendDatas(result.getData().getForum());
                                       }
                                       if (result.getData().getForum() != null && result.getData().getForum().size() != 0) {
                                           pageNo++;
                                       }
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }

    @SuppressLint("CheckResult")
    private void fetchData1() {
        String userId = "";
        if (UserHelper.getInstance(getActivity()) != null) {
            userId = UserHelper.getInstance(getActivity()).getProfile().getId();
        }
        Api.getInstance().queryRecommendTopic(ParamsUtils.generateRecommendTopicParams(userId, pageNo, pageSize))
                .compose(RxUtil.<ResultEntity<SocialTopicBean>>io2main())
                .subscribe(new Consumer<ResultEntity<SocialTopicBean>>() {
                               @Override
                               public void accept(ResultEntity<SocialTopicBean> result) {
                                   if (result.isSuccess()) {
                                       if (1 == pageNo) {
                                           refreshLayout.finishRefresh();
                                           adapter.setDatas(result.getData().getForum());
                                       } else {
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
                            public void accept(Throwable throwable) {
                                refreshLayout.finishRefresh();
                                refreshLayout.finishLoadMore();
                                Log.e("exception_custom", throwable.getMessage());
                            }
                        });
    }
}