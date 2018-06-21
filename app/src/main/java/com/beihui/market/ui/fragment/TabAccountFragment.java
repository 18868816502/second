package com.beihui.market.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.TabAccountNewBean;
import com.beihui.market.event.XTabAccountDialogMoxieFinishEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.NutEmailLeadInListener;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtComponent;
import com.beihui.market.injection.module.DebtModule;
import com.beihui.market.ui.activity.CreditCardDebtDetailActivity;
import com.beihui.market.ui.activity.CreditCardWebActivity;
import com.beihui.market.ui.activity.DebtAnalyzeActivity;
import com.beihui.market.ui.activity.DebtCalendarActivity;
import com.beihui.market.ui.activity.DebtChannelActivity;
import com.beihui.market.ui.activity.DebtSourceActivity;
import com.beihui.market.ui.activity.EBankActivity;
import com.beihui.market.ui.activity.LoanDebtDetailActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.adapter.XTabAccountRvAdapter;
import com.beihui.market.ui.contract.TabAccountContract;
import com.beihui.market.ui.presenter.TabAccountPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.Px2DpUtils;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.pulltoswipe.PullToRefreshListener;
import com.beihui.market.view.pulltoswipe.PullToRefreshScrollLayout;
import com.beihui.market.view.pulltoswipe.PulledTabAccountRecyclerView;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import zhy.com.highlight.HighLight;


/**
 * 账单 模块 Fragment
 */
public class TabAccountFragment extends BaseTabFragment implements TabAccountContract.View{


    @BindView(R.id.tb_tab_account_header_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.rv_fg_tab_account_title)
    TextView mTitle;
    @BindView(R.id.srl_tab_account_refresh_root)
    SwipeRefreshLayout swipeRefreshLayout;
//    //30天待还
//    @BindView(R.id.tv_last_one_month_wait_pay)
//    TextView mLastThirtyDayWaitPay;
//    //30天待还 总笔数
//    @BindView(R.id.tv_last_one_month_wait_pay_num)
//    TextView mLastThirtyDayWaitPayNum;

    @BindView(R.id.x_load_state_tv_more_view)
    View moreView;

    @BindView(R.id.pull_up_to_head_view)
    RelativeLayout mRefreshRoot;

    @BindView(R.id.prl_fg_tab_account_list)
    PullToRefreshScrollLayout mPullContainer;
    @BindView(R.id.rv_fg_tab_account_list)
    PulledTabAccountRecyclerView mRecyclerView;


//    @BindView(R.id.fl_tab_account_header_bill_loan)
//    FrameLayout billLoamAnalysis;

    //依附的Activity
    public Activity mActivity;

    @Inject
    TabAccountPresenter presenter;

    //适配器
    public XTabAccountRvAdapter mAdapter;

    //高亮
    private HighLight infoHighLight;

    public Integer total = null;

    public int pageNo = 2;
    public int pageSize = 10;

    public int mMeasuredRecyclerViewHeaderHeight;
    public float mScrollY = 0f;

    //上拉与下拉的刷新监听器
    public PullToRefreshListener mPullToRefreshListener = new PullToRefreshListener();
    private LinearLayoutManager manager;

    /**
     * 统计点击tab事件
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_ACCOUNT);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * 布局
     */
    @Override
    public int getLayoutResId() {
        return R.layout.xlayout_fg_tab_account;
    }


    /**
     * 返回fragment
     */
    public static TabAccountFragment newInstance() {
        return new TabAccountFragment();
    }

