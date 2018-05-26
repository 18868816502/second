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
import com.beihui.market.entity.FastDebtDetail;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerDebtDetailComponent;
import com.beihui.market.injection.module.DebtDetailModule;
import com.beihui.market.ui.adapter.DebtDetailRVAdapter;
import com.beihui.market.ui.adapter.FastDebtDetailRVAdapter;
import com.beihui.market.ui.contract.DebtDetailContract;
import com.beihui.market.ui.dialog.BillEditAmountDialog;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.dialog.CreditCardDebtDetailDialog;
import com.beihui.market.ui.dialog.RemarkDialog;
import com.beihui.market.ui.fragment.TabAccountFragment;
import com.beihui.market.ui.presenter.DebtDetailPresenter;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

import static android.text.TextUtils.isEmpty;
import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

/**
 * @author xhb
 * @version 3.0.1
 * 快速记账详情
 */
public class FastDebtDetailActivity extends BaseComponentActivity {

    private static final int REQUEST_CODE_EDIT = 1;

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

    class Header {
        View itemView;
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

        Header(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }

    //期数列表适配器
    private FastDebtDetailRVAdapter adapter;
    //头布局
    private Header header;

    //账单ID
    private String debtId;
    //分期账单ID
    private String billId;

    //数据源 （包括列表数据 以及头数据ShowBill）
    private FastDebtDetail fastDebtDetail;

    /**
     * 弹框
     */
    private CreditCardDebtDetailDialog dialog;


    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_detail;
    }

    /**
     * 网络请求 单个账单的请求
     */
    @Override
    public void initDatas() {
        Intent intent = getIntent();
        debtId = intent.getStringExtra("debt_id");
        billId = intent.getStringExtra("bill_id");

        loadDebtDetail();
    }

    /**
     * 获取账单详情
     */
    private void loadDebtDetail() {
        Api.getInstance().queryFastDebtBillDetail(UserHelper.getInstance(this).getProfile().getId(),billId, debtId)
                .compose(RxUtil.<ResultEntity<FastDebtDetail>>io2main())
                .subscribe(new Consumer<ResultEntity<FastDebtDetail>>() {
                               @Override
                               public void accept(ResultEntity<FastDebtDetail> resultEntity) throws Exception {
                                    if (resultEntity.isSuccess()) {
                                        showDebtDetail(resultEntity.getData());
                                    } else {
                                        ToastUtils.showShort(FastDebtDetailActivity.this, resultEntity.getMsg(), null);
                                    }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
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
         * 修改备注 其实就是快捷记账的名称
         */
        header.remarkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemarkDialog().setNickNameChangedListener(new RemarkDialog.NickNameChangedListener() {
                    @Override
                    public void onNickNameChanged(final String remark) {
                        Api.getInstance().updateFastDebtName(UserHelper.getInstance(FastDebtDetailActivity.this).getProfile().getId(), debtId, remark)
                                .compose(RxUtil.<ResultEntity>io2main())
                                .subscribe(new Consumer<ResultEntity>() {
                                               @Override
                                               public void accept(ResultEntity result) throws Exception {
                                                   if (result.isSuccess()) {
                                                       header.remarkContent.setText(remark);
                                                       fastDebtDetail.setProjectName(remark);
                                                   } else {
                                                       Toast.makeText(FastDebtDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
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
                }).show(getSupportFragmentManager(), "remark");
            }
        });

        //创建适配器
        adapter = new FastDebtDetailRVAdapter();
        //添加头布局
        adapter.setHeaderView(header.itemView);
        /**
         * Item的点击事件
         */
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((TextView)view.findViewById(R.id.th)).setTextColor(Color.RED);
                FastDebtDetailActivity.this.adapter.setThTextColor(position);
                showSetStatus(position,  fastDebtDetail.getDetailList().get(position).getStatus());
            }
        });
        recyclerView.setAdapter(adapter);

        SlidePanelHelper.attach(this);
    }



    @Override
    protected void configureComponent(AppComponent appComponent) {

    }



    /**
     * 回调 显示单个账单数据
     * @param fastDebtDetail 借款详情
     */
    public void showDebtDetail(final FastDebtDetail fastDebtDetail) {
        this.fastDebtDetail = fastDebtDetail;
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

        if (fastDebtDetail.showBill.returnDay > 3 || fastDebtDetail.showBill.status == 2) {
            header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_black_bg));
        } else {
            header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_red_bg));
        }

