package com.beiwo.klyjaz.ui.adapter;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.AllDebt;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.beiwo.klyjaz.util.CommonUtils.convertInterestRate;
import static com.beiwo.klyjaz.util.CommonUtils.keep2digitsWithoutZero;


public class AllDebtRVAdapter extends BaseQuickAdapter<AllDebt.Row, BaseViewHolder> {

    private List<AllDebt.Row> dataSet = new ArrayList<>();
    private boolean showStatusTag;

    public AllDebtRVAdapter(boolean showStatusTag) {
        super(R.layout.list_item_all_debt_debt);
        this.showStatusTag = showStatusTag;
    }

    @Override
    protected void convert(BaseViewHolder helper, AllDebt.Row debt) {
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
                .setText(R.id.capital_amount, keep2digitsWithoutZero(debt.getCapital()))
                .setText(R.id.interest_amount, keep2digitsWithoutZero(debt.getInterest()))
                .setText(R.id.debt_rate_type, debt.getTermType() == 1 ? "日息" : "月息")
                .setText(R.id.debt_rate, convertInterestRate(debt.getRate()) + "%");

        if (!TextUtils.isEmpty(debt.getProjectName())) {
            helper.setVisible(R.id.project_name_container, true);
            helper.setText(R.id.project_name, debt.getProjectName());
        } else {
            helper.setVisible(R.id.project_name_container, false);
        }

        if (showStatusTag) {
            helper.setVisible(R.id.debt_status, true);
            TextView textView = helper.getView(R.id.debt_status);
            textView.setSelected(debt.getStatus() == 2);
            textView.setText(debt.getStatus() == 2 ? "已还" : "待还");
        }
    }

    public void notifyDebtChanged(List<AllDebt.Row> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
        disableLoadMoreIfNotFullPage();
    }

    public int getDataSetCount() {
        return dataSet.size();
    }
}