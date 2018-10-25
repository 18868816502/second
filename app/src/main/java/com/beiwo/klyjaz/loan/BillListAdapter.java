package com.beiwo.klyjaz.loan;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.entity.Bill;
import com.beiwo.klyjaz.entity.BillState;
import com.beiwo.klyjaz.helper.DataStatisticsHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.tang.MoxieUtil;
import com.beiwo.klyjaz.tang.activity.CommonDetailActivity;
import com.beiwo.klyjaz.tang.activity.CreditDetailActivity;
import com.beiwo.klyjaz.tang.activity.NetLoanDetailActivity;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.util.FormatNumberUtils;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.CustomSwipeMenuLayout;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/15
 */

public class BillListAdapter extends BaseQuickAdapter<Bill, BaseViewHolder> {
    private BillListActivity mActivity;
    private Resources resources;
    private Handler handler = new Handler(Looper.getMainLooper());
    private UserHelper userHelper;
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            mActivity.request();
        }
    };

    public BillListAdapter() {
        super(R.layout.f_layout_home_bill_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Bill item) {
        mActivity = (BillListActivity) helper.itemView.getContext();
        resources = mActivity.getResources();
        userHelper = UserHelper.getInstance(mActivity);
        //icon
        ImageView iv_home_bill_icon = helper.getView(R.id.iv_home_bill_icon);
        Glide.with(mActivity).load(item.getLogoUrl()).error(R.color.white_7).transform(new GlideCircleTransform(mActivity)).into(iv_home_bill_icon);
        //name
        helper.setText(R.id.tv_home_bill_name, item.getTitle());
        //tag + 同步按钮
        View tv_home_bill_synchronized = helper.getView(R.id.tv_home_bill_synchronized);
        TextView tv_home_bill_tag = helper.getView(R.id.tv_home_bill_tag);
        if (item.getType() == 2) {//2-信用卡
            tv_home_bill_synchronized.setVisibility(View.VISIBLE);
            tv_home_bill_synchronized.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoxieUtil.sychronized(item.getMoxieCode(), mActivity);
                }
            });
            tv_home_bill_tag.setText(String.format(resources.getString(R.string.x_home_bill_credit), item.getMonth()));
        } else {//1-网贷(3-快捷记账（删了）)
            tv_home_bill_synchronized.setVisibility(View.GONE);
            if (item.getTotalTerm() == -1) {
                tv_home_bill_tag.setText(String.format(resources.getString(R.string.x_home_bill_loan_), item.getTerm(), "∞"));
            } else {
                tv_home_bill_tag.setText(String.format(resources.getString(R.string.x_home_bill_loan), item.getTerm(), item.getTotalTerm() + ""));
            }
        }
        //账单到期时间
        TextView tv_home_bill_time_tip = helper.getView(R.id.tv_home_bill_time_tip);
        if (item.getReturnDay() > 0) {//x天后到期
            tv_home_bill_time_tip.setTextColor(ContextCompat.getColor(mActivity, R.color.black_1));
            tv_home_bill_time_tip.setText(String.format(resources.getString(R.string.x_home_bill_will_come), item.getReturnDay()));
        } else if (item.getReturnDay() == 0) {//今天到期
            tv_home_bill_time_tip.setTextColor(ContextCompat.getColor(mActivity, R.color.refresh_one));
            tv_home_bill_time_tip.setText(resources.getString(R.string.x_home_bill_today));
        } else {//逾期x天
            tv_home_bill_time_tip.setTextColor(ContextCompat.getColor(mActivity, R.color.refresh_one));
            tv_home_bill_time_tip.setText(String.format(resources.getString(R.string.x_home_bill_overdue), -item.getReturnDay()));
        }
        //金额
        helper.setText(R.id.tv_home_bill_loan_num, FormatNumberUtils.FormatNumberFor2(item.getAmount()));
        //time
        helper.setText(R.id.tv_home_bill_time, item.getRepayTime());

        final CustomSwipeMenuLayout csm_bill_wrap = helper.getView(R.id.csm_bill_wrap);
        if (item.getType() == 2) {//信用卡
            csm_bill_wrap.setSwipeEnable(false);
        } else {
            csm_bill_wrap.setSwipeEnable(true);
        }
        //结清当期 + item点击事件
        helper.getView(R.id.tv_home_bill_over).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                csm_bill_wrap.smoothClose();
                showDialog(item, helper.getAdapterPosition());
            }
        });
        helper.getView(R.id.ll_bill_wrap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (item.getType() == 1) {//网贷
                    intent = new Intent(mActivity, NetLoanDetailActivity.class);
                } else if (item.getType() == 2) {//信用卡
                    intent = new Intent(mActivity, CreditDetailActivity.class);
                } else if (item.getType() == 3) {//通用
                    intent = new Intent(mActivity, CommonDetailActivity.class);
                }
                if (intent != null) {
                    intent.putExtra("recordId", item.getRecordId());
                    intent.putExtra("billId", item.getBillId());
                    intent.putExtra("title", item.getTitle());
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    private void showDialog(Bill item, final int position) {
        final int type = item.getType();
        final String billId = item.getBillId();
        DlgUtil.createDlg(mActivity, R.layout.dlg_pay_over_bill, new DlgUtil.OnDlgViewClickListener() {
            @Override
            public void onViewClick(final Dialog dialog, final View dlgView) {
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.confirm:
                                //pv，uv统计
                                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_SET_STATUS_PAID);
                                if (type == 2) {//信用卡记账
                                    Api.getInstance().updateCreditCardBillStatus(userHelper.id(), billId, 2)
                                            .compose(RxResponse.compatO())
                                            .subscribe(new ApiObserver<Object>() {
                                                @Override
                                                public void onNext(@NonNull Object data) {
                                                    notifyItemRemoved(position);
                                                    //dataSet.remove(position);
                                                    handler.postDelayed(task, 500);
                                                }
                                            });
                                } else if (type == 1) {//网贷记账
                                    Api.getInstance().updateStatus(userHelper.id(), billId, 2, 1)
                                            .compose(RxResponse.<BillState>compatT())
                                            .subscribe(new ApiObserver<BillState>() {
                                                @Override
                                                public void onNext(@NonNull BillState data) {
                                                    notifyItemRemoved(position);
                                                    //dataSet.remove(position);
                                                    handler.postDelayed(task, 500);
                                                    if (data.status == 2) {
                                                        ToastUtil.toast(data.message);
                                                    }
                                                }
                                            });
                                } else if (type == 3) {//快捷记账
                                    Api.getInstance().updateStatus(userHelper.id(), billId, 2, 0)
                                            .compose(RxResponse.<BillState>compatT())
                                            .subscribe(new ApiObserver<BillState>() {
                                                @Override
                                                public void onNext(@NonNull BillState data) {
                                                    notifyItemRemoved(position);
                                                    //dataSet.remove(position);
                                                    handler.postDelayed(task, 500);
                                                    if (data.status == 2) {
                                                        ToastUtil.toast(data.message);
                                                    }
                                                }
                                            });
                                }
                                dialog.dismiss();
                                break;
                            case R.id.cancel:
                                dialog.dismiss();
                                break;
                            default:
                                break;
                        }
                    }
                };
                dlgView.findViewById(R.id.cancel).setOnClickListener(clickListener);
                dlgView.findViewById(R.id.confirm).setOnClickListener(clickListener);
            }
        });
    }
}