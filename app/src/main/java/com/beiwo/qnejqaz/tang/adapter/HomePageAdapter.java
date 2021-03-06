package com.beiwo.qnejqaz.tang.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.entity.Bill;
import com.beiwo.qnejqaz.entity.BillState;
import com.beiwo.qnejqaz.entity.EventBean;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.tang.DlgUtil;
import com.beiwo.qnejqaz.tang.MoxieUtil;
import com.beiwo.qnejqaz.tang.activity.CommonDetailActivity;
import com.beiwo.qnejqaz.tang.activity.CreditBillActivity;
import com.beiwo.qnejqaz.tang.activity.CreditDetailActivity;
import com.beiwo.qnejqaz.tang.activity.LoanBillActivity;
import com.beiwo.qnejqaz.tang.activity.NetLoanDetailActivity;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.beiwo.qnejqaz.ui.activity.UserAuthorizationActivity;
import com.beiwo.qnejqaz.ui.activity.WebViewActivity;
import com.beiwo.qnejqaz.ui.fragment.HomeFragment;
import com.beiwo.qnejqaz.util.FormatNumberUtils;
import com.beiwo.qnejqaz.util.SPUtils;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.beiwo.qnejqaz.view.CustomSwipeMenuLayout;
import com.beiwo.qnejqaz.view.GlideCircleTransform;
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
    public static final int VIEW_EMPTY = R.layout.f_layout_home_bill_empty;
    public static final int VIEW_EMPTY_NOT_LGOIN = R.layout.f_layout_home_bill_empty_not_login;

    private Activity mActivity;
    private List<Bill> dataSet = new ArrayList<>();
    private HomeFragment homeFragment;
    private double totalAmount;
    private String xMonth;
    private String currentMonth;
    private Resources resources;
    private UserHelper userHelper;
    private EventBean bean;
    private final String hideNum = "****";
    private final String makeBill = "赶紧先记上一笔";
    private boolean numVisible;
    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            homeFragment.request();
        }
    };

    public void notifyEmpty() {
        bean = null;
        totalAmount = -1;
        dataSet.clear();
        notifyDataSetChanged();
    }

    public void notifyEventEnter(EventBean bean) {
        this.bean = bean;
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

    public void addData(List<Bill> list) {
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
            notifyDataSetChanged();
        }
    }

    public HomePageAdapter(Activity activity, HomeFragment homeFragment) {
        this.mActivity = activity;
        this.homeFragment = homeFragment;
        currentMonth = new SimpleDateFormat("MM", Locale.CHINA).format(System.currentTimeMillis());
        resources = activity.getResources();
        userHelper = UserHelper.getInstance(mActivity);
        numVisible = SPUtils.getNumVisible();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mActivity).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (holder.viewType == VIEW_HEADER) {
            if (bean == null) {
                holder.headEventEntry.setVisibility(View.GONE);
            } else {
                holder.headEventEntry.setVisibility(View.VISIBLE);
                Glide.with(mActivity).load(bean.getImgUrl()).into(holder.headEventEntry);
                /*活动入口*/
                holder.headEventEntry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, WebViewActivity.class);
                        intent.putExtra("webViewUrl", bean.getUrl());
                        intent.putExtra("webViewTitleName", bean.getTitle());
                        mActivity.startActivity(intent);
                    }
                });
            }
            if (!userHelper.isLogin() || totalAmount < 0) {
                holder.headMonthNum.setText(String.format(resources.getString(R.string.x_month_repay), Integer.parseInt(currentMonth) + ""));
                if (numVisible) {
                    holder.headBillNum.setText(makeBill);
                } else {
                    holder.headBillNum.setText(hideNum);
                }
            } else {
                holder.headMonthNum.setText(String.format(resources.getString(R.string.x_month_repay), xMonth));
                if (numVisible) {
                    String billNum;
                    if (totalAmount == 0 && dataSet.size() == 0) {
                        billNum = makeBill;
                    } else {
                        billNum = "¥" + FormatNumberUtils.FormatNumberFor2(totalAmount);
                    }
                    holder.headBillNum.setText(billNum);
                    holder.headBillVisible.setImageResource(R.mipmap.ic_eye_open);
                } else {
                    holder.headBillNum.setText(hideNum);
                    holder.headBillVisible.setImageResource(R.mipmap.ic_eye_close);
                }
            }
            /*账单数目是否可见*/
            holder.headBillVisible.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (numVisible) {
                        holder.headBillVisible.setImageResource(R.mipmap.ic_eye_close);
                        holder.headBillNum.setText(hideNum);
                        SPUtils.putNumVisible(false);
                    } else {
                        holder.headBillVisible.setImageResource(R.mipmap.ic_eye_open);

                        String billNum;
                        if (totalAmount == 0 && dataSet.size() == 0) {
                            billNum = makeBill;
                        } else {
                            billNum = "¥" + FormatNumberUtils.FormatNumberFor2(totalAmount);
                        }
                        String num = userHelper.isLogin() ? billNum : makeBill;
                        holder.headBillNum.setText(num);
                        SPUtils.putNumVisible(true);
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
            if (item == null) return;
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
                if (item.getTotalTerm() == -1) {
                    holder.tv_home_bill_tag.setText(String.format(resources.getString(R.string.x_home_bill_loan_), item.getTerm(), "∞"));
                } else {
                    holder.tv_home_bill_tag.setText(String.format(resources.getString(R.string.x_home_bill_loan), item.getTerm(), item.getTotalTerm() + ""));
                }
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
            holder.tv_home_bill_loan_num.setText(FormatNumberUtils.FormatNumberFor2(item.getAmount()));
            //time
            holder.tv_home_bill_time.setText(item.getRepayTime());

            if (item.getType() == 2) {//信用卡
                holder.csm_bill_wrap.setSwipeEnable(false);
            } else {
                holder.csm_bill_wrap.setSwipeEnable(true);
            }
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
                        intent.putExtra("title", item.getTitle());
                        mActivity.startActivity(intent);
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            homeFragment.recycler().smoothScrollToPosition(0);
                        }
                    }, 300);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_HEADER;
        else if (dataSet == null || dataSet.size() == 0) {
            if (!userHelper.isLogin()) return VIEW_EMPTY_NOT_LGOIN;
            else return VIEW_EMPTY;
        } else return VIEW_NORMAL;
    }

    @Override
    public int getItemCount() {
        if (dataSet.size() == 0) return 2;
        return dataSet.size() + 1;
    }

    @Override
    public void onClick(View v) {
        if (!userHelper.isLogin()) {
            mActivity.startActivity(new Intent(mActivity, UserAuthorizationActivity.class));
            return;
        }
        switch (v.getId()) {
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
        DlgUtil.createDlg(mActivity, R.layout.dlg_pay_over_bill, new DlgUtil.OnDlgViewClickListener() {
            @Override
            public void onViewClick(final Dialog dialog, final View dlgView) {
                View.OnClickListener clickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (v.getId()) {
                            case R.id.confirm:
                                //pv，uv统计
                                DataHelper.getInstance(mActivity).onCountUv(DataHelper.ID_SET_STATUS_PAID);
                                if (type == 2) {//信用卡记账
                                    Api.getInstance().updateCreditCardBillStatus(userHelper.id(), billId, 2)
                                            .compose(RxResponse.compatO())
                                            .subscribe(new ApiObserver<Object>() {
                                                @Override
                                                public void onNext(@NonNull Object data) {
                                                    notifyItemRemoved(position);
                                                    dataSet.remove(position - 1);
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
                                                    dataSet.remove(position - 1);
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
                                                    dataSet.remove(position - 1);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int viewType;
        private ImageView headEventEntry;
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
                headEventEntry = itemView.findViewById(R.id.iv_event_entry);
                headMonthNum = itemView.findViewById(R.id.tv_month_num);
                headBillNum = itemView.findViewById(R.id.tv_bill_num);
                headBillVisible = itemView.findViewById(R.id.iv_bill_visible);
                headAddBillWrap = itemView.findViewById(R.id.fl_add_account_bill_wrap);
                headCreditIn = itemView.findViewById(R.id.tv_credit_in);
            }
            if (viewType == VIEW_NORMAL) {
                csm_bill_wrap = itemView.findViewById(R.id.csm_bill_wrap);
                ll_bill_wrap = itemView.findViewById(R.id.ll_bill_wrap);
                iv_home_bill_icon = itemView.findViewById(R.id.iv_home_bill_icon);
                tv_home_bill_name = itemView.findViewById(R.id.tv_home_bill_name);
                tv_home_bill_tag = itemView.findViewById(R.id.tv_home_bill_tag);
                tv_home_bill_synchronized = itemView.findViewById(R.id.tv_home_bill_synchronized);
                tv_home_bill_time_tip = itemView.findViewById(R.id.tv_home_bill_time_tip);
                tv_home_bill_loan_num = itemView.findViewById(R.id.tv_home_bill_loan_num);
                tv_home_bill_time = itemView.findViewById(R.id.tv_home_bill_time);
                tv_home_bill_over = itemView.findViewById(R.id.tv_home_bill_over);
            }
        }
    }
}