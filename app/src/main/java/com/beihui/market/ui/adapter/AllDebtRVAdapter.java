package com.beihui.market.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.Debt;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.beihui.market.util.CommonUtils.keep2digits;


public class AllDebtRVAdapter extends BaseQuickAdapter<Debt, BaseViewHolder> {

    private List<Debt> dataSet = new ArrayList<>();
    private boolean showStatusTag;

    public AllDebtRVAdapter(boolean showStatusTag) {
        super(R.layout.list_item_all_debt_debt);
        this.showStatusTag = showStatusTag;
    }

    @Override
    protected void convert(BaseViewHolder helper, Debt debt) {
        if (!TextUtils.isEmpty(debt.getLogo())) {
            Glide.with(helper.itemView.getContext())
                    .load(debt.getLogo())
                    .asBitmap()
                    .placeholder(R.drawable.image_place_holder)
                    .into(helper.<ImageView>getView(R.id.debt_image));
        } else {
            helper.<ImageView>getView(R.id.debt_image).setImageResource(R.drawable.image_place_holder);
        }
        helper.setText(R.id.channel_name, debt.getChannelName())
                .setText(R.id.capital_amount, keep2digits(debt.getCapital()))
                .setText(R.id.interest_amount, keep2digits(debt.getInterest()))
                .setText(R.id.debt_rate, keep2digits(debt.getRate()) + "%");

        if (!TextUtils.isEmpty(debt.getProjectName())) {
            helper.setVisible(R.id.project_name_container, true);
            helper.setText(R.id.project_name, debt.getProjectName());
        } else {
            helper.setVisible(R.id.project_name_container, false);
        }

        if (showStatusTag) {
            helper.setVisible(R.id.debt_status, true);
            TextView textView = helper.getView(R.id.debt_status);
            textView.setSelected(debt.isPaid());
            textView.setText(debt.isPaid() ? "已还" : "待还");
        }
    }

    public void notifyDebtChanged(List<Debt> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
