package com.beiwo.klyjaz.ui.adapter;


import android.text.TextUtils;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.CreditCardBank;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CreditCardBankAdapter extends BaseQuickAdapter<CreditCardBank, BaseViewHolder> {

    private List<CreditCardBank> dataSet = new ArrayList<>();

    public CreditCardBankAdapter() {
        super(R.layout.rv_item_credit_card_bank);
    }

    @Override
    protected void convert(BaseViewHolder helper, CreditCardBank item) {
        if (!TextUtils.isEmpty(item.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into((ImageView) helper.getView(R.id.bank_logo));
        } else {
            ((ImageView) helper.getView(R.id.bank_logo)).setImageResource(R.drawable.image_place_holder);
        }

        if (!TextUtils.isEmpty(item.getBankName())) {
            helper.setText(R.id.bank_name, item.getBankName());
        } else {
            helper.setText(R.id.bank_name, "");
        }
    }

    public void notifyCreditCardBankChanged(List<CreditCardBank> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
