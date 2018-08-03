package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.DebeDetailRecord;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.event.MyLoanDebtListFragmentEvent;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtDetailComponent;
import com.beihui.market.injection.module.DebtDetailModule;
import com.beihui.market.ui.adapter.DebtDetailRVAdapter;
import com.beihui.market.ui.contract.DebtDetailContract;
import com.beihui.market.ui.dialog.BillEditAmountDialog;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.dialog.CreditCardDebtDetailDialog;
import com.beihui.market.ui.dialog.EditPayPlanDialog;
import com.beihui.market.ui.dialog.NicknameDialog;
import com.beihui.market.ui.dialog.RemarkDialog;
import com.beihui.market.ui.presenter.DebtDetailPresenter;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FormatNumberUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

import static android.text.TextUtils.isEmpty;
import static com.beihui.market.util.CommonUtils.convertInterestRate;
import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

/**
 * @author xhb
 * 借贷详情
 */
public class LoanDebtDetailActivity extends BaseComponentActivity implements DebtDetailContract.View {

    private static final int REQUEST_CODE_EDIT = 1;

    static final String[] PAY_METHOD = {"到期一次性还款", "每月等额还款", "等额本金"};

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_debt_info_edit_right_button)
    View ivTitleBarRightButton;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.sticky_header_container)
    FrameLayout stickyHeaderContainer;

    //底部根布局  全部还  还部分
    @BindView(R.id.ll_debt_info_foot_root)
    LinearLayout mFootRoot;
    @BindView(R.id.ll_debt_info_foot_root_line)
    View mFootRootLine;
    //全部还
    @BindView(R.id.rv_debt_info_foot_set_pay)
    TextView footSetAllPay;
    //分割线
    @BindView(R.id.tv_debt_info_foot_middle_line)
    View footSetMiddleLine;
    //还部分
    @BindView(R.id.rv_debt_info_foot_pay_part)
    TextView footSetPartPay;

    private DebtDetail debtDetail;

    class Header {
        View itemView;
        @BindView(R.id.logo)
        CircleImageView logo;
        @BindView(R.id.channel_name)
        TextView channelName;
//        @BindView(R.id.project_name_container)
//        View projectNameContainer;
//        @BindView(R.id.project_name)
//        TextView projectName;
        /**
         * 备注内容 或者 备注按钮
         */
        @BindView(R.id.iv_debt_info_header_edit_remark)
        ImageView remarkButton;
        @BindView(R.id.tv_credit_card_remark)
        TextView remarkContent;
        /**
         * 全部待还金额
         */
        @BindView(R.id.debt_detail_term_amount)
        TextView debtTermAmount;
        @BindView(R.id.debt_detail_term_amount_text)
        TextView debtTermAmountText;

        //还款周期
        @BindView(R.id.debt_detail_pay_term)
        TextView debtPayTerm;

        /**
         * 还款期数 当前期/总期数
         */
        @BindView(R.id.debt_detail_pay_day)
        TextView debtPayDay;
        @BindView(R.id.debt_detail_pay_day_text)
        TextView debtPayDayUpText;

        /**
         * 当前还款日
         */
        @BindView(R.id.debt_detail_pay_date)
        TextView debtUnpaid;
        @BindView(R.id.debt_detail_pay_date_text)
        TextView debtUnpaidText;
        //卡片背景
        @BindView(R.id.ll_debt_info_header_card_bg)
        LinearLayout mHeaderCardBg;

//        @BindView(R.id.debt_amount_total_content)
//        TextView debtAmount;
//        @BindView(R.id.capital_total_content)
//        TextView capital;
//        @BindView(R.id.debt_platform_content)
//        TextView debtPlatform;
//        @BindView(R.id.interest_rate_content)
//        TextView interestRate;
//        @BindView(R.id.remark_content)
//        TextView remark;
//        @BindView(R.id.pay_method_content)
//        TextView payMethodContent;
//        @BindView(R.id.interest_total_content)
//        TextView interest;
//        @BindView(R.id.debt_life_content)
//        TextView debtLife;
//        @BindView(R.id.debt_start_date_content)
//        TextView debtStartDate;

//        @BindView(R.id.sticky_header)
//        View stickyHeader;
//        @BindView(R.id.sticky_header_container)
//        FrameLayout stickyHeaderContainer;

        Header(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }


    @Inject
    DebtDetailPresenter presenter;

    //期数列表适配器
    private DebtDetailRVAdapter adapter;
    //头布局
    private Header header;

    private String debtId;
    private String billId;

    private CreditCardDebtDetailDialog dialog;

    /**
     * 还款金额
     */
    public String money;

    /**
     * 初始化 埋点
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_DEBT_DETAIL);

        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTD);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_detail;
    }

    @Override
    public void configViews() {
        setupToolbarBackNavigation(toolbar, R.drawable.x_normal_back);
        //设置状态栏文字为黑色字体
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //头布局 加载
        header = new Header(LayoutInflater.from(this)
                .inflate(R.layout.layout_debt_detail_header, recyclerView, false));

        /**
         * 修改备注
         */
        header.remarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemarkDialog().setNickNameChangedListener(new RemarkDialog.NickNameChangedListener() {
                    @Override
                    public void onNickNameChanged(final String remark) {
                        //pv，uv统计
//                        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDREMARK);

                        if (TextUtils.isEmpty(remark) || remark.length() > 50) {
                            Toast.makeText(LoanDebtDetailActivity.this, "备注不能为空或者字数过多", Toast.LENGTH_SHORT).show();
                        } else {
                            Api.getInstance().updateLoanDebtBillRemark(UserHelper.getInstance(LoanDebtDetailActivity.this).getProfile().getId(), debtId, remark)
                                    .compose(RxUtil.<ResultEntity>io2main())
                                    .subscribe(new Consumer<ResultEntity>() {
                                                   @Override
                                                   public void accept(ResultEntity result) throws Exception {
                                                       if (result.isSuccess()) {
                                                           header.remarkContent.setText("备注  "+remark);
                                                           debtDetail.setRemark(remark);
                                                       } else {
                                                           Toast.makeText(LoanDebtDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               },
                                            new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    //Log.e("exception_custom", throwable.getMessage());
                                                }
                                            });;
                        }
                    }
                }).show(getSupportFragmentManager(), "remark");
            }
        });

        adapter = new DebtDetailRVAdapter(this);
        adapter.setHeaderView(header.itemView);
        /**
         * Item的点击事件
         */
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {

                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDLISTCLICK);

                if (view.getId() == R.id.ll_item_debt_detail_root) {
                    ((TextView) view.findViewById(R.id.th)).setTextColor(Color.RED);
                    LoanDebtDetailActivity.this.adapter.setThTextColor(debtDetail.detailList.get(position).termRepayDate);
                    presenter.clickSetStatus(position);

                    money = FormatNumberUtils.FormatNumberFor2(debtDetail.detailList.get(position).getTermPayableAmount());

                    /**
                     * 请求还款记录
                     */
                    Api.getInstance().getDebeDetailRecord(UserHelper.getInstance(LoanDebtDetailActivity.this).getProfile().getId(), debtDetail.getRepayPlan().get(index).getId())
                            .compose(RxUtil.<ResultEntity<List<DebeDetailRecord>>>io2main())
                            .subscribe(new Consumer<ResultEntity<List<DebeDetailRecord>>>() {
                                           @Override
                                           public void accept(ResultEntity<List<DebeDetailRecord>> result) throws Exception {
                                               if (result.isSuccess()) {
                                                   LoanDebtDetailActivity.this.adapter.showDebtDetailRecord(position, result.getData());
                                               } else {
                                                   Toast.makeText(LoanDebtDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       },
                                    new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            //Log.e("exception_custom", throwable.getMessage());
                                        }
                                    });
                }
            }
        });
        recyclerView.setAdapter(adapter);

        SlidePanelHelper.attach(this);
    }



    @Override
    protected void configureComponent(AppComponent appComponent) {
        debtId = getIntent().getStringExtra("debt_id");
        billId = getIntent().getStringExtra("bill_id");
        DaggerDebtDetailComponent.builder()
                .appComponent(appComponent)
                .debtDetailModule(new DebtDetailModule(this))
                .debtId(debtId)
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(DebtDetailContract.Presenter presenter) {
        //
    }

    /**
     * 网络请求 单个账单的请求
     */
    @Override
    public void initDatas() {
        presenter.loadDebtDetail(billId);
    }

    /**
     * 回调 显示单个账单数据
     * @param debtDetail 借款详情
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void showDebtDetail(final DebtDetail debtDetail) {
        if (!debtDetail.getId().equals(SPUtils.getValue(this, debtDetail.getId()))) {
            if (debtDetail.getTerm() > 12) {
                //com.beihui.market.util.ToastUtils.showToast(this, "账单分期大于12期，只显示最近6期");
                ToastUtil.toast("账单分期大于12期，只显示最近6期");
                SPUtils.setValue(this, debtDetail.getId());
            } else if (debtDetail.getTerm() == -1) {
                //com.beihui.market.util.ToastUtils.showToast(this, "循环账单只显示最近2期");
                ToastUtil.toast("循环账单只显示最近2期");
                SPUtils.setValue(this, debtDetail.getId());
            }
        }


        this.debtDetail = debtDetail;
        /**
         * 头卡片 背景颜色
         */
        //字体颜色
//        header.debtTermAmount.setTextColor(Color.parseColor("#ffffff"));
//        header.debtPayDay.setTextColor(Color.parseColor("#aaffffff"));
//        header.debtUnpaid.setTextColor(Color.parseColor("#aaffffff"));
//        header.debtTermAmountText.setTextColor(Color.parseColor("#88ffffff"));
//        header.debtPayDayUpText.setTextColor(Color.parseColor("#88ffffff"));
//        header.debtUnpaidText.setTextColor(Color.parseColor("#88ffffff"));

//        if (debtDetail.showBill.returnDay > 3 || debtDetail.showBill.status == 2) {
//            header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_black_bg));
//        } else {
//            header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_red_bg));
//        }

        /**
         * 标题栏 右上角菜单栏 点击事件
         */
        ivTitleBarRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.clickMenu();
            }
        });
        //全部待还金额
        header.debtTermAmount.setText(FormatNumberUtils.FormatNumberFor2(debtDetail.getStayReturnedAmount()));
        /**
         * 判断是一次性还款还是分期还款
         * termType 1 为一次性还款 2 分期还款
         */
        if (debtDetail.getRepayType()== 1) {
            //还款期数 当前期/总期数
            header.debtPayDay.setText("一次性还款");
        } else {
            //还款期数 当前期/总期数
            if (debtDetail.getTerm() == -1) {
                header.debtPayDay.setText(debtDetail.returnedTerm + "/" + "循环");
            } else {
                header.debtPayDay.setText(debtDetail.returnedTerm + "/" + debtDetail.getTerm());
            }
        }
        //还款周期
        if (1 == debtDetail.getTermType() || 0 == debtDetail.getTermType()) {
            //日 一次性还款
            header.debtPayTerm.setText("每月");
        }
        if (2 == debtDetail.getTermType()) {
            //月
            header.debtPayTerm.setText( -1 == debtDetail.cycle? "循环" : CommonUtils.getChaneseNum(debtDetail.cycle));
        }
        if (3 == debtDetail.getTermType()) {
            //年
            header.debtPayTerm.setText( -1 == debtDetail.cycle? "循环" : "每年");
        }


        //当期还款日
        header.debtUnpaid.setText(debtDetail.showBill.termRepayDate.replace("-","."));

        /**
         * 设置当前期号 index
         */
