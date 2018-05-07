package com.beihui.market.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.request.XAccountInfo;
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
import com.beihui.market.ui.dialog.ShareDialog;
import com.beihui.market.ui.dialog.XTabAccountDialog;
import com.beihui.market.ui.presenter.TabAccountPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.SoundUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.pulltoswipe.PullToRefreshListener;
import com.beihui.market.view.pulltoswipe.PullToRefreshScrollLayout;
import com.beihui.market.view.pulltoswipe.PulledRecyclerView;
import com.beihui.market.view.refreshlayout.manager.ComRefreshManager;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import zhy.com.highlight.HighLight;

import static com.beihui.market.view.pulltoswipe.PullToRefreshScrollLayout.DONE;

/**
 * 账单 模块 Fragment
 */
public class TabAccountFragment extends BaseTabFragment implements TabAccountContract.View {


    @BindView(R.id.tb_tab_account_header_tool_bar)
    Toolbar mToolBar;
    //30天待还
    @BindView(R.id.tv_last_one_month_wait_pay)
    TextView mLastThirtyDayWaitPay;
    //30天待还 总笔数
    @BindView(R.id.tv_last_one_month_wait_pay_num)
    TextView mLastThirtyDayWaitPayNum;
    @BindView(R.id.include_tab_account_foot_view)
    View mFootViewMoney;
    @BindView(R.id.x_load_state_tv_more_view)
    View moreView;

    @BindView(R.id.prl_fg_tab_account_list)
    PullToRefreshScrollLayout mPullContainer;
    @BindView(R.id.rv_fg_tab_account_list)
    PulledRecyclerView mRecyclerView;

    @BindView(R.id.rv_foot_account_tab_yes_pay)
    TextView yesPay;
    @BindView(R.id.rv_foot_account_tab_no_pay)
    TextView noPay;


    public Activity mActivity;

    @Inject
    TabAccountPresenter presenter;

    //适配器
    public XTabAccountRvAdapter mAdapter;

    //上拉与下拉的刷新监听器
    public PullToRefreshListener mPullToRefreshListener = new PullToRefreshListener();
    //要查询的页数
    public int overduePageNo = 1;
    public int noOverduePageNo = 2;
    //当页数据条数
    public int pageSize = 10;

    public Boolean isNoMoreData = false;
    public Boolean isNoRefreshData = false;

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

    /**
     * 初始化数据
     */
    @Override
    public void initDatas() {
        mActivity = getActivity();
        mAdapter = new XTabAccountRvAdapter(mActivity, this);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
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
                if (UserHelper.getInstance(mActivity).getProfile() == null) {
                    showNoUserLoginBlock();
                    mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.SUCCEED;
                    mPullToRefreshListener.onRefresh(mPullContainer);
                } else {
                    if (!isNoRefreshData) {
                        //请求的是逾期的
                        presenter.loadInDebtList(3, false, overduePageNo, 10);
                    } else {
                        mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.SUCCEED;
                        mPullToRefreshListener.onRefresh(mPullContainer);
                    }
                }
            }

