package com.beihui.market.ui.adapter;


import android.graphics.Color;

import com.beihui.market.R;
import com.beihui.market.entity.DebtDetail;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class DebtDetailRVAdapter extends BaseQuickAdapter<DebtDetail.RepayPlanBean, BaseViewHolder> {

    private List<DebtDetail.RepayPlanBean> dataSet = new ArrayList<>();

    private int[] colors = {Color.WHITE, Color.parseColor("#424251"), Color.parseColor("#909298"), Color.parseColor("#ff395e")};
    private String[] status = {"", "待还", "已还", "逾期"};

    public DebtDetailRVAdapter() {
        super(R.layout.list_item_debt_detail_pay_plan);
    }

    @Override
    protected void convert(BaseViewHolder helper, DebtDetail.RepayPlanBean item) {
        helper.setText(R.id.th, item.getTermNo() + "")
                .setText(R.id.date, item.getTermRepayDate())
                .setText(R.id.amount, item.getTermPayableAmount() + "")
                .setText(R.id.status, status[item.getStatus()])
                .setTextColor(R.id.status, colors[item.getStatus()]);

    }

    public void notifyPayPlanChanged(List<DebtDetail.RepayPlanBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
