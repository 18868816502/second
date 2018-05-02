package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CreditCardDebtBill;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerCreditCardDebtDetailComponent;
import com.beihui.market.injection.module.CreditCardDebtDetailModule;
import com.beihui.market.ui.adapter.CreditCardDebtDetailAdapter;
import com.beihui.market.ui.adapter.multipleentity.CreditCardDebtDetailMultiEntity;
import com.beihui.market.ui.contract.CreditCardDebtDetailContract;
import com.beihui.market.ui.dialog.BillEditAmountDialog;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.dialog.CreditCardDebtDetailDialog;
import com.beihui.market.ui.presenter.CreditCardDebtDetailPresenter;
import com.beihui.market.ui.rvdecoration.CommVerItemDeco;
import com.beihui.market.util.DateFormatUtils;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.EditTextUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

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

/**
 * @author xhb
 * 信用卡详情
 *
 * @3.0.0 信用卡暂无手动添加 则无编辑情况
 */
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

    @BindView(R.id.tv_credit_card_footer_set_status)
    TextView tvFootSetStatus;
    @BindView(R.id.tv_credit_card_footer_middle_line)
    View tvFootMiddleLine;


    class Header {
        View itemView;

        @BindView(R.id.ll_credit_card_info_header_bg)
        LinearLayout mHeaderCardBg;

//        @BindView(R.id.bank_logo)
//        ImageView ivBankLogo;
//        @BindView(R.id.bank_name)
//        TextView tvBankName;
//        @BindView(R.id.credit_card_number)
//        TextView tvCreditCardNumber;
        @BindView(R.id.debt_date)
        TextView tvDebtDate;
        @BindView(R.id.debt_amount)
        TextView tvDebtAmount;
        @BindView(R.id.tv_credit_card_info_header_status)
        TextView tvStatus;
//        @BindView(R.id.set_status)
//        TextView tvSetStatus;
        @BindView(R.id.min_payment)
        TextView tvMinPayment;
        @BindView(R.id.debt_bill_day)
        TextView tvDebtBillDay;
        @BindView(R.id.debt_due_day)
        TextView tvDebtDueDay;
//        @BindView(R.id.max_interest_free_time)
//        TextView tvMaxInterestFreeTime;
//        @BindView(R.id.card_owner)
//        TextView tvCardOwner;
//        @BindView(R.id.credit_amount)
//        TextView tvCreditAmount;

        @BindView(R.id.tv_credit_card_remark)
        TextView tvRemark;

        Header(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, this.itemView);
        }
    }

    @Inject
    CreditCardDebtDetailPresenter presenter;

    //头布局
    private Header header;
    //列表适配器
    private CreditCardDebtDetailAdapter detailAdapter;
    //详情bean 除去列表数据
    private CreditCardDebtDetail debtDetail;

    //是否手写账单
    private boolean byHand;

    //弹框
    private CreditCardDebtDetailDialog dialog;

    /**
     * 开启信用卡页面
     * @param debtId 账单ID
     * @param byHand 是否是手写账单
     * @param banKName 银行名字
     * @param cardNum 银行卡号
     * @param logo 图标
     */
    public static void launchActivity(Context context, String debtId, boolean byHand, String banKName, String cardNum, String logo) {
        Intent intent = new Intent(context, CreditCardDebtDetailActivity.class);
        intent.putExtra("debt_id", debtId);
        intent.putExtra("by_hand", byHand);
        intent.putExtra("bank_name", banKName);
        intent.putExtra("card_num", cardNum);
        intent.putExtra("logo", logo);
        context.startActivity(intent);
    }

    public static void putExtra(Intent intent, String debtId, boolean byHand, String banKName, String cardNum, String logo) {
        intent.putExtra("debt_id", debtId);
        intent.putExtra("by_hand", byHand);
        intent.putExtra("bank_name", banKName);
        intent.putExtra("card_num", cardNum);
        intent.putExtra("logo", logo);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_credit_card_debt_detail;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void configViews() {
        byHand = getIntent().getBooleanExtra("by_hand", false);
        String backName = getIntent().getStringExtra("bank_name");
        String cardNum = getIntent().getStringExtra("card_num");
        String logoUrl = getIntent().getStringExtra("logo");

        //沉浸式
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        header = new Header(LayoutInflater.from(this).inflate(R.layout.rv_item_credit_card_debt_header, recyclerView, false));
        detailAdapter = new CreditCardDebtDetailAdapter(byHand);
        detailAdapter.setHeaderView(header.itemView);
        recyclerView.setAdapter(detailAdapter);
        recyclerView.addItemDecoration(new CommVerItemDeco((int) (getResources().getDisplayMetrics().density * 0.5), 0, 0, 1));

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
                                    //展开头部
                                    detailAdapter.expandMonthBill(viewIndex);
                                    //如果已经获取过数据，则直接展开
                                    adapter.expand(viewIndex + 1);
                                } else {
                                    CreditCardDebtBill bill = entity.getMonthBill();
                                    if (bill.getBillSource() == 3) {
                                        //手动记账的账单没有详细
                                        //展开头部
                                        detailAdapter.expandMonthBill(viewIndex);
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

        tvBankNameNum.setText(backName + " " + cardNum);
//        header.tvBankName.setText(backName);
//        header.tvCreditCardNumber.setText(cardNum);
//        if (!isEmpty(logoUrl)) {
//            Glide.with(this)
//                    .load(logoUrl)
//                    .asBitmap()
//                    .centerCrop()
//                    .placeholder(R.drawable.image_place_holder)
//                    .into(header.ivBankLogo);
//        } else {
//            header.ivBankLogo.setImageResource(R.drawable.image_place_holder);
//        }

        if (byHand) {
            tvDebtStatusOperation.setText("开始同步");
        } else {
            tvDebtStatusOperation.setText("更新账单");
            tvDebtStatusTime.setVisibility(View.VISIBLE);
        }

        /**
         * TODO 设置已还
         */
//        header.tvSetStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new CommNoneAndroidDialog()
//                        .withMessage("确认设为已还？")
//                        .withNegativeBtn("取消", null)
//                        .withPositiveBtn("确认", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                presenter.clickSetStatus();
//                            }
//                        })
//                        .show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
//            }
//        });

        flDebtStatusOperationBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreditCardDebtDetailActivity.this, EBankActivity.class));
            }
        });
    }

    @Override
    public void initDatas() {
        presenter.fetchDebtDetail();
        presenter.fetchDebtMonthBill();

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_ENTER_CREDIT_CARD_BILL_DETAIL);
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

            /**
             * 头卡片 背景颜色
             */
            if (debtDetail.returnDay <= 3) {
                header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_red_bg));
            } else {
                header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_black_bg));
            }

            /**
             * 备注内容
             */
            if (!TextUtils.isEmpty(debtDetail.remark)) {
                header.tvRemark.setText(debtDetail.remark);
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
                header.tvDebtAmount.setText(String.valueOf((char) 165) + keep2digitsWithoutZero(debtDetail.getShowBill().getNewBalance()));
                /**
                 * 账单状态
                 */
                tvFootSetStatus.setVisibility(View.VISIBLE);
                switch (showBill.getStatus()) {
                    case 1://待还
                        tvFootMiddleLine.setVisibility(View.VISIBLE);
                        tvFootSetStatus.setEnabled(true);
                        tvFootSetStatus.setText("设为已还");
                        header.tvStatus.setText("待还款");
                        break;
                    case 2://已还
                        tvFootSetStatus.setText("已还清");
                        tvFootSetStatus.setEnabled(false);
                        tvFootMiddleLine.setVisibility(View.VISIBLE);
                        header.tvStatus.setText("已还款");
                        break;
                    case 3://逾期
                        tvFootSetStatus.setText("设为已还");
                        tvFootSetStatus.setEnabled(true);
                        tvFootMiddleLine.setVisibility(View.VISIBLE);
                        header.tvStatus.setText("逾期");
                        break;
                    case 4://已出账
                        tvFootSetStatus.setVisibility(View.GONE);
                        tvFootMiddleLine.setVisibility(View.GONE);
                        header.tvStatus.setText("已出账");
                        break;
                    case 5://未出账
                        tvFootSetStatus.setVisibility(View.GONE);
                        tvFootMiddleLine.setVisibility(View.GONE);
                        header.tvStatus.setText("未出账");
                        break;
                    default:
                        tvFootSetStatus.setVisibility(View.GONE);
                        tvFootMiddleLine.setVisibility(View.GONE);
                        header.tvStatus.setVisibility(View.GONE);
                        break;
                }
                //最低应还
                if (byHand) {
                    //手动账单没有最低应还
                    header.tvMinPayment.setText("----");
                } else {
                    header.tvMinPayment.setText(keep2digitsWithoutZero(showBill.getMinPayment()) + "元");
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
//            //最长免息期
//            header.tvMaxInterestFreeTime.setText(debtDetail.getMaxFreeInterestDay() + "天");
//            //信用额度
//            if (byHand) {
//                //手动账单没有信用额度
//                header.tvCreditAmount.setText("----");
//            } else {
//                header.tvCreditAmount.setText(debtDetail.getCreditLimit() + "元");
//            }
//            //卡主姓名
//            header.tvCardOwner.setText(debtDetail.getCardUserName());
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
                                             new CommNoneAndroidDialog().withMessage("确认删除该卡吗？")
                                                     .withNegativeBtn("取消", null)
                                                     .withPositiveBtn("确认", new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             presenter.clickDelete();
                                                         }
                                                     })
                                                     .show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());

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
                Intent intent = new Intent();
                intent.putExtra("deleted_id", debtDetail.getId());
                setResult(RESULT_OK, intent);
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
