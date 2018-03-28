package com.beihui.market.ui.adapter;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.ImageView;

import com.beihui.market.R;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.util.CommonUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

public class DebtRVAdapter extends BaseQuickAdapter<AccountBill, BaseViewHolder> {

    private List<AccountBill> dataSet = new ArrayList<>();


    public DebtRVAdapter() {
        super(R.layout.list_item_in_debt);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountBill item) {
        helper.addOnClickListener(R.id.debt_container);
        helper.addOnClickListener(R.id.hide);
        helper.addOnClickListener(R.id.sync);
        helper.addOnClickListener(R.id.set_status);

        if (!isEmpty(item.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.<ImageView>getView(R.id.debt_image));
        } else {
            helper.<ImageView>getView(R.id.debt_image).setImageResource(R.drawable.image_place_holder);
        }

        if (item.getBillType() == 1) {
            //网贷账单
            helper.setVisible(R.id.credit_card_number, false);
            if (!isEmpty(item.getChannelName())) {
                helper.setText(R.id.debt_name, item.getChannelName());
            }
        } else {
            //信用卡账单
            helper.setVisible(R.id.credit_card_number, true);
            if (!isEmpty(item.getBankName())) {
                helper.setText(R.id.debt_name, item.getBankName());
            }
            if (!isEmpty(item.getCardNums())) {
                helper.setText(R.id.credit_card_number, item.getCardNums());
            }
        }
        //是否是新账单
        helper.setVisible(R.id.account_debt_new_badge, item.getBillFlag() == 1);
        //有金额时才显示
        if (item.getAmount() > 0) {
            char symbol = 165;
            helper.setText(R.id.debt_term_amount, String.valueOf(symbol) + CommonUtils.keep2digitsWithoutZero(item.getAmount()));
        }
        //是否逾期
        helper.setVisible(R.id.status_badge_overdue, item.getReturnDay() < 0);
        //是否已还
        helper.setVisible(R.id.status_badge_paid, item.getStatus() == 2);
        //是否未出账或者无账单
        helper.setVisible(R.id.sync, item.getStatus() == 5 || item.getStatus() == 6);
        String dayStr = item.getReturnDay() + "天";
        SpannableString ss = new SpannableString(dayStr);
        if (item.getReturnDay() > 7) {
            ss.setSpan(new AbsoluteSizeSpan(20, true), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(new TextAppearanceSpan(null, 0, ((int) (20 * helper.itemView.getResources().getDisplayMetrics().density)), ColorStateList.valueOf(Color.parseColor("#ff395e")), null), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.debt_due_time, ss);
    }

    public void notifyDebtChanged(List<AccountBill> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
