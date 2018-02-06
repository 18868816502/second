package com.beihui.market.ui.adapter;


import com.beihui.market.R;
import com.beihui.market.entity.PayPlan;
import com.beihui.market.util.CommonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ConfirmPayPlanRVAdapter extends BaseQuickAdapter<PayPlan.RepayPlanBean, BaseViewHolder> {

    private List<PayPlan.RepayPlanBean> dataSet = new ArrayList<>();

    public ConfirmPayPlanRVAdapter() {
        super(R.layout.list_item_pay_plan);
    }


    @Override
    protected void convert(BaseViewHolder helper, PayPlan.RepayPlanBean item) {
        helper.setText(R.id.th, item.getTermNo() + "")
                .setText(R.id.date, item.getTermRepayDate())
                .setText(R.id.amount, CommonUtils.keep2digits(item.getTermPayableAmount()))
                .addOnClickListener(R.id.amount_edit)
                .addOnClickListener(R.id.date_edit);
    }

    public void notifyPayPlanChanged(List<PayPlan.RepayPlanBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
