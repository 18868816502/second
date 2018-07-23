package com.beihui.market.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.anim.SlideInLeftAnimator;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.EventBean;
import com.beihui.market.entity.HomeData;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.adapter.HomePageAdapter;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.view.pulltoswipe.PullToRefreshListener;
import com.beihui.market.view.pulltoswipe.PullToRefreshScrollLayout;
import com.beihui.market.view.pulltoswipe.PulledTabAccountRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    PullToRefreshScrollLayout mPullContainer;
    @BindView(R.id.rv_fg_tab_account_list)
    PulledTabAccountRecyclerView mRecyclerView;
    @BindView(R.id.tv_top_loan_month)
    TextView tv_top_loan_month;
    @BindView(R.id.tv_top_loan_num)
    TextView tv_top_loan_num;

    private Activity mActivity;
    private UserHelper userHelper;
    private HomePageAdapter pageAdapter;
    //上拉与下拉的刷新监听器
    public PullToRefreshListener mPullToRefreshListener = new PullToRefreshListener();
    public Integer total = null;
    public int pageNo = 2;
    public int mMeasuredRecyclerViewHeaderHeight;
    public float mScrollY = 0f;

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
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void configViews() {
        mActivity = getActivity();
        int statusHeight = CommonUtils.getStatusBarHeight(mActivity);
        //设置toolbar的高度为状态栏相同高度
        mToolBar.setPadding(mToolBar.getPaddingLeft(), statusHeight, mToolBar.getPaddingRight(), 0);
        ViewGroup.LayoutParams lp = mToolBar.getLayoutParams();
        lp.height = 0;
        mToolBar.setLayoutParams(lp);
    }

    @Override
    public void initDatas() {
        request();
        initRecyclerView();
    }

    public void request() {
        userHelper = UserHelper.getInstance(getActivity());
        if (userHelper.isLogin()) {
            //活动入口
            Api.getInstance().homeEvent("3", 1)
                    .compose(RxResponse.<EventBean>compatT())
                    .subscribe(new ApiObserver<EventBean>() {
                        @Override
                        public void onNext(@NonNull EventBean data) {
                            pageAdapter.notifyEventEnter(data.getUrl());
                        }
                    });
            //首页数据
            Api.getInstance().home(userHelper.id(), "1")
                    .compose(RxResponse.<HomeData>compatT())
                    .subscribe(new ApiObserver<HomeData>() {
                        @Override
                        public void onNext(@NonNull HomeData data) {
                            swipeRefreshLayout.setRefreshing(false);
                            mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.LOAD_ALL;
                            mPullToRefreshListener.onLoadMore(mPullContainer);
                            //账单头
                            tv_top_loan_month.setText(String.format(getString(R.string.x_month_repay), data.getXmonth()));//x月应还
                            tv_top_loan_num.setText(String.format("￥%.2f", data.getTotalAmount()));//应还金额
                            pageAdapter.notifyHead(data.getTotalAmount(), data.getXmonth());
                            pageAdapter.notifyPayChanged(data.getItem());
                        }
                    });
        }
    }

    private void initRecyclerView() {
        pageAdapter = new HomePageAdapter(getActivity(), this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(pageAdapter);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
        mRecyclerView.setCanPullUp(false);

        //PullToRefreshLayout设置监听
        mPullContainer.setOnRefreshListener(mPullToRefreshListener);
        /**
         * 下拉加载与加载更多的监听
         * 请求分页数据
         */
        mPullContainer.setOnRefreshListener(new PullToRefreshScrollLayout.OnRefreshListener() {
            //下拉刷新 这里要将pageNo 置为 1，刷新第一页数据
            @Override
            public void onRefresh(PullToRefreshScrollLayout pullToRefreshScrollLayout) {
            }

            //上拉加载更多 这里要将pageNo++，刷新下一页数据
            @Override
            public void onLoadMore(PullToRefreshScrollLayout pullToRefreshScrollLayout) {
                mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.LOAD_SUCCESS;
                mPullToRefreshListener.onLoadMore(mPullContainer);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_one);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (userHelper.isLogin()) request();
                else swipeRefreshLayout.setRefreshing(false);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                final LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                manager.getChildAt(0).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        //移出视图树
                        manager.getChildAt(0).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        //列表头布局高度
                        mMeasuredRecyclerViewHeaderHeight = mRecyclerView.getChildAt(0).getMeasuredHeight();
                    }
                });
                int mTitleMeasuredHeight = mTitle.getMeasuredHeight();
                int height = mMeasuredRecyclerViewHeaderHeight - mTitleMeasuredHeight;
                mScrollY += dy;
                //隐藏显示布局的变化率
                float max = Math.max(mScrollY / height, 0f);
                if (max > 1) max = 1;
                if (max > 0.55f) mTitle.setAlpha(max);
                else mTitle.setAlpha(0);
                if (!manager.canScrollVertically()) mTitle.setAlpha(0);
            }
        });
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