        /**
         * 标题栏 右上角菜单栏 点击事件
         */
        ivTitleBarRightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu(true, fastDebtDetail.getRemind() != -1);
            }
        });
        //全部待还金额
        header.debtTermAmount.setText(keep2digitsWithoutZero(fastDebtDetail.payableAmount));
        /**
         * 判断是一次性还款还是分期还款
         * termType 1 为一次性还款 2 分期还款
         */
        if (fastDebtDetail.getRepayType()== 1) {
            //fastDebtDetail 当前期/总期数
            header.debtPayDay.setText("一次性还款");
        } else {
            //还款期数 当前期/总期数
            header.debtPayDay.setText(fastDebtDetail.returnedTerm + "/" + fastDebtDetail.getTerm());
        }

        //当期还款日
        header.debtUnpaid.setText(fastDebtDetail.showBill.termRepayDate.replace("-","."));

        /**
         * 设置当前期号 index
         */
        showSetStatus(fastDebtDetail.showBill.termNo <= 0 ? 0 : fastDebtDetail.showBill.termNo - 1, fastDebtDetail.getDetailList().get(fastDebtDetail.showBill.termNo <= 0 ? 0 : fastDebtDetail.showBill.termNo - 1).getStatus());

        /**
         * 设置备注
         */
        header.remarkContent.setText(TextUtils.isEmpty(fastDebtDetail.getProjectName())? "备注" : fastDebtDetail.getProjectName());

        /**
         * 设置标题
         */
        title.setText("快捷记账");

        /**
         * 当前期是否已还状态
         *  0-无效, 1-待还 2-已还
         */
        //先设置底部状态 1 "待还", 2 "已还", 3，"逾期"
        if (fastDebtDetail.showBill.status == 1 || fastDebtDetail.showBill.status == 3) {
            footSetMiddleLine.setVisibility(View.VISIBLE);
            footSetPartPay.setVisibility(View.VISIBLE);
            footSetAllPay.setText("设为已还");
            footSetPartPay.setText("还部分");
            footSetAllPay.setEnabled(true);
        } else if (fastDebtDetail.showBill.status == 2) {
            footSetMiddleLine.setVisibility(View.GONE);
            footSetPartPay.setVisibility(View.GONE);
            footSetAllPay.setText("已还");
            footSetAllPay.setEnabled(false);
        } else {
            mFootRoot.setVisibility(View.GONE);
        }

        /**
         * 设置已还
         */
        footSetAllPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetAllPayDialog(index);
            }
        });

        /**
         * 还部分
         */
        footSetPartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSetPartPayDialog(index);
            }
        });

        adapter.notifyPayPlanChanged(fastDebtDetail.getDetailList(), fastDebtDetail.showBill.termNo);
    }


    /**
     * 设置已还 或者未还 使用index获取list的分期账单ID
     */
    public int index;

    /**
     * 设置已还的对话框
     * @param index     选中的位置，-1则为当前期
     * @param newStatus 新的还款状态
     */
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
     */
    private void showSetPartPayDialog(final int pos) {
        BillEditAmountDialog dialog = new BillEditAmountDialog()
                .attachConfirmListener(new BillEditAmountDialog.EditAmountConfirmListener() {
                    @Override
                    public void onEditAmountConfirm(final double amount) {
                        final String billId = index == -1 ? fastDebtDetail.showBill.getId() : fastDebtDetail.getDetailList().get(pos).getId();
                        Api.getInstance().updateFastDebtBillStatus(UserHelper.getInstance(FastDebtDetailActivity.this).getProfile().getId(),  billId, debtId, 2,  amount)
                                .compose(RxUtil.<ResultEntity>io2main())
                                .subscribe(new Consumer<ResultEntity>() {
                                               @Override
                                               public void accept(ResultEntity result) throws Exception {
                                                   if (result.isSuccess()) {
                                                       if (amount - fastDebtDetail.getDetailList().get(pos).getTermPayableAmount() < 0.01) {
                                                           loadDebtDetail();
                                                       } else {
                                                            //更新成功后刷新数据
                                                           updateLoanDetail(billId);
                                                       }
                                                   } else {
                                                       showErrorMsg(result.getMsg());
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
                });
        dialog.show(FastDebtDetailActivity.this.getSupportFragmentManager(), "paypart");
    }

    /**
     * 设为已还
     * @param pos
     */
    private void showSetAllPayDialog(final int pos) {
        final Dialog dialog = new Dialog(FastDebtDetailActivity.this, 0);
        View dialogView = LayoutInflater.from(FastDebtDetailActivity.this).inflate(R.layout.dialog_debt_detail_set_status, null);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.confirm) {
                    final String billId = index == -1 ? fastDebtDetail.showBill.getId() : fastDebtDetail.getDetailList().get(pos).getId();
                    Api.getInstance().updateFastDebtBillStatus(UserHelper.getInstance(FastDebtDetailActivity.this).getProfile().getId(),  billId, debtId, 2, null)
                            .compose(RxUtil.<ResultEntity>io2main())
                            .subscribe(new Consumer<ResultEntity>() {
                                           @Override
                                           public void accept(ResultEntity result) throws Exception {
                                               if (result.isSuccess()) {
                                                   //更新成功后刷新数据
                                                   updateLoanDetail(billId);
                                               } else {
                                                   showErrorMsg(result.getMsg());
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
                dialog.dismiss();
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
     * 标题栏 右上角事件 点击弹窗
     * @param editable 是否可编辑
     * @param remind   是否是提醒状态
     */
    public void showMenu(boolean editable, boolean remind) {
        dialog = new CreditCardDebtDetailDialog();
        dialog.attachEditable(editable)
                .attachListeners(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         dialog.dismiss();
                                         if (v.getId() == R.id.edit) {
                                             /**
                                          * 跳转到快捷记账的编辑页面
                                          */
                                             navigateAddDebt();
                                         } else if (v.getId() == R.id.delete) {
                                             new CommNoneAndroidDialog().withMessage("确认删除该提醒吗？")
                                                     .withNegativeBtn("取消", null)
                                                     .withPositiveBtn("确认", new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             /**
                                                        * 删除账单
                                                        */
                                                             deleteDebt();
                                                         }
                                                     })
                                                     .show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                                         }
                                     }
                                 },
                        new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                /**
                              * 更新还款提醒
                              */
                                 clickUpdateRemind();
                            }
                        })
                .attachInitStatus(remind).show(getSupportFragmentManager(), "Operation");

    }

    /**
     * 更新还款提醒
     */
    private void clickUpdateRemind() {
        final int remind = fastDebtDetail.getRemind() == -1 ? 1 : -1;
        Api.getInstance().updateRemindStatus(UserHelper.getInstance(this).getProfile().getId(), fastDebtDetail.getId(), null, remind)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       fastDebtDetail.setRemind(remind);
                                       showUpdateRemind(fastDebtDetail.getRemind() != -1);
                                   } else {
                                       showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {

                            }
                        });
    }

    /**
     * 更新还款提醒
     */
    public void showUpdateRemind(boolean remind) {
        if (dialog != null) {
            dialog.updateRemind(remind);
        }
    }

    /**
     * 删除账单
     */
    private void deleteDebt() {
        Api.getInstance().deleteFastDebt(UserHelper.getInstance(this).getProfile().getId(), debtId)
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result) throws Exception {
                                   if (result.isSuccess()) {
                                       showDeleteDebtSuccess("删除成功");
                                   } else {
                                       showErrorMsg(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {


                            }
                        });

    }

    /**
     * 删除该账单
     * @param msg 相关消息
     */
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
     */
    public void navigateAddDebt() {
        //一次性还款付息，等额本息跳转到新版本界面
        Intent intent = new Intent(this, FastAddDebtActivity.class);
        intent.putExtra("fast_debt_detail", fastDebtDetail);
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    /**
     * 设置已还 刷新数据
     */
    public void updateLoanDetail(String billId) {
        /**
         * 判断是一次性还款还是分期还款
         * termType 1 为一次性还款 2 分期还款
         *
         * 如果是一次性还款则关闭页面 如果是分期付款则刷新数据
         */
        if (fastDebtDetail.getRepayType()== 1) {
            finish();
        } else {
            loadDebtDetail();
        }
    }
}
