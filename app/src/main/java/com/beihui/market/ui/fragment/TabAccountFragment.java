package com.beihui.market.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.NutEmailLeadInListener;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtComponent;
import com.beihui.market.injection.module.DebtModule;
import com.beihui.market.ui.activity.CreditCardDebtDetailActivity;
import com.beihui.market.ui.activity.CreditCardWebActivity;
import com.beihui.market.ui.activity.DebtAnalyzeActivity;
import com.beihui.market.ui.activity.DebtCalendarActivity;
import com.beihui.market.ui.activity.XNetLoanAccountInputActivity;
import com.beihui.market.ui.activity.XCreditCardAccountInputActivity;
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
import com.beihui.market.view.pulltoswipe.PullToRefreshScrollLayout;
import com.beihui.market.view.pulltoswipe.PulledRecyclerView;
import com.beihui.market.view.refreshlayout.manager.ComRefreshManager;
import com.gyf.barlibrary.ImmersionBar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import zhy.com.highlight.HighLight;

/**
 * 账单 模块 Fragment
 */
public class TabAccountFragment extends BaseTabFragment implements TabAccountContract.View {


    @BindView(R.id.tb_tab_account_header_tool_bar)
    Toolbar mToolBar;
    @BindView(R.id.tv_last_one_month_wait_pay)
    TextView mLastThirtyDayWaitPay;

    @BindView(R.id.prl_fg_tab_account_list)
    PullToRefreshScrollLayout mPullContainer;
    @BindView(R.id.rv_fg_tab_account_list)
    PulledRecyclerView mRecyclerView;

    public Activity mActivity;

    @Inject
    TabAccountPresenter presenter;

    //适配器
    public XTabAccountRvAdapter mAdapter;

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
        mAdapter = new XTabAccountRvAdapter(mActivity);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

    }

    /**
     * 查询信用卡账单采集结果 TODO
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


    @OnClick({R.id.iv_tab_account_header_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //添加账单
            case R.id.iv_tab_account_header_add:
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_ACCOUNT_HOME_NEW_BILL);
                //点击音效
                SoundUtils.getInstance().playAdd();

                XTabAccountDialog dialog = new XTabAccountDialog();
                dialog.show(getChildFragmentManager(), ShareDialog.class.getSimpleName());
                break;
        }
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
    public void showInDebtList(List<AccountBill> list) {
        mAdapter.notifyDebtChanged(list);
    }


    /**
     * 显示头信息数据
     * @param debtAmount   当前负债
     * @param debtSevenDay 近7天待还
     * @param debtMonth    近30天待还
     */
    @Override
    public void showDebtInfo(double debtAmount, double debtSevenDay, double debtMonth) {
        //近30天待还金额
        mLastThirtyDayWaitPay.setText(CommonUtils.keep2digitsWithoutZero(debtMonth));
    }








//    private DebtRVAdapter adapter;

    private HighLight infoHighLight;
    private boolean eyeClosed;

    private ComRefreshManager comRefreshManager;












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
    public void showNoUserLoginBlock() {
        comRefreshManager.updateRefreshContainer(false);

        completeRefresh();

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
        startActivity(new Intent(getContext(), XCreditCardAccountInputActivity.class));
    }

    @Override
    public void navigateAddCreditCardDebt() {
        Intent intent = new Intent(getContext(), XCreditCardAccountInputActivity.class);
        intent.putExtra("only_credit_card", true);
        startActivity(intent);
    }

    @Override
    public void navigateAddLoanDebt() {
        startActivity(new Intent(getContext(), XNetLoanAccountInputActivity.class));
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
        startActivity(intent);
    }

    @Override
    public void navigateCreditCardDebtDetail(AccountBill accountBill) {
        Intent intent = new Intent(getContext(), CreditCardDebtDetailActivity.class);
        intent.putExtra("debt_id", accountBill.getRecordId());
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
        completeRefresh();

        updateContent(true);
        updateNoUserBlock(false);
        updateHide(false);
    }

    @Override
    public void hideDebtInfo() {
        completeRefresh();

        updateContent(false);
        updateNoUserBlock(false);
        updateHide(true);
    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        completeRefresh();
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
