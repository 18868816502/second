package com.beihui.market.tang.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.Bill;
import com.beihui.market.view.CustomSwipeMenuLayout;
import com.beihui.market.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/19
 */

public class HomeBillAdapter extends BaseQuickAdapter<Bill, BaseViewHolder> {
    public HomeBillAdapter() {
        super(R.layout.f_layout_home_bill_item);
        openLoadAnimation(ALPHAIN);
    }

    @Override
    protected void convert(BaseViewHolder helper, final Bill item) {
        Context context = helper.itemView.getContext();
        //icon
        ImageView ivIcon = helper.getView(R.id.iv_home_bill_icon);
        Glide.with(context).load(item.getLogoUrl()).transform(new GlideCircleTransform(context)).into(ivIcon);
        //name
        helper.setText(R.id.tv_home_bill_name, item.getTitle());
        //tag + 同步按钮
        TextView tvTag = helper.getView(R.id.tv_home_bill_tag);
        TextView tvSynchronized = helper.getView(R.id.tv_home_bill_synchronized);
        if (item.getType() == 2) {//2-信用卡
            tvSynchronized.setVisibility(View.VISIBLE);
            tvTag.setText(String.format(context.getString(R.string.x_home_bill_credit), item.getMonth()));
        } else {//1-网贷(3-快捷记账（删了）)
            tvSynchronized.setVisibility(View.GONE);
            tvTag.setText(String.format(context.getString(R.string.x_home_bill_loan), item.getTerm(), item.getTotalTerm()));
        }
        //账单到期时间
        TextView tvTimeTip = helper.getView(R.id.tv_home_bill_time_tip);
        if (item.getReturnDay() > 0) {//x天后到期
            tvTimeTip.setTextColor(ContextCompat.getColor(context, R.color.black_1));
            tvTimeTip.setText(String.format(context.getString(R.string.x_home_bill_will_come), item.getReturnDay()));
        } else if (item.getReturnDay() == 0) {//今天到期
            tvTimeTip.setTextColor(ContextCompat.getColor(context, R.color.refresh_one));
            tvTimeTip.setText(context.getString(R.string.x_home_bill_today));
        } else {//逾期x天
            tvTimeTip.setTextColor(ContextCompat.getColor(context, R.color.refresh_one));
            tvTimeTip.setText(String.format(context.getString(R.string.x_home_bill_overdue), -item.getReturnDay()));
        }
        //金额
        helper.setText(R.id.tv_home_bill_loan_num, String.format("%.2f", item.getAmount()));
        //time
        helper.setText(R.id.tv_home_bill_time, item.getRepayTime());

        CustomSwipeMenuLayout cmsLayout = helper.getView(R.id.csm_bill_wrap);
        cmsLayout.setSwipeEnable(true);
        //结清当期 + item点击事件
        helper.addOnClickListener(R.id.tv_home_bill_over)
                .addOnClickListener(R.id.ll_bill_wrap);
        helper.getView(R.id.tv_home_bill_over).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payAllAndItemClickListener != null)
                    payAllAndItemClickListener.payAllClick(item.getType(), item.getBillId(), item.getRecordId(), item.getAmount());
            }
        });
        helper.getView(R.id.ll_bill_wrap).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payAllAndItemClickListener != null)
                    payAllAndItemClickListener.itemClick(item.getRecordId(), item.getBillId());
            }
        });
    }

    private OnPayAllAndItemClickListener payAllAndItemClickListener;

    public void setPayAllAndItemClickListener(OnPayAllAndItemClickListener payAllAndItemClickListener) {
        this.payAllAndItemClickListener = payAllAndItemClickListener;
    }

    public interface OnPayAllAndItemClickListener {
        void payAllClick(int type, String billId, String recordId, double amount);

        void itemClick(String recordId, String billId);
    }
}
