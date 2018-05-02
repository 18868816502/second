package com.beihui.market.ui.adapter;


import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.beihui.market.R;
import com.beihui.market.entity.DebtDetail;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

public class DebtDetailRVAdapter extends BaseQuickAdapter<DebtDetail.RepayPlanBean, BaseViewHolder> {

    private List<DebtDetail.RepayPlanBean> dataSet = new ArrayList<>();

    public int currentIndex;

    private int[] colors = {Color.WHITE, Color.parseColor("#424251"), Color.parseColor("#909298"), Color.parseColor("#ff395e")};
    private String[] status = {"", "待还", "已还", "逾期"};

    public DebtDetailRVAdapter() {
        super(R.layout.list_item_debt_detail_pay_plan);
    }

    @Override
    protected void convert(final BaseViewHolder helper, DebtDetail.RepayPlanBean item) {
        helper.setText(R.id.th, item.getTermNo() + "期")      //设置期数
                .setText(R.id.date, item.getTermRepayDate())    //设置日期
                .setText(R.id.amount, "￥" + keep2digitsWithoutZero(item.getTermPayableAmount()))  //设置金额
                .setText(R.id.status, status[item.getStatus()])
                .setTextColor(R.id.status, colors[item.getStatus()])
                .setTextColor(R.id.th, item.getTermNo() == currentIndex ? Color.RED : Color.parseColor("#424251"));

    }

    public void notifyPayPlanChanged(List<DebtDetail.RepayPlanBean> list, int currentTerm) {
        currentIndex = currentTerm;
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }



    public void setThTextColor(int position) {
        currentIndex = position + 1;
        notifyDataSetChanged();
    }
}
