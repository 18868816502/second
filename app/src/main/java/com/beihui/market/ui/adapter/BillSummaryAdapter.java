package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.entity.BillSummaryBean;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.view.CircleImageView;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.text.DecimalFormat;
import java.text.NumberFormat;
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

    public BillSummaryAdapter(int layoutResId, @Nullable List<BillSummaryBean.PersonBillItemBean> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, BillSummaryBean.PersonBillItemBean item) {
        helper.setText(R.id.total_money_tv, CommonUtils.numToString(item.getTotalAmount()));
        helper.setText(R.id.bill_title_tv, item.getTitle());
        Glide.with(context).load(item.getLogoUrl()).asBitmap().into((CircleImageView) helper.getView(R.id.circleImg));
        if (item.getType() != null && item.getType().equals("2")) {
            helper.getView(R.id.label_card).setVisibility(View.VISIBLE);
            helper.setText(R.id.term_tag_tv, Integer.valueOf(item.getMonth()) + "月账单");
        } else {
            helper.getView(R.id.label_card).setVisibility(View.GONE);
            helper.setText(R.id.term_tag_tv, item.getTotalTerm() + "期");

        }
    }

}




