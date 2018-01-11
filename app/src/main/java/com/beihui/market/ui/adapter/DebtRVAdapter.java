package com.beihui.market.ui.adapter;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.InDebt;
import com.beihui.market.util.CommonUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DebtRVAdapter extends BaseQuickAdapter<InDebt, BaseViewHolder> {

    private List<InDebt> dataSet = new ArrayList<>();


    public DebtRVAdapter() {
        super(R.layout.list_item_in_debt);
    }

    @Override
    protected void convert(BaseViewHolder helper, InDebt item) {
        helper.addOnClickListener(R.id.set_status);
        helper.addOnClickListener(R.id.debt_container);

        if (!TextUtils.isEmpty(item.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.<ImageView>getView(R.id.debt_image));
        } else {
            helper.<ImageView>getView(R.id.debt_image).setImageResource(R.drawable.image_place_holder);
        }

        helper.setText(R.id.debt_title, item.getChannelName());
        if (!TextUtils.isEmpty(item.getProjectName())) {
            helper.setVisible(R.id.debt_subtitle, true);
            helper.setText(R.id.debt_subtitle, "-" + item.getProjectName());
        } else {
            helper.setVisible(R.id.debt_subtitle, false);
        }

        String lifeStr = item.getTermNum() + "/" + item.getTerm() + "期";
        SpannableString ss = new SpannableString(lifeStr);
        ss.setSpan(new AbsoluteSizeSpan(20, true), 0, lifeStr.indexOf("/"), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        helper.setText(R.id.debt_life, ss);

        String amountStr = CommonUtils.keep2digits(item.getTermPayableAmount()) + "元";
        ss = new SpannableString(amountStr);
        if (amountStr.contains(".")) {
            ss.setSpan(new AbsoluteSizeSpan(20, true), 0, amountStr.indexOf("."), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(new AbsoluteSizeSpan(20, true), 0, -1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.debt_amount, ss);

        String dayStr = item.getReturnDay() + "天";
        ss = new SpannableString(dayStr);
        if (item.getReturnDay() > 0) {
            helper.setVisible(R.id.status_badge, false);
            ss.setSpan(new AbsoluteSizeSpan(20, true), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            helper.setVisible(R.id.status_badge, true);
            ss.setSpan(new TextAppearanceSpan(null, 0, ((int) (20 * helper.itemView.getResources().getDisplayMetrics().density)), ColorStateList.valueOf(Color.parseColor("#ff395e")), null), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.debt_deadline, ss);
    }

    public void notifyDebtChanged(List<InDebt> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