//        if (debtDetail.showBill == null || debtDetail.showBill.termNo == null || debtDetail.showBill.termNo > debtDetail.detailList.size()) {
//            showSetStatus(0, 0);
//        } else {
//            showSetStatus(debtDetail.showBill.termNo <= 0 ? 0 : debtDetail.showBill.termNo - 1, debtDetail.getRepayPlan().get(debtDetail.showBill.termNo <= 0 ? 0 : debtDetail.showBill.termNo - 1).getStatus());
//        }
        showSetStatus(0, debtDetail.getRepayPlan().get(0).getStatus());

        /**
         * 设置备注
         */
        header.remarkContent.setText(TextUtils.isEmpty(debtDetail.getRemark())? "备注" : "备注  "+debtDetail.getRemark());

        /**
         * 设置标题
         */
//        String titleName = debtDetail.getChannelName() + (isEmpty(debtDetail.getProjectName()) ? "" : " - " + debtDetail.getProjectName());
        title.setText("账单详情");

        /**
         * 当前期是否已还状态
         *  0-无效, 1-待还 2-已还
         */
        //先设置底部状态 1 "待还", 2 "已还", 3，"逾期"
        if (debtDetail.detailList.get(0).getStatus() == 1 || debtDetail.detailList.get(0).getStatus() == 3) {
            mFootRoot.setVisibility(View.VISIBLE);
            mFootRootLine.setVisibility(View.VISIBLE);
            footSetMiddleLine.setVisibility(View.VISIBLE);
            footSetPartPay.setVisibility(View.VISIBLE);
            footSetAllPay.setText("设为已还");
            footSetPartPay.setText("还部分");
            money = FormatNumberUtils.FormatNumberFor2(debtDetail.showBill.termPayableAmount);
            footSetAllPay.setEnabled(true);
        } else if (debtDetail.detailList.get(0).getStatus() == 2) {
            mFootRoot.setVisibility(View.VISIBLE);
            mFootRootLine.setVisibility(View.VISIBLE);
            footSetMiddleLine.setVisibility(View.GONE);
            footSetPartPay.setVisibility(View.GONE);
            footSetAllPay.setText("已还");
//            footSetAllPay.setEnabled(false);
            footSetAllPay.setEnabled(true);
        } else {
            mFootRoot.setVisibility(View.GONE);
            mFootRootLine.setVisibility(View.GONE);
        }
        footSetAllPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("已还".equals(footSetAllPay.getText().toString())) {
                    //pv，uv统计
                    DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDALREADYREPAY);
                    //设置为待还
                    showSetAllPayDialog(index, 1);
                } else {
                    //pv，uv统计
                    DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDSETALREADYREPAY);

                    //设置为已还
                    showSetAllPayDialog(index, 2);
                }
            }
        });

        footSetPartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDREPAYPORTION);

                showSetPartPayDialog(debtDetail, index);
            }
        });

        adapter.notifyPayPlanChanged(debtDetail.getRepayPlan(), debtDetail.showBill.termNo);


        //渠道logo
        if (!isEmpty(debtDetail.getLogo())) {
            Glide.with(this)
                    .load(debtDetail.getLogo())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into(header.logo);
        } else {
            header.logo.setImageResource(R.drawable.image_place_holder);
        }
