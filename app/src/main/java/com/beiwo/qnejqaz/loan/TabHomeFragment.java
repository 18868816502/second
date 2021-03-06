package com.beiwo.qnejqaz.loan;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseComponentFragment;
import com.beiwo.qnejqaz.entity.AdBanner;
import com.beiwo.qnejqaz.entity.FloatingBean;
import com.beiwo.qnejqaz.entity.Goods;
import com.beiwo.qnejqaz.entity.Product;
import com.beiwo.qnejqaz.entity.UserProfileAbstract;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.jjd.bean.CashOrder;
import com.beiwo.qnejqaz.social.bean.IndexForum;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.tang.StringUtil;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.activity.MainActivity;
import com.beiwo.qnejqaz.ui.activity.WebViewActivity;
import com.beiwo.qnejqaz.util.AnimationUtil;
import com.beiwo.qnejqaz.util.CommonUtils;
import com.beiwo.qnejqaz.util.DensityUtil;
import com.beiwo.qnejqaz.util.ParamsUtils;
import com.beiwo.qnejqaz.view.floatbutton.DragFloatButton;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

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
    @BindView(R.id.float_button)
    DragFloatButton floatButton;

    private TabHomeAdapter homeAdapter = new TabHomeAdapter();
    private int dyTranslate;
    private FloatingBean floatingBean;

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
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserHelper.getInstance(getActivity()).isLogin()) {
                    DlgUtil.loginDlg(getActivity(), new DlgUtil.OnLoginSuccessListener() {
                        @Override
                        public void success(UserProfileAbstract data) {
                            clickDragFloat();
                        }
                    });
                    return;
                }
                clickDragFloat();
            }
        });
        initRecycler();
        request();
    }

    //悬浮窗
    private void clickDragFloat() {
        Api.getInstance().loadFloating(ParamsUtils.generateLoadFloatingParams(floatingBean.getAdvertId(), UserHelper.getInstance(getActivity()).id()))
                .compose(RxResponse.<FloatingBean>compatT())
                .subscribe(new ApiObserver<FloatingBean>() {
                    @Override
                    public void onNext(FloatingBean data) {
                        if (data != null) {
                            if (2 == data.getType()) {
                                Api.getInstance().queryGroupProductSkip(UserHelper.getInstance(getActivity()).id(), data.getProductId())
                                        .compose(RxResponse.<String>compatT())
                                        .subscribe(new ApiObserver<String>() {
                                            @Override
                                            public void onNext(@NonNull String data) {
                                                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                                intent.putExtra("webViewUrl", data);
                                                intent.putExtra("webViewTitleName", floatingBean.getTitle());
                                                startActivity(intent);
                                            }
                                        });

                            } else {
                                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                                intent.putExtra("webViewUrl", data.getUrl());
                                intent.putExtra("webViewTitleName", floatingBean.getTitle());
                                startActivity(intent);
                            }
                        }
                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycler.setItemAnimator(new DefaultItemAnimator());
        recycler.setAdapter(homeAdapter);
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                dyTranslate += dy;
                float alphaValue = dyTranslate * 1.0f / DensityUtil.dp2px(getActivity(), 170f);
                if (dyTranslate > DensityUtil.dp2px(getActivity(), 125f)) {
                    ll_toolbar_wrap.setAlpha(alphaValue < 1 ? alphaValue : 1f);
                    ImmersionBar.with(TabHomeFragment.this).statusBarDarkFont(true).init();
                } else {
                    ll_toolbar_wrap.setAlpha(alphaValue < 1 && alphaValue > 0 ? alphaValue : 0f);
                    ImmersionBar.with(TabHomeFragment.this).statusBarDarkFont(false).init();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (recyclerView.getScrollState() == SCROLL_STATE_IDLE) {
                    AnimationUtil.with().bottomMoveToViewLocation(floatButton, 500);
                } else {
                    AnimationUtil.with().moveToViewBottom(floatButton, 500);
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
                        homeAdapter.setHeadBanner(data);
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
        //下款推荐
        Api.getInstance().hotGoods(1, 3)
                .compose(RxResponse.<List<Goods>>compatT())
                .subscribe(new ApiObserver<List<Goods>>() {
                    @Override
                    public void onNext(List<Goods> data) {
                        homeAdapter.setHotGoodsData(data);
                    }
                });
        //topic
        topicData();
        //recommond product
        Api.getInstance().groupProducts(NetConstants.SECOND_PRODUCT)
                .compose(RxResponse.<List<Product>>compatT())
                .subscribe(new ApiObserver<List<Product>>() {
                    @Override
                    public void onNext(@NonNull List<Product> data) {
                        refresh_layout.finishRefresh();
                        homeAdapter.setNormalData(data);
                        recycler.smoothScrollToPosition(0);
                    }
                });
        //悬浮窗
        Api.getInstance().floating(ParamsUtils.generateFloatingParams(UserHelper.getInstance(getActivity()).id()))
                .compose(RxResponse.<FloatingBean>compatT())
                .subscribe(new ApiObserver<FloatingBean>() {
                    @Override
                    public void onNext(FloatingBean data) {
                        floatingBean = data;
                        if (data == null) {
                            floatButton.setVisibility(View.GONE);
                        } else {
                            floatButton.setVisibility(View.VISIBLE);
                            floatButton.setFloatBackground(getActivity(), data.getImage());
                        }
                    }
                });
    }

    private void topicData() {
        Api.getInstance().indexForum()
                .compose(RxResponse.<IndexForum>compatT())
                .subscribe(new ApiObserver<IndexForum>() {
                    @Override
                    public void onNext(IndexForum data) {
                        refresh_layout.finishRefresh();
                        homeAdapter.setTopic(data);
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

    private long nao;
    private boolean viewVisible;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        viewVisible = hidden;
        if (!hidden) {
            nao = System.currentTimeMillis();
            recycler.smoothScrollToPosition(0);
        } else {
            if (viewVisible && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < Integer.MAX_VALUE) {
                DataHelper.getInstance(getActivity()).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_HOMEPAGE, "", System.currentTimeMillis() - nao);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        nao = System.currentTimeMillis();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!viewVisible && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < Integer.MAX_VALUE) {
            DataHelper.getInstance(getActivity()).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_HOMEPAGE, "", System.currentTimeMillis() - nao);
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
        refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("loan", true);
                startActivity(intent);
                refresh_layout.finishLoadMore();
            }
        });
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                topicData();
            }
        }, new IntentFilter("refresh_layout"));
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