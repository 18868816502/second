package com.beihui.market.ui.adapter;


import android.text.TextUtils;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.DebtChannel;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DebtSourceChannelAdapter extends BaseQuickAdapter<DebtChannel, BaseViewHolder> {

    private List<DebtChannel> dataSet = new ArrayList<>();

    public DebtSourceChannelAdapter() {
        super(R.layout.rv_item_debt_source_channel);
    }

    @Override
    protected void convert(BaseViewHolder helper, DebtChannel item) {
        if (!TextUtils.isEmpty(item.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into((ImageView) helper.getView(R.id.channel_logo));
        } else {
            ((ImageView) helper.getView(R.id.channel_logo)).setImageResource(R.drawable.image_place_holder);
        }

        if (!TextUtils.isEmpty(item.getChannelName())) {
            helper.setText(R.id.channel_name, item.getChannelName());
        } else {
            helper.setText(R.id.channel_name, "");
        }
    }

    public void notifyDebtChannelChanged(List<DebtChannel> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