//        //账单状态
//        if (debtDetail.getStatus() != 2) {
//            header.setStatus.setVisibility(View.VISIBLE);
//            header.setStatus.setText(debtDetail.getTermStatus() == 2 ? "设为待还" : "设为已还");
//        } else {
//            header.setStatus.setVisibility(View.GONE);
//        }
        //渠道名
        header.channelName.setText(debtDetail.getChannelName());
//        if (!isEmpty(debtDetail.getProjectName())) {
//            header.projectNameContainer.setVisibility(View.VISIBLE);
//            header.projectName.setText(debtDetail.getProjectName());
//        } else {
//            header.projectNameContainer.setVisibility(View.GONE);
//        }
//        //当期应还金额
//        header.debtTermAmount.setText(keep2digitsWithoutZero(debtDetail.getTermPayableAmount()));
//        //还款时间
//        header.debtPayDay.setText(debtDetail.getTermRepayDate());
//        //未还金额
//        header.debtUnpaid.setText(keep2digitsWithoutZero(debtDetail.getStayReturnedAmount()));
        //还款总额
//        header.debtAmount.setText(keep2digitsWithoutZero(debtDetail.getPayableAmount()) + "元");
        //借款本金
        //本金不大于0则认为本金没有设置
//        if (debtDetail.getCapital() > 0) {
//            header.capital.setText(keep2digitsWithoutZero(debtDetail.getCapital()) + "元");
//        } else {
//            header.capital.setText("--");
//        }
//        //网贷平台
//        header.debtPlatform.setText(debtDetail.getChannelName());
//        //借款利率
//        //利率不大于0则认为利率没有设置
//        if (debtDetail.getRate() > 0) {
//            header.interestRate.setText(convertInterestRate(debtDetail.getRate()) + "%" + (debtDetail.getTermType() == 1 ? "日息" : "月息"));
//        } else {
//            header.interestRate.setText("--");
//        }
//        //备注
//        if (!isEmpty(debtDetail.getRemark())) {
//            header.remark.setText(debtDetail.getRemark());
//        }
//        //还款方式
//        header.payMethodContent.setText(PAY_METHOD[debtDetail.getRepayType() - 1]);
//        //借款利息
//        //利息不大于0则认为利息没有设置
//        if (debtDetail.getInterest() > 0) {
//            header.interest.setText(keep2digitsWithoutZero(debtDetail.getInterest()) + "元");
//        } else {
//            header.interest.setText("--");
//        }
//        //借款期限
//        //期限不大于0则认为期限没有设置
//        if (debtDetail.getTerm() > 0) {
//            header.debtLife.setText(debtDetail.getTerm() + (debtDetail.getTermType() == 1 ? "日" : "月"));
//        } else {
//            header.debtLife.setText("--");
//        }
//        //起息日期
//        if (!isEmpty(debtDetail.getStartDate())) {
//            header.debtStartDate.setText(debtDetail.getStartDate());
//        } else {
//            header.debtStartDate.setText("--");
//        }


    }



    public int index;

    /**
     * 设置已还的对话框
     * @param index     选中的位置，-1则为当前期
     * @param newStatus 新的还款状态
     */
    @Override
    public void showSetStatus(final int index, int newStatus) {
        this.index = index;

        //先设置底部状态 1 "待还", 2 "已还",
        if (newStatus == 1) {
            footSetMiddleLine.setVisibility(View.VISIBLE);
            footSetPartPay.setVisibility(View.VISIBLE);
            footSetAllPay.setText("设为已还");
            footSetPartPay.setText("还部分");
            footSetAllPay.setEnabled(true);
        }
        if (newStatus == 2) {
            footSetMiddleLine.setVisibility(View.GONE);
            footSetPartPay.setVisibility(View.GONE);
            footSetAllPay.setText("已还");
            footSetAllPay.setEnabled(true);
        }

        if (newStatus == 3) {
            footSetMiddleLine.setVisibility(View.VISIBLE);
            footSetPartPay.setVisibility(View.VISIBLE);
            footSetAllPay.setText("设为已还");
            footSetPartPay.setText("还部分");
            footSetAllPay.setEnabled(true);
        }
    }

    /**
     * 还部分
     * @param index
     */
    private void showSetPartPayDialog(final DebtDetail debtDetail, final int index) {
        /**
         * 埋点 	详情页还部分点击
         */
        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_DETAIL_PART_PAY);


        BillEditAmountDialog dialog = new BillEditAmountDialog()
                .attachConfirmListener(new BillEditAmountDialog.EditAmountConfirmListener() {
                    @Override
                    public void onEditAmountConfirm(double amount) {
                        Double copyTermPayableAmount = index == -1 ? debtDetail.showBill.termPayableAmount : debtDetail.getRepayPlan().get(index).getTermPayableAmount();
                        if (amount > copyTermPayableAmount) {
                            Toast.makeText(LoanDebtDetailActivity.this, "还款金额不能大于待还金额", Toast.LENGTH_SHORT).show();
                        } else {
                            Api.getInstance().updateDebtStatus(UserHelper.getInstance(LoanDebtDetailActivity.this).getProfile().getId(), debtDetail.getRepayPlan().get(index).getId(), amount, 2)
                                    .compose(RxUtil.<ResultEntity>io2main())
                                    .subscribe(new Consumer<ResultEntity>() {
                                                   @Override
                                                   public void accept(ResultEntity result) throws Exception {
                                                       if (result.isSuccess()) {
//                                                           Toast.makeText(LoanDebtDetailActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                                            /*
                                                      * 刷新数据
                                                      */
                                                           presenter.loadDebtDetail(billId);

                                                           EventBus.getDefault().postSticky(new MyLoanDebtListFragmentEvent(1));
                                                       } else {
                                                           Toast.makeText(LoanDebtDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               },
                                            new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    //Log.e("exception_custom", throwable.getMessage());
                                                }
                                            });
                        }
                    }
                });
        dialog.show(LoanDebtDetailActivity.this.getSupportFragmentManager(), "paypart");
    }

    /**
     * 设为已还
     * @param pos
     */
    private void showSetAllPayDialog(final int pos, final int status) {
        final Dialog dialog = new Dialog(LoanDebtDetailActivity.this, 0);
        View dialogView = LayoutInflater.from(LoanDebtDetailActivity.this).inflate(R.layout.dialog_debt_detail_set_status, null);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == R.id.confirm) {
                    //pv，uv统计
                    //DataStatisticsHelper.getInstance().onCountUv(status == 2 ? DataStatisticsHelper.ID_SET_STATUS_PAID : DataStatisticsHelper.ID_SET_STATUS_UNPAID);
                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_SET_STATUS_PAID);
                    presenter.updateDebtStatus(pos, status);
                }
            }
        };
        dialogView.findViewById(R.id.confirm).setOnClickListener(clickListener);
        dialogView.findViewById(R.id.cancel).setOnClickListener(clickListener);
        /**
         * 1 设置为待还
         * 2 设置为已还
         */
        /**
         * 1 设置为待还
         * 2 设置为已还
         */
        if (status == 1) {
            ((TextView) dialogView.findViewById(R.id.title)).setText("设为未还");
            ((TextView) dialogView.findViewById(R.id.content)).setText("确定本期账单设为未还？");
        } else {
            ((TextView) dialogView.findViewById(R.id.title)).setText("设为已还");
            ((TextView) dialogView.findViewById(R.id.content)).setText("确定还款"+money+"元");
        }
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

    /**
     * 设置已还回调
     * @param msg 相关消息
     */
    @Override
    public void showUpdateStatusSuccess(String msg) {
        //粗鲁的刷新账单
        presenter.loadDebtDetail(billId);
    }

    /**
     * 标题栏 右上角事件 点击弹窗
     * @param editable 是否可编辑
     * @param remind   是否是提醒状态
     */
    @Override
    public void showMenu(boolean editable, boolean remind) {
        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDTOPRIGHTMORE);

        dialog = new CreditCardDebtDetailDialog();
        dialog.attachEditable(editable)
                .attachListeners(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         dialog.dismiss();
                                         if (v.getId() == R.id.edit) {
                                             //pv，uv统计
//                                             DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDEDIT);
                                             presenter.editDebt();
                                         } else if (v.getId() == R.id.delete) {
                                             new CommNoneAndroidDialog().withMessage("确认删除该提醒吗？")
                                                     .withNegativeBtn("取消", null)
                                                     .withPositiveBtn("确认", new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             //pv，uv统计
//                                                             DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDDELETE);

                                                             presenter.deleteDebt();
                                                         }
                                                     })
                                                     .show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                                         }
                                     }
                                 },
                        new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                //pv，uv统计
