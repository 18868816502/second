package com.beihui.market.ui.adapter;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.entity.request.XAccountInfo;
import com.beihui.market.ui.activity.CreditCardDebtDetailActivity;
import com.beihui.market.ui.activity.DebtAnalyzeActivity;
import com.beihui.market.ui.activity.LoanDebtDetailActivity;
import com.beihui.market.util.CommonUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/**
 * @author xhb
 * 账单首页 列表适配器
 */
public class XTabAccountRvAdapter extends RecyclerView.Adapter<XTabAccountRvAdapter.ViewHolder> {

    //数据源
    private List<XAccountInfo> dataSet = new ArrayList<>();

    public static final int VIEW_NORMAL = R.layout.xitem_tab_account_info;

    public Activity mActivity;

    public Drawable mUpArrow;
    public Drawable mDownArrow;

    public Drawable mRedDot;
    public Drawable mGrayDot;

    public Drawable mCardRedBg;
    public Drawable mCardPinkBg;
    public Drawable mCardBlackBg;
    public Drawable mCardGraygBg;

    public XTabAccountRvAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        mUpArrow = mActivity.getResources().getDrawable(R.drawable.xbill_up_arrow);
        mDownArrow = mActivity.getResources().getDrawable(R.drawable.xbill_down_arrow);

        mRedDot = mActivity.getResources().getDrawable(R.drawable.xshape_tab_account_red_circle);
        mGrayDot = mActivity.getResources().getDrawable(R.drawable.xshape_tab_account_black_circle);

        mCardRedBg = mActivity.getResources().getDrawable(R.drawable.xshape_tab_account_card_red_bg);
        mCardPinkBg = mActivity.getResources().getDrawable(R.drawable.xshape_tab_account_card_pink_bg);
        mCardBlackBg = mActivity.getResources().getDrawable(R.drawable.xshape_tab_account_card_black_bg);
        mCardGraygBg = mActivity.getResources().getDrawable(R.drawable.xshape_tab_account_card_gray_bg);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(VIEW_NORMAL, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final XAccountInfo accountBill = dataSet.get(position);

        //实例图标隐藏
        holder.mSampleIcon.setVisibility(View.GONE);

        //闹钟的显示
        if (position == 0) {
            holder.mClock.setVisibility(View.VISIBLE);
            holder.mOverdueTotal.setVisibility(View.VISIBLE);
        } else {
            holder.mClock.setVisibility(View.GONE);
            holder.mOverdueTotal.setVisibility(View.GONE);
        }

        //账单名称 网贷账单 信用卡账单
        holder.mAccountTypeName.setText(accountBill.getTitle());
        //当期应还
        holder.mAccountTypeMoney.setText(CommonUtils.keep2digitsWithoutZero(accountBill.getAmount()));

        if (accountBill.isShow) {
            holder.mArrow.setImageDrawable(mUpArrow);
            holder.mSetBg.setVisibility(View.VISIBLE);
        } else {
            holder.mArrow.setImageDrawable(mDownArrow);
            holder.mSetBg.setVisibility(View.GONE);
        }

        /**
         * 逾期或者最近三天还款日 显示小红点 背景颜色
         */
        if (accountBill.getReturnDay() <= 3) {
            holder.mDot.setImageDrawable(mRedDot);
            holder.mCardBg.setBackground(mCardRedBg);
            holder.mSetBg.setBackground(mCardPinkBg);
        } else {
            holder.mDot.setImageDrawable(mGrayDot);
            holder.mCardBg.setBackground(mCardBlackBg);
            holder.mSetBg.setBackground(mCardGraygBg);
        }

        /**
         * 逾期时间
         */
        getDeteName(holder.mDateName, accountBill.getRepayTime(), accountBill.getReturnDay());

        //是否是最近的逾期
        if (accountBill.isLastOverdue()) {
            holder.mClock.setVisibility(View.VISIBLE);
            holder.mOverdueTotal.setVisibility(View.VISIBLE);
            Log.e("adfad", accountBill.getOverdueTotal()+ "       afdsf");
            holder.mOverdueTotal.setText("逾期总数为" + accountBill.getOverdueTotal()+"笔");
        } else {
            holder.mClock.setVisibility(View.GONE);
            holder.mOverdueTotal.setVisibility(View.GONE);
        }

        /**
         * 如果是网贷 有还部分 和 已还
         * 如果是信用卡 只有已还
         * 账单类型 1-网贷 2-信用卡
         */
        if (accountBill.getType() == 1 && accountBill.getStatus() != 2) {

            //显示还部分
            holder.mSetPartPay.setVisibility(View.VISIBLE);
        }
        if (accountBill.getType() == 2 && accountBill.getStatus() != 2) {
            //隐藏还部分
            holder.mSetPartPay.setVisibility(View.GONE);
        }

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

        /**
         * 账单类型 1-网贷 2-信用卡
         */
        holder.mCardBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accountBill.getType() == 1) {
                    Intent intent = new Intent(mActivity, LoanDebtDetailActivity.class);
                    intent.putExtra("debt_id", accountBill.getRecordId());
                    mActivity.startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, CreditCardDebtDetailActivity.class);
                    intent.putExtra("debt_id", accountBill.getRecordId());
                    intent.putExtra("logo", "");
                    intent.putExtra("bank_name", accountBill.getTitle());
                    intent.putExtra("card_num", "");
                    intent.putExtra("by_hand", false);//是否是手动记账
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    private void getDeteName(TextView mDateName, String replyTime, int returnDay) {
        if (returnDay == 0) {
            mDateName.setText("今天");
            mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else if (returnDay < 0) {
            mDateName.setText("逾期" + Math.abs(returnDay) + "天");
            mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }else if (returnDay <= 15) {
            mDateName.setText(Math.abs(returnDay) + "天后");
            mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }else if (returnDay > 15) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DATE, returnDay);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DATE) + 1;
            mDateName.setText(month+"月"+day+"日");
            mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void notifyDebtChanged(List<XAccountInfo> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void notifyDebtChangedRefresh(List<XAccountInfo> list) {
        int size = list.size();
        if (dataSet.get(0).isLastOverdue() && size > 0) {
            list.remove(0);
        }
        ListIterator<XAccountInfo> iterator = list.listIterator();
        while (iterator.hasNext()) {
            dataSet.add(0, iterator.next());
        }
        notifyDataSetChanged();
    }

    public void notifyDebtChangedMore(List<XAccountInfo> list) {
        dataSet.addAll(list);
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