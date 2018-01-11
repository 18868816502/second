package com.beihui.market.ui.activity;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtDetailComponent;
import com.beihui.market.injection.module.DebtDetailModule;
import com.beihui.market.ui.adapter.DebtDetailRVAdapter;
import com.beihui.market.ui.contract.AllDebtContract;
import com.beihui.market.ui.contract.DebtDetailContract;
import com.beihui.market.ui.presenter.DebtDetailPresenter;
import com.beihui.market.view.CircleImageView;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.beihui.market.util.CommonUtils.keep2digits;

public class DebtDetailActivity extends BaseComponentActivity implements DebtDetailContract.View {

    private static final int REQUEST_CODE_EDIT = 1;

    static final String[] STATUS_TAG = {"待还", "已还"};
    static final String[] PAY_METHOD = {"一次性还本付息", "等额本息", "等额本金"};

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.fun)
    View fun;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.sticky_header_container)
    FrameLayout stickyHeaderContainer;

    class Header {
        View itemView;
        @BindView(R.id.logo)
        CircleImageView logo;
        @BindView(R.id.channel_name)
        TextView channelName;
        @BindView(R.id.project_name_container)
        View projectNameContainer;
        @BindView(R.id.project_name)
        TextView projectName;
        @BindView(R.id.debt_term_amount)
        TextView debtTermAmount;
        @BindView(R.id.set_status)
        TextView setStatus;
        @BindView(R.id.debt_pay_day)
        TextView debtPayDay;
        @BindView(R.id.debt_paid)
        TextView debtPaid;
        @BindView(R.id.debt_unpaid)
        TextView debtUnpaid;

        @BindView(R.id.debt_amount_total_content)
        TextView debtAmount;
        @BindView(R.id.capital_total_content)
        TextView capital;
        @BindView(R.id.debt_platform_content)
        TextView debtPlatform;
        @BindView(R.id.interest_rate_content)
        TextView interestRate;
        @BindView(R.id.remark_content)
        TextView remark;
        @BindView(R.id.pay_method_content)
        TextView payMethodContent;
        @BindView(R.id.interest_total_content)
        TextView interest;
        @BindView(R.id.debt_life_content)
        TextView debtLife;
        @BindView(R.id.debt_start_date_content)
        TextView debtStartDate;

        @BindView(R.id.sticky_header)
        View stickyHeader;
        @BindView(R.id.sticky_header_container)
        FrameLayout stickyHeaderContainer;

        Header(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    @Inject
    DebtDetailPresenter presenter;

    private DebtDetailRVAdapter adapter;
    private Header header;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_debt_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final PopupWindow popupWindow = new PopupWindow();

        View view = LayoutInflater.from(this).inflate(R.layout.popup_window_debt_detail, null);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                if (v.getId() == R.id.edit) {
                    presenter.editDebt();
                } else {
                    presenter.deleteDebt();
                }
            }
        };
        view.findViewById(R.id.edit).setOnClickListener(clickListener);
        view.findViewById(R.id.delete).setOnClickListener(clickListener);

        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);

        popupWindow.showAsDropDown(fun, -90, 10);
        popupWindow.update((int) (getResources().getDisplayMetrics().density * 125), (int) (getResources().getDisplayMetrics().density * 80.5));
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        header = new Header(LayoutInflater.from(this)
                .inflate(R.layout.layout_debt_detail_header, recyclerView, false));
        header.setStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(DebtDetailActivity.this, 0);
                View dialogView = LayoutInflater.from(DebtDetailActivity.this).inflate(R.layout.dialog_debt_detail_set_status, null);
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (v.getId() == R.id.confirm) {
                            presenter.updateDebtStatus();
                        }
                    }
                };
                dialogView.findViewById(R.id.confirm).setOnClickListener(clickListener);
                dialogView.findViewById(R.id.cancel).setOnClickListener(clickListener);
                ((TextView) dialogView.findViewById(R.id.title)).setText("修改分期状态为" + header.setStatus.getText().toString().substring(2, 4));
                dialog.setContentView(dialogView);
                dialog.setCanceledOnTouchOutside(true);
                Window window = dialog.getWindow();
                if (window != null) {
                    WindowManager.LayoutParams lp = window.getAttributes();
                    window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    window.setAttributes(lp);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                dialog.show();
            }
        });
        adapter = new DebtDetailRVAdapter();
        adapter.setHeaderView(header.itemView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.status) {
                    final int pos = position;
                    OptionsPickerView pickerView = new OptionsPickerView.Builder(DebtDetailActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
                        @Override
                        public void onOptionsSelect(int options1, int options2, int options3, View v) {
                            int status = AllDebtContract.Presenter.STATUS_IN;
                            if (options1 == 1) {
                                status = AllDebtContract.Presenter.STATUS_OFF;
                            }
                            presenter.updateDebtStatus(pos, status);
                        }
                    }).setCancelText("取消")
                            .setCancelColor(Color.parseColor("#5591ff"))
                            .setSubmitText("确认")
                            .setSubmitColor(Color.parseColor("#5591ff"))
                            .setTitleText("")
                            .setTitleColor(getResources().getColor(R.color.black_1))
                            .build();
                    pickerView.setPicker(Arrays.asList(STATUS_TAG));
                    pickerView.show();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int scrollY;
            int edge = -1;

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

        SlidePanelHelper.attach(this);
    }

    @Override
    public void initDatas() {
        presenter.loadDebtDetail();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerDebtDetailComponent.builder()
                .appComponent(appComponent)
                .debtDetailModule(new DebtDetailModule(this))
                .debtId(getIntent().getStringExtra("debt_id"))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(DebtDetailContract.Presenter presenter) {
        //
    }

    @Override
    public void showDebtDetail(DebtDetail debtDetail) {
        String titleName = debtDetail.getChannelName() + (TextUtils.isEmpty(debtDetail.getProjectName()) ? "" : " - " + debtDetail.getProjectName());
        title.setText(titleName);
        if (!TextUtils.isEmpty(debtDetail.getLogo())) {
            Glide.with(this)
                    .load(debtDetail.getLogo())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into(header.logo);
        } else {
            header.logo.setImageResource(R.drawable.image_place_holder);
        }

        header.channelName.setText(debtDetail.getChannelName());
        if (!TextUtils.isEmpty(debtDetail.getProjectName())) {
            header.projectNameContainer.setVisibility(View.VISIBLE);
            header.projectName.setText(debtDetail.getProjectName());
        } else {
            header.projectNameContainer.setVisibility(View.GONE);
        }
        header.debtTermAmount.setText(keep2digits(debtDetail.getTermPayableAmount()));
        header.debtPayDay.setText(debtDetail.getTermRepayDate());
        header.debtPaid.setText(keep2digits(debtDetail.getReturnedAmount()));
        header.debtUnpaid.setText(keep2digits(debtDetail.getStayReturnedAmount()));

        header.debtAmount.setText(keep2digits(debtDetail.getPayableAmount()));
        header.capital.setText(keep2digits(debtDetail.getCapital()));
        header.debtPlatform.setText(debtDetail.getChannelName());
        header.interestRate.setText(keep2digits(debtDetail.getRate()) + "%年息");
        if (!TextUtils.isEmpty(debtDetail.getRemark())) {
            header.remark.setText(debtDetail.getRemark());
        }
        header.payMethodContent.setText(PAY_METHOD[debtDetail.getRepayType() - 1]);
        header.interest.setText(keep2digits(debtDetail.getInterest()));
        header.debtLife.setText(debtDetail.getTerm() + (debtDetail.getTermType() == 1 ? "日" : "月"));
        header.debtStartDate.setText(debtDetail.getStartDate());

        adapter.notifyPayPlanChanged(debtDetail.getRepayPlan());
    }

    @Override
    public void showUpdateStatusSuccess(String msg) {

    }

    @Override
    public void showDeleteDebtSuccess(String msg) {
        finish();
    }

    @Override
    public void navigateAddDebt(DebtDetail debtDetail) {
        Intent intent = new Intent(this, AddDebtActivity.class);
        intent.putExtra("pending_debt", debtDetail);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_EDIT) {
            finish();
        }
    }
}
