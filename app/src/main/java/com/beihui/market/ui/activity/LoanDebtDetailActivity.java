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
import com.beihui.market.entity.DebtDetail;
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
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

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
//        @BindView(R.id.logo)
//        CircleImageView logo;
//        @BindView(R.id.channel_name)
//        TextView channelName;
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
     * 初始化 埋点
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //pv，uv统计
        DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_DEBT_DETAIL);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_detail;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
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
                        if (TextUtils.isEmpty(remark) || remark.length() > 50) {
                            Toast.makeText(LoanDebtDetailActivity.this, "备注不能为空或者字数过多", Toast.LENGTH_SHORT).show();
                        } else {
                            Api.getInstance().updateLoanOrCreditCardRemark(UserHelper.getInstance(LoanDebtDetailActivity.this).getProfile().getId(), remark, debtId, 1)
                                    .compose(RxUtil.<ResultEntity>io2main())
                                    .subscribe(new Consumer<ResultEntity>() {
                                                   @Override
                                                   public void accept(ResultEntity result) throws Exception {
                                                       if (result.isSuccess()) {
                                                           header.remarkContent.setText(remark);
                                                       } else {
                                                           Toast.makeText(LoanDebtDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               },
                                            new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    Log.e("exception_custom", throwable.getMessage());
                                                }
                                            });;
                        }
                    }
                }).show(getSupportFragmentManager(), "remark");
            }
        });

        adapter = new DebtDetailRVAdapter();
        adapter.setHeaderView(header.itemView);
        /**
         * Item的点击事件
         */
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                ((TextView)view.findViewById(R.id.th)).setTextColor(Color.RED);
                LoanDebtDetailActivity.this.adapter.setThTextColor(position);
                presenter.clickSetStatus(position);
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
        this.debtDetail = debtDetail;
        /**
         * 头卡片 背景颜色
         */
        //字体颜色
        header.debtTermAmount.setTextColor(Color.parseColor("#ffffff"));
        header.debtPayDay.setTextColor(Color.parseColor("#aaffffff"));
        header.debtUnpaid.setTextColor(Color.parseColor("#aaffffff"));
        header.debtTermAmountText.setTextColor(Color.parseColor("#88ffffff"));
        header.debtPayDayUpText.setTextColor(Color.parseColor("#88ffffff"));
        header.debtUnpaidText.setTextColor(Color.parseColor("#88ffffff"));

        if (debtDetail.showBill.returnDay <= 3) {
            header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_red_bg));
        } else {
            header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_black_bg));
        }

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
        header.debtTermAmount.setText(keep2digitsWithoutZero(debtDetail.getStayReturnedAmount()));
        /**
         * 判断是一次性还款还是分期还款
         * termType 1 为一次性还款 2 分期还款
         */
        if (debtDetail.getRepayType()== 1) {
            //还款期数 当前期/总期数
            header.debtPayDay.setText("一次性还款");
        } else {
            //还款期数 当前期/总期数
            header.debtPayDay.setText(debtDetail.returnedTerm + "/" + debtDetail.getTerm());
        }

        //当期还款日
        header.debtUnpaid.setText(debtDetail.showBill.termRepayDate.replace("-","."));

        /**
         * 设置当前期号 index
         */
        showSetStatus(debtDetail.showBill.termNo <= 0 ? 0 : debtDetail.showBill.termNo - 1, debtDetail.getRepayPlan().get(debtDetail.showBill.termNo <= 0 ? 0 : debtDetail.showBill.termNo - 1).getStatus());

        /**
         * 设置备注
         */
        header.remarkContent.setText(TextUtils.isEmpty(debtDetail.getRemark())? "备注" : debtDetail.getRemark());

        /**
         * 设置标题
         */
        String titleName = debtDetail.getChannelName() + (isEmpty(debtDetail.getProjectName()) ? "" : " - " + debtDetail.getProjectName());
        title.setText(titleName);

        /**
         * 当前期是否已还状态
         *  0-无效, 1-待还 2-已还
         */
        //先设置底部状态 1 "待还", 2 "已还", 3，"逾期"
        if (debtDetail.showBill.status == 1 || debtDetail.showBill.status == 3) {
            footSetMiddleLine.setVisibility(View.VISIBLE);
            footSetPartPay.setVisibility(View.VISIBLE);
            footSetAllPay.setText("设为已还");
            footSetPartPay.setText("还部分");
            footSetAllPay.setEnabled(true);
        } else if (debtDetail.showBill.status == 2) {
            footSetMiddleLine.setVisibility(View.GONE);
            footSetPartPay.setVisibility(View.GONE);
            footSetAllPay.setText("已还");
            footSetAllPay.setEnabled(false);
        } else {
            mFootRoot.setVisibility(View.GONE);
        }
        footSetAllPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetAllPayDialog(index);
            }
        });

        footSetPartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetPartPayDialog(debtDetail, index);
            }
        });

        adapter.notifyPayPlanChanged(debtDetail.getRepayPlan(), debtDetail.showBill.termNo);


        //渠道logo
