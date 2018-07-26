package com.beihui.market.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.BillSummaryBean;
import com.beihui.market.tang.MoxieUtil;
import com.beihui.market.tang.activity.CommonDetailActivity;
import com.beihui.market.tang.activity.CreditDetailActivity;
import com.beihui.market.tang.activity.NetLoanDetailActivity;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Copyright: zhujia (C)2018
 * FileName: BillSummaryAdapter
 * Author: jiang
 * Create on: 2018/7/20 11:26
 * Description: 账单汇总
 */
public class BillSummaryAdapter extends BaseQuickAdapter<BillSummaryBean.PersonBillItemBean, BaseViewHolder> {
    private Context context;
    private Activity activity;

    public BillSummaryAdapter(int layoutResId, @Nullable List<BillSummaryBean.PersonBillItemBean> data, Context context, Activity activity) {
        super(layoutResId, data);
        this.context = context;
        this.activity = activity;
    }

    @Override
    protected void convert(BaseViewHolder helper, final BillSummaryBean.PersonBillItemBean item) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/dinmedium.ttf");
        TextView totalTv = helper.getView(R.id.total_money_tv);
        totalTv.setTypeface(typeface);
        totalTv.setText(CommonUtils.numToString(item.getTotalAmount()));
        helper.setText(R.id.bill_title_tv, item.getTitle());
        Glide.with(context).load(item.getLogoUrl()).asBitmap().into((CircleImageView) helper.getView(R.id.circleImg));
        if (item.getType() != null && item.getType().equals("2")) {
            helper.getView(R.id.label_card).setVisibility(View.VISIBLE);
            helper.getView(R.id.label_card).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.getMoxieCode() != null) {
                        MoxieUtil.sychronized(item.getMoxieCode(), activity);
                    }
                }
            });
            helper.setText(R.id.term_tag_tv, Integer.valueOf(item.getMonth()) + "月账单");
        } else {
            helper.getView(R.id.label_card).setVisibility(View.GONE);
            helper.setText(R.id.term_tag_tv, item.getTotalTerm() + "期");
        }

        helper.getView(R.id.ll_bill_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (item.getType().equals("1")) {//网贷
                    intent = new Intent(activity, NetLoanDetailActivity.class);
                } else if (item.getType().equals("2")) {//信用卡
                    intent = new Intent(activity, CreditDetailActivity.class);
                } else if (item.getType().equals("3")) {//通用
                    intent = new Intent(activity, CommonDetailActivity.class);
                }
                if (intent != null) {
                    intent.putExtra("recordId", item.getRecordId());
                    intent.putExtra("billId", item.getRecordId());
                    activity.startActivityForResult(intent, 0);
                }
            }
        });


    }
}