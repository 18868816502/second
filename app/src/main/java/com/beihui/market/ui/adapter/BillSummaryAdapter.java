package com.beihui.market.ui.adapter;

import android.support.annotation.Nullable;

import com.beihui.market.R;
import com.beihui.market.entity.BillSummaryBean;
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

    public BillSummaryAdapter(int layoutResId, @Nullable List<BillSummaryBean.PersonBillItemBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BillSummaryBean.PersonBillItemBean item) {
        helper.setText(R.id.total_money_tv, item.getTotalAmount());
        helper.setText(R.id.bill_title_tv, item.getTitle());

    }
}
