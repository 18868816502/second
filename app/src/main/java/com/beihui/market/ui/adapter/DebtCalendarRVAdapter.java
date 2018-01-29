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
import com.beihui.market.entity.DebtCalendar;
import com.beihui.market.util.CommonUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DebtCalendarRVAdapter extends BaseQuickAdapter<DebtCalendar.DetailBean, BaseViewHolder> {

    private List<DebtCalendar.DetailBean> dataSet = new ArrayList<>();

    public DebtCalendarRVAdapter() {
        super(R.layout.list_item_debt_calendar);
    }


    @Override
    protected void convert(BaseViewHolder helper, DebtCalendar.DetailBean item) {
        if (!TextUtils.isEmpty(item.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(item.getLogo())
                    .asBitmap()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.<ImageView>getView(R.id.debt_image));
        } else {
            helper.<ImageView>getView(R.id.debt_image).setImageResource(R.drawable.image_place_holder);
        }

        helper.setText(R.id.channel_name, item.getChannelName());
        if (!TextUtils.isEmpty(item.getProjectName())) {
            helper.setVisible(R.id.project_name_container, true);
            helper.setText(R.id.project_name, item.getProjectName());
        } else {
            helper.setVisible(R.id.project_name_container, false);
        }
        String lifeStr;
        if (item.getRepayType() == 1) {
            //一次性还本付息只有一期
            lifeStr = "1/1期";
        } else {
            lifeStr = item.getTermNo() + "/" + item.getTerm() + "期";
        }
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

        if (item.getStatus() == 2) {
            //已还
            helper.setVisible(R.id.debt_deadline_container, false);
            helper.setVisible(R.id.paid, true);
            helper.setVisible(R.id.status_badge, false);
        } else {
            //待还
            helper.setVisible(R.id.debt_deadline_container, true);
            helper.setVisible(R.id.paid, false);

            helper.setVisible(R.id.status_badge, item.getReturnDay() < 0);
            String dayStr = item.getReturnDay() + "天";
            ss = new SpannableString(dayStr);
            if (item.getReturnDay() > 7) {
                ss.setSpan(new AbsoluteSizeSpan(20, true), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            } else {
                ss.setSpan(new TextAppearanceSpan(null, 0, ((int) (20 * helper.itemView.getResources().getDisplayMetrics().density)), ColorStateList.valueOf(Color.parseColor("#ff395e")), null), 0, dayStr.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            }
            helper.setText(R.id.debt_deadline, ss);
        }
    }

    public void notifyDebtChanged(List<DebtCalendar.DetailBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
