package com.beiwo.klyjaz.ui.adapter;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.TextAppearanceSpan;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.CalendarDebt;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.beiwo.klyjaz.util.CommonUtils.keep2digitsWithoutZero;

public class DebtCalendarRVAdapter extends BaseQuickAdapter<CalendarDebt.DetailBean, BaseViewHolder> {

    private List<CalendarDebt.DetailBean> dataSet = new ArrayList<>();

    public DebtCalendarRVAdapter() {
        super(R.layout.rv_item_calendar_debt);
    }


    @Override
    protected void convert(BaseViewHolder helper, CalendarDebt.DetailBean item) {
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
        helper.setText(R.id.debt_name, isEmpty(item.getTopic()) ? "" : item.getTopic());
        //信用卡卡号，网贷账单没有该字段
        helper.setText(R.id.card_num, isEmpty(item.getBillName()) ? "" : item.getBillName());

        //当期应还金额
        String amountStr = keep2digitsWithoutZero(item.getAmount()) + "元";
        SpannableString ss = new SpannableString(amountStr);
        if (amountStr.contains(".")) {
            ss.setSpan(new AbsoluteSizeSpan(20, true), 0, amountStr.indexOf("."), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        } else {
            ss.setSpan(new AbsoluteSizeSpan(20, true), 0, amountStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        }
        helper.setText(R.id.debt_amount, ss);
        //还款状态
        helper.setVisible(R.id.debt_deadline_container, false);
        helper.setVisible(R.id.paid, false);
        if (item.getStatus() == 2) {//已还清
            helper.setVisible(R.id.paid, true);
        } else { //待还或者逾期
            helper.setVisible(R.id.debt_deadline_container, true);
            helper.setVisible(R.id.status_badge, item.getReturnDay() < 0);
            String dayStr = item.getReturnDay() == 0 ? "今天" : item.getReturnDay() + "天";
            ss = new SpannableString(dayStr);
            if (item.getReturnDay() > 7) {
                ss.setSpan(new AbsoluteSizeSpan(20, true), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                ss.setSpan(new TextAppearanceSpan(null, 0, ((int) (20 * helper.itemView.getResources().getDisplayMetrics().density)), ColorStateList.valueOf(Color.parseColor("#ff395e")), null), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            helper.setText(R.id.debt_deadline, ss);
        }
        //已逾期
        helper.setVisible(R.id.status_badge, item.getStatus() == 3);
    }

    public void notifyDebtChanged(List<CalendarDebt.DetailBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