            //上拉加载更多 这里要将pageNo++，刷新下一页数据
            @Override
            public void onLoadMore(PullToRefreshScrollLayout pullToRefreshScrollLayout) {
                if (UserHelper.getInstance(mActivity).getProfile() == null) {
                    showNoUserLoginBlock();
                    mPullContainer.changeState(PullToRefreshScrollLayout.DONE);
                    mPullContainer.hide();
                } else {
                    if (!isNoMoreData) {
                        presenter.loadInDebtList(1, false, noOverduePageNo, 10);
                    } else {
                        mPullContainer.changeState(PullToRefreshScrollLayout.DONE);
                        mPullContainer.hide();
                    }
                }
            }
        });
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
    }

    //空事件
    @Override
    public void setPresenter(TabAccountContract.Presenter presenter) {
        //
    }

    /**
     * 销毁事件
     */
    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }


    @OnClick({R.id.iv_tab_account_header_add, R.id.iv_tab_account_header_today_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //添加账单
            case R.id.iv_tab_account_header_add:
                if (UserHelper.getInstance(mActivity).getProfile() != null) {
                    //pv，uv统计
                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ACCOUNT_HOME_NEW_BILL);
                    //点击音效
                    SoundUtils.getInstance().playAdd();

                    XTabAccountDialog dialog = new XTabAccountDialog();
                    dialog.show(getChildFragmentManager(), ShareDialog.class.getSimpleName());
                } else {
                    showNoUserLoginBlock();
                }
                break;
            case R.id.iv_tab_account_header_today_button:
                Toast.makeText(mActivity, "已至今日应还账单处，别再点啦", Toast.LENGTH_SHORT).show();
                if (UserHelper.getInstance(mActivity).getProfile() != null) {
                    initStatus();
                } else {
                    showNoUserLoginBlock();
                }
                break;
        }
    }

    /**
     * 回调首屏
     */
    public void initStatus() {
        //获取头信息
        presenter.loadDebtAbstract();
        presenter.loadInDebtList(0, true, 1, 10);
        mRecyclerView.scrollToPosition(0);
    }

    /**
     * 刷新头信息
     */
    public void initHeaderData() {
        //获取头信息
        presenter.loadDebtAbstract();
    }


    @Override
    public void configViews() {
        //设置状态栏文字为黑色字体
        ImmersionBar.with(this).titleBar(mToolBar).statusBarDarkFont(true).init();
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        //设置toolbar的高度为状态栏相同高度
        mToolBar.setPadding(mToolBar.getPaddingLeft(), statusHeight, mToolBar.getPaddingRight(), 0);
        ViewGroup.LayoutParams lp = mToolBar.getLayoutParams();
        lp.height = statusHeight;
        mToolBar.setLayoutParams(lp);
    }


    /**
     * 加载数据
     * @param list 账单列表
     */
    @Override
    public void showInDebtList(List<XAccountInfo> list, int billType) {
        if (billType == 0) {
            overduePageNo=1;
            noOverduePageNo=2;
            isNoMoreData = false;
            isNoRefreshData = false;
            if (list.size() == 0) {
                //TODO 没有数据 使用模拟数据 用户没有登录也是使用模拟数据
                XAccountInfo analogLoan = new XAccountInfo();
                analogLoan.isAnalog = true;
                analogLoan.setTitle("招商银行");
                analogLoan.setAmount(1000);
                analogLoan.setLastOverdue(true);
                analogLoan.setOverdueTotal(-1);

                XAccountInfo analogCard = new XAccountInfo();
                analogCard.isAnalog = true;
                analogCard.setTitle("分期乐");
                analogCard.setAmount(2000);
                analogCard.setLastOverdue(false);
                analogCard.setOverdueTotal(0);
                list.add(analogLoan);
                list.add(analogCard);

                mLastThirtyDayWaitPay.setText("赶紧先记上一笔");
                mLastThirtyDayWaitPayNum.setText("");

            }else if (list.size() < pageSize) {
                mFootViewMoney.setVisibility(View.VISIBLE);
                moreView.setVisibility(View.INVISIBLE);

                if (list.get(0).getOverdueTotal() <= 1) {
                    isNoRefreshData = true;
                }
                isNoMoreData = true;
            }  else {
                mFootViewMoney.setVisibility(View.GONE);
                moreView.setVisibility(View.VISIBLE);
            }

            mAdapter.notifyDebtChanged(list);
        }
        if (billType == 3) {
            if (list.size() < pageSize) {
                isNoRefreshData = true;
            } else {
                isNoRefreshData = false;
                overduePageNo++;
            }
            mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.SUCCEED;
            mPullToRefreshListener.onRefresh(mPullContainer);
            mAdapter.notifyDebtChangedRefresh(list);
        }
        if (billType == 1 || billType == 2) {
            if (list.size() < pageSize) {
                //说明上拉已经没有数据了 可以显示未还 已还数据了
                isNoMoreData = true;
                mFootViewMoney.setVisibility(View.VISIBLE);
                moreView.setVisibility(View.INVISIBLE);
                mPullContainer.changeState(PullToRefreshScrollLayout.DONE);
                mPullContainer.hide();
            } else {
                mFootViewMoney.setVisibility(View.GONE);
                moreView.setVisibility(View.VISIBLE);
                isNoMoreData = false;
                noOverduePageNo++;

                mPullToRefreshListener.REFRESH_RESULT = mPullToRefreshListener.LOAD_ALL;
                mPullToRefreshListener.onLoadMore(mPullContainer);
            }

            mAdapter.notifyDebtChangedMore(list);
        }
    }


    /**
     * 显示头信息数据
     */
    @Override
    public void showDebtInfo(DebtAbstract debtAbstract) {
        //近30天待还金额
        if (debtAbstract.getLast30DayStayStill() <= 0D && debtAbstract.unRepayAmount <= 0D) {
            mLastThirtyDayWaitPay.setText("赶紧先记上一笔");
        } else {
            mLastThirtyDayWaitPay.setText(CommonUtils.keep2digitsWithoutZero(debtAbstract.getLast30DayStayStill()));
        }
        //近30天待还总笔数
        if (debtAbstract.last30DayStayStillCount > 0) {
            mLastThirtyDayWaitPayNum.setText("共" + CommonUtils.keep2digitsWithoutZero(debtAbstract.last30DayStayStillCount) + "笔");
        } else {
            mLastThirtyDayWaitPayNum.setText("");
        }

        /**
         * 已还 未还数据
         */
        if (debtAbstract.repayAmount > 0) {
            yesPay.setText(CommonUtils.keep2digitsWithoutZero(debtAbstract.repayAmount));
        } else {
            yesPay.setText("0");
        }

        if (debtAbstract.unRepayAmount > 0) {
            noPay.setText(CommonUtils.keep2digitsWithoutZero(debtAbstract.unRepayAmount));
        } else {
            noPay.setText("0");
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
     * TODO
     */
    @Override
    public void showNoUserLoginBlock() {
        UserAuthorizationActivity.launch(getActivity(), null);


//        comRefreshManager.updateRefreshContainer(false);
//
//        completeRefresh();

        //如果用户退出，则设置eye open
//        eyeClosed = false;
//        if (adapter.getFooterLayout().getChildCount() == 1) {
//            //可能出现重读调用，确保只添加一次
//            View noUserLoginFootView = LayoutInflater.from(getContext())
//                    .inflate(R.layout.layout_tab_account_no_user_foot, recyclerView, false);
//            noUserLoginFootView.findViewById(R.id.add_loan_debt).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //pv，uv统计
//                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ACCOUNT_HOME_CLICK_NEW_LOAN_BILL);
//
//                    presenter.clickAddLoanDebt();
//                }
//            });
//            noUserLoginFootView.findViewById(R.id.add_credit_card_debt).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //pv，uv统计
//                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ACCOUNT_HOME_CLICK_NEW_CREDIT_CARD_BILL);
//
//                    presenter.clickAddCreditCardDebt();
//                }
//            });
//            adapter.addFooterView(noUserLoginFootView, 0);
//        }
//
//        addView.setVisibility(View.GONE);
//
//        updateContent(false);
//        updateHide(false);
//        updateNoUserBlock(true);
    }







//    private DebtRVAdapter adapter;

//    private HighLight infoHighLight;
//    private boolean eyeClosed;
//
//    private ComRefreshManager comRefreshManager;












//    public void configViewss() {
//        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
//        toolbar.setPadding(toolbar.getPaddingLeft(), statusHeight, toolbar.getPaddingRight(), 0);
//        ViewGroup.LayoutParams lp = toolbar.getLayoutParams();
//        lp.height += statusHeight;
//        toolbar.setLayoutParams(lp);
//
//        comRefreshManager = new ComRefreshManager();
//        refreshLayout.setRefreshManager(comRefreshManager);
//        refreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefreshing() {
//                presenter.refresh();
//            }
//        });
//
//        addView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ACCOUNT_HOME_NEW_BILL);
//                //点击音效
//                SoundUtils.getInstance().playAdd();
//
//                presenter.clickAdd();
//            }
//        });
//        calendarView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                presenter.clickCalendar();
//            }
//        });
//        analyzeView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                presenter.clickAnalyze();
//            }
//        });
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        adapter = new DebtRVAdapter();
//        final View headerView = LayoutInflater.from(getContext())
//                .inflate(R.layout.layout_tab_account_header, recyclerView, false);
//        header = new DebtHeader(headerView);
//        header.debtEye.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (UserHelper.getInstance(getContext()).getProfile() != null) {
//                    header.debtEye.setSelected(!header.debtEye.isSelected());
//                    eyeClosed = header.debtEye.isSelected();
//                    presenter.clickEye(eyeClosed);
//                }
//            }
//        });
//        /**
//         * 还款日历点击事件
//         */
//        header.debtCalendar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_DEBT_CALENDAR);
//
//                presenter.clickCalendar();
//            }
//        });
//        /**
//         * 负债分析点击事件
//         */
//        header.debtAnalyze.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_DEBT_ANALYZE);
//
//                presenter.clickAnalyze();
//            }
//        });
//        headerView.setPadding(headerView.getPaddingLeft(), statusHeight, headerView.getPaddingRight(), 0);
//        adapter.setHeaderView(headerView);
//
//        View footView = LayoutInflater.from(getContext())
//                .inflate(R.layout.layout_tab_account_foot, recyclerView, false);
//
//        /**
//         * 办理信用卡点击事件 进入webView页面
//         */
//        footView.findViewById(R.id.credit_card).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //pv uv
//                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ACCOUNT_GO_TO_CREDIT_CARD_CENTER);
//
//                presenter.clickCreditCard();
//            }
//        });
//        adapter.setFooterView(footView);
//        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                final int index = position;
//                switch (view.getId()) {
//                    case R.id.debt_container:
//                        presenter.clickDebt(position);
//                        break;
//                    case R.id.hide:
//                        new CommNoneAndroidDialog()
//                                .withMessage("确认隐藏该项吗？")
//                                .withNegativeBtn("取消", null)
//                                .withPositiveBtn("确认", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        ((SwipeMenuLayout) ((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(index + 1)).getView(R.id.swipe_menu_layout)).quickClose();
//                                        presenter.clickDebtHide(index);
//                                    }
//                                }).show(getChildFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
//                        break;
//                    case R.id.sync:
//                        ((SwipeMenuLayout) ((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position + 1)).getView(R.id.swipe_menu_layout)).quickClose();
//                        presenter.clickDebtSync(position);
//                        break;
//                    case R.id.set_status:
//                        new CommNoneAndroidDialog()
//                                .withMessage("确认设为已还？")
//                                .withNegativeBtn("取消", null)
//                                .withPositiveBtn("确认", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        ((SwipeMenuLayout) ((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(index + 1)).getView(R.id.swipe_menu_layout)).quickClose();
//                                        presenter.clickDebtSetStatus(index);
//                                    }
//                                }).show(getChildFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
//                        break;
//                }
//            }
//        });
//        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new DebtItemDeco());
//
//        billsBgImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if (billsBgImage != null) {
//                    billsBgImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                    ViewGroup.LayoutParams lp = header.itemView.getLayoutParams();
//                    lp.height = billsBgImage.getMeasuredHeight();
//                    header.itemView.setLayoutParams(lp);
//
//                    billsBgImage.setDrawingCacheEnabled(true);
//                    Bitmap drawingCache = billsBgImage.getDrawingCache();
//                    if (drawingCache != null) {
//                        Bitmap res = Bitmap.createBitmap(drawingCache, 0, 0, toolbar.getMeasuredWidth(), toolbar.getMeasuredHeight());
//                        toolbar.setBackground(new BitmapDrawable(getResources(), res));
//                    }
//                }
//            }
//        });
//    }


    @Override
    protected boolean needFakeStatusBar() {
        return false;
    }




    @Override
    public void showUserLoginBlock() {
//        comRefreshManager.updateRefreshContainer(true);
//        completeRefresh();
//
//        if (adapter.getFooterLayout().getChildCount() > 1) {
//            adapter.getFooterLayout().removeViewAt(0);
//        }
//
//        addView.setVisibility(View.VISIBLE);
//
//        header.debtEye.setSelected(eyeClosed);
//        updateContent(!eyeClosed);
//        updateHide(eyeClosed);
//        updateNoUserBlock(false);
    }

    @Override
    public void showNoDebtListBlock() {
        showNoUserLoginBlock();
    }






    @Override
    public void showGuide() {
//        infoHighLight = new HighLight(getActivity())
//                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
//                    @Override
//                    public void onLayouted() {
//                        infoHighLight
//                                .addHighLight(addView, R.layout.layout_tab_account_highlight_guide, new OnBaseCallback() {
//                                    @Override
//                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                        marginInfo.rightMargin = rectF.width() / 2 + 20;
//                                        marginInfo.bottomMargin = bottomMargin - getResources().getDisplayMetrics().density * 90 - rectF.height();
//                                    }
//                                }, new CircleLightShape())
//                                .addHighLight(guideConfirmAnchor, R.layout.layout_highlight_confirm, new OnBaseCallback() {
//                                    @Override
//                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
//                                        marginInfo.rightMargin = rightMargin / 2;
//                                        marginInfo.bottomMargin = bottomMargin - getResources().getDisplayMetrics().density * 90;
//                                    }
//                                }, new CircleLightShape())
//                                .autoRemove(false)
//                                .show();
//                        infoHighLight.getHightLightView().findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                infoHighLight.remove();
//                            }
//                        });
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
//        completeRefresh();
//
//        updateContent(true);
//        updateNoUserBlock(false);
//        updateHide(false);
    }

    @Override
    public void hideDebtInfo() {
//        completeRefresh();
//
//        updateContent(false);
//        updateNoUserBlock(false);
//        updateHide(true);
    }



    private void completeRefresh() {
//        if (refreshLayout.isRefreshing()) {
//            refreshLayout.setRefreshing(false);
//        }
    }

    private void updateContent(boolean show) {
//        header.debtAmount.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        header.debtSevenDay.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        header.debtMonth.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        debtAmount.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void updateNoUserBlock(boolean show) {
//        header.debtAmountNoUserLogin.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        header.debtSevenDayNoUserLogin.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        header.debtMonthNoUserLogin.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateHide(boolean show) {
//        header.debtAmountHide.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        header.debtSevenHide.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        header.debtMonthHide.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
//        debtAmountHide.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
