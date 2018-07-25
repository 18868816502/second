package com.beihui.market.tang.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.entity.Bill;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.tang.DlgUtil;
import com.beihui.market.tang.MoxieUtil;
import com.beihui.market.tang.activity.CommonDetailActivity;
import com.beihui.market.tang.activity.CreditBillActivity;
import com.beihui.market.tang.activity.CreditDetailActivity;
import com.beihui.market.tang.activity.NetLoanDetailActivity;
import com.beihui.market.tang.activity.LoanBillActivity;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.activity.WebViewActivity;
import com.beihui.market.ui.fragment.HomeFragment;
import com.beihui.market.util.SPUtils;
import com.beihui.market.view.CustomSwipeMenuLayout;
import com.beihui.market.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/20
 */

public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.ViewHolder> implements View.OnClickListener {

    public static final int VIEW_HEADER = R.layout.f_layout_home_head;
    public static final int VIEW_NORMAL = R.layout.f_layout_home_bill_item;

    private Activity mActivity;
    private List<Bill> dataSet = new ArrayList<>();
    private HomeFragment homeFragment;
    private double totalAmount;
    private String xMonth;
    private String currentMonth;
    private Resources resources;
    private UserHelper userHelper;
    private String url;
    private final String hideNum = "****";
    private boolean numVisible;

    public void notifyEmpty() {
        System.out.println("notifyEmpty");
        url = "";
        totalAmount = -1;
        dataSet.clear();
        notifyDataSetChanged();
    }

    public void notifyEventEnter(String url) {
        this.url = url;
        notifyItemChanged(0);
    }

    public void notifyHead(double totalAmount, String xMonth) {
        this.totalAmount = totalAmount;
        this.xMonth = xMonth;
        if (totalAmount >= 0) notifyItemChanged(0);
        else notifyDataSetChanged();
    }

    public void notifyPayChanged(List<Bill> list) {
        if (list != null) dataSet = list;
        else dataSet.clear();
        notifyDataSetChanged();
    }

    public HomePageAdapter(Activity activity, HomeFragment homeFragment) {
        this.mActivity = activity;
        this.homeFragment = homeFragment;
        currentMonth = new SimpleDateFormat("MM", Locale.CHINA).format(System.currentTimeMillis());
        resources = activity.getResources();
        userHelper = UserHelper.getInstance(mActivity);
        numVisible = SPUtils.getNumVisible(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder.viewType == VIEW_HEADER) {
            if (url == null || url.isEmpty()) {
                holder.headEventEntry.setVisibility(View.GONE);
            } else holder.headEventEntry.setVisibility(View.VISIBLE);
            if (!userHelper.isLogin() || totalAmount < 0) {
                holder.headMonthNum.setText(String.format(resources.getString(R.string.x_month_repay), currentMonth));
                holder.headBillNum.setText("赶紧先记上一笔");
            } else {
                holder.headMonthNum.setText(String.format(resources.getString(R.string.x_month_repay), xMonth));
                if (numVisible) {
                    holder.headBillNum.setText(String.format("￥%.2f", totalAmount));
                    holder.headBillVisible.setImageResource(R.mipmap.ic_eye_open);
                } else {
                    holder.headBillNum.setText(hideNum);
                    holder.headBillVisible.setImageResource(R.mipmap.icon_eye_close);
                }
            }
            /*活动入口*/
            holder.headEventEntry.setOnClickListener(this);
            /*账单数目是否可见*/
            holder.headBillVisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (!userHelper.isLogin()) {
                        mActivity.startActivity(new Intent(mActivity, UserAuthorizationActivity.class));
                        return;
                    }*/
                    if (numVisible) {
                        holder.headBillVisible.setImageResource(R.mipmap.ic_eye_close);
                        holder.headBillNum.setText(hideNum);
                        SPUtils.putNumVisible(mActivity, false);
                    } else {
                        holder.headBillVisible.setImageResource(R.mipmap.ic_eye_open);
                        String num = userHelper.isLogin() ? String.format("￥%.2f", totalAmount) : "赶紧先记上一笔";
                        holder.headBillNum.setText(num);
                        SPUtils.putNumVisible(mActivity, true);
                    }
                    numVisible = !numVisible;
                }
            });
            /*添加账单*/
            holder.headAddBillWrap.setOnClickListener(this);
            /*导入信用卡*/
            holder.headCreditIn.setOnClickListener(this);
        } else if (holder.viewType == VIEW_NORMAL) {
            Bill bill = null;
            if (dataSet.size() > 0) bill = dataSet.get(position - 1);
            final Bill item = bill;
            if (item == null) {
                holder.csm_bill_wrap.setVisibility(View.GONE);
                return;
            } else {
                holder.csm_bill_wrap.setVisibility(View.VISIBLE);
            }
            //icon
            Glide.with(mActivity).load(item.getLogoUrl()).transform(new GlideCircleTransform(mActivity)).into(holder.iv_home_bill_icon);
            //name
            holder.tv_home_bill_name.setText(item.getTitle());
            //tag + 同步按钮
            if (item.getType() == 2) {//2-信用卡
                holder.tv_home_bill_synchronized.setVisibility(View.VISIBLE);
                holder.tv_home_bill_synchronized.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MoxieUtil.sychronized(item.getMoxieCode(), mActivity);
                    }
                });
                holder.tv_home_bill_tag.setText(String.format(resources.getString(R.string.x_home_bill_credit), item.getMonth()));
            } else {//1-网贷(3-快捷记账（删了）)
                holder.tv_home_bill_synchronized.setVisibility(View.GONE);
                holder.tv_home_bill_tag.setText(String.format(resources.getString(R.string.x_home_bill_loan), item.getTerm(), item.getTotalTerm()));
            }
            //账单到期时间
            if (item.getReturnDay() > 0) {//x天后到期
                holder.tv_home_bill_time_tip.setTextColor(ContextCompat.getColor(mActivity, R.color.black_1));
                holder.tv_home_bill_time_tip.setText(String.format(resources.getString(R.string.x_home_bill_will_come), item.getReturnDay()));
            } else if (item.getReturnDay() == 0) {//今天到期
                holder.tv_home_bill_time_tip.setTextColor(ContextCompat.getColor(mActivity, R.color.refresh_one));
                holder.tv_home_bill_time_tip.setText(resources.getString(R.string.x_home_bill_today));
            } else {//逾期x天
                holder.tv_home_bill_time_tip.setTextColor(ContextCompat.getColor(mActivity, R.color.refresh_one));
                holder.tv_home_bill_time_tip.setText(String.format(resources.getString(R.string.x_home_bill_overdue), -item.getReturnDay()));
            }
            //金额
            holder.tv_home_bill_loan_num.setText(String.format("%.2f", item.getAmount()));
            //time
            holder.tv_home_bill_time.setText(item.getRepayTime());

            holder.csm_bill_wrap.setSwipeEnable(true);
            //结清当期 + item点击事件
            holder.tv_home_bill_over.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.csm_bill_wrap.smoothClose();
                    showDialog(item, position);
                }
            });
            holder.ll_bill_wrap.setOnClickListener(new View.OnClickListener() {
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
                        mActivity.startActivity(intent);
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_HEADER;
        else return VIEW_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (dataSet.size() == 0) return 1;
        return dataSet.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (!userHelper.isLogin()) {
            mActivity.startActivity(new Intent(mActivity, UserAuthorizationActivity.class));
            return;
        }
        switch (v.getId()) {
            case R.id.tv_event_entry://活动入口
                Intent intent = new Intent(mActivity, WebViewActivity.class);
                intent.putExtra("webViewUrl", url);
                mActivity.startActivity(intent);
                break;
            case R.id.fl_add_account_bill_wrap://添加账单
                mActivity.startActivity(new Intent(mActivity, LoanBillActivity.class));
                break;
            case R.id.tv_credit_in://导入信用卡
                mActivity.startActivity(new Intent(mActivity, CreditBillActivity.class));
                break;
            default:
                break;
        }
    }

    private void showDialog(Bill item, final int position) {
        final int type = item.getType();
        final String billId = item.getBillId();
        final String recordId = item.getRecordId();
        final double amount = item.getAmount();
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
                                                    dataSet.remove(position - 1);
                                                    dlgView.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            homeFragment.request();
                                                        }
                                                    }, 500);
                                                }
                                            });
                                } else if (type == 1) {//网贷记账
                                    Api.getInstance().updateDebtStatus(userHelper.id(), billId, 2)
                                            .compose(RxResponse.compatO())
                                            .subscribe(new ApiObserver<Object>() {
                                                @Override
                                                public void onNext(@NonNull Object data) {
                                                    notifyItemRemoved(position);
                                                    dataSet.remove(position - 1);
                                                    dlgView.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            homeFragment.request();
                                                        }
                                                    }, 500);
                                                }
                                            });
                                } else if (type == 3) {//快捷记账
                                    Api.getInstance().updateFastDebtBillStatus(userHelper.id(), billId, recordId, 2, amount)
                                            .compose(RxResponse.compatO())
                                            .subscribe(new ApiObserver<Object>() {
                                                @Override
                                                public void onNext(@NonNull Object data) {
                                                    notifyItemRemoved(position);
                                                    dataSet.remove(position - 1);
                                                    dlgView.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            homeFragment.request();
                                                        }
                                                    }, 500);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int viewType;
        private View headEventEntry;
        private TextView headMonthNum;
        private TextView headBillNum;
        private ImageView headBillVisible;
        private View headAddBillWrap;
        private View headCreditIn;

        private CustomSwipeMenuLayout csm_bill_wrap;
        private View ll_bill_wrap;
        private ImageView iv_home_bill_icon;
        private TextView tv_home_bill_name;
        private TextView tv_home_bill_tag;
        private View tv_home_bill_synchronized;
        private TextView tv_home_bill_time_tip;
        private TextView tv_home_bill_loan_num;
        private TextView tv_home_bill_time;
        private View tv_home_bill_over;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_HEADER) {
                headEventEntry = itemView.findViewById(R.id.tv_event_entry);
                headMonthNum = (TextView) itemView.findViewById(R.id.tv_month_num);
                headBillNum = (TextView) itemView.findViewById(R.id.tv_bill_num);
                headBillVisible = (ImageView) itemView.findViewById(R.id.iv_bill_visible);
                headAddBillWrap = itemView.findViewById(R.id.fl_add_account_bill_wrap);
                headCreditIn = itemView.findViewById(R.id.tv_credit_in);
            }
            if (viewType == VIEW_NORMAL) {
                csm_bill_wrap = (CustomSwipeMenuLayout) itemView.findViewById(R.id.csm_bill_wrap);
                ll_bill_wrap = itemView.findViewById(R.id.ll_bill_wrap);
                iv_home_bill_icon = (ImageView) itemView.findViewById(R.id.iv_home_bill_icon);
                tv_home_bill_name = (TextView) itemView.findViewById(R.id.tv_home_bill_name);
                tv_home_bill_tag = (TextView) itemView.findViewById(R.id.tv_home_bill_tag);
                tv_home_bill_synchronized = itemView.findViewById(R.id.tv_home_bill_synchronized);
                tv_home_bill_time_tip = (TextView) itemView.findViewById(R.id.tv_home_bill_time_tip);
                tv_home_bill_loan_num = (TextView) itemView.findViewById(R.id.tv_home_bill_loan_num);
                tv_home_bill_time = (TextView) itemView.findViewById(R.id.tv_home_bill_time);
                tv_home_bill_over = itemView.findViewById(R.id.tv_home_bill_over);
            }
        }
    }
}