package com.beihui.market.ui.adapter;


import android.text.TextUtils;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.AccountFlowIconBean;
import com.beihui.market.entity.DebtChannel;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AccountFlowLoanRvAdapter extends BaseQuickAdapter<AccountFlowIconBean, BaseViewHolder> {

    private List<AccountFlowIconBean> dataSet = new ArrayList<>();

    public AccountFlowLoanRvAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountFlowIconBean item) {
        if (!TextUtils.isEmpty(item.logo)) {
            Glide.with(helper.itemView.getContext())
                    .load(item.logo)
                    .asBitmap()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.<ImageView>getView(R.id.debt_channel_image));
        } else {
            helper.<ImageView>getView(R.id.debt_channel_image).setImageResource(R.drawable.image_place_holder);
        }
        helper.setText(R.id.debt_channel_name, item.iconName);
    }

    public void notifyDebtChannelChanged(List<AccountFlowIconBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