//                                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDREPAYREMINDERSWITCH);
                                //更新还款提醒
                                presenter.clickUpdateRemind();
                            }
                        })
                .attachInitStatus(remind).show(getSupportFragmentManager(), "Operation");

    }

    @Override
    public void showUpdateRemind(boolean remind) {
        if (dialog != null) {
            dialog.updateRemind(remind);
        }
    }

    /**
     * 删除该账单
     * @param msg 相关消息
     */
    @Override
    public void showDeleteDebtSuccess(String msg) {
        ToastUtils.showShort(this, "删除成功", null);
        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra("deleteDebtId", debtId);
                setResult(RESULT_OK, intent);
                finish();
            }
        }, 100);

        EventBus.getDefault().postSticky(new MyLoanDebtListFragmentEvent(1));
    }

    /**
     * 跳转到编辑 详情页面
     * @param debtDetail 借款详情
     */
    @Override
    public void navigateAddDebt(DebtDetail debtDetail) {
        //一次性还款付息，等额本息跳转到新版本界面
//        Intent intent = new Intent(this, DebtNewActivity.class);
        Intent intent = new Intent(this, AccountFlowActivity.class);
        intent.putExtra("debt_detail", this.debtDetail);
        intent.putExtra("debt_type", "1");
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT && requestCode == 1 && data != null) {
            debtId = data.getStringExtra("recordId");
            billId = data.getStringExtra("billId");

            presenter.debtId = debtId;
            presenter.loadDebtDetail(billId);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 设置已还 刷新数据
     */
    @Override
    public void updateLoanDetail(String billId) {
        /**
         * 判断是一次性还款还是分期还款
         * termType 1 为一次性还款 2 分期还款
         */
//        if (debtDetail.getRepayType()== 1) {
//            finish();
//        } else {
//            presenter.loadDebtDetail(billId);
//        }
        presenter.loadDebtDetail(billId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
