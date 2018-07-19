package com.beihui.market.ui.adapter;


import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.LoanAccountIconBean;
import com.beihui.market.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AccountFlowLoanRvAdapter extends BaseQuickAdapter<LoanAccountIconBean, BaseViewHolder> {

    private List<LoanAccountIconBean> dataSet = new ArrayList<>();

    public AccountFlowLoanRvAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoanAccountIconBean item) {
        Glide.with(helper.itemView.getContext())
                .load(item.logo)
                .asBitmap()
                .placeholder(R.drawable.image_place_holder)
                .transform(new GlideCircleTransform(helper.itemView.getContext()))
                .into(helper.<ImageView>getView(R.id.debt_channel_image));
        helper.setText(R.id.debt_channel_name, item.iconName);
    }

    public void notifyDebtChannelChanged(List<LoanAccountIconBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
