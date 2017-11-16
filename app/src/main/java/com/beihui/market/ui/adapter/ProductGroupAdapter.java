package com.beihui.market.ui.adapter;


import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.LoanGroup;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ProductGroupAdapter extends BaseQuickAdapter<LoanGroup, BaseViewHolder> {

    private List<LoanGroup> dataSet = new ArrayList<>();

    public ProductGroupAdapter() {
        super(R.layout.list_item_product_group);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoanGroup item) {
        Glide.with(helper.itemView.getContext())
                .load(item.getUrl())
                .into((ImageView) helper.getView(R.id.group_image));

    }

    public void notifyLoanGroupChanged(List<LoanGroup> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
