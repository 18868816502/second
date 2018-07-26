package com.beihui.market.tang.adapter;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.entity.DetailHead;
import com.beihui.market.entity.DetailList;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.tang.activity.CommonDetailActivity;
import com.beihui.market.tang.activity.RemarkActivity;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.util.ToastUtils;
import com.beihui.market.view.CustomSwipeMenuLayout;
import com.beihui.market.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/23
 */

public class DetailCommonAdapter extends RecyclerView.Adapter<DetailCommonAdapter.ViewHolder> {

    public static final int VIEW_HEADER = R.layout.f_layout_detail_head;
    public static final int VIEW_NORMAL = R.layout.f_layout_detail_item;

    private CommonDetailActivity mActivity;
    private List<DetailList.RowsBean> dataSet = new ArrayList<>();
    private DetailHead data = null;
    private int color1;
    private int color2;
    private int color3;
    private Drawable drawable1;
    private Drawable drawable2;

    public DetailCommonAdapter(CommonDetailActivity activity) {
        this.mActivity = activity;
        color1 = ContextCompat.getColor(activity, R.color.refresh_one);
        color2 = ContextCompat.getColor(activity, R.color.black_1);
        color3 = ContextCompat.getColor(activity, R.color.black_2);
        drawable1 = ContextCompat.getDrawable(activity, R.color.bill_detail_not_pay_color);
        drawable2 = ContextCompat.getDrawable(activity, R.color.bill_detail_pay_color);
    }

    public boolean getBillStatus() {
        if (data != null && data.getStatus() == 2) return true;
        return false;
    }

    public void notifyHead(DetailHead data) {
        if (data != null) {
            this.data = data;
            notifyItemChanged(0);
        } else notifyDataSetChanged();
    }

    public void notifyRemark(String remark) {
        if (remark != null && !remark.isEmpty()) {
            if (data != null) {
                data.setRemark(remark);
                notifyItemChanged(0);
            }
        }
    }

