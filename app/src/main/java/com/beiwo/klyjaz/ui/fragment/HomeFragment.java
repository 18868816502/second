package com.beiwo.klyjaz.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.anim.SlideInLeftAnimator;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseTabFragment;
import com.beiwo.klyjaz.entity.EventBean;
import com.beiwo.klyjaz.entity.HomeData;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.adapter.HomePageAdapter;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.MainActivity;
import com.beiwo.klyjaz.umeng.NewVersionEvents;
import com.beiwo.klyjaz.util.CommonUtils;
import com.beiwo.klyjaz.util.FormatNumberUtils;
import com.beiwo.klyjaz.util.SPUtils;
import com.beiwo.klyjaz.view.pulltoswipe.PullToRefreshListener;
import com.beiwo.klyjaz.view.pulltoswipe.PullToRefreshScrollLayout;
import com.beiwo.klyjaz.view.pulltoswipe.PulledTabAccountRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * @date: 2018/7/17
 */

public class HomeFragment extends BaseTabFragment {

    @BindView(R.id.tb_tab_account_header_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.fl_top_wrap)
    FrameLayout mTitle;
    @BindView(R.id.srl_tab_account_refresh_root)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.x_load_state_tv_more_view)
    View moreView;

    @BindView(R.id.pull_up_to_head_view)
    RelativeLayout mRefreshRoot;

    @BindView(R.id.prl_fg_tab_account_list)
    PullToRefreshScrollLayout pullMoreLayout;
    @BindView(R.id.rv_fg_tab_account_list)
    PulledTabAccountRecyclerView mRecyclerView;
    @BindView(R.id.tv_top_loan_month)
    TextView tv_top_loan_month;
    @BindView(R.id.tv_top_loan_num)
    TextView tv_top_loan_num;
    @BindView(R.id.ll_home_wrap)
    View ll_home_wrap;

    private MainActivity mActivity;
    private UserHelper userHelper;
    public HomePageAdapter pageAdapter;
    //上拉与下拉的刷新监听器
    public PullToRefreshListener mPullToRefreshListener = new PullToRefreshListener();
    public int page = 1;
    private double num;
    private LinearLayoutManager layoutManager;
    private int itemPos;

    public PulledTabAccountRecyclerView recycler() {
        return mRecyclerView;
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (recyclerView.canScrollVertically(1)) {//可以上拉
                int fullHeight = recyclerView.getHeight();
                View headView = recyclerView.getChildAt(0);
                int headHeight = headView.getHeight();
                if (layoutManager.getItemCount() > 0) {
                    View itemView = recyclerView.getChildAt(1);
                    int itemHeight = itemView.getHeight();
                    if (headHeight != itemHeight) {
                        float pos = (fullHeight - headHeight / 3) * 1.0f / itemHeight;
                        itemPos = Math.round(pos + 0.5f);
                    }
                    int lastPos = layoutManager.findLastVisibleItemPosition();
                    if (lastPos >= itemPos) {
                        mTitle.setAlpha(1);
                    } else {
                        mTitle.setAlpha(0);
                    }
                    if (SPUtils.getNumVisible(mActivity)) {
                        tv_top_loan_num.setText("¥" + FormatNumberUtils.FormatNumberFor2(num));//应还金额
                    } else {
                        tv_top_loan_num.setText("****");//应还金额
                    }
                }
            }
        }
    };

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_fragment_home;
    }

    /**
     * 统计点击tab事件
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_ACCOUNT);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /*账单详情改变数据时，刷新界面*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRecieve(String value) {
        if ("1".equals(value)) request();
        pageAdapter.light();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void configViews() {
        mActivity = (MainActivity) getActivity();
        userHelper = UserHelper.getInstance(mActivity);

        int statusHeight = CommonUtils.getStatusBarHeight(mActivity);
        //设置toolbar的高度为状态栏相同高度
        mToolBar.setPadding(mToolBar.getPaddingLeft(), statusHeight, mToolBar.getPaddingRight(), 0);
        ViewGroup.LayoutParams lp = mToolBar.getLayoutParams();
        lp.height = 0;
        mToolBar.setLayoutParams(lp);
    }

    @Override
    public void initDatas() {
        initRecyclerView();
        request();
        /*上拉加载更多的监听 请求分页数据*/
        pullMoreLayout.setOnRefreshListener(new PullToRefreshScrollLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshScrollLayout pullToRefreshScrollLayout) {
            }

            @Override
            public void onLoadMore(PullToRefreshScrollLayout pullToRefreshScrollLayout) {
                page++;
                Api.getInstance().home(userHelper.id(), page)
                        .compose(RxResponse.<HomeData>compatT())
                        .subscribe(new ApiObserver<HomeData>() {
                            @Override
                            public void onNext(@NonNull HomeData data) {
                                mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.LOAD_SUCCESS;
                                mPullToRefreshListener.onLoadMore(pullMoreLayout);
                                if (data.getItem() != null && data.getItem().size() > 0) {
                                    pageAdapter.addData(data.getItem());
                                    if (data.getItem() != null && data.getItem().size() >= 20) {
                                        mRecyclerView.setCanPullUp(true);
                                    } else {
                                        mRecyclerView.setCanPullUp(false);
                                    }
                                } else {
                                    mRecyclerView.setCanPullUp(false);
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable t) {
                                super.onError(t);
                                mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.FAIL;
                                mPullToRefreshListener.onLoadMore(pullMoreLayout);
                                mRecyclerView.setCanPullUp(false);
                            }
                        });
            }
        });
    }

    public void request() {
        if (userHelper.isLogin()) {
            page = 1;
            //活动入口
            Api.getInstance().isShowOwnerActive("3", 1)
                    .compose(RxResponse.<List<EventBean>>compatT())
                    .subscribe(new ApiObserver<List<EventBean>>() {
                        @Override
                        public void onNext(@NonNull List<EventBean> data) {
                            if (data != null && data.size() > 0) {
                                pageAdapter.notifyEventEnter(data.get(0));
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            pageAdapter.notifyEventEnter(null);
                            super.onError(t);
                        }
                    });
            //首页数据
            Api.getInstance().home(userHelper.id(), page)
                    .compose(RxResponse.<HomeData>compatT())
                    .subscribe(new ApiObserver<HomeData>() {
                        @Override
                        public void onNext(@NonNull HomeData data) {
                            swipeRefreshLayout.setRefreshing(false);
                            //账单头
                            tv_top_loan_month.setText(String.format(getString(R.string.x_month_repay), data.getXmonth()));//x月应还
                            num = data.getTotalAmount();
                            if (SPUtils.getNumVisible(mActivity)) {
                                tv_top_loan_num.setText(String.format("￥%.2f", num));//应还金额
                            } else {
                                tv_top_loan_num.setText("****");//应还金额
                            }
                            pageAdapter.notifyHead(data.getTotalAmount(), data.getXmonth());
                            pageAdapter.notifyPayChanged(data.getItem());
                            if (data.getItem() != null && data.getItem().size() >= 20) {
                                mRecyclerView.setCanPullUp(true);
                            } else {
                                mRecyclerView.setCanPullUp(false);
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable t) {
                            super.onError(t);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
        } else {
            pageAdapter.notifyEmpty();
            swipeRefreshLayout.setRefreshing(false);
            mRecyclerView.setCanPullUp(false);
        }
        if (itemPos != 0 && layoutManager.findLastVisibleItemPosition() >= itemPos) {
            mTitle.setAlpha(1);
        } else {
            mTitle.setAlpha(0);
        }
    }

    private void initRecyclerView() {
        pageAdapter = new HomePageAdapter((MainActivity) getActivity(), this);
        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(pageAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());

        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_one);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                request();
            }
        });

        mRecyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    @Override
    protected boolean needFakeStatusBar() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.HP);
    }
}