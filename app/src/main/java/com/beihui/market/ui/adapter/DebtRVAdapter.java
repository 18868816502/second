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

        //渠道logo或银行logo
        if (!isEmpty(item.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.<ImageView>getView(R.id.debt_image));
        } else {
            helper.<ImageView>getView(R.id.debt_image).setImageResource(R.drawable.image_place_holder);
        }
        //账单名称
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
        //当期应还
        helper.setText(R.id.debt_term_amount, String.valueOf((char) 165) + CommonUtils.keep2digitsWithoutZero(item.getAmount()));
        //距出账日，距还款日
        if (item.getStatus() == 5) {
            //未出账账单显示"距出账日"
            helper.setText(R.id.debt_due_time_text, "距出账日");
            String dayStr = item.getOutBillDay() == 0 ? "今天" : item.getOutBillDay() + "天";
            SpannableString ss = new SpannableString(dayStr);
            ss.setSpan(new AbsoluteSizeSpan(20, true), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.debt_due_time, ss);
        } else {
            //非未出账账单都显示距还款日
            helper.setText(R.id.debt_due_time_text, "距还款日");
            String dayStr = item.getReturnDay() == 0 ? "今天" : item.getReturnDay() + "天";
            SpannableString ss = new SpannableString(dayStr);
            if (item.getReturnDay() > 7) {
                ss.setSpan(new AbsoluteSizeSpan(20, true), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                ss.setSpan(new TextAppearanceSpan(null, 0, ((int) (20 * helper.itemView.getResources().getDisplayMetrics().density)), ColorStateList.valueOf(Color.parseColor("#ff395e")), null), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            helper.setText(R.id.debt_due_time, ss);
        }
        //是否逾期
        helper.setVisible(R.id.status_badge_overdue, item.getStatus() == 3);
        //是否已还
        helper.setVisible(R.id.status_badge_paid, item.getStatus() == 2);

        //是否已出账或者未出账
        helper.setVisible(R.id.sync, item.getStatus() == 4 || item.getStatus() == 5);
        //如果是无账单，则不显示任何信息
        if (item.getStatus() == 6) {
            helper.setText(R.id.debt_term_amount, "----");
            helper.setText(R.id.debt_due_time, "----");
        }
        //只有待还和逾期的账单可以设置已还
        helper.setGone(R.id.set_status, item.getStatus() == 1 || item.getStatus() == 3);
    }

    public void notifyDebtChanged(List<AccountBill> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
