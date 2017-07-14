package com.beihui.market.ui.adapter;

import com.beihui.market.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


public class LoanRVAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LoanRVAdapter() {
        super(R.layout.rv_item_loan);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

    }

    public void notifyDataSetChanged(List<String> dataSet) {
        setNewData(dataSet);
    }
}
