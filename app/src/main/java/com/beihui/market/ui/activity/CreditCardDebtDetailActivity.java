package com.beihui.market.ui.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.BuildConfig;
import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.NetConstants;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CreditCardDebtBill;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerCreditCardDebtDetailComponent;
import com.beihui.market.injection.module.CreditCardDebtDetailModule;
import com.beihui.market.ui.adapter.CreditCardDebtDetailAdapter;
import com.beihui.market.ui.adapter.multipleentity.CreditCardDebtDetailMultiEntity;
import com.beihui.market.ui.contract.CreditCardDebtDetailContract;
import com.beihui.market.ui.dialog.BillEditAmountDialog;
import com.beihui.market.ui.dialog.CommNoneAndroidDialog;
import com.beihui.market.ui.dialog.CreditCardDebtDetailDialog;
import com.beihui.market.ui.dialog.RemarkDialog;
import com.beihui.market.ui.presenter.CreditCardDebtDetailPresenter;
import com.beihui.market.ui.rvdecoration.CommVerItemDeco;
import com.beihui.market.umeng.NewVersionEvents;
import com.beihui.market.util.DateFormatUtils;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.FormatNumberUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.ToastUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.moxie.client.exception.ExceptionType;
import com.moxie.client.exception.MoxieException;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.moxie.client.model.TitleParams;

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
import io.reactivex.functions.Consumer;

import static android.text.TextUtils.isEmpty;

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

    //账单ID
    private String debtId;
    private MxParam mxParam = null;


    class Header {
        View itemView;

        @BindView(R.id.ll_credit_card_info_header_bg)
        LinearLayout mHeaderCardBg;

        @BindView(R.id.bank_logo)
        ImageView ivBankLogo;
        @BindView(R.id.bank_name)
        TextView tvBankName;
        @BindView(R.id.credit_card_number)
        TextView tvCreditCardNumber;
        /**
         * 几月账单
         */
        @BindView(R.id.debt_date)
        TextView tvDebtDate;
        /**
         * 次月账单金额
         */
        @BindView(R.id.debt_amount)
        TextView tvDebtAmount;
//        //账单状态
//        @BindView(R.id.tv_credit_card_info_header_status)
//        TextView tvStatus;
//        @BindView(R.id.set_status)
//        TextView tvSetStatus;
        //最低应还
        @BindView(R.id.min_payment)
        TextView tvMinPayment;
        @BindView(R.id.min_payment_text)
        TextView tvMinPaymentText;
        //账单日
        @BindView(R.id.debt_bill_day)
        TextView tvDebtBillDay;
        @BindView(R.id.debt_bill_day_text)
        TextView tvDebtBillDayText;
        //还款日
        @BindView(R.id.debt_due_day)
        TextView tvDebtDueDay;
        @BindView(R.id.debt_due_day_text)
        TextView tvDebtDueDayText;
//        @BindView(R.id.max_interest_free_time)
//        TextView tvMaxInterestFreeTime;
//        @BindView(R.id.card_owner)
//        TextView tvCardOwner;
//        @BindView(R.id.credit_amount)
//        TextView tvCreditAmount;

        //备注
        @BindView(R.id.tv_credit_card_remark)
        TextView tvRemark;
        @BindView(R.id.tv_credit_card_remark_button)
        ImageView ivRemarkButton;

        Header(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, this.itemView);
        }
    }

    /**
     * 记录show的billID
     */
    public String setShowbillMonth;


    public int position = 0;

    @Inject
    CreditCardDebtDetailPresenter presenter;

    //头布局
    private Header header;
    //列表适配器
    private CreditCardDebtDetailAdapter detailAdapter;
    //详情bean 除去列表数据
    private CreditCardDebtDetail debtDetail;

    public CreditCardDebtBill mSelectBill = null;

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

    public static void putExtra(Intent intent, String debtId, String billId, boolean byHand, String banKName, String cardNum, String logo) {
        intent.putExtra("debt_id", debtId);
        //过时了
        intent.putExtra("billId", billId);
        //@version 3.1.0
        intent.putExtra("bill_id", billId);
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
        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCD);

        byHand = getIntent().getBooleanExtra("by_hand", false);
        String backName = getIntent().getStringExtra("bank_name");
        String cardNum = getIntent().getStringExtra("card_num");
        String logoUrl = getIntent().getStringExtra("logo");

        //沉浸式
        setupToolbarBackNavigation(toolbar, R.drawable.x_normal_back);
        ImmersionBar.with(this).titleBar(toolbar).statusBarDarkFont(true).init();

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
                //pv，uv统计
