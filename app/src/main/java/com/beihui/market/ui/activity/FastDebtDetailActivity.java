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
import com.beihui.market.entity.DebeDetailRecord;
import com.beihui.market.entity.DebtDetail;
import com.beihui.market.entity.FastDebtDetail;
import com.beihui.market.event.MyLoanDebtListFragmentEvent;
import com.beihui.market.helper.DataStatisticsHelper;
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
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FormatNumberUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.SPUtils;
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
import static com.beihui.market.util.CommonUtils.getChaneseNum;
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

    class Header {
        View itemView;
        @BindView(R.id.logo)
        CircleImageView logo;
        @BindView(R.id.channel_name)
        TextView channelName;
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

        //还款周期
        @BindView(R.id.debt_detail_pay_term)
        TextView debtPayTerm;

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

    /**
     * 还款金额
     */
    public String money;

    @Override
    public int getLayoutId() {
        return R.layout.activity_debt_detail;
    }

    /**
     * 网络请求 单个账单的请求
     */
    @Override
    public void initDatas() {
        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTD);

        Intent intent = getIntent();
        debtId = intent.getStringExtra("debt_id");
        billId = intent.getStringExtra("bill_id");

        loadDebtDetail();
    }

    /**
     * 获取账单详情
     */
    private void loadDebtDetail() {
        Api.getInstance().queryFastDebtBillDetail(UserHelper.getInstance(this).getProfile().getId(), billId, debtId)
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
        setupToolbarBackNavigation(toolbar, R.drawable.x_normal_back);
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

                        //pv，uv统计
//                        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDREMARK);

                        Api.getInstance().updateFastDebtBillRemark(UserHelper.getInstance(FastDebtDetailActivity.this).getProfile().getId(), debtId, remark)
                                .compose(RxUtil.<ResultEntity>io2main())
                                .subscribe(new Consumer<ResultEntity>() {
                                               @Override
                                               public void accept(ResultEntity result) throws Exception {
                                                   if (result.isSuccess()) {
                                                       header.remarkContent.setText("备注  "+remark);
                                                       fastDebtDetail.remark = remark;
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
        adapter = new FastDebtDetailRVAdapter(this);
        //添加头布局
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
                    FastDebtDetailActivity.this.adapter.setThTextColor(fastDebtDetail.getDetailList().get(position).termRepayDate);
                    showSetStatus(position, fastDebtDetail.getDetailList().get(position).getStatus());

                    money = FormatNumberUtils.FormatNumberFor2(fastDebtDetail.getDetailList().get(position).getTermPayableAmount());

                    /**
                     * 请求还款记录
                     */
                    Api.getInstance().getFastDetailRecord(UserHelper.getInstance(FastDebtDetailActivity.this).getProfile().getId(), fastDebtDetail.getDetailList().get(index).getId())
                            .compose(RxUtil.<ResultEntity<List<DebeDetailRecord>>>io2main())
                            .subscribe(new Consumer<ResultEntity<List<DebeDetailRecord>>>() {
                                           @Override
                                           public void accept(ResultEntity<List<DebeDetailRecord>> result) throws Exception {
                                               if (result.isSuccess()) {
                                                   FastDebtDetailActivity.this.adapter.showDebtDetailRecord(position, result.getData());
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
                                    });
                }
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
        if (!fastDebtDetail.getId().equals(SPUtils.getValue(this, fastDebtDetail.getId()))) {
            if (fastDebtDetail.getTerm() > 12) {
                com.beihui.market.util.ToastUtils.showToast(this, "账单分期大于12期，只显示最近6期");
                SPUtils.setValue(this, fastDebtDetail.getId());
            } else if (fastDebtDetail.getTerm() == -1) {
                com.beihui.market.util.ToastUtils.showToast(this, "循环账单只显示最近2期");
                SPUtils.setValue(this, fastDebtDetail.getId());
            }
        }

        this.fastDebtDetail = fastDebtDetail;

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
        header.debtTermAmount.setText(FormatNumberUtils.FormatNumberFor2(fastDebtDetail.payableAmount));
        /**
         * 判断是一次性还款还是分期还款
         * termType 1 为一次性还款 2 分期还款
         */
        if (fastDebtDetail.getRepayType()== 1) {
            //fastDebtDetail 当前期/总期数
            header.debtPayDay.setText("一次性还款");
        } else {
            //还款期数 当前期/总期数
            if (fastDebtDetail.getTerm() == -1) {
                header.debtPayDay.setText(fastDebtDetail.returnedTerm + "/" + "循环");
            } else {
                header.debtPayDay.setText(fastDebtDetail.returnedTerm + "/" + fastDebtDetail.getTerm());
            }
        }

        //还款周期
        if (1 == fastDebtDetail.cycleType || 0 == fastDebtDetail.cycleType) {
            //日 一次性还款
            header.debtPayTerm.setText("每月");
        }
        if (2 == fastDebtDetail.cycleType) {
            //月
            header.debtPayTerm.setText( -1 == fastDebtDetail.cycle? "循环" : CommonUtils.getChaneseNum(fastDebtDetail.cycle));
        }
        if (3 == fastDebtDetail.cycleType) {
            //年
            header.debtPayTerm.setText( -1 == fastDebtDetail.cycle? "循环" : "每年");
        }
        //当期还款日
        header.debtUnpaid.setText(fastDebtDetail.showBill.termRepayDate.replace("-","."));

        /**
         * 设置当前期号 index
         */
//        if (fastDebtDetail.showBill == null || fastDebtDetail.showBill.termNo == null || fastDebtDetail.showBill.termNo > fastDebtDetail.detailList.size()) {
//            showSetStatus(0, 0);
//        } else {
//            showSetStatus(fastDebtDetail.showBill.termNo <= 0 ? 0 : fastDebtDetail.showBill.termNo - 1, fastDebtDetail.getDetailList().get(fastDebtDetail.showBill.termNo <= 0 ? 0 : fastDebtDetail.showBill.termNo - 1).getStatus());
//        }
        showSetStatus(0, fastDebtDetail.getDetailList().get(0).getStatus());
        /**
         * 设置备注
         */
        header.remarkContent.setText(TextUtils.isEmpty(fastDebtDetail.getProjectName())? "备注" : "备注  "+fastDebtDetail.getRemark());

        /**
         * 设置标题
         */
        title.setText("账单详情");

        //渠道logo
        if (!isEmpty(fastDebtDetail.logo)) {
            Glide.with(this)
                    .load(fastDebtDetail.logo)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into(header.logo);
        } else {
            header.logo.setImageResource(R.drawable.image_place_holder);
        }

        //渠道名
        header.channelName.setText(fastDebtDetail.getProjectName());

        /**
         * 当前期是否已还状态
         *  0-无效, 1-待还 2-已还
         */
        //先设置底部状态 1 "待还", 2 "已还", 3，"逾期"
        if (fastDebtDetail.detailList.get(0).getStatus() == 1 || fastDebtDetail.detailList.get(0).getStatus() == 3) {
            mFootRoot.setVisibility(View.VISIBLE);
            mFootRootLine.setVisibility(View.VISIBLE);
            footSetMiddleLine.setVisibility(View.VISIBLE);
            footSetPartPay.setVisibility(View.VISIBLE);
            footSetAllPay.setText("设为已还");
            money = FormatNumberUtils.FormatNumberFor2(fastDebtDetail.showBill.getTermPayableAmount());
            footSetPartPay.setText("还部分");
            footSetAllPay.setEnabled(true);
        } else if (fastDebtDetail.detailList.get(0).getStatus() == 2) {
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

        /**
         * 设置已还
         */
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

        /**
         * 还部分
         */
        footSetPartPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDREPAYPORTION);

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
        if (newStatus == 0) {
            mFootRoot.setVisibility(View.VISIBLE);
            mFootRootLine.setVisibility(View.VISIBLE);
            mFootRoot.setVisibility(View.GONE);
            mFootRootLine.setVisibility(View.GONE);
        }

        if (newStatus == 1) {
            mFootRoot.setVisibility(View.VISIBLE);
            mFootRootLine.setVisibility(View.VISIBLE);
            footSetMiddleLine.setVisibility(View.VISIBLE);
            footSetPartPay.setVisibility(View.VISIBLE);
            footSetAllPay.setText("设为已还");
            footSetPartPay.setText("还部分");
            footSetAllPay.setEnabled(true);
        }
        if (newStatus == 2) {
            mFootRoot.setVisibility(View.VISIBLE);
            mFootRootLine.setVisibility(View.VISIBLE);
            footSetMiddleLine.setVisibility(View.GONE);
            footSetPartPay.setVisibility(View.GONE);
            footSetAllPay.setText("已还");
            footSetAllPay.setEnabled(true);
        }

        if (newStatus == 3) {
            mFootRoot.setVisibility(View.VISIBLE);
            mFootRootLine.setVisibility(View.VISIBLE);
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
                        Double copyTermPayableAmount = index == -1 ? fastDebtDetail.showBill.termPayableAmount : fastDebtDetail.getDetailList().get(pos).getTermPayableAmount();
                        if (amount > copyTermPayableAmount) {
                            Toast.makeText(FastDebtDetailActivity.this, "还款金额不能大于待还金额", Toast.LENGTH_SHORT).show();
                            return;
                        }

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
                                                       EventBus.getDefault().postSticky(new MyLoanDebtListFragmentEvent(0));
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
    private void showSetAllPayDialog(final int pos, final int status) {
        final Dialog dialog = new Dialog(FastDebtDetailActivity.this, 0);
        View dialogView = LayoutInflater.from(FastDebtDetailActivity.this).inflate(R.layout.dialog_debt_detail_set_status, null);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.confirm) {
                    final String billId = index == -1 ? fastDebtDetail.showBill.getId() : fastDebtDetail.getDetailList().get(pos).getId();
                    Api.getInstance().updateFastDebtBillStatus(UserHelper.getInstance(FastDebtDetailActivity.this).getProfile().getId(),  billId, debtId, status, null)
                            .compose(RxUtil.<ResultEntity>io2main())
                            .subscribe(new Consumer<ResultEntity>() {
                                           @Override
                                           public void accept(ResultEntity result) throws Exception {
                                               if (result.isSuccess()) {
                                                   //更新成功后刷新数据
                                                   updateLoanDetail(billId);
                                                   EventBus.getDefault().postSticky(new MyLoanDebtListFragmentEvent(0));
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
     * 标题栏 右上角事件 点击弹窗
     * @param editable 是否可编辑
     * @param remind   是否是提醒状态
     */
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
                                                             //pv，uv统计
//                                                             DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDDELETE);

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
                                //pv，uv统计
//                                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CNTDREPAYREMINDERSWITCH);

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
        final int remind = fastDebtDetail.getRemind() == -1 ? 3 : -1;
        Api.getInstance().updateRemindStatus(UserHelper.getInstance(this).getProfile().getId(), "3", fastDebtDetail.getId(), remind)
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

        EventBus.getDefault().postSticky(new MyLoanDebtListFragmentEvent(0));
    }

    /**
     * 跳转到编辑 详情页面
     */
    public void navigateAddDebt() {
        //一次性还款付息，等额本息跳转到新版本界面
//        Intent intent = new Intent(this, FastAddDebtActivity.class);
//        intent.putExtra("fast_debt_detail", fastDebtDetail);
//        startActivityForResult(intent, REQUEST_CODE_EDIT);
        Intent intent = new Intent(this, AccountFlowActivity.class);
        intent.putExtra("debt_detail", fastDebtDetail);
        intent.putExtra("debt_type", "0");
        startActivityForResult(intent, REQUEST_CODE_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_EDIT && requestCode == 1 && data != null) {
            debtId = data.getStringExtra("recordId");
            billId = data.getStringExtra("billId");
            loadDebtDetail();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
//        if (fastDebtDetail.getRepayType()== 1) {
//            finish();
//        } else {
//            loadDebtDetail();
//        }
        loadDebtDetail();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
