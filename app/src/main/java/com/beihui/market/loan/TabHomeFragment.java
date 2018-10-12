package com.beihui.market.loan;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.GroupProductBean;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.jjd.bean.CashOrder;
import com.beihui.market.tang.StringUtil;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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

    private List<String> imgs = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private TabHomeAdapter homeAdapter = new TabHomeAdapter();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void configViews() {
        initRecycler();
        refresh_layout.setEnableLoadMore(false);
        request();
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(homeAdapter);
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
                        }
                        homeAdapter.setHeadBanner(imgs, urls, titles);
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
                                    if (current >= gap) {//超过设定时间
                                        homeAdapter.setState(1);
                                    } else {//未超过设定时间
                                        homeAdapter.setState(3);
                                    }
                                } else {//审核中
                                    homeAdapter.setState(2);
                                }
                            } else {
                                homeAdapter.setState(1);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            super.onError(t);
                            homeAdapter.setState(1);
                        }
                    });
        } else homeAdapter.setState(1);
        //recommond product
        Api.getInstance().queryGroupProductList()
                .compose(RxResponse.<List<GroupProductBean>>compatT())
                .subscribe(new ApiObserver<List<GroupProductBean>>() {
                    @Override
                    public void onNext(@NonNull List<GroupProductBean> data) {
                        refresh_layout.finishRefresh();
                        homeAdapter.setNormalData(data);
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        System.out.println("hidden : " + hidden);
        if (!hidden) recycler.smoothScrollToPosition(0);
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

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }
}