    /**
     * Dagger2
     */
    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtComponent.builder()
                .appComponent(appComponent)
                .debtModule(new DebtModule(this))
                .build()
                .inject(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMainMoxieEvent(XTabAccountDialogMoxieFinishEvent event) {
        Toast.makeText(mActivity, "3秒后刷新页面信用卡就会显示啦", Toast.LENGTH_SHORT).show();
    }

    /**
     * 查询信用卡账单采集结果
     * presenter.onStart 调用头布局数据 与 列表数据 接口
     */
    @Override
    public void onResume() {

        super.onResume();
        presenter.onStart();

        NutEmailLeadInListener.getInstance().checkLeadInResult(new NutEmailLeadInListener.OnCheckLeadInResultListener() {
            @Override
            public void onCheckLeadInResult(boolean success) {
                if (success) {
                    ToastUtils.showLeadInResultToast(getContext());
                }
            }
        });

        if (TextUtils.isEmpty(SPUtils.getValue(mActivity, "showGuideButton1"))) {
            showGuide();
            SPUtils.setValue(mActivity, "showGuideButton1");
        }

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                initMeasure();
                int mTitleMeasuredHeight = mTitle.getMeasuredHeight();
                int height = mMeasuredRecyclerViewHeaderHeight - mTitleMeasuredHeight;
                mScrollY += dy;
                //隐藏显示布局的变化率
                float max = Math.max(mScrollY / height, 0f);
                if (max > 1) {
                    max = 1;
                }
                //TitleBar背景色透明度
                mTitle.setBackgroundColor(Color.argb((int) (max * 255), 20, 74, 158));
                if (max > 0.2f) {
                    mTitle.setTextColor(Color.argb((int) (max * 255), 255, 255, 255));
                } else {
                    mTitle.setTextColor(Color.argb(0, 255, 255, 255));
                }
            }
        });
    }


    /**
     * 测量控件高度
     */
    private void initMeasure() {
        manager.getChildAt(0).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //移出视图树
                manager.getChildAt(0).getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //列表头布局高度
                mMeasuredRecyclerViewHeaderHeight = mRecyclerView.getChildAt(0).getMeasuredHeight();
            }
        });

    }

    /**
     * 初始化数据
     */
    @Override
    public void initDatas() {

        //注册EventBus
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mActivity = getActivity();
        mAdapter = new XTabAccountRvAdapter(mActivity, this);
        manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

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
                if (pageNo*10 < total && total != null && mAdapter.showAll()) {
                    presenter.loadInDebtList(2, pageNo);
                } else {
                    mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.LOAD_ALL;
                    mPullToRefreshListener.onLoadMore(mPullContainer);
                }
            }
        });


        /**
         * 下拉刷新
         */
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_one);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (UserHelper.getInstance(mActivity).getProfile() != null && UserHelper.getInstance(mActivity).getProfile().getId() != null) {
                    presenter.onRefresh();
                }else {
                    swipeRefreshLayout.setRefreshing(false);
                    showInDebtList(new ArrayList<TabAccountNewBean>());
                }
            }
        });

    }

    /**
     * 刷新数据
     */
    public void refreshData(){
        presenter.onRefresh();
    }

    //空事件
    @Override
    public void setPresenter(TabAccountContract.Presenter presenter) {
        //
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    /**
     * 销毁事件
     */
    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


//    @OnClick({R.id.iv_tab_account_header_add, R.id.iv_tab_account_header_today_button, R.id.fl_tab_account_header_bill_loan})
//    @OnClick({ R.id.iv_tab_account_header_today_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
//            //添加账单
//            case R.id.iv_tab_account_header_add:
//                if (UserHelper.getInstance(mActivity).getProfile() != null) {
//                    //pv，uv统计
//                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ACCOUNT_HOME_NEW_BILL);
//                    //点击音效
//                    SoundUtils.getInstance().playAdd();
//
//                    XTabAccountDialog dialog = new XTabAccountDialog();
//                    dialog.show(getChildFragmentManager(), ShareDialog.class.getSimpleName());
//                } else {
//                    showNoUserLoginBlock();
//                }
//                break;
//            case R.id.iv_tab_account_header_today_button:
////                Toast.makeText(mActivity, "已至今日应还账单处，别再点啦", Toast.LENGTH_SHORT).show();
//                if (UserHelper.getInstance(mActivity).getProfile() != null) {
//                    /**
//                     * TODOD 点击今天的按钮事件
//                     */
////                    initStatus();
//                    manager.scrollToPositionWithOffset(scrollToPosition, 0);
//                } else {
//                    showNoUserLoginBlock();
//                }
//                break;

//            //进入网贷分析
//            case R.id.fl_tab_account_header_bill_loan:
//                /**
//                 * 防止重复点击
//                 */
//                if (!FastClickUtils.isFastClick()) {
//                    if (UserHelper.getInstance(mActivity).getProfile() != null) {
//                        //pv，uv统计 快捷记账按钮
//                        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_NET_BILL_LOAN_ANALYSIS);
//                        startActivity(new Intent(mActivity, BillLoanAnalysisFragmentWeek.class));
//                    } else {
//                        showNoUserLoginBlock();
//                    }
//                }
//                break;
        }
    }


    /**
     * 刷新头信息
     */
    public void initHeaderData() {
        //获取头信息
        presenter.loadDebtAbstract();
    }

    /**
     * 沉浸式
     */
    @Override
    public void configViews() {
        //设置状态栏文字为黑色字体
        ImmersionBar.with(this).transparentBar().init();
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        //设置toolbar的高度为状态栏相同高度
        mToolBar.setPadding(mToolBar.getPaddingLeft(), statusHeight, mToolBar.getPaddingRight(), 0);
        ViewGroup.LayoutParams lp = mToolBar.getLayoutParams();
        lp.height = 0;
        mToolBar.setLayoutParams(lp);

        pageNo = 2;
        total = null;
    }


    /**
     * 加载数据
     * @param list 账单列表
     */
    @Override
    public void showInDebtList(List<TabAccountNewBean> list) {
        swipeRefreshLayout.setRefreshing(false);
        if (list.size() == 0) {
            //TODO 没有数据 使用模拟数据 用户没有登录也是使用模拟数据
            TabAccountNewBean.TabAccountNewInfoBean analogLoan = new TabAccountNewBean.TabAccountNewInfoBean();
            analogLoan.isAnalog = true;
            analogLoan.setTitle("招商银行");
            analogLoan.setAmount(1000);
            analogLoan.setLastOverdue(true);
            analogLoan.setOverdueTotal(-1);
            analogLoan.setType(2);//账单类型 1-网贷 2-信用卡 3-快捷记账

            TabAccountNewBean.TabAccountNewInfoBean analogCard = new TabAccountNewBean.TabAccountNewInfoBean();
            analogCard.isAnalog = true;
            analogCard.setTitle("分期乐");
            analogCard.setAmount(2000);
            analogCard.setLastOverdue(false);
            analogCard.setOverdueTotal(0);
            ArrayList<TabAccountNewBean.TabAccountNewInfoBean> infoList = new ArrayList<>();
            infoList.add(analogLoan);
            infoList.add(analogCard);
            analogLoan.setType(1);
//
//            mLastThirtyDayWaitPay.setText("赶紧先记上一笔");
//            mLastThirtyDayWaitPayNum.setText("");

            total = null;
            mAdapter.notifyPayChanged(list);
        } else {
            total = list.get(list.size() - 1).total;
            mAdapter.notifyUnPayChanged(list);
        }


//        if (billType == 3) {
//            if (list.size() < pageSize) {
//                isNoRefreshData = true;
//            } else {
//                isNoRefreshData = false;
//                overduePageNo++;
//            }
//            mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.SUCCEED;
//            mPullToRefreshListener.onRefresh(mPullContainer);
//            mAdapter.notifyDebtChangedRefresh(list);
//        }
//        if (billType == 1 || billType == 2) {
//            if (list.size() < pageSize) {
//                //说明上拉已经没有数据了 可以显示未还 已还数据了
//                isNoMoreData = true;
//                mFootViewMoney.setVisibility(View.VISIBLE);
//                moreView.setVisibility(View.INVISIBLE);
//                mPullContainer.changeState(PullToRefreshScrollLayout.DONE);
//                mPullContainer.hide();
//            } else {
//                mFootViewMoney.setVisibility(View.GONE);
//                moreView.setVisibility(View.VISIBLE);
//                isNoMoreData = false;
//                noOverduePageNo++;
//
//                mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.LOAD_ALL;
//                mPullToRefreshListener.onLoadMore(mPullContainer);
//            }
//
//            mAdapter.notifyDebtChangedMore(list);
//        }
    }

    /**
     * 加载数据
     * @param list 账单列表
     */
    @Override
    public void showPayedInDebtList(List<TabAccountNewBean> list) {
        mAdapter.notifyPayChanged(list);
        pageNo++;
        mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.LOAD_ALL;
        mPullToRefreshListener.onLoadMore(mPullContainer);
    }


    /**
     * 显示头信息数据
     */
    @Override
    public void showDebtInfo(DebtAbstract debtAbstract) {
        if (mAdapter != null) {
            mAdapter.notifyHeaderData(debtAbstract);
        }
    }

    /**
     * 显示错误提示
     */
    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
    }

    /**
     * 需要跳转到登陆页面
     */
    @Override
    public void showNoUserLoginBlock() {
        UserAuthorizationActivity.launch(getActivity(), null);
    }

    @Override
    protected boolean needFakeStatusBar() {
        return false;
    }

    @Override
    public void showUserLoginBlock() {
    }

    @Override
    public void showNoDebtListBlock() {
        showNoUserLoginBlock();
    }


    /**
     * TODO 高亮暂时隐藏
     */
    @Override
    public void showGuide() {
//        infoHighLight = new HighLight(getActivity())
//                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
//                    @Override
//                    public void onLayouted() {
//                        infoHighLight
//                                .autoRemove(false)
//                                .intercept(true)
//                                .enableNext()
//                                .addHighLight(R.id.iv_tab_account_header_add, R.layout.layout_tab_account_highlight_guide, new OnBaseCallback() {
//                                    @Override
//                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                        marginInfo.rightMargin = rectF.width() / 2;
//                                        marginInfo.bottomMargin = bottomMargin - getResources().getDisplayMetrics().density * 90 - rectF.height() + Px2DpUtils.dp2px(mActivity, 5);
//                                    }
//                                }, new CircleLightShape())
//                                .addHighLight(R.id.iv_tab_account_header_bill_loan, R.layout.layout_highlight_confirm, new OnBaseCallback() {
//                                    @Override
//                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                        marginInfo.leftMargin = Px2DpUtils.dp2px(mActivity, 6);
//                                        marginInfo.bottomMargin = bottomMargin - getResources().getDisplayMetrics().density * 90 - rectF.height() - Px2DpUtils.dp2px(mActivity, 2);
//                                    }
//                                }, new CircleLightShape())
//                                .setOnRemoveCallback(new HighLightInterface.OnRemoveCallback() {
//                                    @Override
//                                    public void onRemove() {
//                                        //监听移除回调
//                                    }
//                                })
//                                .setOnShowCallback(new HighLightInterface.OnShowCallback() {
//                                    @Override
//                                    public void onShow(HightLightView hightLightView) {
//                                        //监听显示回调
//                                    }
//                                }).setOnNextCallback(new HighLightInterface.OnNextCallback() {
//                                    @Override
//                                    public void onNext(HightLightView hightLightView, View targetView, View tipView) {
//                                        // targetView 目标按钮 tipView添加的提示布局 可以直接找到'我知道了'按钮添加监听事件等处理
//                                        if (targetView.getId() == R.id.iv_tab_account_header_add) {
//
//                                            infoHighLight.getHightLightView().findViewById(R.id.iv_bill_guide_one).setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        infoHighLight.next();
//                                                    }
//                                                });
//                                        } else {
//                                            infoHighLight.getHightLightView().findViewById(R.id.iv_bill_guide_two).setOnClickListener(new View.OnClickListener() {
//                                                @Override
//                                                public void onClick(View v) {
//                                                    infoHighLight.remove();
//                                                }
//                                            });
//                                        }
//                                    }
//                                }).show();
//                    }
//                });
    }

    @Override
    public void navigateUserLogin() {
        UserAuthorizationActivity.launch(getActivity(), null);
    }

    @Override
    public void navigateAdd() {
        startActivity(new Intent(getContext(), DebtSourceActivity.class));
    }

    @Override
    public void navigateAddCreditCardDebt() {
        Intent intent = new Intent(getContext(), DebtSourceActivity.class);
        intent.putExtra("only_credit_card", true);
        startActivity(intent);
    }

    @Override
    public void navigateAddLoanDebt() {
        startActivity(new Intent(getContext(), DebtChannelActivity.class));
    }

    @Override
    public void navigateVisaLeadingIn() {
        startActivity(new Intent(getContext(), EBankActivity.class));
    }

    /**
     * 登录成功 点击还款日历 进入日历
     */
    @Override
    public void navigateCalendar() {
        startActivity(new Intent(getContext(), DebtCalendarActivity.class));
    }

    @Override
    public void navigateAnalyze() {
        startActivity(new Intent(getContext(), DebtAnalyzeActivity.class));
    }

    @Override
    public void navigateLoanDebtDetail(AccountBill accountBill) {
        Intent intent = new Intent(getContext(), LoanDebtDetailActivity.class);
        intent.putExtra("debt_id", accountBill.getRecordId());
        intent.putExtra("bill_id", accountBill.getBillId());
        startActivity(intent);
    }

    @Override
    public void navigateCreditCardDebtDetail(AccountBill accountBill) {
        Intent intent = new Intent(getContext(), CreditCardDebtDetailActivity.class);
        intent.putExtra("debt_id", accountBill.getRecordId());
        intent.putExtra("bill_id", accountBill.getBillId());
        intent.putExtra("logo", accountBill.getLogo());
        intent.putExtra("bank_name", accountBill.getBankName());
        intent.putExtra("card_num", accountBill.getCardNums());
        intent.putExtra("by_hand", accountBill.getCardSource() == 3);//是否是手动记账
        startActivity(intent);
    }


    @Override
    public void navigateCreditCardCenter() {
        startActivity(new Intent(getContext(), CreditCardWebActivity.class));
    }

    @Override
    public void showDebtInfo() {
    }

    @Override
    public void hideDebtInfo() {
    }
}