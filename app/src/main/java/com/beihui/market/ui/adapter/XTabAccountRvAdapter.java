package com.beihui.market.ui.adapter;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xhb
 * 账单首页 列表适配器
 */
public class XTabAccountRvAdapter extends RecyclerView.Adapter<XTabAccountRvAdapter.ViewHolder> {

    //数据源
    private List<AccountBill> dataSet = new ArrayList<>();

    public static final int VIEW_NORMAL = R.layout.xitem_tab_account_info;

    public Activity mActivity;

    public Drawable mUpArrow;
    public Drawable mDownArrow;

    public XTabAccountRvAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mUpArrow = mActivity.getResources().getDrawable(R.drawable.xbill_down_arrow);
        mDownArrow = mActivity.getResources().getDrawable(R.drawable.xbill_up_arrow);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(VIEW_NORMAL, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final AccountBill accountBill = dataSet.get(position);

        //闹钟的显示
        if (position == 0) {
            holder.mClock.setVisibility(View.VISIBLE);
            holder.mOverdueTotal.setVisibility(View.VISIBLE);
        } else {
            holder.mClock.setVisibility(View.GONE);
            holder.mOverdueTotal.setVisibility(View.GONE);
        }

        //账单名称
        if (accountBill.getBillType() == 1) {
            //网贷账单
            holder.mAccountTypeName.setText(accountBill.getChannelName());
        } else {
            //信用卡账单
            holder.mAccountTypeName.setText(accountBill.getBankName());
        }
        //当期应还
        holder.mAccountTypeMoney.setText(CommonUtils.keep2digitsWithoutZero(accountBill.getAmount()));


        /**
         * 箭头的点击事件
         */
        holder.mArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountBill.isShow) {
                    holder.mArrow.setImageDrawable(mDownArrow);
                    holder.mSetBg.setVisibility(View.GONE);
                } else {
                    holder.mArrow.setImageDrawable(mUpArrow);
                    holder.mSetBg.setVisibility(View.VISIBLE);
                }
                accountBill.isShow = !accountBill.isShow;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void notifyDebtChanged(List<AccountBill> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView mClock;
        public ImageView mArrow;
        public ImageView mSampleIcon;
        public ImageView mDot;
        public TextView mDateName;
        public TextView mAccountTypeName;
        public TextView mAccountTypeMoney;
        public TextView mSetAllPay;
        public TextView mSetPartPay;
        //逾期总数
        public TextView mOverdueTotal;


        public LinearLayout mCardBg;
        public LinearLayout mSetBg;

        public ViewHolder(View itemView) {
            super(itemView);
            mClock = (ImageView) itemView.findViewById(R.id.iv_item_tab_account_clock);
            mArrow = (ImageView) itemView.findViewById(R.id.iv_item_tab_acount_arrow);
            mDot = (ImageView) itemView.findViewById(R.id.iv_item_tab_account_dot);
            mSampleIcon = (ImageView) itemView.findViewById(R.id.iv_item_tab_acount_sample_icon);
            mDateName = (TextView) itemView.findViewById(R.id.tv_item_tab_account_date_name);
            mAccountTypeName = (TextView) itemView.findViewById(R.id.tv_item_tab_acount_name_type);
            mAccountTypeMoney = (TextView) itemView.findViewById(R.id.tv_item_tab_acount_loan_money);
            mSetAllPay = (TextView) itemView.findViewById(R.id.tv_item_tab_acount_all_pay);
            mSetPartPay = (TextView) itemView.findViewById(R.id.tv_item_tab_acount_part_pay);
            mOverdueTotal = (TextView) itemView.findViewById(R.id.iv_item_tab_account_overdue_total);

            mCardBg = (LinearLayout) itemView.findViewById(R.id.ll_item_tab_account_card);
            mSetBg= (LinearLayout) itemView.findViewById(R.id.ll_item_tab_acount_set_pay_bg);
        }
    }


}