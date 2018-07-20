package com.beihui.market.ui.adapter;

import android.support.annotation.Nullable;

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
public class BillSummaryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public BillSummaryAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }
}
