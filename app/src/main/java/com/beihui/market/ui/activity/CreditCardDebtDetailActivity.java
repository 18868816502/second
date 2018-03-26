package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CreditCardDebtBill;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerCreditCardDebtDetailComponent;
import com.beihui.market.injection.module.CreditCardDebtDetailModule;
import com.beihui.market.ui.adapter.CreditCardDebtDetailAdapter;
import com.beihui.market.ui.adapter.multipleentity.CreditCardDebtDetailMultiEntity;
import com.beihui.market.ui.contract.CreditCardDebtDetailContract;
import com.beihui.market.ui.dialog.BillEditAmountDialog;
import com.beihui.market.ui.dialog.CreditCardDebtDetailDialog;
import com.beihui.market.ui.presenter.CreditCardDebtDetailPresenter;
import com.beihui.market.util.DateFormatUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.text.TextUtils.isEmpty;
import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

public class CreditCardDebtDetailActivity extends BaseComponentActivity implements CreditCardDebtDetailContract.View {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.bank_name_num)
    TextView tvBankNameNum;
    @BindView(R.id.detail_menu)
    ImageView ivDetailMenu;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.debt_status_operation_block)
    FrameLayout flDebtStatusOperationBlock;
    @BindView(R.id.debt_status_operation)
    TextView tvDebtStatusOperation;
    @BindView(R.id.debt_status_time)
    TextView tvDebtStatusTime;


    class Header {
        View itemView;

        @BindView(R.id.bank_logo)
        ImageView ivBankLogo;
        @BindView(R.id.bank_name)
        TextView tvBankName;
        @BindView(R.id.credit_card_number)
        TextView tvCreditCardNumber;
        @BindView(R.id.debt_date)
        TextView tvDebtDate;
        @BindView(R.id.debt_amount)
        TextView tvDebtAmount;
        @BindView(R.id.set_status)
        TextView tvSetStatus;
        @BindView(R.id.min_payment)
        TextView tvMinPayment;
        @BindView(R.id.debt_bill_day)
        TextView tvDebtBillDay;
        @BindView(R.id.debt_due_day)
        TextView tvDebtDueDay;
        @BindView(R.id.max_interest_free_time)
        TextView tvMaxInterestFreeTime;
        @BindView(R.id.card_owner)
        TextView tvCardOwner;
        @BindView(R.id.credit_amount)
        TextView tvCreditAmount;

        Header(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, this.itemView);
        }
    }

    @Inject
    CreditCardDebtDetailPresenter presenter;

    private Header header;
    private CreditCardDebtDetailAdapter detailAdapter;
    private CreditCardDebtDetail debtDetail;

    private boolean byHand;

    private CreditCardDebtDetailDialog dialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_credit_card_debt_detail;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void configViews() {
        byHand = getIntent().getBooleanExtra("by_hand", false);

        setupToolbar(toolbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        header = new Header(LayoutInflater.from(this).inflate(R.layout.rv_item_credit_card_debt_header, recyclerView, false));
        detailAdapter = new CreditCardDebtDetailAdapter(byHand);
        detailAdapter.setHeaderView(header.itemView);
        recyclerView.setAdapter(detailAdapter);

        detailAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.fetchDebtMonthBill();
            }
        }, recyclerView);
        detailAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {

            private int lastExpandedPos;

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.month_bill_container: {
                        CreditCardDebtDetailMultiEntity entity = (CreditCardDebtDetailMultiEntity) adapter.getItem(position);
                        if (entity != null) {
                            if (entity.isExpanded()) {
                                //使用adapter方法收缩
                                detailAdapter.collapseMonthBill(position);
                            } else {
                                int dataIndex = detailAdapter.indexOf(entity);
                                int viewIndex = position;
                                //先关闭之前的item
                                CreditCardDebtDetailMultiEntity last = (CreditCardDebtDetailMultiEntity) adapter.getItem(lastExpandedPos);
                                if (last != null && last.isExpanded()) {
                                    //如果先前已经有展开的item，则item在关闭之后，viewIndex会有变化
                                    detailAdapter.collapseMonthBill(lastExpandedPos);
                                    //更新viewIndex的位置
                                    if (lastExpandedPos < position) {
                                        //如果当前点击位置大于之前点击位置，则更新viewIndex位置信息，否则则以position信息作为viewIndex
                                        viewIndex = last.getSubItems() != null ? position - last.getSubItems().size() : position;
                                    }
                                }
                                if (entity.isInit()) {
                                    //如果已经获取过数据，则直接展开
                                    adapter.expand(viewIndex + 1);
                                } else {
                                    CreditCardDebtBill bill = entity.getMonthBill();
                                    if (bill.getBillSource() == 3) {
                                        //手动记账的账单没有详细
                                        detailAdapter.notifyBillDetailChanged(Collections.<BillDetail>emptyList(), viewIndex);
                                    } else {
                                        //获取数据后再展开全部
                                        showProgress();
                                        presenter.fetchBillDetail(dataIndex, viewIndex);
                                    }
                                }
                                //上次点击位置以收缩头部之后的viewIndex为准
                                lastExpandedPos = viewIndex;
                            }
                        }
                        break;
                    }
                    case R.id.edit_amount: {
                        CreditCardDebtDetailMultiEntity entity = (CreditCardDebtDetailMultiEntity) adapter.getItem(position);
                        final int dataIndex = detailAdapter.indexOf(entity);
                        if (entity != null) {
                            new BillEditAmountDialog().attachConfirmListener(new BillEditAmountDialog.EditAmountConfirmListener() {
                                @Override
                                public void onEditAmountConfirm(double amount) {
                                    presenter.updateBillAmount(dataIndex, amount);
                                }
                            }).attachPendingAmount(entity.getMonthBill().getNewBalance()).show(getSupportFragmentManager(), "EditAmount");
                        }
                        break;
                    }
                    case R.id.add_amount: {
                        CreditCardDebtDetailMultiEntity entity = (CreditCardDebtDetailMultiEntity) adapter.getItem(position);
                        final int dataIndex = detailAdapter.indexOf(entity);
                        if (entity != null) {
                            new BillEditAmountDialog().attachConfirmListener(new BillEditAmountDialog.EditAmountConfirmListener() {
                                @Override
                                public void onEditAmountConfirm(double amount) {
                                    presenter.updateBillAmount(dataIndex, amount);
                                }
                            }).attachPendingAmount(entity.getMonthBill().getNewBalance()).show(getSupportFragmentManager(), "EditAmount");
                        }
                        break;
                    }
                }
            }
        });

        String backName = getIntent().getStringExtra("bank_name");
        String cardNum = getIntent().getStringExtra("card_num");
        tvBankNameNum.setText(backName + " " + cardNum);
        header.tvBankName.setText(backName);
        header.tvCreditCardNumber.setText(cardNum);
        String logoUrl = getIntent().getStringExtra("logo");
        if (!isEmpty(logoUrl)) {
            Glide.with(this)
                    .load(logoUrl)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into(header.ivBankLogo);
        } else {
            header.ivBankLogo.setImageResource(R.drawable.image_place_holder);
        }

        if (byHand) {
            tvDebtStatusOperation.setText("开始同步");
        } else {
            tvDebtStatusOperation.setText("更新账单");
            tvDebtStatusTime.setVisibility(View.VISIBLE);
        }

        header.tvSetStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickSetStatus();
            }
        });
    }

    @Override
    public void initDatas() {
        presenter.fetchDebtDetail();
        presenter.fetchDebtMonthBill();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerCreditCardDebtDetailComponent.builder()
                .appComponent(appComponent)
                .creditCardDebtDetailModule(new CreditCardDebtDetailModule(this))
                .debtId(getIntent().getStringExtra("debt_id"))
                .build()
                .inject(this);
    }

    @OnClick(R.id.detail_menu)
    void onItemClicked() {
        presenter.clickMenu();
    }

    @Override
    public void setPresenter(CreditCardDebtDetailContract.Presenter presenter) {
        //
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showDebtDetailInfo(CreditCardDebtDetail debtDetail) {
        this.debtDetail = debtDetail;
        if (debtDetail != null) {
            CreditCardDebtDetail.ShowBillBean showBill = debtDetail.getShowBill();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINA);
            Calendar calendar = Calendar.getInstance(Locale.CHINA);
            if (!isEmpty(debtDetail.getLastCollectionDate())) {
                //上次更新时间
                try {
                    if (tvDebtStatusTime.getVisibility() == View.VISIBLE) {
                        tvDebtStatusTime.setText(DateFormatUtils.generateCreditCardDebtUpdateTime(dateFormat.parse(debtDetail.getLastCollectionDate()).getTime()));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (showBill != null) {
                //账单月份
                if (!isEmpty(showBill.getBillDate())) {
                    try {
                        Date date = dateFormat.parse(showBill.getBillDate());
                        calendar.setTime(date);
                        header.tvDebtDate.setText((calendar.get(Calendar.MONTH) + 1) + "月账单");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //账单金额
                char symbol = 165;
                header.tvDebtAmount.setText(String.valueOf(symbol) + keep2digitsWithoutZero(debtDetail.getShowBill().getNewBalance()));
                //账单状态
                switch (showBill.getStatus()) {
                    case 1://待还
                        header.tvSetStatus.setText("设为已还");
                        break;
                    case 2://已还
                        header.tvSetStatus.setText("已还清");
                        header.tvSetStatus.setEnabled(false);
                        break;
                    case 3:
                        header.tvSetStatus.setText("设为已还");
                        break;
                    default:
                        header.tvSetStatus.setVisibility(View.GONE);
                        break;
                }
                //最低应还
                if (showBill.getMinPayment() > 0) {
                    header.tvMinPayment.setText(keep2digitsWithoutZero(showBill.getMinPayment()) + "元");
                } else {
                    header.tvMinPayment.setText("----");
                }
                //出账日
                if (!isEmpty(showBill.getBillDate())) {
                    header.tvDebtBillDay.setText(showBill.getBillDate().substring(0, 10));
                }
                //还款日
                if (!isEmpty(showBill.getPaymentDueDate())) {
                    header.tvDebtDueDay.setText(showBill.getPaymentDueDate().substring(0, 10));
                }
            }
            //最长免息期
            header.tvMaxInterestFreeTime.setText(debtDetail.getMaxFreeInterestDay() + "天");
            //信用额度
            header.tvCreditAmount.setText(debtDetail.getCreditLimit() + "元");
            //卡主姓名
            header.tvCardOwner.setText(debtDetail.getCardUserName());
        }
    }

    @Override
    public void showDebtBillList(List<CreditCardDebtBill> list, boolean canLoadMore) {
        if (detailAdapter.isLoading()) {
            detailAdapter.loadMoreComplete();
        }
        detailAdapter.setEnableLoadMore(canLoadMore);
        detailAdapter.notifyDebtListChanged(list);
    }

    @Override
    public void showBillDetail(List<BillDetail> list, int index) {
        dismissProgress();
        //展开头部
        detailAdapter.expandMonthBill(index);
        //展开详细
        detailAdapter.notifyBillDetailChanged(list, index);
    }

    @Override
    public void showSetStatusSuccess() {

    }

    @Override
    public void showMenu(boolean remind) {
        dialog = new CreditCardDebtDetailDialog();
        dialog.attachEditable(byHand)
                .attachListeners(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         dialog.dismiss();
                                         if (v.getId() == R.id.edit) {
                                             if (debtDetail != null) {
                                                 Intent intent = new Intent(CreditCardDebtDetailActivity.this, CreditCardDebtNewActivity.class);
                                                 intent.putExtra("credit_card_debt_detail", debtDetail);
                                                 startActivity(intent);
                                             }
                                         } else if (v.getId() == R.id.delete) {
                                             presenter.clickDelete();
                                         }
                                     }
                                 },
                        new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                //更新还款提醒
                                presenter.clickUpdateRemind();
                            }
                        })
                .attachInitStatus(remind).show(getSupportFragmentManager(), "Operation");
    }

    @Override
    public void showDeleteSuccess() {
        ToastUtils.showShort(this, "删除成功", null);
        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 100);
    }

    @Override
    public void showUpdateRemindStatus(boolean success) {
        if (dialog != null) {
            dialog.updateRemind(success);
        }
    }

    @Override
    public void showUpdateBillAmountSuccess() {

    }

    @Override
    public void showErrorMsg(String msg) {
        super.showErrorMsg(msg);
        if (detailAdapter.isLoading()) {
            detailAdapter.loadMoreComplete();
        }
        detailAdapter.setEnableLoadMore(false);
    }
}