    public void notifyList(List<DetailList.RowsBean> data) {
        if (data != null) dataSet = data;
        else dataSet.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailCommonAdapter.ViewHolder(LayoutInflater.from(mActivity).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == VIEW_HEADER) {
            if (data != null) {
                Glide.with(mActivity).load(data.getLogo()).transform(new GlideCircleTransform(mActivity)).into(holder.iv_detail_icon);
                holder.tv_detail_name.setText(data.getChannelName());
                holder.tv_still_balance.setText(String.format("￥%.2f", data.getStayReturnedAmount()));
                holder.tv_already_num.setText(String.format("%.2f", data.getReturnedAmount()));
                holder.tv_cycle_num.setText(data.getReturnedTerm() + "/" + data.getTerm());
                if (data.getRemark() != null && !data.getRemark().isEmpty()) {
                    holder.tv_add_remark.setText(data.getRemark());
                }
                if (data.getStatus() == 2) {//已结清
                    holder.iv_bill_over_tag.setVisibility(View.VISIBLE);
                } else {//未结清
                    holder.iv_bill_over_tag.setVisibility(View.GONE);
                }
                final String remark = holder.tv_add_remark.getText().toString().trim();
                holder.tv_add_remark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*if (data.getStatus() == 2) {
                            ToastUtils.showToast(mActivity, "账单已结清，无需更多操作");
                            return;
                        }*/
                        Intent intent = new Intent(mActivity, RemarkActivity.class);
                        intent.putExtra("type", 1);
                        intent.putExtra("recordId", data.getRecordId());
                        if (remark != null && !remark.isEmpty()) intent.putExtra("remark", remark);
                        mActivity.startActivity(intent);
                    }
                });
            }
        } else if (holder.viewType == VIEW_NORMAL) {
            DetailList.RowsBean item = null;
            if (dataSet.size() > 0) item = dataSet.get(position - 1);
            final DetailList.RowsBean bill = item;
            if (bill == null) return;
            //第几期
            holder.tv_term_num.setText(String.format(mActivity.getString(R.string.whick_period), bill.getTermNo()));
            //是否本期标识位
            holder.tv_present_flag.setVisibility(bill.isPresentFlag() ? View.VISIBLE : View.GONE);
            //金额
            holder.tv_term_pay_amount.setText(String.format("%.2f", bill.getTermPayableAmount()));
            //日期
            holder.tv_term_repay_date.setText(bill.getTermRepayDate().replace("-", "."));
            //账单状态 + 右侧文字背景
            final int billStatus = bill.getStatus();
            if (billStatus == 1) {//未还
                holder.tv_status.setText("未还");
                holder.tv_status.setTextColor(color2);

                holder.tv_set_status.setBackground(drawable1);
                holder.tv_set_status.setText(mActivity.getString(R.string.finish_current_pay));
            } else if (billStatus == 2) {//已还
                holder.tv_status.setText("已还");
                holder.tv_status.setTextColor(color3);

                holder.tv_set_status.setBackground(drawable2);
                holder.tv_set_status.setText(mActivity.getString(R.string.set_not_pay));
            } else if (billStatus == 3) {//逾期
                holder.tv_status.setText(String.format(mActivity.getString(R.string.x_home_bill_overdue), -bill.getReturnDay()));
                holder.tv_status.setTextColor(color1);

                holder.tv_set_status.setBackground(drawable1);
                holder.tv_set_status.setText(mActivity.getString(R.string.finish_current_pay));
            }
            //侧滑
            holder.csm_bill_wrap.setSwipeEnable(true);
            //右侧按钮点击事件
            holder.tv_set_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (billStatus == 1 || billStatus == 3) {//结清当期
                        Api.getInstance().updateFastDebtBillStatus(UserHelper.getInstance(mActivity).id(), bill.getBillId(), bill.getRecordId(), 2, null)
                                .compose(RxResponse.compatO())
                                .subscribe(new ApiObserver<Object>() {
                                    @Override
                                    public void onNext(@NonNull Object data) {
                                        com.beihui.market.util.viewutils.ToastUtils.showShort(mActivity,
                                                "恭喜，本期账单已结清", ContextCompat.getDrawable(mActivity, R.drawable.ic_detail_over));
                                        mActivity.request();
                                        EventBus.getDefault().post("1");
                                    }
                                });
                    }
                    if (billStatus == 2) {//置为未还
                        Api.getInstance().updateFastDebtBillStatus(UserHelper.getInstance(mActivity).id(), bill.getBillId(), bill.getRecordId(), 1, null)
                                .compose(RxResponse.compatO())
                                .subscribe(new ApiObserver<Object>() {
                                    @Override
                                    public void onNext(@NonNull Object data) {
                                        ToastUtils.showToast(mActivity, "已设为未还");
                                        mActivity.request();
                                        EventBus.getDefault().post("1");
                                    }
                                });
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public int viewType;
        private ImageView iv_detail_icon;
        private TextView tv_detail_name;
        private TextView tv_still_balance;
        private TextView tv_already_num;
        private TextView tv_cycle_num;
        private TextView tv_add_remark;
        private ImageView iv_bill_over_tag;

        private CustomSwipeMenuLayout csm_bill_wrap;
        private TextView tv_term_num;
        private TextView tv_present_flag;
        private TextView tv_term_pay_amount;
        private TextView tv_term_repay_date;
        private TextView tv_status;
        private TextView tv_set_status;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_HEADER) {
                iv_detail_icon = (ImageView) itemView.findViewById(R.id.iv_detail_icon);
                tv_detail_name = (TextView) itemView.findViewById(R.id.tv_detail_name);
                tv_still_balance = (TextView) itemView.findViewById(R.id.tv_still_balance);
                tv_already_num = (TextView) itemView.findViewById(R.id.tv_already_num);
                tv_cycle_num = (TextView) itemView.findViewById(R.id.tv_cycle_num);
                tv_add_remark = (TextView) itemView.findViewById(R.id.tv_add_remark);
                iv_bill_over_tag = (ImageView) itemView.findViewById(R.id.iv_bill_over_tag);
            }
            if (viewType == VIEW_NORMAL) {
                csm_bill_wrap = (CustomSwipeMenuLayout) itemView.findViewById(R.id.csm_bill_wrap);
                tv_term_num = (TextView) itemView.findViewById(R.id.tv_term_num);
                tv_present_flag = (TextView) itemView.findViewById(R.id.tv_present_flag);
                tv_term_pay_amount = (TextView) itemView.findViewById(R.id.tv_term_pay_amount);
                tv_term_repay_date = (TextView) itemView.findViewById(R.id.tv_term_repay_date);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                tv_set_status = (TextView) itemView.findViewById(R.id.tv_set_status);
            }
        }
    }
}