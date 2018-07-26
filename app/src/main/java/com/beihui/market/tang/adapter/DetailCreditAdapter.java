package com.beihui.market.tang.adapter;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.entity.BillDetail;
import com.beihui.market.entity.CreditBill;
import com.beihui.market.entity.CreditCardDebtDetail;
import com.beihui.market.tang.activity.CreditDetailActivity;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.view.GlideCircleTransform;
import com.bumptech.glide.Glide;

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
 * @date: 2018/7/24
 */

public class DetailCreditAdapter extends RecyclerView.Adapter<DetailCreditAdapter.ViewHolder> {

    public static final int VIEW_HEADER = R.layout.f_layout_detail_head_credit;
    public static final int VIEW_NORMAL = R.layout.f_layout_detail_credit;

    private List<CreditBill> dataSet = new ArrayList<>();
    private CreditDetailActivity mActivity;
    private CreditCardDebtDetail data;
    private boolean isExpand = false;
    private SubDetailAdapter detailAdapter = new SubDetailAdapter();

    public void notifyHead(CreditCardDebtDetail debtDetail) {
        if (debtDetail != null) {
            this.data = debtDetail;
            notifyItemChanged(0);
        } else notifyDataSetChanged();
    }

    public String getBankCode() {
        return data.moxieCode;
    }

    public void notifyList(List<CreditBill> list) {
        if (list != null) this.dataSet = list;
        else dataSet.clear();
        notifyDataSetChanged();
    }

