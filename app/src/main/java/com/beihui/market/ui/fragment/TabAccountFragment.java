package com.beihui.market.ui.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseTabFragment;
import com.beihui.market.entity.InDebt;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtComponent;
import com.beihui.market.injection.module.DebtModule;
import com.beihui.market.ui.activity.AddDebtActivity;
import com.beihui.market.ui.activity.AllDebtActivity;
import com.beihui.market.ui.activity.CreditCardWebActivity;
import com.beihui.market.ui.activity.DebtAnalyzeActivity;
import com.beihui.market.ui.activity.DebtCalendarActivity;
import com.beihui.market.ui.activity.DebtDetailActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.adapter.DebtRVAdapter;
import com.beihui.market.ui.contract.DebtContract;
import com.beihui.market.ui.dialog.TabAccountHintDialog;
import com.beihui.market.ui.presenter.DebtPresenter;
import com.beihui.market.ui.rvdecoration.DebtItemDeco;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.SoundUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBaseCallback;
import zhy.com.highlight.shape.CircleLightShape;

import static com.beihui.market.util.CommonUtils.keep2digits;

public class TabAccountFragment extends BaseTabFragment implements DebtContract.View {
    @BindView(R.id.bills_bg_image)
    ImageView billsBgImage;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.debt_amount_container)
    View debtAmountContainer;
    @BindView(R.id.debt_amount)
    TextView debtAmount;
    @BindView(R.id.calendar)
    View calendarView;
    @BindView(R.id.analyze)
    View analyzeView;
    @BindView(R.id.add)
    View addView;
    @BindView(R.id.bill_add)
    View billAdd;
    @BindView(R.id.bill_add_no_circle)
    View billAddNoCircle;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.guide_confirm_anchor)
    View guideConfirmAnchor;

    class DebtHeader {
        View itemView;
        @BindView(R.id.debt_eye)
        CheckBox debtEye;
        @BindView(R.id.all_debt)
        View allDebt;
        @BindView(R.id.debt_amount)
        TextView debtAmount;
        @BindView(R.id.debt_amount_hide)
        View debtAmountHide;
        @BindView(R.id.debt_amount_no_user_login)
        View debtAmountNoUserLogin;
        @BindView(R.id.debt_seven_day)
        TextView debtSevenDay;
        @BindView(R.id.debt_seven_day_hide)
        View debtSevenHide;
        @BindView(R.id.debt_seven_day_no_user_login)
        View debtSevenDayNoUserLogin;
        @BindView(R.id.debt_month)
        TextView debtMonth;
        @BindView(R.id.debt_month_hide)
        View debtMonthHide;
        @BindView(R.id.debt_month_no_user_login)
        View debtMonthNoUserLogin;
        @BindView(R.id.debt_calendar)
        View debtCalendar;
        @BindView(R.id.debt_analyze)
        View debtAnalyze;

        DebtHeader(View view) {
            this.itemView = view;
            ButterKnife.bind(this, view);
        }
    }

    @Inject
    DebtPresenter presenter;

    private DebtHeader header;
    private DebtRVAdapter adapter;

    private HighLight infoHighLight;

    class TabScrollListener extends RecyclerView.OnScrollListener {
        int topViewHeight;
        int scrollY;
        int firEdge = -1;
        int secEdge = -1;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            scrollY += dy;
            updateContentByScrollY();
        }

        void resetScrollY() {
            if (topViewHeight == 0) {
                topViewHeight = header.itemView.getMeasuredHeight();
            }
            LinearLayoutManager llm = ((LinearLayoutManager) recyclerView.getLayoutManager());
            int first = llm.findFirstVisibleItemPosition();
            View firstVisibleView = llm.findViewByPosition(first);
            if (first == 0) {
                scrollY = -firstVisibleView.getTop();
            } else {
                scrollY = topViewHeight + (first - 1) * firstVisibleView.getMeasuredHeight() - firstVisibleView.getTop();
            }
            updateContentByScrollY();
        }


        void updateContentByScrollY() {
            if (firEdge == -1 && secEdge == -1) {
                int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
                firEdge = header.debtAmount.getBottom() + statusHeight;
                secEdge = header.itemView.getBottom() + getResources().getDimensionPixelSize(R.dimen.tool_bar_height);
            }
            if (scrollY >= firEdge) {
                if (debtAmountContainer.getVisibility() == View.GONE) {
                    debtAmountContainer.setVisibility(View.VISIBLE);
                }
            } else {
                if (debtAmountContainer.getVisibility() == View.VISIBLE) {
                    debtAmountContainer.setVisibility(View.GONE);
                }
            }
            if (scrollY >= secEdge) {
                if (analyzeView.getVisibility() == View.GONE) {
                    analyzeView.setVisibility(View.VISIBLE);
                    calendarView.setVisibility(View.VISIBLE);
                }
                if (billAdd.getVisibility() == View.VISIBLE) {
                    billAdd.setVisibility(View.GONE);
                }
                if (billAddNoCircle.getVisibility() == View.INVISIBLE) {
                    billAddNoCircle.setVisibility(View.VISIBLE);
                }
            } else {
                if (analyzeView.getVisibility() == View.VISIBLE) {
                    analyzeView.setVisibility(View.GONE);
                    calendarView.setVisibility(View.GONE);
                }
                if (billAdd.getVisibility() == View.GONE) {
                    billAdd.setVisibility(View.VISIBLE);
                }
                if (billAddNoCircle.getVisibility() == View.VISIBLE) {
                    billAddNoCircle.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private TabScrollListener tabScrollListener = new TabScrollListener();

    public static TabAccountFragment newInstance() {
        return new TabAccountFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_TAB_ACCOUNT);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onStart();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_account;
    }

    @Override
    public void configViews() {
        int statusHeight = CommonUtils.getStatusBarHeight(getActivity());
        toolbar.setPadding(toolbar.getPaddingLeft(), statusHeight, toolbar.getPaddingRight(), 0);
        ViewGroup.LayoutParams lp = toolbar.getLayoutParams();
        lp.height += statusHeight;
        toolbar.setLayoutParams(lp);

        addView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_ADD_DEBT);
                //点击音效
                SoundUtils.getInstance().playAdd();

                presenter.clickAdd();
            }
        });
        calendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickCalendar();
            }
        });
        analyzeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickAnalyze();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DebtRVAdapter();
        final View headerView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_tab_account_header, recyclerView, false);
        header = new DebtHeader(headerView);
        header.allDebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_ALL_DEBT);

                presenter.clickAllDebt();
            }
        });
        header.debtEye.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                presenter.clickEye(isChecked);
            }
        });
        header.debtCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_DEBT_CALENDAR);

                presenter.clickCalendar();
            }
        });
        header.debtAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_DEBT_ANALYZE);

                presenter.clickAnalyze();
            }
        });
        headerView.setPadding(headerView.getPaddingLeft(), statusHeight, headerView.getPaddingRight(), 0);
        adapter.setHeaderView(headerView);

        View footView = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_tab_account_foot, recyclerView, false);
        footView.findViewById(R.id.credit_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickCreditCard();
            }
        });
        adapter.setFooterView(footView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.set_status) {
                    ((SwipeMenuLayout) ((BaseViewHolder) recyclerView.findViewHolderForAdapterPosition(position + 1)).getView(R.id.swipe_menu_layout)).quickClose();
                    presenter.updateDebtStatus(position);
                } else if (view.getId() == R.id.debt_container) {
                    presenter.clickDebt(position);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DebtItemDeco());
        recyclerView.addOnScrollListener(tabScrollListener);

        if (!SPUtils.getTabAccountDialogShowed(getContext())) {
            new TabAccountHintDialog().show(getChildFragmentManager(), "TabAccountHint");
            SPUtils.setTabAccountDialogShowed(getContext(), true);
        }

        billsBgImage.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                billsBgImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ViewGroup.LayoutParams lp = header.itemView.getLayoutParams();
                lp.height = billsBgImage.getMeasuredHeight();
                header.itemView.setLayoutParams(lp);

                billsBgImage.setDrawingCacheEnabled(true);
                Bitmap drawingCache = billsBgImage.getDrawingCache();
                if (drawingCache != null) {
                    Bitmap res = Bitmap.createBitmap(drawingCache, 0, 0, toolbar.getMeasuredWidth(), toolbar.getMeasuredHeight());
                    toolbar.setBackground(new BitmapDrawable(getResources(), res));
                    drawingCache.recycle();
                }
            }
        });
    }

    @Override
    public void initDatas() {
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtComponent.builder()
                .appComponent(appComponent)
                .debtModule(new DebtModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected boolean needFakeStatusBar() {
        return false;
    }

    @Override
    public void setPresenter(DebtContract.Presenter presenter) {
        //
    }

    @Override
    public void showNoUserLoginBlock() {
        if (adapter.getFooterLayout().getChildCount() == 1) {
            //可能出现重读调用，确保只添加一次
            View noUserLoginFootView = LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_tab_account_no_user_foot, recyclerView, false);
            noUserLoginFootView.findViewById(R.id.debt_add).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //pv，uv统计
                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CLICK_ADD_DEBT);

                    presenter.clickAdd();
                }
            });
            adapter.addFooterView(noUserLoginFootView, 0);
        }

        addView.setVisibility(View.GONE);

        updateContent(false);
        updateHide(false);
        updateNoUserBlock(true);
    }

    @Override
    public void showUserLoginBlock() {
        if (adapter.getFooterLayout().getChildCount() > 1) {
            adapter.getFooterLayout().removeViewAt(0);
        }

        addView.setVisibility(View.VISIBLE);

        updateContent(true);
        updateHide(false);
        updateNoUserBlock(false);
    }

    @Override
    public void showNoDebtListBlock() {
        showNoUserLoginBlock();
    }

    @Override
    public void showDebtInfo(double debtAmount, double debtSevenDay, double debtMonth) {
        String amountStr = keep2digits(debtAmount);
        if (amountStr.contains(".")) {
            SpannableString ss = new SpannableString(amountStr);
            ss.setSpan(new AbsoluteSizeSpan(20, true), amountStr.indexOf("."), amountStr.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            header.debtAmount.setText(ss);
        } else {
            header.debtAmount.setText(amountStr);
        }
        header.debtSevenDay.setText(keep2digits(debtSevenDay));
        header.debtMonth.setText(keep2digits(debtMonth));

        this.debtAmount.setText(amountStr);
    }


    @Override
    public void showInDebtList(List<InDebt> list) {
        if (isAdded()) {
            adapter.notifyDebtChanged(list);
            //RecyclerView重新layout之后再做调整
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tabScrollListener.resetScrollY();
                }
            }, 20);
        }
    }

    @Override
    public void showGuide() {
        infoHighLight = new HighLight(getActivity())
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        infoHighLight
                                .addHighLight(addView, R.layout.layout_tab_account_highlight_guide, new OnBaseCallback() {
                                    @Override
                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                                        marginInfo.rightMargin = rectF.width() / 2 + 20;
                                        marginInfo.bottomMargin = bottomMargin - getResources().getDisplayMetrics().density * 90 - rectF.height();
                                    }
                                }, new CircleLightShape())
                                .addHighLight(guideConfirmAnchor, R.layout.layout_highlight_confirm, new OnBaseCallback() {
                                    @Override
                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                                        marginInfo.rightMargin = rightMargin / 2;
                                        marginInfo.bottomMargin = bottomMargin - getResources().getDisplayMetrics().density * 90;
                                    }
                                }, new CircleLightShape())
                                .autoRemove(false)
                                .show();
                        infoHighLight.getHightLightView().findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                infoHighLight.remove();
                            }
                        });
                    }
                });
    }

    @Override
    public void showUpdateStatusSuccess(String msg) {
        ToastUtils.showShort(getContext(), msg, null);
    }

    @Override
    public void navigateUserLogin() {
        UserAuthorizationActivity.launch(getActivity(), null);
    }

    @Override
    public void navigateAddDebt() {
        startActivity(new Intent(getContext(), AddDebtActivity.class));
    }

    @Override
    public void navigateCalendar() {
        startActivity(new Intent(getContext(), DebtCalendarActivity.class));
    }

    @Override
    public void navigateAnalyze() {
        startActivity(new Intent(getContext(), DebtAnalyzeActivity.class));
    }

    @Override
    public void navigateDebtDetail(InDebt inDebt) {
        Intent intent = new Intent(getContext(), DebtDetailActivity.class);
        intent.putExtra("debt_id", inDebt.getId());
        startActivity(intent);

    }

    @Override
    public void navigateCreditCardCenter() {
        startActivity(new Intent(getContext(), CreditCardWebActivity.class));
    }

    @Override
    public void navigateAllDebt() {
        startActivity(new Intent(getContext(), AllDebtActivity.class));
    }

    @Override
    public void showDebtInfo() {
        updateContent(true);
        updateNoUserBlock(false);
        updateHide(false);
    }

    @Override
    public void hideDebtInfo() {
        updateContent(false);
        updateNoUserBlock(false);
        updateHide(true);
    }

    private void updateContent(boolean show) {
        header.debtAmount.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        header.debtSevenDay.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        header.debtMonth.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateNoUserBlock(boolean show) {
        header.debtAmountNoUserLogin.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        header.debtSevenDayNoUserLogin.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        header.debtMonthNoUserLogin.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void updateHide(boolean show) {
        header.debtAmountHide.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        header.debtSevenHide.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        header.debtMonthHide.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }
}
