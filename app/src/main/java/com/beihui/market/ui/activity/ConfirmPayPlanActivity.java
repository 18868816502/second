package com.beihui.market.ui.activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.RectF;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.PayPlan;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerConfirmPayPlanComponent;
import com.beihui.market.injection.module.ConfirmPayPlanModule;
import com.beihui.market.ui.adapter.ConfirmPayPlanRVAdapter;
import com.beihui.market.ui.contract.ConfirmPayPlanContract;
import com.beihui.market.ui.dialog.EditPayPlanDialog;
import com.beihui.market.ui.presenter.ConfirmPayPlanPresenter;
import com.beihui.market.util.viewutils.ToastUtils;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBaseCallback;
import zhy.com.highlight.shape.CircleLightShape;

import static com.beihui.market.util.CommonUtils.keep2digits;
import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

public class ConfirmPayPlanActivity extends BaseComponentActivity implements ConfirmPayPlanContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.sticky_header_container)
    FrameLayout stickyHeaderContainer;
    @BindView(R.id.confirm)
    View confirmView;

    @BindView(R.id.guide_confirm_anchor)
    View guideConfirmAnchor;

    class Header {
        View itemView;
        @BindView(R.id.logo)
        ImageView logoView;
        @BindView(R.id.channel_name)
        TextView channelName;
        @BindView(R.id.project_name_container)
        View projectNameContainer;
        @BindView(R.id.project_name)
        TextView projectName;
        @BindView(R.id.debt_amount)
        TextView debtAmountView;
        @BindView(R.id.interest_rate)
        TextView rateView;
        @BindView(R.id.sticky_header_container)
        ViewGroup stickyHeaderContainer;
        @BindView(R.id.sticky_header)
        View stickyHeader;

        Header(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    @Inject
    ConfirmPayPlanPresenter presenter;

    private ConfirmPayPlanRVAdapter adapter;
    private Header header;

    private HighLight infoHighLight;

    @Override
    public int getLayoutId() {
        return R.layout.activity_confirm_pay_plan;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        setupToolbarBackNavigation(toolbar, R.mipmap.left_arrow_black);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ConfirmPayPlanRVAdapter();
        header = new Header(LayoutInflater.from(this)
                .inflate(R.layout.layout_confirm_pay_plan_head, recyclerView, false));
        adapter.setHeaderView(header.itemView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                final int pos = position;
                PayPlan.RepayPlanBean bean = ((ConfirmPayPlanRVAdapter) adapter).getItem(pos);
                if (view.getId() == R.id.amount_edit) {
                    new EditPayPlanDialog().setOriginalAmount(bean.getTermPayableAmount() + "")
                            .setPayPlanAmountChangedListener(new EditPayPlanDialog.PayPlanAmountChangedListener() {
                                @Override
                                public void onPayPlanAmountChanged(String amount) {
                                    presenter.editPayPlanAmount(pos, amount);
                                }
                            }).show(getSupportFragmentManager(), "EditPayPlan");
                } else {
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
                    try {
                        calendar.setTime(dateFormat.parse(bean.getTermRepayDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    TimePickerView pickerView = new TimePickerView.Builder(ConfirmPayPlanActivity.this, new TimePickerView.OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            presenter.editPayPlanDate(pos, date);
                        }
                    }).setType(new boolean[]{true, true, true, false, false, false})
                            .setCancelText("取消")
                            .setCancelColor(Color.parseColor("#5591ff"))
                            .setSubmitText("确认")
                            .setSubmitColor(Color.parseColor("#5591ff"))
                            .setTitleText("还款日期")
                            .setTitleColor(getResources().getColor(R.color.black_1))
                            .setTitleBgColor(Color.WHITE)
                            .setBgColor(Color.WHITE)
                            .setLabel("年", "月", "日", null, null, null)
                            .isCenterLabel(false)
                            .setDate(calendar)
                            .build();
                    pickerView.show();
                }
            }
        });
        recyclerView.setAdapter(adapter);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int scrollY;
            private int edge = -1;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (edge == -1) {
                    edge = header.itemView.getMeasuredHeight() - header.stickyHeaderContainer.getMeasuredHeight();
                }
                scrollY += dy;
                if (scrollY >= edge) {
                    //尚未添加进fake容器
                    if (stickyHeaderContainer.getChildCount() == 0) {
                        header.stickyHeaderContainer.removeView(header.stickyHeader);
                        stickyHeaderContainer.addView(header.stickyHeader);
                    }
                } else {
                    //已经添加进fake容器
                    if (stickyHeaderContainer.getChildCount() == 1) {
                        stickyHeaderContainer.removeView(header.stickyHeader);
                        header.stickyHeaderContainer.addView(header.stickyHeader);
                    }
                }
            }
        });

        confirmView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_CONFIRM_REPAY_PLAN);

                showProgress(null);
                presenter.confirmPayPlan();
            }
        });

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        DebtDetail pendingDebt = getIntent().getParcelableExtra("pending_debt");
        if (pendingDebt != null) {
            presenter.attachPendingDebt(pendingDebt);
        }
        presenter.onStart();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerConfirmPayPlanComponent.builder()
                .appComponent(appComponent)
                .ConfirmPayPlanModule(new ConfirmPayPlanModule(this))
                .payPlan((PayPlan) getIntent().getParcelableExtra("pay_plan"))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(ConfirmPayPlanContract.Presenter presenter) {
        //
    }

    @Override
    public void showPayPlanAbstract(PayPlan payPlan) {
        if (payPlan != null) {
            if (!TextUtils.isEmpty(payPlan.getLogo())) {
                Glide.with(this)
                        .load(payPlan.getLogo())
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.image_place_holder)
                        .into(header.logoView);
            } else {
                header.logoView.setImageResource(R.drawable.image_place_holder);
            }
            header.channelName.setText(payPlan.getChannelName());
            if (!TextUtils.isEmpty(payPlan.getProjectName())) {
                header.projectNameContainer.setVisibility(View.VISIBLE);
                header.projectName.setText(payPlan.getProjectName());
            } else {
                header.projectNameContainer.setVisibility(View.GONE);
            }

            header.debtAmountView.setText(keep2digitsWithoutZero(payPlan.getPayableAmount()));
            header.rateView.setText(keep2digits(payPlan.getRate()) + "%");
        }
    }

    @Override
    public void showPayPlanList(List<PayPlan.RepayPlanBean> list) {
        adapter.notifyPayPlanChanged(list);
    }

    @Override
    public void showConfirmSuccess(String msg) {
        dismissProgress();
        ToastUtils.showShort(this, msg, R.drawable.toast_smile);
        header.itemView.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ConfirmPayPlanActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("account", true);
                startActivity(intent);
            }
        }, 200);
    }

    @Override
    public void showGuide() {
        infoHighLight = new HighLight(this)
                .setOnLayoutCallback(new HighLightInterface.OnLayoutCallback() {
                    @Override
                    public void onLayouted() {
                        View anchorView = recyclerView.getChildAt(1).findViewById(R.id.amount_edit);
                        infoHighLight
                                .addHighLight(anchorView, R.layout.layout_confirm_highlight_guide, new OnBaseCallback() {
                                    @Override
                                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                                        marginInfo.bottomMargin = bottomMargin + rectF.height() + 20;
                                        marginInfo.rightMargin = rightMargin - getResources().getDisplayMetrics().density * 26;
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
}
