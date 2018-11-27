package com.beiwo.klyjaz.loan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.NetConstants;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.entity.AdBanner;
import com.beiwo.klyjaz.entity.Product;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.jjd.bean.CashOrder;
import com.beiwo.klyjaz.tang.StringUtil;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.DensityUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/9
 */

public class TabHomeFragment extends BaseComponentFragment {

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.ll_toolbar_wrap)
    LinearLayout ll_toolbar_wrap;
    @BindView(R.id.hold_view)
    View hold_view;

    private List<String> imgs = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private List<Boolean> needLogin = new ArrayList<>();
    private TabHomeAdapter homeAdapter = new TabHomeAdapter();
    private int dyTranslate;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        ViewGroup.LayoutParams params = hold_view.getLayoutParams();
        params.height = statusHeight;
        hold_view.setLayoutParams(params);

        initRecycler();
        refresh_layout.setEnableLoadMore(false);
        request();
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(homeAdapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                dyTranslate += dy;
                if (dyTranslate > DensityUtil.dp2px(getActivity(), 170f)) {
                    ll_toolbar_wrap.setVisibility(View.VISIBLE);
                    ImmersionBar.with(TabHomeFragment.this).statusBarDarkFont(true).init();
                } else {
                    ll_toolbar_wrap.setVisibility(View.GONE);
                    ImmersionBar.with(TabHomeFragment.this).statusBarDarkFont(false).init();
                }
            }
        });
    }

    private void request() {
        //banner
        Api.getInstance().querySupernatant(2)
                .compose(RxResponse.<List<AdBanner>>compatT())
                .subscribe(new ApiObserver<List<AdBanner>>() {
                    @Override
                    public void onNext(@NonNull List<AdBanner> data) {
                        refresh_layout.finishRefresh();
                        imgs.clear();
                        urls.clear();
                        for (int i = 0; i < data.size(); i++) {
                            imgs.add(data.get(i).getImgUrl());
                            urls.add(data.get(i).getUrl());
                            titles.add(data.get(i).getTitle());
                            needLogin.add(data.get(i).needLogin());
                        }
                        homeAdapter.setHeadBanner(imgs, urls, titles, needLogin);
                    }
                });
        //looper text
        Api.getInstance().queryBorrowingScroll()
                .compose(RxResponse.<List<String>>compatT())
                .subscribe(new ApiObserver<List<String>>() {
                    @Override
                    public void onNext(@NonNull List<String> data) {
                        refresh_layout.finishRefresh();
                        homeAdapter.setHeadLoopText(data);
                    }
                });
        //check state
        checkUserState();
        //recommond product
        Api.getInstance().queryGroupProductList(NetConstants.SECOND_PRODUCT)
                .compose(RxResponse.<List<Product>>compatT())
                .subscribe(new ApiObserver<List<Product>>() {
                    @Override
                    public void onNext(@NonNull List<Product> data) {
                        refresh_layout.finishRefresh();
                        homeAdapter.setNormalData(data);
                        recycler.smoothScrollToPosition(0);
                    }
                });
    }

    private void checkUserState() {
        if (UserHelper.getInstance(getActivity()).isLogin()) {
            Api.getInstance().cashOrder(UserHelper.getInstance(getActivity()).id())
                    .compose(RxResponse.<CashOrder>compatT())
                    .subscribe(new ApiObserver<CashOrder>() {
                        @Override
                        public void onNext(@NonNull CashOrder data) {
                            if (data != null) {
                                if ("1".equals(data.getOrderStatus())) {//审核被拒
                                    long current = StringUtil.time2NowSecond(data.getGmtCreate());//目前
                                    long gap = StringUtil.timeGapSecond(data.getOverDate(), data.getGmtCreate());//设定
                                    if (current >= gap) homeAdapter.setStateNormal();//超过设定时间
                                    else homeAdapter.setStateFail(data.getOverDate());//未超过设定时间
                                } else
                                    homeAdapter.setStateChecking(data.getAuditDate(), data.getOverDate());//审核中
                            } else homeAdapter.setStateNormal();
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            super.onError(t);
                            homeAdapter.setStateNormal();
                        }
                    });
        } else homeAdapter.setStateNormal();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            recycler.smoothScrollToPosition(0);
        }
    }

    @Override
    public void initDatas() {
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                request();
            }
        });
    }

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!EventBus.getDefault().isRegistered(this)) EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void recieve(String msg) {
        if (TextUtils.equals("1", msg)) checkUserState();
    }
}