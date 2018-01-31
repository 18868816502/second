package com.beihui.market.ui.adapter;


import android.text.TextUtils;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.CreditCard;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CreditCardRVAdapter extends BaseQuickAdapter<CreditCard.Row, BaseViewHolder> {

    private List<CreditCard.Row> dataSet = new ArrayList<>();

    public CreditCardRVAdapter() {
        super(R.layout.list_item_credit_card_abstract);
        setNewData(dataSet);
    }

    @Override
    protected void convert(BaseViewHolder helper, CreditCard.Row item) {
        if (!TextUtils.isEmpty(item.getImage())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getImage())
                    .asBitmap()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.<ImageView>getView(R.id.credit_card_image));
        } else {
            helper.<ImageView>getView(R.id.credit_card_image).setImageResource(R.drawable.image_place_holder);
        }
        helper.setText(R.id.credit_card_name, item.getName())
                .setText(R.id.credit_card_des, TextUtils.isEmpty(item.getNominate()) ? "" : item.getNominate());
    }

    public void notifyCreditCardChanged(List<CreditCard.Row> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