    public DetailCreditAdapter(CreditDetailActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public DetailCreditAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DetailCreditAdapter.ViewHolder(LayoutInflater.from(mActivity).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final DetailCreditAdapter.ViewHolder holder, int position) {
        if (holder.viewType == VIEW_HEADER) {
            if (data != null) {
                Glide.with(mActivity).load(data.image).transform(new GlideCircleTransform(mActivity)).into(holder.iv_detail_icon);
                holder.tv_detail_name.setText(data.bankName);
                CreditCardDebtDetail.ShowBillBean showBill = data.getShowBill();
                if (showBill != null) {
                    holder.tv_x_month.setText(String.format(mActivity.getString(R.string.x_month_repay), showBill.getBillMonth().substring(5, 7)));
                    holder.tv_still_balance.setText(String.format("￥%.2f", showBill.getNewBalance()));
                    holder.tv_min_amount.setText(String.format("%.2f", showBill.getMinPayment()));
                    holder.tv_bill_date.setText(showBill.getBillDate().substring(5, 11));
                    holder.tv_pay_date.setText(showBill.getPaymentDueDate().substring(5, 11));
                }
            }
        } else if (holder.viewType == VIEW_NORMAL) {
            CreditBill item = null;
            if (dataSet.size() > 0) item = dataSet.get(position - 1);
            final CreditBill bill = item;
            if (bill == null) return;
            holder.tv_term_num.setText(bill.getBillMonth().substring(5, 7) + "月");
            holder.tv_present_flag.setVisibility(bill.isPresentFlag() ? View.VISIBLE : View.GONE);
            holder.tv_term_pay_amount.setText(String.format("%.2f", bill.getNewBalance()));
            holder.tv_term_repay_date.setText(bill.getBillDate().substring(0, 10).replace("-", "."));
            final int status = bill.getStatus();
            String statusTxt = "";
            switch (status) {
                case 0://0-无效
                    statusTxt = "无效";
                    break;
                case 1://1-待还
                    statusTxt = "待还";
                    break;
                case 2://2-已还
                    statusTxt = "已还";
                    break;
                case 3://3-逾期
                    statusTxt = "逾期" + -bill.getReturnDay() + "天";
                    break;
                case 4://4-已出账
                    statusTxt = "已出账";
                    break;
                case 5://5-未出账
                    statusTxt = "未出账";
                    break;
                case 6://6-无账单
                    statusTxt = "无账单";
                    break;
                default:
                    break;
            }
            holder.tv_status.setText(statusTxt);
            holder.tv_bill_start_end_time.setText("账单周期 " + bill.getStartTime().substring(0, 10).replace("-", ".") + "-" + bill.getEndTime().substring(0, 10).replace("-", "."));

            holder.initRecyclerView();
            holder.recycler.setAdapter(detailAdapter);

            holder.fl_credit_item_wrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isExpand) {
                        holder.iv_left.setImageResource(R.mipmap.ic_left);
                        holder.ll_subitem_wrap.setVisibility(View.GONE);
                        detailAdapter.getData().clear();
                    } else {
                        holder.iv_left.setImageResource(R.mipmap.ic_left_down);
                        holder.ll_subitem_wrap.setVisibility(View.VISIBLE);
                        Api.getInstance().billDetail(bill.getUserId(), bill.getId())
                                .compose(RxResponse.<List<BillDetail>>compatT())
                                .subscribe(new ApiObserver<List<BillDetail>>() {
                                    @Override
                                    public void onNext(@NonNull List<BillDetail> data) {
                                        if (data.size() == 0) {
                                            holder.rl_empty_wrap.setVisibility(View.VISIBLE);
                                        } else {
                                            holder.rl_empty_wrap.setVisibility(View.GONE);
                                            detailAdapter.setNewData(data);
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable t) {
                                        holder.rl_empty_wrap.setVisibility(View.VISIBLE);
                                        super.onError(t);
                                    }
                                });
                    }
                    isExpand = !isExpand;
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
        private TextView tv_x_month;
        private TextView tv_still_balance;
        private TextView tv_min_amount;
        private TextView tv_bill_date;
        private TextView tv_pay_date;

        private FrameLayout fl_credit_item_wrap;
        private ImageView iv_left;
        private TextView tv_term_num;
        private TextView tv_present_flag;
        private TextView tv_term_pay_amount;
        private TextView tv_term_repay_date;
        private TextView tv_status;
        private LinearLayout ll_subitem_wrap;
        private TextView tv_bill_start_end_time;
        private RecyclerView recycler;
        private View rl_empty_wrap;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_HEADER) {
                iv_detail_icon = (ImageView) itemView.findViewById(R.id.iv_detail_icon);
                tv_detail_name = (TextView) itemView.findViewById(R.id.tv_detail_name);
                tv_x_month = (TextView) itemView.findViewById(R.id.tv_x_month);
                tv_still_balance = (TextView) itemView.findViewById(R.id.tv_still_balance);
                tv_min_amount = (TextView) itemView.findViewById(R.id.tv_min_amount);
                tv_bill_date = (TextView) itemView.findViewById(R.id.tv_bill_date);
                tv_pay_date = (TextView) itemView.findViewById(R.id.tv_pay_date);
            }
            if (viewType == VIEW_NORMAL) {
                fl_credit_item_wrap = (FrameLayout) itemView.findViewById(R.id.fl_credit_item_wrap);
                iv_left = (ImageView) itemView.findViewById(R.id.iv_left);
                tv_term_num = (TextView) itemView.findViewById(R.id.tv_term_num);
                tv_present_flag = (TextView) itemView.findViewById(R.id.tv_present_flag);
                tv_term_pay_amount = (TextView) itemView.findViewById(R.id.tv_term_pay_amount);
                tv_term_repay_date = (TextView) itemView.findViewById(R.id.tv_term_repay_date);
                tv_status = (TextView) itemView.findViewById(R.id.tv_status);
                ll_subitem_wrap = (LinearLayout) itemView.findViewById(R.id.ll_subitem_wrap);
                tv_bill_start_end_time = (TextView) itemView.findViewById(R.id.tv_bill_start_end_time);
                recycler = (RecyclerView) itemView.findViewById(R.id.recycler);
                rl_empty_wrap = itemView.findViewById(R.id.rl_empty_wrap);
            }
        }

        public void initRecyclerView() {
            if (recycler == null) return;
            recycler.setLayoutManager(new LinearLayoutManager(itemView.getContext()) {
                @Override
                public boolean canScrollVertically() {
                    return true;
                }
            });
            recycler.setItemAnimator(new DefaultItemAnimator());
        }
    }
}
