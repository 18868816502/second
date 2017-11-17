package com.beihui.market.ui.adapter;


import android.graphics.Color;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.LoanProduct;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class HotChoiceRVAdapter extends BaseQuickAdapter<LoanProduct.Row, BaseViewHolder> {

    private List<LoanProduct.Row> dataSet = new ArrayList<>();

    public HotChoiceRVAdapter(int resId) {
        super(resId);
        setNewData(dataSet);
    }

    @Override
    protected void convert(BaseViewHolder helper, LoanProduct.Row item) {
        if (!TextUtils.isEmpty(item.getLogoUrl())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogoUrl())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.image_place_holder)
                    .into((ImageView) helper.getView(R.id.product_logo));
        } else {
            helper.setImageResource(R.id.product_logo, R.drawable.image_place_holder);
        }
        if (item.getProductName() != null) {
            helper.setText(R.id.product_name, item.getProductName());
        }
        if (item.getBorrowingHighText() != null) {
            helper.setText(R.id.product_loan_range, item.getBorrowingHighText());
        }

        //loaned number
        SpannableString ss = new SpannableString("已借款" + item.getSuccessCount() + "人");
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff395e")), 3, ss.length() - 1,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE);
        helper.setText(R.id.loaned_number, ss);
    }

    public void notifyHotProductChanged(List<LoanProduct.Row> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        notifyDataSetChanged();
    }
}