//                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCDLISTCLICK);
                CreditCardDebtDetailActivity.this.position = position;
                switch (view.getId()) {
                    case R.id.month_bill_container: {

                        //Log.e("position", "position--> " + position);
                        if (position < ((CreditCardDebtDetailPresenter)presenter).billList.size()) {
                            ((CreditCardDebtDetailPresenter)presenter).bill = ((CreditCardDebtDetailPresenter)presenter).billList.get(position);
                            showStatus(((CreditCardDebtDetailPresenter) presenter).billList.get(position).getStatus());
                            detailAdapter.setShowbillMonth(((CreditCardDebtDetailPresenter) presenter).billList.get(position).getBillMonth());
                            detailAdapter.notifyDataSetChanged();
                        }

                        if (((CreditCardDebtDetailPresenter)presenter).billList.size() > 0) {
                            mSelectBill = ((CreditCardDebtDetailPresenter) presenter).billList.get(position);
                        }

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

//        tvBankNameNum.setText(backName + " " + cardNum);
        header.tvBankName.setText(backName);
        header.tvCreditCardNumber.setText(cardNum);
        if (!isEmpty(logoUrl)) {
            Glide.with(this)
                    .load(logoUrl)
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .transform(new GlideCircleTransform(this))
                    .into(header.ivBankLogo);
        } else {
            header.ivBankLogo.setImageResource(R.drawable.image_place_holder);
        }

        if (byHand) {
            tvDebtStatusOperation.setText("开始同步");
        } else {
//            tvDebtStatusOperation.setText("更新账单");
            tvDebtStatusOperation.setText("开始同步");
            tvDebtStatusTime.setVisibility(View.VISIBLE);
        }

        flDebtStatusOperationBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCDSTARTSYNC);

                /**
                 * @version 3.10
                 * @desc 使用魔蝎信用
                 */
//                startActivity(new Intent(CreditCardDebtDetailActivity.this, EBankActivity.class));
                /**
                 * 防止重复点击
                 */
                if (FastClickUtils.isFastClick()) {
                    return;
                }
                loginMoxie(debtDetail.moxieCode);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //用来清理数据或解除引用
        MoxieSDK.getInstance().clear();
    }


    @Override
    public void initDatas() {
        debtId = getIntent().getStringExtra("debt_id");
        presenter.isShow = true;
        presenter.fetchDebtDetail(getIntent().getStringExtra("bill_id"));
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

    /**
     * 头布局信息
     * @param debtDetail 账单详情
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void showDebtDetailInfo(final CreditCardDebtDetail debtDetail) {
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
//            if (Integer.parseInt(debtDetail.showBill.getReturnDay()) > 3 || debtDetail.showBill.getStatus() == 2) {
//                header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_black_bg));
//                header.tvStatus.setBackgroundColor(Color.parseColor("#4e4e5d"));
//            } else {
//                header.mHeaderCardBg.setBackground(getResources().getDrawable(R.drawable.xshape_tab_account_card_red_bg));
//                header.tvStatus.setBackgroundColor(Color.parseColor("#ff6757"));
//            }

            //字体颜色
//            header.tvDebtDate.setTextColor(Color.parseColor("#aaffffff"));
//            header.tvDebtAmount.setTextColor(Color.parseColor("#ffffff"));
//            header.tvStatus.setTextColor(Color.parseColor("#aaffffff"));
//            header.tvMinPayment.setTextColor(Color.parseColor("#aaffffff"));
//            header.tvMinPaymentText.setTextColor(Color.parseColor("#aaffffff"));
//
//            header.tvDebtBillDay.setTextColor(Color.parseColor("#88ffffff"));
//            header.tvDebtBillDayText.setTextColor(Color.parseColor("#aaffffff"));
//
//            header.tvDebtDueDay.setTextColor(Color.parseColor("#88ffffff"));
//            header.tvDebtDueDayText.setTextColor(Color.parseColor("#aaffffff"));

            /**
             * 备注内容
             */
            if (!TextUtils.isEmpty(debtDetail.remark)) {
                header.tvRemark.setText(TextUtils.isEmpty(debtDetail.remark)? "备注" : debtDetail.remark);
            }

            if (showBill != null) {
                //账单月份
                if (!isEmpty(showBill.getBillDate())) {
                    try {
                        Date date = dateFormat.parse(showBill.getBillDate());
                        calendar.setTime(date);
                        header.tvDebtDate.setText((calendar.get(Calendar.MONTH) + 1) + "月账单（元）");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                //账单金额
                header.tvDebtAmount.setText( FormatNumberUtils.FormatNumberFor2(debtDetail.getShowBill().getNewBalance()));
                /**
                 * 账单状态
                 */
//                tvFootSetStatus.setVisibility(View.VISIBLE);
//                showStatus(showBill.getStatus());
                /**
                * 设置已还
                */
                tvFootSetStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("已还".equals(tvFootSetStatus.getText().toString())) {
                            //pv，uv统计
                            DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCDALREADYREPAY);

                            if (mSelectBill.lastBill) {
                                new CommNoneAndroidDialog()
                                        .withTitle("设为未还")
                                        .withMessage("确定本期账单设为未还？", Color.parseColor("#909298"))
                                        .withNegativeBtn("取消", null)
                                        .withPositiveBtn("确认", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                presenter.clickSetStatus(1);
                                            }
                                        }).show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                            } else {
                                //ToastUtils.showToast(CreditCardDebtDetailActivity.this, "本期账单已还清");
                                ToastUtil.toast("本期账单已还清");
                            }



                        } else {
                            //pv，uv统计
                            DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCDSETALREADYREPAY);

                            new CommNoneAndroidDialog()
                                    .withMessage("确认设为已还？")
                                    .withNegativeBtn("取消", null)
                                    .withPositiveBtn("确认", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            presenter.clickSetStatus(2);

                                        }
                                    })
                                    .show(getSupportFragmentManager(), CommNoneAndroidDialog.class.getSimpleName());
                        }
                    }
                });

                //最低应还
                if (byHand) {
                    //手动账单没有最低应还
                    header.tvMinPayment.setText("----");
                } else {
                    header.tvMinPayment.setText(FormatNumberUtils.FormatNumberFor2(showBill.getMinPayment()) + "元");
                }
                //出账日
                if (!isEmpty(showBill.getBillDate())) {
                    header.tvDebtBillDay.setText(showBill.getBillDate().substring(0, 10).replace("-", "."));
                }
                //还款日
                if (!isEmpty(showBill.getPaymentDueDate())) {
                    header.tvDebtDueDay.setText(showBill.getPaymentDueDate().substring(0, 10).replace("-", "."));
                }

                /**
                 * 修改备注
                 */
                header.ivRemarkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new RemarkDialog().setNickNameChangedListener(new RemarkDialog.NickNameChangedListener() {
                            @Override
                            public void onNickNameChanged(final String remark) {
                                //pv，uv统计
//                                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCDREMARK);

                                if (TextUtils.isEmpty(remark) || remark.length() > 50) {
                                    Toast.makeText(CreditCardDebtDetailActivity.this, "备注不能为空或者字数过多", Toast.LENGTH_SHORT).show();
                                } else {
                                    Api.getInstance().updateCreditCardBillRemark(UserHelper.getInstance(CreditCardDebtDetailActivity.this).getProfile().getId(), debtId, remark)
                                            .compose(RxUtil.<ResultEntity>io2main())
                                            .subscribe(new Consumer<ResultEntity>() {
                                                           @Override
                                                           public void accept(ResultEntity result) throws Exception {
                                                               if (result.isSuccess()) {
                                                                   header.tvRemark.setText(remark);
                                                               } else {
                                                                   Toast.makeText(CreditCardDebtDetailActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
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
            }
        }
    }

    /**
     * 显示状态
     */
    @Override
    public void showStatus(int status) {
        switch (status) {
            case 1://待还
                tvFootSetStatus.setVisibility(View.VISIBLE);
                tvFootMiddleLine.setVisibility(View.VISIBLE);
//                header.tvStatus.setVisibility(View.VISIBLE);
                tvFootSetStatus.setEnabled(true);
                tvFootSetStatus.setText("设为已还");
//                header.tvStatus.setText("待还款");
                break;
            case 2://已还
                tvFootSetStatus.setVisibility(View.VISIBLE);
                tvFootSetStatus.setText("已还");
                tvFootSetStatus.setEnabled(true);
                tvFootMiddleLine.setVisibility(View.VISIBLE);
//                header.tvStatus.setVisibility(View.VISIBLE);
//                header.tvStatus.setText("已还款");
                break;
            case 3://逾期
                tvFootSetStatus.setVisibility(View.VISIBLE);
                tvFootSetStatus.setText("设为已还");
                tvFootSetStatus.setEnabled(true);
                tvFootMiddleLine.setVisibility(View.VISIBLE);
//                header.tvStatus.setVisibility(View.VISIBLE);
//                header.tvStatus.setText("已逾期");
                break;
            case 4://已出账
                tvFootSetStatus.setVisibility(View.GONE);
                tvFootMiddleLine.setVisibility(View.GONE);
//                header.tvStatus.setVisibility(View.VISIBLE);
//                header.tvStatus.setText("已出账");
                break;
            case 5://未出账
                tvFootSetStatus.setVisibility(View.GONE);
                tvFootMiddleLine.setVisibility(View.GONE);
//                header.tvStatus.setVisibility(View.VISIBLE);
//                header.tvStatus.setText("未出账");
                break;
            default:
                tvFootSetStatus.setVisibility(View.GONE);
                tvFootMiddleLine.setVisibility(View.GONE);
//                header.tvStatus.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 列表数据
     * @param list        月份账单列表
     * @param canLoadMore 是否还能加载更多
     */
    @Override
    public void showDebtBillList(List<CreditCardDebtBill> list, boolean canLoadMore) {
        if (detailAdapter.isLoading()) {
            detailAdapter.loadMoreComplete();
        }
        if (list.size() > 0) {
            setShowbillMonth = list.get(0).getBillMonth();
            mSelectBill = list.get(0);
        }
        detailAdapter.setShowbillMonth(setShowbillMonth);
        detailAdapter.setEnableLoadMore(canLoadMore);
        detailAdapter.notifyDebtListChanged(list);

        showStatus(list.get(0).getStatus());
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
        //pv，uv统计
//        DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCDTOPRIGHTMORE);

        dialog = new CreditCardDebtDetailDialog();
        //dialog.attachEditable(byHand)
        /**
         * 将编辑隐藏
         */
        dialog.attachEditable(false)
                .attachListeners(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         dialog.dismiss();
                                         if (v.getId() == R.id.edit) {
                                             if (debtDetail != null) {
                                                 if (FastClickUtils.isFastClick()) {
                                                     return;
                                                 }
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
                                                             //pv，uv统计
//                                                             DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCDDELETE);

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
                                //pv，uv统计
//                                DataStatisticsHelper.getInstance().onCountUv(NewVersionEvents.CCDREPAYREMINDERSWITCH);

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


    /**
     * TODO
     * @version 3.1.0
     * @author xhb
     */
    private void loginMoxie(String bankTag) {
        mxParam = new MxParam();
        mxParam.setUserId(UserHelper.getInstance(this).getProfile().getId());
        mxParam.setApiKey(BuildConfig.MOXIE_APP_KEY);
        mxParam.setQuitDisable(false);

        mxParam.setQuitLoginDone(MxParam.PARAM_COMMON_YES);
        //设置协议地址
        mxParam.setAgreementUrl(NetConstants.H5_USER_MOXIE_PROTOCOL);
        mxParam.setFunction(MxParam.PARAM_FUNCTION_ONLINEBANK);
        mxParam.setItemType(MxParam.PARAM_ITEM_TYPE_CREDITCARD);  //信用卡

        //设置协议地址
        mxParam.setAgreementUrl(NetConstants.H5_USER_MOXIE_PROTOCOL);

        //自定义Title, 还有更多方法请用IDE查看
        TitleParams titleParams = new TitleParams.Builder()
                //设置返回键的icon，不设置此方法会默认使用魔蝎的icon
                .leftNormalImgResId(R.mipmap.btn_back_normal_black)
                //用于设置selector，表示按下的效果，不设置默认使leftNormalImgResId()设置的图片
                .leftPressedImgResId(R.mipmap.btn_back_normal_black)
                //标题字体颜色
                .titleColor(getResources().getColor(R.color.black_1))
                //title背景色
                .backgroundColor(getResources().getColor(R.color.white))
                //设置右边icon
                .rightNormalImgResId(R.drawable.moxie_client_banner_refresh_black)
                //是否支持沉浸式
                .immersedEnable(true)
                .build();
        mxParam.setTitleParams(titleParams);

        mxParam.setItemCode(bankTag);
        MoxieSDK.getInstance().start(CreditCardDebtDetailActivity.this, mxParam, new MoxieCallBack() {
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {

                //Log.e("customMoxie", Thread.currentThread().getName());
                //Log.e("customMoxie", "魔蝎回调 成功");
                if (moxieCallBackData != null) {
                    //Log.e("customMoxie", "MoxieSDK Callback Data : "+ moxieCallBackData.toString());
                    //Log.e("customMoxie", "MoxieSDK Callback Code : "+ moxieCallBackData.toString());
                    switch (moxieCallBackData.getCode()) {
                        /**
                         * 账单导入中
                         *
                         * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
                         * 魔蝎后台会向贵方后台推送Task通知和Bill通知
                         * Task通知：登录成功/登录失败
                         * Bill通知：账单通知
                         */
                        case MxParam.ResultCode.IMPORTING:
                            if(moxieCallBackData.isLoginDone()) {
                                //状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
                                //Log.d("customMoxie", "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            } else {
                                //状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
                                //Log.d("customMoxie", "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            }
                            break;
                        /**
                         * 任务还未开始
                         *
                         * 假如有弹框需求，可以参考 {@link BigdataFragment#showDialog(MoxieContext)}
                         *
                         * example:
                         *  case MxParam.ResultCode.IMPORT_UNSTART:
                         *      showDialog(moxieContext);
                         *      return true;
                         * */
                        case MxParam.ResultCode.IMPORT_UNSTART:
                            //Log.e("customMoxie", "任务未开始");
                            break;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
//                            Toast.makeText(getContext(), "导入失败(平台方服务问题)", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
//                            Toast.makeText(getContext(), "导入失败(魔蝎数据服务异常)", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.USER_INPUT_ERROR:
//                            Toast.makeText(getContext(), "导入失败(" + moxieCallBackData.getMessage() + ")", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.IMPORT_FAIL:
//                            Toast.makeText(getContext(), "导入失败", Toast.LENGTH_SHORT).show();
                            break;
                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            //Log.e("customMoxie", "任务采集成功，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            //根据taskType进行对应的处理
                            switch (moxieCallBackData.getTaskType()) {
                                case MxParam.PARAM_FUNCTION_EMAIL:
//                                    Toast.makeText(getContext(), "邮箱导入成功", Toast.LENGTH_SHORT).show();
                                    break;
                                case MxParam.PARAM_FUNCTION_ONLINEBANK:
//                                    Toast.makeText(getContext(), "网银导入成功", Toast.LENGTH_SHORT).show();
                                    break;
                                //.....
                                default:
//                                    Toast.makeText(getContext(), "导入成功", Toast.LENGTH_SHORT).show();
                            }
                            moxieContext.finish();
                            return true;
                    }
                }
                return false;
            }

            @Override
            public void onError(MoxieContext moxieContext, MoxieException moxieException) {
                super.onError(moxieContext, moxieException);
                //Log.e("customMoxie","魔蝎失败" + moxieException.getMessage());
                if(moxieException.getExceptionType() == ExceptionType.SDK_HAS_STARTED){
                    Toast.makeText(CreditCardDebtDetailActivity.this, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                } else if(moxieException.getExceptionType() == ExceptionType.SDK_LACK_PARAMETERS){
                    Toast.makeText(CreditCardDebtDetailActivity.this, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