//        if (!isEmpty(debtDetail.getLogo())) {
//            Glide.with(this)
//                    .load(debtDetail.getLogo())
//                    .asBitmap()
//                    .centerCrop()
//                    .placeholder(R.drawable.image_place_holder)
//                    .into(header.logo);
//        } else {
//            header.logo.setImageResource(R.drawable.image_place_holder);
//        }
//        //账单状态
//        if (debtDetail.getStatus() != 2) {
//            header.setStatus.setVisibility(View.VISIBLE);
//            header.setStatus.setText(debtDetail.getTermStatus() == 2 ? "设为待还" : "设为已还");
//        } else {
//            header.setStatus.setVisibility(View.GONE);
//        }
//        //渠道名
//        header.channelName.setText(debtDetail.getChannelName());
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
            footSetAllPay.setEnabled(false);
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
                        if (amount > debtDetail.showBill.termPayableAmount) {
                            Toast.makeText(LoanDebtDetailActivity.this, "只能还部分", Toast.LENGTH_SHORT).show();
                        } else {
                            Api.getInstance().updateDebtStatus(UserHelper.getInstance(LoanDebtDetailActivity.this).getProfile().getId(), debtDetail.getRepayPlan().get(index).getId(), amount, 2)
                                    .compose(RxUtil.<ResultEntity>io2main())
                                    .subscribe(new Consumer<ResultEntity>() {
                                                   @Override
                                                   public void accept(ResultEntity result) throws Exception {
                                                       if (result.isSuccess()) {
                                                           Toast.makeText(LoanDebtDetailActivity.this, "更新成功", Toast.LENGTH_SHORT).show();
                                                            /*
                                                      * 刷新数据
                                                      * xhb
                                                      */
                                                           presenter.loadDebtDetail(billId);
                                                       } else {
                                                           Toast.makeText(LoanDebtDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               },
                                            new Consumer<Throwable>() {
                                                @Override
                                                public void accept(Throwable throwable) throws Exception {
                                                    Log.e("exception_custom", throwable.getMessage());
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
    private void showSetAllPayDialog(final int pos) {
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
                    presenter.updateDebtStatus(pos, 2);
                }
            }
        };
        dialogView.findViewById(R.id.confirm).setOnClickListener(clickListener);
        dialogView.findViewById(R.id.cancel).setOnClickListener(clickListener);
        ((TextView) dialogView.findViewById(R.id.title)).setText("修改分期状态为已还");
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
        dialog = new CreditCardDebtDetailDialog();
        dialog.attachEditable(editable)
                .attachListeners(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         dialog.dismiss();
                                         if (v.getId() == R.id.edit) {
                                             presenter.editDebt();
                                         } else if (v.getId() == R.id.delete) {
                                             new CommNoneAndroidDialog().withMessage("确认删除该提醒吗？")
                                                     .withNegativeBtn("取消", null)
                                                     .withPositiveBtn("确认", new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
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
    }

    /**
     * 跳转到编辑 详情页面
     * @param debtDetail 借款详情
     */
    @Override
    public void navigateAddDebt(DebtDetail debtDetail) {
        //一次性还款付息，等额本息跳转到新版本界面
        Intent intent = new Intent(this, DebtNewActivity.class);
        intent.putExtra("debt_detail", debtDetail);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
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
        if (debtDetail.getRepayType()== 1) {
            finish();
        } else {
            presenter.loadDebtDetail(billId);
        }
    }
}
